package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Optional;

/**
 * Created on 2023-03-02
 *
 * @author mgn
 */
public class MobileOsChecker {

    public Platform getPlatform(WebDriverRequest webDriverRequest) {
        // TODO: Also check for Mobile engines -> https://appium.io/docs/en/commands/session/create/
        Map<String, Object> capabilities = webDriverRequest.getCapabilities();
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_chrome)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_PACKAGE)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_ACTIVITY)
        ) {
            return Platform.ANDROID;
        }
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_safari)
                || capabilities.containsKey(IOSMobileCapabilityType.BUNDLE_ID)
        ) {
            return Platform.IOS;
        }

        return Platform.ANY;
    }

    public Platform getPlatform(WebDriver driver) {
        IWebDriverManager instance = Testerra.getInjector().getInstance(IWebDriverManager.class);
        Optional<WebDriverRequest> optional = instance.getSessionContext(driver).map(SessionContext::getWebDriverRequest);
        if (optional.isPresent()) {
            return getPlatform(optional.get());
        } else {
            return Platform.ANY;
        }
    }

}
