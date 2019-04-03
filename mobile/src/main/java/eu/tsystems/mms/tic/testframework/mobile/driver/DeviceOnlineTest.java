/*
 * Created on 15.01.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.ViewOrientation;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;

/**
 * Checks if the device is connected to WIFI by checking an element for a given url (by default xeta mobiles internal
 * web page). If the expected element is not found we run the resetWlanOfActiveDevice routine to connect to wlan and
 * check the element again. If that's not working we return false and another device is being reserved.
 * 
 * @author sepr
 */
public class DeviceOnlineTest extends DeviceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceOnlineTest.class);
    private final String testPageUrl;

    private final String testPageElementLocator;

    /**
     * Default constructor checking our internal test page.
     */
    public DeviceOnlineTest() {
        this("http://q4deumsy0tt.mms-dresden.de", "xpath=//*[@id='burger']");
    }

    /**
     * Alternative constructor using own url and expected element locator.
     * 
     * @param pageUrl url of web page to check connection.
     * @param elementLocator locator of element expected to be displayed on given page.
     */
    public DeviceOnlineTest(String pageUrl, String elementLocator) {
        super("device is online");
        this.testPageUrl = pageUrl;
        this.testPageElementLocator = elementLocator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.tsystems.mms.tic.testframework.mobile.driver.DeviceTest#doDeviceTest(eu.tsystems.mms.tic.testframework.mobile.
     * driver.MobileDriver, eu.tsystems.mms.tic.testframework.mobile.device.TestDevice)
     */
    @Override
    public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {
        mobileDriver.closeSystemPopUps();
        mobileDriver.changeOrientationTo(ViewOrientation.PORTRAIT);
        boolean found = false;
        try {
            mobileDriver.launchApplication(testPageUrl);
            WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(testPageElementLocator);
            found = webMobileGuiElement.waitForIsDisplayed();
        } catch (FennecRuntimeException xre) {
            LOGGER.warn("Launching test page url failed", xre);
        }
        if (!found) {
            mobileDriver.resetWlanOfActiveDevice();
            mobileDriver.launchApplication(testPageUrl);
            WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(testPageElementLocator);
            return webMobileGuiElement.waitForIsDisplayed();
        } else {
            return true;
        }
    }

}
