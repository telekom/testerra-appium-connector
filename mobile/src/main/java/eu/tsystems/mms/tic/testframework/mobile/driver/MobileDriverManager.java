/*
 * Created on 27.11.2014
 *
 * Copyright(c) 1995 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.mobile.constants.MobileBrowsers;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.experitest.client.Client;
import com.experitest.client.GridClient;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.adapter.MobileGuiElementCoreFactory;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;

/**
 * The mobile client manager for the connection details
 *
 * @author rnhb
 */
public final class MobileDriverManager {
//
//    static {
//        //        TODO rework with jfennec/not needed with open source jfennec
//        //        XetaLicense.checkLicense();
//    }

    /**
     * The working directory.
     */

    /**
     * Thread local map to allow multiple silk mobile agents
     */
    private static final ThreadLocal<MobileDriver> DRIVERS = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileDriverManager.class);

    private static DeviceStore deviceStore;

    private static int port;

    static {
        deviceStore = new DeviceStore();
        port = 8889;
//        port = Integer.parseInt(
//                PropertyManager.getProperty(MobileProperties.MOBILE_SERVER_PORT, DefaultParameter.MOBILE_SERVER_PORT));
    }

    /**
     * @deprecated Use parameterless method instead, only one MobileDriver per Thread
     */
    @Deprecated
    public static MobileDriver getMobileDriver(WebDriver webDriver) {
        return getMobileDriver();
    }

    public static void registerWebDriverFactory() {
        MobileGuiElementCoreFactory mobileGuiElementCoreFactory = new MobileGuiElementCoreFactory();
        GuiElement.registerGuiElementCoreFactory(mobileGuiElementCoreFactory, MobileBrowsers.mobile_chrome,
                MobileBrowsers.mobile_safari);
        WebDriverManager.registerWebDriverFactory(new WebDriverAdapterFactory(), MobileBrowsers.mobile_chrome,
                MobileBrowsers.mobile_safari);
    }

    public static boolean hasActiveMobileDriver() {
        return DRIVERS.get() != null;
    }

    /**
     * @deprecated use parameterless method instead
     */
    @Deprecated
    public static MobileDriver getMobileDriver(String sessionKey) {
        return getMobileDriver();
    }

    public static void main(String[] args) {
        MobileDriver mobileDriver = DRIVERS.get();
    }

    /**
     * Get MobileDriver for current thread. Normally the same object is used for the complete thread.
     *
     * @return {@link MobileDriver}
     */
    public static MobileDriver getMobileDriver() {
        MobileDriver mobileDriver = DRIVERS.get();
        if (mobileDriver != null && mobileDriver.isValid()) {
            // A Driver was already instantiated
            LOGGER.debug("MobileDriver already instantiated, returning the existing instance.");
            return mobileDriver;
        } else {
            String seeTestHost = PropertyManager.getProperty(MobileProperties.MOBILE_SERVER_HOST,
                    DefaultParameter.MOBILE_SERVER_HOST).trim();
            // create a new Driver instance with a seeTest Client underneath
            if (seeTestHost.startsWith("http")) {
                GridClient gridClient;
                String accessKey = PropertyManager.getProperty(MobileProperties.MOBILE_GRID_ACCESS_KEY);
                if (StringUtils.isEmpty(accessKey)) {
                    String user = PropertyManager.getProperty(MobileProperties.MOBILE_GRID_USER);
                    String password = PropertyManager.getProperty(MobileProperties.MOBILE_GRID_PASSWORD);
                    String project = PropertyManager.getProperty(MobileProperties.MOBILE_GRID_PROJECT);
                    LOGGER.info(
                            "Creating GridMobileDriver, connecting to SeeTest Grid at {} with user {} for project {}.",
                            new Object[] { seeTestHost, user, project });
                    gridClient = new GridClient(user, password, project, seeTestHost);
                } else {
                    LOGGER.info("Creating GridMobileDriver, connecting to SeeTest Grid at {} with access key.",
                            seeTestHost);
                    gridClient = new GridClient(accessKey, seeTestHost);
                }
                mobileDriver = new GridMobileDriver(gridClient);
            } else {
                LOGGER.info("Creating ExecutorMobileDriver, connecting to SeeTest Executor on {}:{}", seeTestHost,
                        port);
                Client client = new Client(seeTestHost, port);
                mobileDriver = new ExecutorMobileDriver(client);
            }

            DRIVERS.set(mobileDriver);
            ScreenshotTracker.startTracking();
            return mobileDriver;
        }
    }

    public static DeviceStore deviceStore() {
        return deviceStore;
    }

    /**
     * Reset MobileDriver for current thread.
     * Gives you a completely new MobileDriver instance on next getMobileDriver() call.
     */
    public static void releaseDriverFromThread() {
        MobileDriver mobileDriver = DRIVERS.get();
        if (mobileDriver != null) {
            mobileDriver.release();
            DRIVERS.remove();
        }
    }

    /**
     * Hide default constructor.
     */
    private MobileDriverManager() {

    }
}