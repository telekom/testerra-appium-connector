package eu.tsystems.mms.tic.testframework.mobile.test.apps;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.mobile.driver.AndroidConfig;
import eu.tsystems.mms.tic.testframework.mobile.driver.IOSConfig;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.utils.UITestUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created on 2024-07-17
 *
 * @author mgn
 */
public class TesterraMobileApp2Test extends AbstractAppiumTest {

    @BeforeClass
    public void initCaps() {
        WEB_DRIVER_MANAGER.setUserAgentConfig(Browsers.android, (AndroidConfig) options -> {
            options.setApp("cloud:com.telekom.mms.cqa.mdc.androidapp/.HomeActivity");
            options.setAppPackage("com.telekom.mms.cqa.mdc.androidapp");
            options.setAppActivity(".HomeActivity");
        });

        WEB_DRIVER_MANAGER.setUserAgentConfig(Browsers.ios, (IOSConfig) options -> {
            options.setApp("cloud:com.telekom.mms.cqa.mdc.iosapp");
            options.setBundleId("com.telekom.mms.cqa.mdc.iosapp");
        });
    }

    /**
     * This test opens the mobile app on iOS and Android.
     * <p>
     * The platform specific caps are set by the method 'initCaps'.
     * The property 'tt.browser' defines which platform should used for test:
     * tt.browser=android or tt.browser=ios
     * <p>
     * If a device query is needed, the properties 'tt.mobile.device.query.ios' and
     * 'tt.mobile.device.query.android' are used to select a device from a cloud.
     */
    @Test
    public void testT01OpenApp() {
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        TimerUtils.sleep(4000);
        UITestUtils.takeScreenshots();
    }

}
