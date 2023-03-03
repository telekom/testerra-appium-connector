package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.appium.MobileOs;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Optional;

/**
 * Created on 2023-03-02
 *
 * @author mgn
 */
public class MobileOsChecker implements WebDriverManagerProvider {

    public MobileOs getOS(WebDriverRequest webDriverRequest) {
        Map<String, Object> capabilities = webDriverRequest.getCapabilities();
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_chrome)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_PACKAGE)
                || capabilities.containsKey(AndroidMobileCapabilityType.APP_ACTIVITY)
        ) {
            return MobileOs.ANDROID;
        }
        if (webDriverRequest.getBrowser().equals(Browsers.mobile_safari)
                || capabilities.containsKey(IOSMobileCapabilityType.BUNDLE_ID)
        ) {
            return MobileOs.IOS;
        }

        return MobileOs.OTHER;
    }

    public MobileOs getOS(WebDriver driver) {
        Optional<WebDriverRequest> optional = WEB_DRIVER_MANAGER.getSessionContext(driver).map(SessionContext::getWebDriverRequest);
        if (optional.isPresent()) {
            return getOS(optional.get());
        } else {
            return MobileOs.OTHER;
        }
    }

}
