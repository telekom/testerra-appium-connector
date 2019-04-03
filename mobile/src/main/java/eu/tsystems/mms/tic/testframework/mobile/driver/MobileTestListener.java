package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import eu.tsystems.mms.tic.testframework.execution.testng.worker.MethodWorker;
import eu.tsystems.mms.tic.testframework.internal.Constants;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.XetaListener;
import eu.tsystems.mms.tic.testframework.report.model.TestMethodContainer;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.report.utils.ReportUtils;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by rnhb on 06.04.2016.
 */
//FIXME complete rework or move to different module
public class MobileTestListener implements ITestListener, IConfigurationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileTestListener.class);
    private static boolean registeredInXetaListener = false;

    @Override
    public void onTestStart(ITestResult result) {
        if (MobileDriverManager.hasActiveMobileDriver()) {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
            if (mobileDriver.getActiveDevice() != null) {
                LOGGER.info("Active device:" + mobileDriver.getActiveDevice().getName());
                boolean saveVideoOnFail = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
                boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);

                if (saveVideoOnFail || alwaysSaveVideo) {
                    mobileDriver.startVideoRecord();
                }
            }
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        if (!registeredInXetaListener) {
            registeredInXetaListener = true;
            MobileDriverManager.registerWebDriverFactory();
            boolean saveVideoOnFail = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
            boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);
            if (saveVideoOnFail || alwaysSaveVideo) {
                XetaListener.addAdditionalMethodWorker(new MethodWorker() {
                    @Override
                    public void run() {
                        TestMethodContainer testMethodContainer = ExecutionContextController.getTestMethodContainerForCurrentTestResult();
                        if (MobileDriverManager.hasActiveMobileDriver() && testMethodContainer != null) {
                            boolean isFailed = testMethodContainer.getStatus() == TestStatusController.Status.FAILED;
                            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                            if (mobileDriver.getActiveDevice() != null) {
                                String pathFromSeeTest = mobileDriver.stopVideoRecord();
                                if ((isFailed && saveVideoOnFail) || alwaysSaveVideo) {
                                    if (StringUtils.isEmpty(pathFromSeeTest)) {
                                        LOGGER.error("Stopped video recording, expecting video file name to be returned, but return value was empty.");
                                    } else {
                                        LOGGER.debug("Remote video path returned from seetest is \"" + pathFromSeeTest + "\".");
                                        String suffix = pathFromSeeTest.substring(pathFromSeeTest.lastIndexOf("."));
                                        Path tempFile = null;
                                        try {
                                            tempFile = Files.createTempFile("video_", suffix);
                                        } catch (Exception e) {
                                            LOGGER.error("Failed to copy Video file.", e);
                                        }
                                        if (tempFile != null) {
                                            LOGGER.info("Created temp file for video \"" + tempFile + "\".");
                                            MobileDriverUtils.getRemoteOrLocalFile(mobileDriver, pathFromSeeTest, tempFile);

                                            String videoDirPath = ReportUtils.getVideosPath();
                                            File screenshotFolder = new File(videoDirPath);
                                            if (!screenshotFolder.exists()) {
                                                LOGGER.warn("Folder for videos was not found, creating it: " + screenshotFolder);
                                                screenshotFolder.mkdirs();
                                            }

                                            String testName = testMethodContainer.getTestName();
                                            String fileName = testName + ".ogg";
                                            final String renamedVideoDestinationFileString = videoDirPath + fileName;
                                            File renamedVideoDestinationFile = new File(renamedVideoDestinationFileString);
                                            LOGGER.debug("Final destination of video file in xeta report is \"" + renamedVideoDestinationFileString + "\".");
                                            try {
                                                FileUtils.copyFile(tempFile.toFile(), renamedVideoDestinationFile);
                                            } catch (Exception e) {
                                                LOGGER.error("Failed to move Video. Origin: " + tempFile + ". Destination: " + renamedVideoDestinationFileString, e);
                                                return;
                                            }

                                            String videoPathForReport = "../../" + renamedVideoDestinationFileString;
                                            testMethodContainer.addVideoPath(videoPathForReport);
                                        }
                                    }
                                } else {
                                    LOGGER.debug("Video of this test should not be saved, according to properties.");
                                }
                            } else {
                                LOGGER.error("Cannot handle video recording, no active device in mobile driver.");
                            }
                        }
                    }
                });
            }

            XetaListener.addAdditionalMethodWorker(new MethodWorker() {
                @Override
                public void run() {
                    if (MobileDriverManager.hasActiveMobileDriver()) {
                        try {
                            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                            TestMethodContainer testMethodContainer = ExecutionContextController.getTestMethodContainerForCurrentTestResult();
                            if (testMethodContainer != null && mobileDriver.getActiveDevice() != null) {
                                String screenshot = mobileDriver.prepareNewScreenshot();
                                String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
                                String visualDumpFile = moveDumpToFile(visualDump);
                                testMethodContainer.addScreenshotPath(screenshot, visualDumpFile);
                                testMethodContainer.steps().getCurrentTestStep().getCurrentTestStepAction().addScreenshots(null, screenshot);
                            }
                        } catch (Exception e) {
                            LOGGER.error("Exception on handling test failure.", e);
                        }
                    }
                }
            });
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

    @Override
    public void onFinish(ITestContext context) {
        try {
            if (MobileDriverManager.hasActiveMobileDriver()) {
                ScreenshotTracker.stopTracking();
                MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                mobileDriver.release();
            }
        } catch (Exception e) {
            LOGGER.error("Exception on handling test finish.", e);
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {

    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {

    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {

    }
}
