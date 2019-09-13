/*
 * Created on 03.09.2019
 */
package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.interop.TestEvidenceCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;

import java.util.List;

/**
 * MobileTakeInSessionEvidencesWorker
 * <p>
 * Date: 03.09.2019
 * Time: 07:39
 *
 * @author erku
 */
public class MobileScreenshotEvidenceWorker extends AbstractMobileEvidencesWorker {

    @Override
    protected void collect() {

        if (this.isTest()) {
            if (MobileDriverManager.hasActiveMobileDriver()) {
                // get screenshots
                final List<Screenshot> screenshots = TestEvidenceCollector.collectScreenshots();

                if (screenshots != null) {
                    screenshots.forEach(s -> s.errorContextId = methodContext.id);
                    methodContext.screenshots.addAll(screenshots);
                }
            }
        }

    }
}
