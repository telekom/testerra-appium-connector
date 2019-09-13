package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.interop.VideoCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverUtils;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MobileVideoGrabber implements VideoCollector {

    private static boolean ALREADY_GRABBED = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileVideoGrabber.class);

    @Override
    public List<Video> getVideos() {

        final List<Video> videos = new ArrayList<>();

        if (ALREADY_GRABBED) {

            // TODO: erku - This is a dirty hack to avoid the same video twice in report.
            // TODO TakeOutOfSessionsEvidencesWorker MUST check FOR Desktop Environment
            return videos;
        }

        final MethodContext methodContext = ExecutionContextController.getCurrentMethodContext();

        if (MobileDriverManager.hasActiveMobileDriver() && methodContext != null) {

            final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

            if (mobileDriver.getActiveDevice() != null) {
                final String pathFromSeeTest = mobileDriver.stopVideoRecord();

                if (StringUtils.isEmpty(pathFromSeeTest)) {
                    LOGGER.error("Stopped video recording, expecting video file name to be returned, but return value was empty.");
                } else {

                    LOGGER.debug("Remote video path returned from seetest is \"" + pathFromSeeTest + "\".");
                    final String suffix = pathFromSeeTest.substring(pathFromSeeTest.lastIndexOf("."));
                    Path tempFile = null;
                    try {
                        tempFile = Files.createTempFile("video_", suffix);
                    } catch (Exception e) {
                        LOGGER.error("Failed to copy Video file.", e);
                    }

                    if (tempFile != null) {

                        LOGGER.info("Created temp file for video \"" + tempFile + "\".");
                        MobileDriverUtils.getRemoteOrLocalFile(mobileDriver, pathFromSeeTest, tempFile);

                        try {
                            final Video video = Report.provideVideo(tempFile.toFile(), Report.Mode.MOVE);
                            videos.add(video);
                        } catch (IOException e) {
                            LOGGER.error("Error generating and providing video.", e);
                        }
                    }
                }
            } else {
                LOGGER.error("Cannot handle video recording, no active device in mobile driver.");
            }
        }

        ALREADY_GRABBED = true;
        return videos;
    }
}
