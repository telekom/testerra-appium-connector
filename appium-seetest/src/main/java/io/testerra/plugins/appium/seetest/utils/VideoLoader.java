/*
 * Testerra
 *
 * (C) 2022, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package io.testerra.plugins.appium.seetest.utils;

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.Report;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import eu.tsystems.mms.tic.testframework.utils.Sequence;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class VideoLoader implements Loggable {

    private final Report report = Testerra.getInjector().getInstance(Report.class);

    private final long DOWNLOAD_WAITS_AFTER_RUN_MILLI = 5_000;
    private final long DOWNLOAD_WAITS_TIMEOUT_MILLI = 20_000;

    /**
     * When SeeTest is used, the video will be requested, downloaded and linked to report.
     */
    public Video download(VideoRequest videoRequest) {
        Optional<File> videoFile = this.downloadVideo(videoRequest);
        return videoFile.map(file -> report.provideVideo(videoFile.get(), Report.FileMode.MOVE)).orElse(null);
    }

    private Optional<File> downloadVideo(VideoRequest videoRequest) {
        AtomicBoolean atomicPassed = new AtomicBoolean(false);
        AtomicReference<File> atomicFile = new AtomicReference<>();
        Sequence sequence = new Sequence()
                .setWaitMsAfterRun(DOWNLOAD_WAITS_AFTER_RUN_MILLI)
                .setTimeoutMs(DOWNLOAD_WAITS_TIMEOUT_MILLI);

        sequence.run(() -> {
            try {

                File videoFile = new File(System.getProperty("java.io.tmpdir") + videoRequest.videoName);

                HttpClient client = HttpClient.newBuilder().build();
                SeeTestClientHelper helper = new SeeTestClientHelper();
                String videoDownloadUrl = helper.getVideoDownloadUrl(videoRequest.reportTestId);
                log().info(videoDownloadUrl);
                URI uri = URI.create(videoDownloadUrl);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .timeout(Duration.ofMillis(DOWNLOAD_WAITS_AFTER_RUN_MILLI))
                        .header("Authorization", "Bearer " + AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString())
                        .build();

                HttpResponse<Path> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofFile(videoFile.toPath()));
                if (httpResponse.statusCode() != HttpStatus.SC_OK) {
                    log().info("Download status code: {}", httpResponse.statusCode());
                    log().info("Wait for video is ready for download...");
                } else {
                    atomicFile.set(videoFile);
                    atomicPassed.set(true);
                }
            } catch (IOException | InterruptedException e) {
                log().error("Error at download video file.", e);
            }
            return atomicPassed.get();
        });

        if (atomicPassed.get()) {
            log().info("Downloaded video file {}", atomicFile.get().getName());
            return Optional.of(atomicFile.get());
        } else {
            return Optional.empty();
        }
    }
}
