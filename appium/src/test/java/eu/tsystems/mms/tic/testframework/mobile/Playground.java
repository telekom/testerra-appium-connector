package eu.tsystems.mms.tic.testframework.mobile;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import org.testng.annotations.Test;

/**
 * Created on 2022-12-14
 *
 * @author mgn
 */
public class Playground extends AbstractAppiumTest {

    @Test
    public void testAppiumRequet() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("Apple iPhone X");
        request.setBrowser(Browsers.mobile_safari);
        //            request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.getDesiredCapabilities().setCapability("appiumVersion", "2.0.0.beta.23");

        WEB_DRIVER_MANAGER.getWebDriver(request);
    }

}
