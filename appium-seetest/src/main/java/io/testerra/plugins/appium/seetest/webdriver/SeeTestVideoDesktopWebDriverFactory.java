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
package io.testerra.plugins.appium.seetest.webdriver;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.AppiumDriver;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import io.testerra.plugins.appium.seetest.request.VideoRequestStorage;
import io.testerra.plugins.appium.seetest.utils.SeeTestClientHelper;
import org.openqa.selenium.WebDriver;

import java.util.function.Consumer;

/**
 * Created on 21.07.2022
 *
 * @author mgn
 */
public class SeeTestVideoDesktopWebDriverFactory implements
        Loggable,
        Consumer<WebDriver>,
        WebDriverManagerProvider {

    private final SeeTestClientHelper seeTestClientHelper = new SeeTestClientHelper();
    private final VideoRequestStorage videoRequestStorage = VideoRequestStorage.get();

    // After startup
    @Override
    public void accept(WebDriver webDriver) {
        WEB_DRIVER_MANAGER.getSessionContext(webDriver).ifPresent(sessionContext -> {
            WebDriverRequest webDriverRequest = sessionContext.getWebDriverRequest();

            sessionContext.getRemoteSessionId().ifPresent(remoteSessionId -> {
                webDriverRequest.getServerUrl().ifPresent(url -> {
                    if (this.seeTestClientHelper.isSeeTestUsed(sessionContext)) {
                        // remoteSessionId could look like CLOUD-SID:2022-07-22_12-45-34-7e28799b-6afb-40d0-a898-9e74f93b3a6c
                        String fileName = remoteSessionId.toLowerCase()
                                .replace(":", "_")
                                .replace(".", "_")
                                + ".mp4";

                        String reportTestId = WEB_DRIVER_MANAGER.unwrapWebDriver(webDriver, AppiumDriver.class)
                                .map(driver -> driver.getCapabilities().getCapability("reportTestId").toString())
                                .orElse("na");

                        final VideoRequest videoRequest = new VideoRequest(sessionContext, fileName, reportTestId);

                        // store it.
                        videoRequestStorage.store(videoRequest);
                    } else {
                        log().info("It seems you're not using SeeTest as Appium host.");
                    }
                });
            });
        });
    }
}
