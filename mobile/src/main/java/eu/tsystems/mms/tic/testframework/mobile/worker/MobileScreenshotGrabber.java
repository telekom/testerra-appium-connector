package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.internal.Constants;
import eu.tsystems.mms.tic.testframework.interop.ScreenshotCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenDumpType;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

                    final Screenshot screenshot = mobileDriver.prepareNewScreenshot();
                    final String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
                    final String visualDumpFile = moveDumpToFile(visualDump);

                    final MethodContext methodContext = ExecutionContextController.getCurrentMethodContext();
                    if (methodContext != null) {
                        screenshots.add(screenshot);
                        methodContext.steps().getCurrentTestStep().getCurrentTestStepAction().addScreenshots(null, screenshot);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception on handling test failure.", e);
            }
        }

        return screenshots;
    }

    /**
     * Write visualDump to xml file
     *
     * @param visualDump dump text to save.
     *
     * @return Path to file.
     */
    private String moveDumpToFile(String visualDump) {

        String retString = null;
        try {
            String dumpFileName = String.format("dump_%s.xml", System.currentTimeMillis());
            String path = String.format("../../%s%s", Constants.SCREENSHOTS_PATH, dumpFileName);
            Path dumpPath = Paths.get(Report.SCREENSHOTS_DIRECTORY.getAbsolutePath(), dumpFileName);
            final File file = dumpPath.toAbsolutePath().toFile();
            if (!file.createNewFile()) {
                throw new TesterraRuntimeException("File could not be created - " + dumpPath.toString());
            }
            try (PrintWriter out = new PrintWriter(file)) {
                out.write(visualDump);
            }
            retString = path;
        } catch (Exception e) {
            LOGGER.error("Exception writing visual dump to file.", e);
        }

        return retString;
    }

}