package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.execution.testng.worker.MethodWorker;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.DefaultParameter;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverUtils;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobileVideoWorker extends MethodWorker {
    @Override
    public void run() {
        boolean saveVideoOnFail = PropertyManager
                .getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
        boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);

        if (MobileDriverManager.hasActiveMobileDriver() && methodContext != null) {
            boolean isFailed = methodContext.getStatus() == TestStatusController.Status.FAILED;
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

                            File videoDirectory = Report.VIDEO_DIRECTORY;
                            if (!videoDirectory.exists()) {
                                LOGGER.warn("Folder for videos was not found, creating it: " + videoDirectory);
                                videoDirectory.mkdirs();
                            }

                            String testName = methodContext.getName();
                            String fileName = testName + ".ogg";
                            final String renamedVideoDestinationFileString = videoDirectory.getAbsolutePath() + fileName;
                            File renamedVideoDestinationFile = new File(renamedVideoDestinationFileString);
                            LOGGER.debug("Final destination of video file in xeta report is \"" + renamedVideoDestinationFileString + "\".");
                            try {
                                FileUtils.copyFile(tempFile.toFile(), renamedVideoDestinationFile);
                            } catch (Exception e) {
                                LOGGER.error("Failed to move Video. Origin: " + tempFile + ". Destination: " + renamedVideoDestinationFileString, e);
                                return;
                            }

                            String videoPathForReport = "../../" + renamedVideoDestinationFileString;
                            /*testMethodContainer.addVideoPath(videoPathForReport); */

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
}
