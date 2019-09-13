package eu.tsystems.mms.tic.testframework.mobile.hooks;

import eu.tsystems.mms.tic.testframework.common.TesterraCommons;
import eu.tsystems.mms.tic.testframework.events.TesterraEventService;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.interop.TestEvidenceCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenshotTracker;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileBeforeMethodWorker;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileScreenshotEvidenceWorker;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileScreenshotGrabber;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileVideoEvidenceWorker;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileVideoGrabber;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eric.Kubenka@t-systems.com
 */
public class MobileDriverHook implements ModuleHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileDriverHook.class);

    @Override
    public void init() {

        // init
        TesterraCommons.init();
        MobileDriverManager.registerWebDriverFactory();

        // Register Start Video!
        TesterraEventService.addListener(new MobileBeforeMethodWorker());

        // collectors for shutdown
        TestEvidenceCollector.registerScreenshotCollector(new MobileScreenshotGrabber());
        TestEvidenceCollector.registerVideoCollector(new MobileVideoGrabber());

        // shutdown Worker
        TesterraListener.registerAfterMethodWorker(MobileScreenshotEvidenceWorker.class);
        TesterraListener.registerAfterMethodWorker(MobileVideoEvidenceWorker.class);
    }

    @Override
    public void terminate() {

        try {
            if (MobileDriverManager.hasActiveMobileDriver()) {
                ScreenshotTracker.stopTracking();
                final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                mobileDriver.release();
            }
        } catch (final Exception e) {
            LOGGER.error("Exception on handling test finish.", e);
        }
    }
}
