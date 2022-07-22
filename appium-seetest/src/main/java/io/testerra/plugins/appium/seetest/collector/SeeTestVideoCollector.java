package io.testerra.plugins.appium.seetest.collector;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.Status;
import eu.tsystems.mms.tic.testframework.report.model.context.ExecutionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverSessionsManager;
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
        Loggable {

    //    private final SeleniumBoxHelper seleniumBoxHelper = SeleniumBoxHelper.get();
    private final VideoRequestStorage videoRequestStorage = VideoRequestStorage.get();

    private final boolean VIDEO_ACTIVE_ON_SUCCESS = SeeTestProperties.VIDEO_ON_SUCCESS.asBool();
    private final boolean VIDEO_ACTIVE_ON_FAILED = SeeTestProperties.VIDEO_ON_FAILED.asBool();

    /**
     * This method will be called for each WebDriver that is still active after a Test method
     * Will store the associated {@link VideoRequest} for downloading videos later.
     *
     * @param webDriver @{@link WebDriver}
     */
    @Override
    public void accept(WebDriver webDriver) {
        WebDriverSessionsManager.getSessionContext(webDriver).ifPresent(sessionContext -> {
            // Exklusive session
            if (sessionContext.getParentContext() instanceof ExecutionContext) {
                collectVideoForSessionContext(sessionContext);
            } else {
                // Check if any of the methods allows grabbing videos
                if (sessionContext.readMethodContexts().anyMatch(methodContext -> {
                    if (methodContext.getTestNgResult().isPresent()) {
                        ITestResult testResult = methodContext.getTestNgResult().get();
                        if (!testResult.isSuccess() && VIDEO_ACTIVE_ON_FAILED) {
                            return true;
                        } else if (VIDEO_ACTIVE_ON_SUCCESS) {
                            return true;

                        } else if (testResult.getStatus() == ITestResult.SKIP) {
                            if (methodContext.getStatus() == Status.RETRIED) {
                                return true;
                            }
                        }
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
                .findFirst()
                .ifPresent(videoRequest -> {
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
            log().warn("Unable to download video");
        }

        return Optional.ofNullable(video);
    }
}
