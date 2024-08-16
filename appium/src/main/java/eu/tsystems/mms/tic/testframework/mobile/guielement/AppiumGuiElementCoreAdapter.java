/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.AbstractWebDriverCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.utils.AppiumUtils;
import eu.tsystems.mms.tic.testframework.utils.ExecutionUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements {@link GuiElementCore} to fullfill Testerra {@link GuiElement} functionality.
 * Date: 24.06.2020
 * Time: 13:11
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreAdapter extends AbstractWebDriverCore implements Loggable, WebDriverManagerProvider {

    private AppiumDriver appiumDriver;

    private ExecutionUtils executionUtils = Testerra.getInjector().getInstance(ExecutionUtils.class);

    public AppiumGuiElementCoreAdapter(GuiElementData guiElementData) {
        super(guiElementData);
        appiumDriver = WEB_DRIVER_MANAGER.unwrapWebDriver(this.guiElementData.getWebDriver(), AppiumDriver.class).orElseThrow();
    }

    @Override
    protected void switchToDefaultContent(WebDriver webDriver) {

    }

    @Override
    protected void switchToFrame(WebDriver webDriver, WebElement webElement) {

    }

    @Override
    public void hover() {
        // Caused by: org.openqa.selenium.WebDriverException: Not Implemented (Method 'mouseMoveTo' is not implemented)
        throw new MobileActionNotSupportedException("hover() is not supported on mobile elements.");
    }

    @Override
    public void contextClick() {
        this.findWebElement(webElement -> {
            Point sourceLocation = webElement.getLocation();
            Dimension sourceSize = webElement.getSize();
            int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
            int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

            // Default in iOS: 500ms
            // https://developer.apple.com/documentation/uikit/touches_presses_and_gestures/handling_uikit_gestures/handling_long-press_gestures
            // Default in Android: 1_000ms
            // https://developer.android.com/develop/ui/views/touch-and-input/input-events
            tap.addAction(new Pause(finger, Duration.ofMillis(1_000)));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            this.appiumDriver.perform(List.of(tap));
        });
    }

    // TODO: Migrate to W3C actions
//    @Override
//    public void doubleClick() {
//        this.findWebElement(webElement -> {
//            final ElementOption elementOption = new ElementOption().withElement(webElement);
//            final TouchAction action = new TouchAction<>(appiumDriver);
//
//            final TapOptions tapOptions = new TapOptions().withTapsCount(2).withElement(elementOption);
//            action.tap(tapOptions).perform();
//        });
//    }

    /**
     * This is only a simple method without re-checks (see Testerra eu.tsystems.mms.tic.testframework.pageobjects.internal.core.AbstractWebDriverCore.type()).
     * <p>
     * Android apps does not support 'webElement.getAttribute("value")', 'webElement.getText()' and 'webElement.getAttribute("value")' have always empty results at Chrome and Safari
     */
    @Override
    public void type(String text) {
        if (text == null) {
            log().warn("Text to type is null. Typing nothing.");
            return;
        }
        if (text.isEmpty()) {
            log().warn("Text to type is empty!");
        }

        findWebElement(webElement -> {
            webElement.clear();
            webElement.sendKeys(text);
        });
    }

    @Override
    public void swipe(int offsetX, int offsetY) {
        new AppiumUtils().swipe(this.guiElementData.getGuiElement(), new Point(offsetX, offsetY));
    }

    @Override
    public boolean isVisible(boolean complete) {

        scrollIntoView();
        return this.isDisplayed();

        // the testerra way of doing this is not supported on mobile devices, because sometimes the element rect get exorbitant height/width values
        // my guess :: the high end pixel phone s(4k)

        //        if (!isDisplayed()) return false;

        //        final Object x = JSUtils.executeScript(driver, "return window.pageXOffset.toString();");
        //        final Object y = JSUtils.executeScript(driver, "return window.pageYOffset.toString();");
        //        final Object w = JSUtils.executeScript(driver, "return window.innerWidth.toString();");
        //        final Object h = JSUtils.executeScript(driver, "return window.innerHeight.toString();");
        //
        //        final Rectangle viewport = new Rectangle(
        //                Double.valueOf(x.toString()).intValue(),
        //                Double.valueOf(y.toString()).intValue(),
        //                Double.valueOf(h.toString()).intValue(),
        //                Double.valueOf(w.toString()).intValue());
        //
        //        final WebElement webElement = getWebElement();
        //
        //        // getRect doesn't work
        //        final Point elementLocation = webElement.getLocation();
        //        final Dimension elementSize = webElement.getSize();
        //        final java.awt.Rectangle viewportRect = new java.awt.Rectangle(viewport.x, viewport.y, viewport.width, viewport.height);
        //        final java.awt.Rectangle elementRect = new java.awt.Rectangle(elementLocation.x, elementLocation.y, elementSize.width, elementSize.height);
        //        return ((complete && viewportRect.contains(elementRect)) || viewportRect.intersects(elementRect));
    }

    @Override
    public boolean isSelected() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.findWebElement(webElement -> {
            final String selected = webElement.getAttribute("selected");
//            final String text = webElement.getText();
            String value = executionUtils.getFailsafe(() -> webElement.getAttribute("value")).orElse("");
            String checked = executionUtils.getFailsafe(() -> webElement.getAttribute("checked")).orElse("");

            atomicBoolean.set(
                    "true".equalsIgnoreCase(checked)
                            || "true".equalsIgnoreCase(selected)
                            || "1".equals(value)
            );
        });
        return atomicBoolean.get();
    }

    @Override
    @Deprecated
    public boolean isSelectable() {
        throw new MobileActionNotSupportedException("isSelectable() is not supported on mobile elements");
    }

}
