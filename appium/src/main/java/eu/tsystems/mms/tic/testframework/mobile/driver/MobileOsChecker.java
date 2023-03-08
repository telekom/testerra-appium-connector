package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
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
        Map<String, Object> capabilities = webDriverRequest.getCapabilities();
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_chrome)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_PACKAGE)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_ACTIVITY)
                || "Espresso".equals(capabilities.get(MobileCapabilityType.AUTOMATION_NAME))
                || "UiAutomator2".equals(capabilities.get(MobileCapabilityType.AUTOMATION_NAME))
                || "UiAutomator".equals(capabilities.get(MobileCapabilityType.AUTOMATION_NAME))
        ) {
            return Platform.ANDROID;
        }
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_safari)
                || capabilities.containsKey(IOSMobileCapabilityType.BUNDLE_ID)
                || "XCUITest".equals(capabilities.get(MobileCapabilityType.AUTOMATION_NAME))
                || "UIAutomation".equals(capabilities.get(MobileCapabilityType.AUTOMATION_NAME))
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
