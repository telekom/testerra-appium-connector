package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.execution.testng.worker.MethodWorker;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenDumpType;

public class MobileScreenshotWorker extends MethodWorker {
    @Override
    public void run() {
        if (MobileDriverManager.hasActiveMobileDriver()) {
            try {
                MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                if (methodContext != null && mobileDriver.getActiveDevice() != null) {
                    String screenshot = mobileDriver.prepareNewScreenshot();
                    String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
                    String visualDumpFile = moveDumpToFile(visualDump);
                    //                                methodContext.addScreenshotPath(screenshot, visualDumpFile);
                    //                                methodContext.steps().getCurrentTestStep().getCurrentTestStepAction().addScreenshots(null, screenshot);
                }
            } catch (Exception e) {
                LOGGER.error("Exception on handling test failure.", e);
            }
        }
    }


    /**
     * Write visualDump to xml file
     *
     * @param visualDump dump text to save.
     * @return Path to file.
     */
    private String moveDumpToFile(String visualDump) {
        String retString = null;
        //        TODO rework with jfennec ->
        //                try {
        //            String dumpFileName = String.format("dump_%s.xml", System.currentTimeMillis());
        //            String path = String.format("../../%s%s", Constants.SCREENSHOTS_PATH, dumpFileName);
        //            Path dumpPath = Paths.get(ReportUtils.getScreenshotsPath(), dumpFileName);
        //            final File file = dumpPath.toAbsolutePath().toFile();
        //            if (!file.createNewFile()) {
        //                throw new FennecRuntimeException("File could not be created - " + dumpPath.toString());
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