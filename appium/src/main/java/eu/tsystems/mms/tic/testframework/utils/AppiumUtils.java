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
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.remote.SupportsRotation;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created on 2023-03-08
 *
 * @author mgn
 */
public class AppiumUtils implements WebDriverManagerProvider, Loggable {

    public enum Swipe {
        LEFT,
        RIGHT,
        UP,
        DOWN;
    }

    // Relative positions to the screen dimension of standard swipe actions
    // for example scroll down:
    // - 0.2 * screenHeight -> start y
    // - 0.8 * screenHeight -> end y
    // - x is center of screen
    private static final double SCREEN_SWIPE_START = 0.2;
    private static final double SCREEN_SWIPE_END = 0.8;

    /**
     * Run a shell command, especially at Android devices
     *
     * @param driver
     * @param command
     * @param args
     * @return
     */
    public String runCommand(WebDriver driver, String command, String... args) {
        log().info("Shell command (native): {}, {}", command, Arrays.toString(args)); // ImmutableList does NOT work here...
        return String.valueOf(JSUtils.executeScript(driver, "mobile: shell", ImmutableMap.of("command", command, "args", Lists.newArrayList(args))));
    }

    /**
     * Helper method to start a native app from iOS system. To start installed custom apps use the default way with AppiumDriverRequest
     *
     * @param driver Current webdriver instance
     * @param bundleId Bundle ID of native app
     */
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
     * Perform a swipe action, start point is an element and end point is calculated based on the offset.
     */
    public void swipe(UiElement uiElement, Point offset) {
        AppiumDriver appiumDriver = this.getAppiumDriver(uiElement.getWebDriver());

        uiElement.findWebElement(webElement -> {
            Point sourceLocation = webElement.getLocation();
            Dimension sourceSize = webElement.getSize();
            int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
            int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;
            int endX = centerX + offset.getX();
            int endY = centerY + offset.getY();

            this.swipeAction(appiumDriver, Duration.ofMillis(200), centerX, centerY, endX, endY);
        });
    }

    /**
     * Perform a swipe action, start point is the center of an element. Possible directions are
     * Swipe.LEFT, Swipe.RIGHT, Swipe.UP and Swipe.DOWN.
     */
    public void swipe(UiElement startElement, Swipe direction) {
        startElement.findWebElement(webElement -> {
            Point sourceLocation = webElement.getLocation();
            Dimension sourceSize = webElement.getSize();
            int startX = sourceLocation.getX() + sourceSize.getWidth() / 2;
            int startY = sourceLocation.getY() + sourceSize.getHeight() / 2;
            Point startPoint = new Point(startX, startY);

            this.swipeAction(startElement.getWebDriver(), direction, startPoint);
        });
    }

    /**
     * Perform a swipe action. Start point is the center of the screen. Possible directions are
     * Swipe.LEFT, Swipe.RIGHT, Swipe.UP and Swipe.DOWN.
     */
    public void swipe(WebDriver driver, Swipe direction) {
        Dimension screenDim = driver.manage().window().getSize();
        int startX = screenDim.getWidth() / 2;
        int startY = screenDim.getHeight() / 2;
        Point startPoint = new Point(startX, startY);
        this.swipeAction(driver, direction, startPoint);
    }

    private void swipeAction(WebDriver driver, Swipe direction, Point startPoint) {
        AppiumDriver appiumDriver = this.getAppiumDriver(driver);
        final Duration intensity = Duration.ofMillis(500);
        // Length of swipe action according screen resolution
        final double screenMultiplier = 0.5;

        Dimension screenDim = appiumDriver.manage().window().getSize();
//        final int centerX = screenDim.getWidth() / 2;
//        final int centerY = screenDim.getHeight() / 2;
        final int diffX = (int) (screenDim.getWidth() * screenMultiplier);
        final int diffY = (int) (screenDim.getHeight() * screenMultiplier);

        int startX = startPoint.getX();
        int startY = startPoint.getY();
        int endX = startX;
        int endY = startY;
        switch (direction) {
            case LEFT:
                startX = (int) (screenDim.getWidth() * SCREEN_SWIPE_END);
                endX = (int) (screenDim.getWidth() * SCREEN_SWIPE_START);
//                endX = Math.max(startX - diffX, 1);
                break;
            case RIGHT:
                endX = Math.max(startX + diffX, screenDim.getWidth() - 1);
                break;
            case UP:
                endY = Math.max(startY - diffY, 1);
                break;
            case DOWN:
                endY = Math.max(startY + diffY, screenDim.getHeight() - 1);
        }

        this.swipeAction(appiumDriver, intensity, startX, startY, endX, endY);
    }

    private void swipeAction(AppiumDriver driver, Duration intensity, int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(intensity, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(intensity, PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(0));
        driver.perform(List.of(swipe));
    }

    /**
     * Switch the Appium Context if available
     */
    public void switchContext(WebDriver driver, AppiumContext desiredContext) {
        AppiumDriver appiumDriver = this.getAppiumDriver(driver);

        SupportsContextSwitching contextSwitchingDriver = (SupportsContextSwitching) appiumDriver;

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

    /**
     * Unwrap the current WebDriver instance and returns a native AppiumDriver instance.
     */
    public AppiumDriver getAppiumDriver(WebDriver webDriver) {
        return WEB_DRIVER_MANAGER.unwrapWebDriver(webDriver, AppiumDriver.class)
                .orElseThrow(() -> new RuntimeException("Current Webdriver is not an Appium driver."));
    }

}
