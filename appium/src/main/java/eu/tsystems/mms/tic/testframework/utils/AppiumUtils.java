/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, T-Systems MMS GmbH, Deutsche Telekom AG
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
 */
package eu.tsystems.mms.tic.testframework.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import eu.tsystems.mms.tic.testframework.appium.AppiumContext;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.remote.SupportsRotation;
import org.openqa.selenium.Platform;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * Created on 2023-03-08
 *
 * @author mgn
 */
public class AppiumUtils implements WebDriverManagerProvider, Loggable {

    public String runCommand(WebDriver driver, String command, String... args) {
        log().info("Shell command (native): {}, {}", command, Arrays.toString(args)); // ImmutableList does NOT work here...
        return String.valueOf(JSUtils.executeScript(driver, "mobile: shell", ImmutableMap.of("command", command, "args", Lists.newArrayList(args))));
    }

    public void launchIOSApp(WebDriver driver, String bundleId) {
        log().info("Start application '{}'", bundleId);
        MobileOsChecker checker = new MobileOsChecker();
        Platform platform = checker.getPlatform(driver);
        if (platform == Platform.IOS) {
            JSUtils.executeScript(driver, "mobile: launchApp", ImmutableMap.of("bundleId", bundleId));
            JSUtils.executeScript(driver, "mobile: activateApp", ImmutableMap.of("bundleId", bundleId));
        } else {
            throw new RuntimeException("Cannot start application " + bundleId + " at " + platform);
        }
    }

    public void rotate(WebDriver driver, ScreenOrientation screenOrientation) {
        WEB_DRIVER_MANAGER.unwrapWebDriver(driver, AppiumDriver.class).ifPresent(appiumDriver -> {
            ((SupportsRotation) appiumDriver).rotate(screenOrientation);
        });
    }

    /**
     * Switch the Appium Context if available
     */
    public void switchContext(WebDriver driver, AppiumContext desiredContext) {
        Optional<AppiumDriver> appiumDriver = WEB_DRIVER_MANAGER.unwrapWebDriver(driver, AppiumDriver.class);
        if (appiumDriver.isEmpty()) {
            throw new RuntimeException("Current Webdriver is not an Appium driver.");
        }

        SupportsContextSwitching contextSwitchingDriver = (SupportsContextSwitching) appiumDriver.get();

        String currentContext = contextSwitchingDriver.getContext();
        AppiumContext parsedInitialContext = AppiumContext.parse(currentContext);
        log().info("Current context: {} ({})", currentContext, parsedInitialContext);

        if (parsedInitialContext.equals(desiredContext)) {
            log().info("Already in context {}", desiredContext);
            return;
        }

        Set<String> contextHandles = contextSwitchingDriver.getContextHandles();
        log().info("Available contexts: {}", contextHandles);
        contextHandles.stream()
                .filter(contextHandle -> AppiumContext.parse(contextHandle).equals(desiredContext))
                .findFirst()
                .ifPresentOrElse(handle -> {
                    log().info("Switch to context {} ({})", handle, desiredContext);
                    contextSwitchingDriver.context(handle);
                }, () -> {
                    log().error("Couldn't find a {} context in {}", desiredContext, contextHandles);
                    throw new RuntimeException(String.format("Cannot switch in %s, because it does not exist. (%s)", desiredContext, contextHandles));
                });

    }

}
