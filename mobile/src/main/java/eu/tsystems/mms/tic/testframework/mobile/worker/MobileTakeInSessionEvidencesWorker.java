/*
 * Created on 03.09.2019
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.interop.TestEvidenceCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
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
public class MobileTakeInSessionEvidencesWorker extends AbstractMobileEvidencesWorker {

    void collect() {

        if (MobileDriverManager.hasActiveMobileDriver()) {

            // get screenshots and videos
            final List<Screenshot> screenshots = TestEvidenceCollector.collectScreenshots();
            final List<Video> videos = TestEvidenceCollector.collectVideos();

            if (screenshots != null) {
                screenshots.forEach(s -> s.errorContextId = methodContext.id);
                methodContext.screenshots.addAll(screenshots);
            }

            if (videos != null) {
                videos.forEach(video -> video.errorContextId = methodContext.id);
                methodContext.videos.addAll(videos);
            }
        }
    }

}
