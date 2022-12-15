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

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverManager;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.AbstractWebDriverCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * Implements {@link GuiElementCore} to fullfill Testerra {@link GuiElement} functionality.
 * Date: 24.06.2020
 * Time: 13:11
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreAdapter extends AbstractWebDriverCore implements Loggable {

    private final AppiumDriver appiumDriver;

    private static final AppiumDriverManager appiumDriverManager = new AppiumDriverManager();

    public AppiumGuiElementCoreAdapter(GuiElementData guiElementData) {
        super(guiElementData);
        this.appiumDriver = appiumDriverManager.fromWebDriver(this.guiElementData.getWebDriver());
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
        throw new MobileActionNotSupportedException("hover() is not supported on mobile element.s");
    }

    @Override
    public void contextClick() {
        this.findWebElement(webElement -> {
            final ElementOption elementOption = new ElementOption().withElement(webElement);
            final TouchAction action = new TouchAction<>(appiumDriver);

            action.longPress(new LongPressOptions().withElement(elementOption));
            action.perform();
        });
    }

    @Override
    public void doubleClick() {
        this.findWebElement(webElement -> {
            final ElementOption elementOption = new ElementOption().withElement(webElement);
            final TouchAction action = new TouchAction<>(appiumDriver);

            final TapOptions tapOptions = new TapOptions().withTapsCount(2).withElement(elementOption);
            action.tap(tapOptions).perform();
        });
    }

    @Override
    public void swipe(int offsetX, int offsetY) {
        this.findWebElement(webElement -> {
            TouchAction touchAction = new TouchAction(appiumDriver);

            final TapOptions tapOption = new TapOptions().withElement(new ElementOption().withElement(webElement));
            touchAction.tap(tapOption);
            touchAction.waitAction(new WaitOptions().withDuration(Duration.ofMillis(1500)));
            touchAction.moveTo(new PointOption().withCoordinates(offsetX, offsetY));
            touchAction.waitAction(new WaitOptions().withDuration(Duration.ofMillis(1500)));
            touchAction.release();
            touchAction.perform();
        });
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
            final String checked = webElement.getAttribute("checked");
            final String selected = webElement.getAttribute("selected");
            atomicBoolean.set(checked.equalsIgnoreCase("true") || selected.equalsIgnoreCase("true"));
        });
        return atomicBoolean.get();
    }

    @Override
    @Deprecated
    public boolean isSelectable() {

        throw new MobileActionNotSupportedException("isSelectable() is not supported on mobile elements");
    }
}
