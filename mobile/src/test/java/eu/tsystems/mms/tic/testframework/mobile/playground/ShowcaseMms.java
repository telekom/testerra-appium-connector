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
import eu.tsystems.mms.tic.testframework.utils.TestUtils;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 14.06.2016.
 */
public class ShowcaseMms {

    @Test
    public void testMmsWeb1() throws Exception {
        TestDevice testDevice = TestDevice.builder("PF28_Huawei_P8lite", MobileOperatingSystem.ANDROID).build();
        MobileDriverManager.deviceStore().addDevice(testDevice);

        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "false");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8889");

        test(testDevice, "com.google.chrome", "chrome");
    }

    @Test
    public void testMmsWeb2() throws Exception {
        TestDevice testDevice = TestDevice.builder("PF28_iPhone_5C_A", MobileOperatingSystem.IOS).build();
        MobileDriverManager.deviceStore().addDevice(testDevice);

        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "false");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_SERVER_PORT, "8890");

        test(testDevice, "com.apple.safari", "safari");
    }

    private void test(TestDevice testDevice, String packageName, String browserName) throws DeviceNotAvailableException {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDevice(testDevice);
        mobileDriver.switchToDevice(testDevice);
        mobileDriver.openDevice();

        for (int i = 0; i < 1000; i++) {
            mobileDriver.closeApplication(packageName);
            mobileDriver.launchApplication(browserName + ":http://www.t-systems-mms.com");

            WebMobileGuiElement toInterviewLink = new WebMobileGuiElement("xpath=//*[@text='Toggle navigation']");
            toInterviewLink.waitForIsPresent();
            toInterviewLink.click();

            WebMobileGuiElement solutionsLink = new WebMobileGuiElement("xpath=//*[@text='Lösungen']");
            solutionsLink.waitForIsPresent();
            solutionsLink.click();

            WebMobileGuiElement productionLink = new WebMobileGuiElement("xpath=//*[@text='Produktion']");
            productionLink.waitForIsPresent();
            productionLink.click();

            WebMobileGuiElement productionHeadline = new WebMobileGuiElement("xpath=//*[@text='Wir machen Ihr Business smart.']");
            productionHeadline.waitForIsDisplayed();

            mobileDriver.swipe(Direction.DOWN, 300, 1000);
            TestUtils.sleep(1000);
            mobileDriver.swipe(Direction.DOWN, 300, 1000);
            TestUtils.sleep(1000);
            mobileDriver.swipe(Direction.DOWN, 300, 1000);

            WebMobileGuiElement cloudApplicationServicesLink = new WebMobileGuiElement("xpath=//*[@text='Cloud Application Service']");
            cloudApplicationServicesLink.click();
        }
    }
}
