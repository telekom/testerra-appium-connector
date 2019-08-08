/*
 * Created on 08.08.2019
 *
 * Copyright(c) 1995 - 2007 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.execution.testng.worker.MethodWorker;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.DefaultParameter;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;

/**
 * MobileBeforeMethodWorker
 * <p>
 * Date: 08.08.2019
 * Time: 14:31
 *
 * @author erku
 */
public class MobileBeforeMethodWorker extends MethodWorker {

    @Override
    public void run() {

        // Early exit if configuration method...
        if (!this.isTest()) {
            return;
        }

        if (MobileDriverManager.hasActiveMobileDriver()) {

            final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

            if (mobileDriver.getActiveDevice() != null) {

                LOGGER.info("Active device:" + mobileDriver.getActiveDevice().getName());
                boolean saveVideoOnFail = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
                boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);

                if (saveVideoOnFail || alwaysSaveVideo) {
                    mobileDriver.startVideoRecord();
                }
            }
        }
    }
}
