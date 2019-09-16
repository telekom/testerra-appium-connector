/*
 * Created on 13.09.2019
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.DefaultParameter;

/**
 * MobileVideoHandlerInterface
 * <p>
 * Date: 13.09.2019
 * Time: 09:05
 *
 * @author erku
 */
public interface MobileVideoHandlerInterface {

    boolean saveVideoOnFail = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
    boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);

    default boolean isSaveVideoOnFail() {
        return saveVideoOnFail;
    }

    default boolean isAlwaysSaveVideo() {
        return alwaysSaveVideo;
    }

}
