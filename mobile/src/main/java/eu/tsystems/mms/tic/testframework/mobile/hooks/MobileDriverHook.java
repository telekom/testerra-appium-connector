package eu.tsystems.mms.tic.testframework.mobile.hooks;

import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.interop.TestEvidenceCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileScreenshotGrabber;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileVideoGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileDriverHook implements ModuleHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileDriverHook.class);

    @Override
    public void init() {

        MobileDriverManager.registerWebDriverFactory();

        TestEvidenceCollector.registerScreenshotCollector(new MobileScreenshotGrabber());
        TestEvidenceCollector.registerVideoCollector(new MobileVideoGrabber());
    }

    @Override
    public void terminate() {

    }
}
