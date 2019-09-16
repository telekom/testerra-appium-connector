package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 14.06.2016.
 */
public class ShowcaseMms extends TesterraTest {

    @Test(enabled = false)
    public void testMmsWeb1() throws Exception {
        TestDevice testDevice = TestDevice.builder("PF28_Huawei_P8lite", MobileOperatingSystem.ANDROID).build();
        MobileDriverManager.deviceStore().addDevice(testDevice);

        //PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "true");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8889");

        test(testDevice, "com.google.chrome", "chrome");
    }

    @Test(enabled = false)
    public void testMmsWeb2() throws Exception {
        TestDevice testDevice = TestDevice.builder("Apple iPhone 6 (Nr 1)", MobileOperatingSystem.IOS).build();
        //        MobileDriverManager.deviceStore().addDevice(testDevice);

        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "false");
        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8890");

        test(testDevice, "com.apple.safari", "safari");
    }

    @Test
    public void testMmsWeb3() throws Exception {
        TestDevice testDevice = TestDevice.builder("Apple iPhone 6 (Nr 6)", MobileOperatingSystem.IOS).build();
        //        MobileDriverManager.deviceStore().addDevice(testDevice);

        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "true");
        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8890");

        test(testDevice, "com.apple.safari", "safari");
    }

    @Test
    public void testMmsWeb4() throws Exception {
        TestDevice testDevice = TestDevice.builder("Apple iPhone 6 (Nr 6)", MobileOperatingSystem.IOS).build();
        //        MobileDriverManager.deviceStore().addDevice(testDevice);

        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "false");
        //        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8890");

        test(testDevice, "com.apple.safari", "safari");
        Assert.fail("This test must fail");
    }

    //    @AfterMethod
    //    public void releaseDevice(){
    //
    //        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
    //        mobileDriver.release();
    //    }

    private void test(TestDevice testDevice, String packageName, String browserName) throws DeviceNotAvailableException {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        if (mobileDriver.getActiveDevice() == null) {
            mobileDriver.reserveDevice(testDevice);
            mobileDriver.switchToDevice(testDevice);
            mobileDriver.openDevice();
        }

        //        TestDevice testDevice1 = mobileDriver.reserveDeviceByFilter();

        for (int i = 1; i < 3; i++) {
            TestStep.begin("Teststep " + i);
            mobileDriver.closeApplication(packageName);
            mobileDriver.launchApplication(browserName + ":http://www.t-systems-mms.com");

            WebMobileGuiElement toInterviewLink = new WebMobileGuiElement("xpath=//*[@text='Toggle navigation']");
            toInterviewLink.waitForIsPresent();
            toInterviewLink.click();
            toInterviewLink.takeScreenshot();

            WebMobileGuiElement solutionsLink = new WebMobileGuiElement("xpath=//*[@text='expertise']");
            solutionsLink.waitForIsPresent();
            solutionsLink.click();

            WebMobileGuiElement digInnoLab = new WebMobileGuiElement("xpath=//*[@text='Business IT- Management']");
            digInnoLab.waitForIsPresent();
            digInnoLab.click();

            WebMobileGuiElement digInnoLabHeadline = new WebMobileGuiElement("xpath=//*[@text='Cloud Computing & Agile Softwareentwicklung']]");
            digInnoLabHeadline.swipeWhileNotFound(Direction.DOWN.toString(), 500, 250, 250, 5, false);
            //            digInnoLabHeadline.click();
            //            mobileDriver.swipe(Direction.DOWN, 300, 1000);
            //            TestUtils.sleep(1000);
            //            mobileDriver.swipe(Direction.DOWN, 300, 1000);
            //            TestUtils.sleep(1000);
            //            mobileDriver.swipe(Direction.DOWN, 300, 1000);

            WebMobileGuiElement cloudApplicationServicesLink = new WebMobileGuiElement("xpath=//*[@text='Zum Fachartikel']");
            cloudApplicationServicesLink.swipeWhileNotFound(Direction.DOWN.toString(), 500, 250, 250, 5, false);
            cloudApplicationServicesLink.click();
        }
    }
}
