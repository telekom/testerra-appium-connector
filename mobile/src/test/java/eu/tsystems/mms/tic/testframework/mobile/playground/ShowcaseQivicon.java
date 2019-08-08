package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 13.07.2016.
 */
@Listeners(TesterraListener.class)
public class ShowcaseQivicon {

    @Test
    public void testQivicon() throws Exception {
        TestStep.begin("Reserve Device");

        TestDevice moto3G = TestDevice.builder("PF28_G_4", MobileOperatingSystem.ANDROID).build();
        MobileDriverManager.deviceStore().addDevice(moto3G);

        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "true");

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDevice(moto3G);
        mobileDriver.switchToDevice(moto3G);
        mobileDriver.openDevice();

        TestStep.begin("Launch Clean Application");
        mobileDriver.closeApplication("cloud:de.telekom.smarthomeb2c/.ui.StartScreenActivity");
        mobileDriver.clearApplicationData("cloud:de.telekom.smarthomeb2c/.ui.StartScreenActivity");
        mobileDriver.launchApplication("cloud:de.telekom.smarthomeb2c/.ui.StartScreenActivity");

        TestStep.begin("Login");
        NativeMobileGuiElement closeButton = new NativeMobileGuiElement("xpath=//*[@id='close_button']/*");
        closeButton.setTimeoutInSeconds(10);
        if (closeButton.waitForIsDisplayed()) {
            closeButton.click();
        }

        NativeMobileGuiElement loginField = new NativeMobileGuiElement("xpath=//*[@id='username_input']");
        NativeMobileGuiElement passwordField = new NativeMobileGuiElement("xpath=//*[@id='password_input']");
        NativeMobileGuiElement loginButton = new NativeMobileGuiElement("xpath=//*[@id='login_button']");

        loginField.waitForIsDisplayed();
        loginField.sendText("a.gorzel@t-online.de");
        passwordField.sendText("555Nase555");
        loginButton.click();

        NativeMobileGuiElement errorMessage = new NativeMobileGuiElement("xpath=//*[@id='message']");
        errorMessage.setTimeoutInSeconds(20);
        if (errorMessage.waitForIsDisplayed()) {
            Assert.fail("Login failed: " + errorMessage.getText());
        }
    }
}
