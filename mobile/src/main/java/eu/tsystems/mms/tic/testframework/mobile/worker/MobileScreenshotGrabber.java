package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.interop.ScreenshotCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenDumpType;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStepController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MobileScreenshotGrabber implements ScreenshotCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileScreenshotGrabber.class);

    @Override
    public List<Screenshot> takeScreenshots() {

        final List<Screenshot> screenshots = new ArrayList<>();

        if (MobileDriverManager.hasActiveMobileDriver()) {
            try {
                final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

                if (mobileDriver.getActiveDevice() != null) {

                    final File screenshotFile = mobileDriver.prepareNewScreenshot();
                    final File visualDumpFile = this.getVisualDump(mobileDriver);
                    final Screenshot screenshot = Report.provideScreenshot(screenshotFile, visualDumpFile, Report.Mode.MOVE, null);

                    screenshots.add(screenshot);
                    TestStepController.addScreenshotsToCurrentAction(null, screenshot);
                }
            } catch (Exception e) {
                LOGGER.error("Exception on handling test failure.", e);
            }
        }

        return screenshots;
    }

    /**
     * Write visualDump to xml file
     */
    private File getVisualDump(MobileDriver mobileDriver) {

        final String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());

        final String dumpFileName = String.format("dump_%s.xml", System.currentTimeMillis());
        final Path tmpDumpPath = Paths.get(System.getProperty("java.io.tmpdir"), dumpFileName);
        final File tmpDumpFile = tmpDumpPath.toAbsolutePath().toFile();

        try {
            if (!tmpDumpFile.createNewFile()) {
                throw new TesterraRuntimeException("File could not be created - " + tmpDumpPath.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Exception writing visual dump to file.", e);
        }

        try (PrintWriter out = new PrintWriter(tmpDumpFile)) {
            out.write(visualDump);
        } catch (FileNotFoundException e) {
            LOGGER.error("Exception writing visual dump to file.", e);

        }

        return tmpDumpFile;
    }
}