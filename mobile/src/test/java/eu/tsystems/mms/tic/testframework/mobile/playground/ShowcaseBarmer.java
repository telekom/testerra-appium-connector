package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.utils.TestUtils;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 07.06.2016.
 */
public class ShowcaseBarmer {

    @Test(enabled = false)
    public void testBarmer() throws Exception {
        TestDevice moto3G = TestDevice.builder("PF28_Huawei_P8lite", MobileOperatingSystem.ANDROID).build();
        //TestDevice moto3G = TestDevice.builder("PF28_Moto_G3", MobileOperatingSystem.ANDROID).build();
        MobileDriverManager.deviceStore().addDevice(moto3G);

        //PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project2");
        PropertyManager.getThreadLocalProperties().setProperty(MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS, "false");

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDevice(moto3G);
        mobileDriver.switchToDevice(moto3G);
        mobileDriver.openDevice();


        mobileDriver.closeApplication("de.barmergek.arztnavi");

        for (int i = 0; i < 1000; i++) {
            // open apps
            NativeMobileGuiElement appsButton = new NativeMobileGuiElement("xpath=//*[@contentDescription='Apps']");
            if (appsButton.isDisplayed()) {
              //  appsButton.click();
            }

            // launch arztnavi
            NativeMobileGuiElement arztNaviButton = new NativeMobileGuiElement("xpath=//*[@text='Arztnavi' and @onScreen='true']");
            arztNaviButton.waitForIsDisplayed();
            arztNaviButton.click();

           /* // go to arztsuche
            NativeMobileGuiElement arztSucheButton = new NativeMobileGuiElement("xpath=//*[@text='Arzt suchen']");
            arztSucheButton.waitForIsDisplayed();
            arztSucheButton.click();*/

            // go to diagnosen dolmetscher
            NativeMobileGuiElement diagnosenDolmetscherButton = new NativeMobileGuiElement("xpath=//*[@text='Diagnosen-Dolmetscher']");
            diagnosenDolmetscherButton.waitForIsDisplayed();
            diagnosenDolmetscherButton.click();

            NativeMobileGuiElement infoButton = new NativeMobileGuiElement("xpath=//*[@id='rightNavInfoButton']");
            infoButton.waitForIsDisplayed();
            infoButton.click();

            NativeMobileGuiElement prescriptionImage = new NativeMobileGuiElement("xpath=//*[@class='android.widget.Image']");
            prescriptionImage.assertIsDisplayed();

            mobileDriver.swipe(Direction.DOWN,500,200);

            NativeMobileGuiElement doneButton = new NativeMobileGuiElement("xpath=//*[@id='rightNavDarkButton']");
            doneButton.waitForIsDisplayed();
            doneButton.click();

            NativeMobileGuiElement menuButton = new NativeMobileGuiElement("xpath=//*[@id='menuToggle']");
            menuButton.waitForIsDisplayed();
            menuButton.click();

            NativeMobileGuiElement startPageButton = new NativeMobileGuiElement("xpath=//*[@text='Startseite']");
            startPageButton.waitForIsDisplayed();
            startPageButton.click();

            TestUtils.sleep(1000);
            mobileDriver.closeApplication("de.barmergek.arztnavi");
        }
    }
}
