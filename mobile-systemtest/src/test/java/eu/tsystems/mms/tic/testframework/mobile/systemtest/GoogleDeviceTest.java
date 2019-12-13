package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.ViewOrientation;
import eu.tsystems.mms.tic.testframework.mobile.driver.DeviceTest;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleDeviceTest extends DeviceTest {

    static final Logger logger = LoggerFactory.getLogger(GoogleDeviceTest.class);

    public GoogleDeviceTest() {

        super("test google/online access");
    }

    @Override
    public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {

        mobileDriver.changeOrientationTo(ViewOrientation.PORTRAIT);
        mobileDriver.launchApplication("https://google.com");
        WebMobileGuiElement continueButton = new WebMobileGuiElement("xpath=//*[@nodeName='DIV' and @text='Weiter']");
        WebMobileGuiElement googleLogo = new WebMobileGuiElement("xpath=//*[@id='hplogo']");

        boolean isOnline = googleLogo.waitForIsDisplayed() || continueButton.waitForIsDisplayed();
        if (!isOnline) {
            logger.error("Device is offline!");
            return false;
        }

        return true;
    }
}