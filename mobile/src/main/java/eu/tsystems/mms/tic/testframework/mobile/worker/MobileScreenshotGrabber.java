package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.interop.ScreenshotCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenDumpType;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MobileScreenshotGrabber implements ScreenshotCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileScreenshotGrabber.class);

    @Override
    public List<Screenshot> takeScreenshots() {

        if (MobileDriverManager.hasActiveMobileDriver()) {
            try {
                final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                if (mobileDriver.getActiveDevice() != null) {
                    final String screenshot = mobileDriver.prepareNewScreenshot();
                    final String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
                    final String visualDumpFile = moveDumpToFile(visualDump);
                    //                                methodContext.addScreenshotPath(screenshot, visualDumpFile);
                    //                                methodContext.steps().getCurrentTestStep().getCurrentTestStepAction().addScreenshots(null, screenshot);
                }
            } catch (Exception e) {
                LOGGER.error("Exception on handling test failure.", e);
            }
        }

        // TODO - return list
        return null;
    }

    // TODO - Old sbke implementation before chnage everything to test evidence collector in testerra
    //    public void run() {
    //
    //
    //        if (MobileDriverManager.hasActiveMobileDriver()) {
    //            try {
    //                MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
    //                if (methodContext != null && mobileDriver.getActiveDevice() != null) {
    //                    String screenshot = mobileDriver.prepareNewScreenshot();
    //                    String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
    //                    String visualDumpFile = moveDumpToFile(visualDump);
    //                    //                                methodContext.addScreenshotPath(screenshot, visualDumpFile);
    //                    //                                methodContext.steps().getCurrentTestStep().getCurrentTestStepAction().addScreenshots(null, screenshot);
    //                }
    //            } catch (Exception e) {
    //                LOGGER.error("Exception on handling test failure.", e);
    //            }
    //        }
    //    }
    //

    /**
     * Write visualDump to xml file
     *
     * @param visualDump dump text to save.
     *
     * @return Path to file.
     */
    private String moveDumpToFile(String visualDump) {
        String retString = null;
        //        TODO rework with testerra ->
        //                try {
        //            String dumpFileName = String.format("dump_%s.xml", System.currentTimeMillis());
        //            String path = String.format("../../%s%s", Constants.SCREENSHOTS_PATH, dumpFileName);
        //            Path dumpPath = Paths.get(ReportUtils.getScreenshotsPath(), dumpFileName);
        //            final File file = dumpPath.toAbsolutePath().toFile();
        //            if (!file.createNewFile()) {
        //                throw new TesterraRuntimeException("File could not be created - " + dumpPath.toString());
        //            }
        //            try (PrintWriter out = new PrintWriter(file)) {
        //                out.write(visualDump);
        //            }
        //            retString = path;
        //        } catch (Exception e) {
        //            LOGGER.error("Exception writing visual dump to file.", e);
        //        }
        return retString;
    }

}