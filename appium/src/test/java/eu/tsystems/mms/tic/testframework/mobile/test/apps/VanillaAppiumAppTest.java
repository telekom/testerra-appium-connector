package eu.tsystems.mms.tic.testframework.mobile.test.apps;

import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created on 2023-02-07
 *
 * @author mgn
 */
public class VanillaAppiumAppTest extends AbstractAppiumTest {

    protected AndroidDriver<AndroidElement> driver = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        final String accessKey = AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString();
        Assert.assertNotNull(accessKey, "No access key loaded");

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("testName", "Demo Tests");
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("deviceQuery", "contains(@name, 'Galaxy S20') and @version='13.0'");
        dc.setCapability("appiumVersion", "1.22.3");
        dc.setCapability(MobileCapabilityType.APP, "cloud:eu.tsystems.mms.tic.mdc.app.android/.HomeActivity");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "eu.tsystems.mms.tic.mdc.app.android");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".HomeActivity");
        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        URL url = new URL(AppiumProperties.MOBILE_GRID_URL.asString());
        //        driver = new IOSDriver<>(new URL(PropertyManager.getProperty("tt.mobile.grid.url")), dc);
        driver = new AndroidDriver<>(url, dc);
    }

    @Test
    public void testT01CheckApp() {
        TimerUtils.sleep(5000);
    }

    @AfterMethod
    public void tearDown() {

        log().info("Report URL: " + driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }

}
