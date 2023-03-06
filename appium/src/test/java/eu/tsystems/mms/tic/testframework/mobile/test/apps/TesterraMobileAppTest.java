package eu.tsystems.mms.tic.testframework.mobile.test.apps;

import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

/**
 * Created on 2023-02-07
 *
 * @author mgn
 */
public class TesterraMobileAppTest extends AbstractAppiumTest {

    @Test
    public void testT01AndroidApp() throws MalformedURLException {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'Galaxy S20') and @version='13.0'");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");

        request.getDesiredCapabilities().setCapability(MobileCapabilityType.APP, "cloud:eu.tsystems.mms.tic.mdc.app.android/.HomeActivity");
        request.getDesiredCapabilities().setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "eu.tsystems.mms.tic.mdc.app.android");
        request.getDesiredCapabilities().setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".HomeActivity");

        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);
        TimerUtils.sleep(4000);

//        UITestUtils.takeScreenshot(webDriver, true);

//        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
//        Screenshot screenshot = new Screenshot();
//        screenshot.setFile(scrFile);
//        UITestUtils.takeScreenshot(webDriver, screenshot);
    }

}
