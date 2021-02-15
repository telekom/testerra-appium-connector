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

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverManager;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.frames.FrameLogic;
import eu.tsystems.mms.tic.testframework.utils.JSUtils;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.awt.Color;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implements {@link GuiElementCore} to fullfill Testerra {@link GuiElement} functionality.
 * Date: 24.06.2020
 * Time: 13:11
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreAdapter implements GuiElementCore, Loggable {

    private static final int DELAY_AFTER_GUIELEMENT_FIND_MILLIS = PropertyManager.getIntProperty(TesterraProperties.DELAY_AFTER_GUIELEMENT_FIND_MILLIS);

    private final WebDriver driver;
    private final AppiumDriver appiumDriver;
    private final By by;
    private final GuiElementData guiElementData;

    private static final AppiumDriverManager appiumDriverManager = new AppiumDriverManager();

    public AppiumGuiElementCoreAdapter(By by, WebDriver webDriver, GuiElementData guiElementData) {

        this.by = by;
        this.driver = webDriver;
        this.guiElementData = guiElementData;
        this.appiumDriver = appiumDriverManager.fromWebDriver(this.driver);
    }

    @Override
    public WebElement getWebElement() {

        find();
        return this.guiElementData.webElement;
    }

    @Override
    public By getBy() {

        return this.by;
    }

    @Override
    @Deprecated
    public void scrollToElement() {

        this.scrollToElement(0);
    }

    @Override
    @Deprecated
    public void scrollToElement(int yOffset) {

        final Point location = getWebElement().getLocation();
        final int x = location.getX();
        final int y = location.getY() - yOffset;
        log().trace("Scrolling into view: " + x + ", " + y);

        JSUtils.executeScript(appiumDriver, "scroll(" + x + ", " + y + ");");
    }

    @Override
    public void scrollIntoView() {

        this.scrollIntoView(new Point(0, 0));
    }

    @Override
    public void scrollIntoView(Point offset) {

        JSUtils utils = new JSUtils();
        utils.scrollToCenter(appiumDriver, getWebElement(), offset);
    }

    @Override
    public void select() {

        if (!isSelected()) {
            click();
        }
    }

    @Override
    public void deselect() {

        if (isSelected()) {
            click();
        }
    }

    @Override
    public void type(String text) {

        clear();
        getWebElement().sendKeys(text);
    }

    @Override
    public void click() {

        getWebElement().click();
    }

    @Override
    @Deprecated
    public void clickJS() {

        click();
    }

    @Override
    @Deprecated
    public void clickAbsolute() {

        click();
    }

    @Override
    @Deprecated
    public void mouseOverAbsolute2Axis() {

        throw new MobileActionNotSupportedException("mouseOverAbsolute2Axis() not supported on mobile GuiElement");
    }

    @Override
    public void submit() {

        getWebElement().submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {

        getWebElement().sendKeys(charSequences);
    }

    @Override
    public void clear() {

        getWebElement().clear();
    }

    @Override
    public String getTagName() {

        return getWebElement().getTagName();
    }

    @Override
    public GuiElement getSubElement(By byLocator, String description) {

        return getSubElement(byLocator).setName(description);
    }

    @Override
    public GuiElement getSubElement(By byLocator) {

        return getSubElement(Locate.by(byLocator));
    }

    @Override
    public GuiElement getSubElement(Locate locator) {

        final FrameLogic frameLogic = guiElementData.frameLogic;
        GuiElement[] frames = null;
        if (frameLogic != null) {
            frames = frameLogic.getFrames();
        }

        String abstractLocatorString = locator.getBy().toString();
        if (abstractLocatorString.toLowerCase().contains("xpath")) {
            int i = abstractLocatorString.indexOf(":") + 1;
            String xpath = abstractLocatorString.substring(i).trim();
            String prevXPath = xpath;
            // Check if locator does not start with dot, ignoring a leading parenthesis for choosing the n-th element
            if (xpath.startsWith("/")) {
                xpath = xpath.replaceFirst("/", "./");
                log().warn(String.format("Replaced absolute xpath locator \"%s\" to relative: \"%s\"", prevXPath, xpath));
                locator = Locate.by(By.xpath(xpath));
            } else if (!xpath.startsWith(".")) {
                xpath = "./" + xpath;
                log().warn(String.format("Added relative xpath locator for children to \"%s\": \"%s\"", prevXPath, xpath));
                locator = Locate.by(By.xpath(xpath));
            }
        }

        GuiElement subElement = new GuiElement(this.driver, locator, frames);
        subElement.setParent(this);
        return subElement;
    }

    @Override
    public Point getLocation() {

        return this.getWebElement().getLocation();
    }

    @Override
    public Dimension getSize() {

        return this.getWebElement().getSize();
    }

    @Override
    public String getCssValue(String cssIdentifier) {

        return this.getWebElement().getCssValue(cssIdentifier);
    }

    @Override
    @Deprecated
    public void mouseOver() {

        this.hover();
    }

    @Override
    public void hover() {

        // Caused by: org.openqa.selenium.WebDriverException: Not Implemented (Method 'mouseMoveTo' is not implemented)
        throw new MobileActionNotSupportedException("hover() is not supported on mobile element.s");
    }

    @Override
    public void contextClick() {

        final ElementOption elementOption = new ElementOption().withElement(appiumDriver.findElement(this.by));
        final TouchAction action = new TouchAction<>(appiumDriver);

        action.longPress(new LongPressOptions().withElement(elementOption));
        action.perform();
    }

    @Override
    @Deprecated
    public void mouseOverJS() {

        this.mouseOver();
    }

    @Override
    public Select getSelectElement() {

        return new Select(this.getWebElement());
    }

    @Override
    public List<String> getTextsFromChildren() {

        final List<WebElement> childElements = this.getWebElement().findElements(By.xpath(".//*"));
        final ArrayList<String> childTexts = new ArrayList<>();

        for (final WebElement childElement : childElements) {
            final String text = childElement.getText();
            if (StringUtils.isNotBlank(text)) {
                childTexts.add(text);
            }
        }

        return childTexts;
    }

    @Override
    public void doubleClick() {

        final ElementOption elementOption = new ElementOption().withElement(getWebElement());
        final TouchAction action = new TouchAction<>(appiumDriver);

        final TapOptions tapOptions = new TapOptions().withTapsCount(2).withElement(elementOption);
        action.tap(tapOptions).perform();
    }

    @Override
    public void highlight(Color color) {

        JSUtils.highlightWebElement(appiumDriver, this.getWebElement(), color);
    }

    @Override
    public void swipe(int offsetX, int offsetY) {

        TouchAction touchAction = new TouchAction(appiumDriver);

        final TapOptions tapOption = new TapOptions().withElement(new ElementOption().withElement(getWebElement()));
        touchAction.tap(tapOption);
        touchAction.waitAction(new WaitOptions().withDuration(Duration.ofMillis(1500)));
        touchAction.moveTo(new PointOption().withCoordinates(offsetX, offsetY));
        touchAction.waitAction(new WaitOptions().withDuration(Duration.ofMillis(1500)));
        touchAction.release();
        touchAction.perform();
    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {

        this.sendKeys(textToInput);
        return this.getWebElement().getAttribute("value").length();
    }

    @Override
    public int getNumberOfFoundElements() {

        // isPresent() is the save way of getWebElement()
        if (isPresent()) {
            return this.find();
        }

        return 0;
    }

    @Override
    @Deprecated
    public void rightClick() {

        this.contextClick();
    }

    @Override
    @Deprecated
    public void rightClickJS() {

        this.contextClick();
    }

    @Override
    @Deprecated
    public void doubleClickJS() {

        this.doubleClick();
    }

    @Override
    public File takeScreenshot() {

        // TODO takeScreenshot
        return null;
    }

    @Override
    public boolean isPresent() {

        try {
            log().debug("isPresent(): trying to find WebElement");
            find();
        } catch (Exception e) {
            log().debug("isPresent(): Element not found: " + by, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean isEnabled() {

        return getWebElement().isEnabled();
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {

        final GuiElement textNode = this.getSubElement(Locate.by(By.xpath(String.format(".//*[contains(text(),'%s')]", contains))));
        return textNode.isPresent();
    }

    @Override
    public boolean isDisplayed() {

        return getWebElement().isDisplayed();
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

        final String checked = this.getWebElement().getAttribute("checked");
        final String selected = this.getWebElement().getAttribute("selected");
        return checked.equalsIgnoreCase("true") || selected.equalsIgnoreCase("true");
    }

    @Override
    public String getText() {

        return this.getWebElement().getText();
    }

    @Override
    public String getAttribute(String attributeName) {

        return this.getWebElement().getAttribute(attributeName);
    }

    @Override
    @Deprecated
    public boolean isSelectable() {

        throw new MobileActionNotSupportedException("isSelectable() is not supported on mobile elements");
    }

    private int find() {

        int numberOfFoundElements = 0;
        guiElementData.webElement = null;
        GuiElementCore parent = guiElementData.parent;
        Exception notFoundCause = null;
        try {
            List<WebElement> elements;
            if (parent != null) {
                elements = parent.getWebElement().findElements(by);
            } else {
                elements = this.appiumDriver.findElements(by);
            }
            if (elements != null) {
                final Locate selector = guiElementData.guiElement.getLocator();
                Predicate<WebElement> filter = selector.getFilter();
                if (filter != null) {
                    elements.removeIf(webElement -> !filter.test(webElement));
                }
                if (selector.isUnique() && elements.size() > 1) {
                    throw new Exception("To many WebElements found (" + elements.size() + ")");
                }
                numberOfFoundElements = elements.size();

                setWebElement(elements);
            }
        } catch (Exception e) {
            notFoundCause = e;
        }

        throwExceptionIfWebElementIsNull(notFoundCause);

        if (DELAY_AFTER_GUIELEMENT_FIND_MILLIS > 0) {
            TimerUtils.sleep(DELAY_AFTER_GUIELEMENT_FIND_MILLIS);
        }

        return numberOfFoundElements;
    }

    private void setWebElement(List<WebElement> elements) {

        int numberOfFoundElements = elements.size();
        if (numberOfFoundElements < guiElementData.index + 1) {
            throw new StaleElementReferenceException("Request index of GuiElement was " + guiElementData.index + ", but only " + numberOfFoundElements + " were found. There you go, GuiElementList-Fanatics!");
        }

        if (numberOfFoundElements > 0) {

            WebElement webElement = elements.get(Math.max(0, guiElementData.index));

            // check for shadowRoot
            if (guiElementData.shadowRoot) {
                Object o = JSUtils.executeScript(appiumDriver, "return arguments[0].shadowRoot", webElement);
                if (o instanceof WebElement) {
                    webElement = (WebElement) o;
                }
            }

            // set webelement
            guiElementData.webElement = webElement;
            GuiElementData.WEBELEMENT_MAP.put(webElement, guiElementData.guiElement);
        } else {
            log().debug("find(): GuiElement " + toString() + " was NOT found. Element list has 0 entries.");
        }
    }

    private void throwExceptionIfWebElementIsNull(Exception cause) {
        if (guiElementData.webElement == null) {
            throw new ElementNotFoundException(this.guiElementData.guiElement, cause);
        }
    }

}
