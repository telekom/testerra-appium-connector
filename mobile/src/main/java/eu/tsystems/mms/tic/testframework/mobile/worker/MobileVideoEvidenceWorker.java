/*
 * Created on 03.09.2019
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.interop.TestEvidenceCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;

import java.util.List;

/**
 * MobileTakeInSessionEvidencesWorker
 * <p>
 * Date: 03.09.2019
 * Time: 07:39
 *
 * @author erku
 */
public class MobileVideoEvidenceWorker extends AbstractMobileEvidencesWorker implements MobileVideoHandlerInterface {

    @Override
    public void run() {

        if(isTest()) {
            // Grab videos when...
            // 1. ... test is failed and "save video on failed" is true
            // 2. ... test is NOT failed, but "always save video" is true
            if (isFailed() && isSaveVideoOnFail() || isAlwaysSaveVideo()) {
                Object attribute = testResult.getAttribute(SharedTestResultAttributes.failsFromCollectedAssertsOnly);
                if (attribute != Boolean.TRUE) {
                    this.collect();
                }
            } else if (isSkipped()) {
                if (methodContext.status == TestStatusController.Status.FAILED_RETRIED) {
                    this.collect();
                }
            }
        }
    }


    @Override
    protected void collect() {

        if (MobileDriverManager.hasActiveMobileDriver()) {

            // get videos
            final List<Video> videos = TestEvidenceCollector.collectVideos();

            if (videos != null) {
                videos.forEach(video -> video.errorContextId = methodContext.id);
                methodContext.videos.addAll(videos);
            }
        }
    }
}
