package io.testerra.plugins.appium.seetest.collector;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.Status;
import eu.tsystems.mms.tic.testframework.report.model.context.ExecutionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import io.testerra.plugins.appium.seetest.request.VideoRequestStorage;
import io.testerra.plugins.appium.seetest.utils.SeeTestProperties;
import io.testerra.plugins.appium.seetest.utils.VideoLoader;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public class SeeTestVideoCollector implements
        Consumer<WebDriver>,
        WebDriverManagerProvider,
        Loggable {

    private final VideoRequestStorage videoRequestStorage = VideoRequestStorage.get();

    private final boolean VIDEO_ACTIVE_ON_SUCCESS = SeeTestProperties.VIDEO_ON_SUCCESS.asBool();
    private final boolean VIDEO_ACTIVE_ON_FAILED = SeeTestProperties.VIDEO_ON_FAILED.asBool();

    /**
     * This method will be called for each WebDriver that is still active after a Test method
     * Will store the associated {@link VideoRequest} for downloading videos later.
     */
    @Override
    public void accept(WebDriver webDriver) {
        WEB_DRIVER_MANAGER.getSessionContext(webDriver).ifPresent(sessionContext -> {
            // Exklusive session
            if (sessionContext.getParentContext() instanceof ExecutionContext) {
                collectVideoForSessionContext(sessionContext);
            } else {
                // Check if any of the methods allows grabbing videos
                if (sessionContext.readMethodContexts().anyMatch(methodContext -> {
                    if (methodContext.getTestNgResult().isPresent()) {
                        ITestResult testResult = methodContext.getTestNgResult().get();
                        return !testResult.isSuccess() && VIDEO_ACTIVE_ON_FAILED
                                || VIDEO_ACTIVE_ON_SUCCESS
                                || testResult.getStatus() == ITestResult.SKIP && methodContext.getStatus() == Status.RETRIED;
                    }
                    return false;
                })) {
                    collectVideoForSessionContext(sessionContext);
                }
                ;
            }
        });
    }

    protected void collectVideoForSessionContext(SessionContext sessionContext) {
        // Check if there exists a video request for this session
        videoRequestStorage.list().stream()
                .filter(videoRequest -> videoRequest.sessionContext == sessionContext)
                .forEach(videoRequest -> {
                    this.downloadAndLinkVideo(videoRequest);
                    videoRequestStorage.remove(videoRequest);
                });
    }

    /**
     * Downloads the video and add its to the {@link SessionContext}
     */
    private Optional<Video> downloadAndLinkVideo(final VideoRequest videoRequest) {
        Video video = new VideoLoader().download(videoRequest);

        if (video != null) {
            videoRequest.sessionContext.setVideo(video);
        } else {
            log().warn("Unable to download video {}", videoRequest.videoName);
        }

        return Optional.ofNullable(video);
    }
}
