/*
 * (C) Copyright T-Systems Multimedia Solutions GmbH 2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Eric Kubenka <Eric.Kubenka@t-systems.com>
 */
package io.testerra.plugins.appium.seetest.utils;

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.Report;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.transfer.ThrowablePackedResponse;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import eu.tsystems.mms.tic.testframework.utils.Timer;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import org.apache.http.HttpStatus;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

/**
 * Will load Video via API
 * Date: 16.04.2020
 * Time: 10:38
 *
 * @author Eric Kubenka
 */
public class VideoLoader implements Loggable {

    private final Report report = Testerra.getInjector().getInstance(Report.class);

    /**
     * When SeeTest is used, the video will be requested, downloaded and linked to report.
     */
    public Video download(VideoRequest videoRequest) {
        Optional<File> videoFile = this.downloadVideo(videoRequest);
        return videoFile.map(file -> report.provideVideo(videoFile.get(), Report.FileMode.MOVE)).orElse(null);
    }

    private Optional<File> downloadVideo(VideoRequest videoRequest) {
        final Timer timer = new Timer(5000, 20_000);
        final ThrowablePackedResponse<File> response = timer.executeSequence(new Timer.Sequence<File>() {
            @Override
            public void run() throws Throwable {
                setSkipThrowingException(true);
                setAddThrowableToMethodContext(false);

                File videoFile = new File(System.getProperty("java.io.tmpdir") + videoRequest.videoName);

                HttpClient client = HttpClient.newBuilder().build();
                SeeTestClientHelper helper = new SeeTestClientHelper();
                String videoDownloadUrl = helper.getVideoDownloadUrl(videoRequest.reportTestId);
                log().info(videoDownloadUrl);
                URI uri = URI.create(videoDownloadUrl);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .timeout(Duration.ofSeconds(30))
                        .header("Authorization", "Bearer " + AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString())
                        .build();

                HttpResponse<Path> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofFile(videoFile.toPath()));
                if (httpResponse.statusCode() != HttpStatus.SC_OK) {
                    log().info("Download status code: {}", httpResponse.statusCode());
                    setPassState(false);
                    log().info("Wait for video is ready for download...");
                } else {
                    setPassState(true);
                }

                setReturningObject(videoFile);
            }
        });
        if (response.isSuccessful()) {
            log().info("Downloaded video file {}", response.getResponse().getName());
            return Optional.of(response.getResponse());
        } else {
            return Optional.empty();
        }
    }
}
