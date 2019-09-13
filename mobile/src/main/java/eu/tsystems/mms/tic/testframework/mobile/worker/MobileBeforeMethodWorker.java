/*
 * Created on 08.08.2019
 *
 * Copyright(c) 1995 - 2007 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.events.TesterraEvent;
import eu.tsystems.mms.tic.testframework.events.TesterraEventListener;
import eu.tsystems.mms.tic.testframework.events.TesterraEventType;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
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
public class MobileBeforeMethodWorker implements TesterraEventListener, Loggable, MobileVideoHandlerInterface {

    @Override
    public void fireEvent(TesterraEvent testerraEvent) {

        if (testerraEvent.getTesterraEventType() == TesterraEventType.TEST_METHOD_START) {

            if (MobileDriverManager.hasActiveMobileDriver()) {

                final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

                if (mobileDriver.getActiveDevice() != null) {

                    log().info("Active device:" + mobileDriver.getActiveDevice().getName());

                    if (isSaveVideoOnFail() || isAlwaysSaveVideo()) {
                        mobileDriver.startVideoRecord();
                    }
                }
            }
        }
    }
}
