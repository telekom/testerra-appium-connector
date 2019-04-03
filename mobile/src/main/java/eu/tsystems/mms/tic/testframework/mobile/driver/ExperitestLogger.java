/*
 * Created on 12.10.2017
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.experitest.client.log.ILogger;
import com.experitest.client.log.Level;

/**
 * Experitest Logger impl.
 * 
 * @author sepr
 */
public class ExperitestLogger implements ILogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperitestLogger.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.experitest.client.log.ILogger#log(com.experitest.client.log.Level, java.lang.Object,
     * java.lang.Throwable)
     */
    @Override
    public void log(Level arg0, Object arg1, Throwable arg2) {
        switch (arg0) {
            case OFF:
                break;
            case DEBUG:
                LOGGER.debug(arg1.toString(), arg2);
                break;
            case INFO:
                if (arg1.toString().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                    // FIXME bit of a workaround to not messup report
                    Exception t = new Exception(arg1.toString());
                    t.setStackTrace(new StackTraceElement[0]);
                    LOGGER.debug("Captured XML:", t);
                } else {
                    LOGGER.info(arg1.toString(), arg2);
                }
                break;
            case WARN:
                LOGGER.warn(arg1.toString(), arg2);
                break;
            case ERROR:
                LOGGER.error(arg1.toString(), arg2);
                break;
            case FATAL:
                LOGGER.error(arg1.toString(), arg2);
                break;
            case ALL:
                ;
            default:
                LOGGER.trace(arg1.toString(), arg2);
                break;
        }
    }

}
