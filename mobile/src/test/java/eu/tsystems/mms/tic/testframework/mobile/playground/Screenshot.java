package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TesterraListener.class)
public class Screenshot {


    @BeforeSuite
    public void registerMobileDriver() {

        System.setProperty(MobileProperties.MOBILE_SERVER_HOST, "localhost");
        System.setProperty(MobileProperties.MOBILE_GRID_USER, "xeta_systemtest");
        System.setProperty(MobileProperties.MOBILE_GRID_PASSWORD, "Mas4test#");
        System.setProperty(MobileProperties.MOBILE_GRID_PROJECT, "Testing");
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=android");
        System.setProperty(MobileProperties.MOBILE_SCREENSHOT_QUALITY, "100");
        System.setProperty(TesterraProperties.PROXY_SETTINGS_LOAD, "false");
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDeviceByFilter();
    }


    /**
     * Check Screenshot Quality from Cloud.
     */
    @Test
    public void T01_Screenshot_Quality_And_StepView() {

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

        for (int i = 0; i < 2; i++) {
            TestStep.begin("Screen " + i);
            mobileDriver.takeAfterScreenshot();
            TimerUtils.sleep(5000);
        }
    }

    @Test
    public void T02_Screenshot_Quality_onFailed() {

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

        for (int i = 0; i < 5; i++) {
            TestStep.begin("Screen " + i);
            mobileDriver.takeAfterScreenshot();
            TimerUtils.sleep(5000);
        }

        Assert.fail("Error in Test! :)");
    }

    @Test
    public void T03_Video_Quality_onFailed() {

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        TimerUtils.sleep(5000);
        Assert.fail("Error in Test! :)");
    }
}
