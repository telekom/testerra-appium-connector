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

import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.utils.JSUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.awt.Color;
import java.io.File;
import java.util.List;

/**
 * Implements {@link GuiElementCore} to fullfill Testerra {@link GuiElement} functionality.
 * Date: 24.06.2020
 * Time: 13:11
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreAdapter implements GuiElementCore, Loggable {

    private final WebDriver driver;
    private final By by;
    private final GuiElementData guiElementData;

    public AppiumGuiElementCoreAdapter(By by, WebDriver webDriver, GuiElementData guiElementData) {

        this.by = by;
        this.driver = webDriver;
        this.guiElementData = guiElementData;
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

        JSUtils.executeScript(driver, "scroll(" + x + ", " + y + ");");
    }

    @Override
    public void scrollIntoView() {

        this.scrollIntoView(new Point(0, 0));
    }

    @Override
    public void scrollIntoView(Point offset) {

        JSUtils utils = new JSUtils();
        utils.scrollToCenter(guiElementData.webDriver, getWebElement(), offset);
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

        // TODO mouseOverAbsolute2Axis
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

        return getSubElement(by).setName(description);
    }

    @Override
    public GuiElement getSubElement(By by) {

        return getSubElement(Locate.by(by));
    }

    @Override
    public GuiElement getSubElement(Locate locator) {

        // TODO getSubElement
        return null;
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

        final Actions action = new Actions(driver);
        action.moveToElement(this.getWebElement()).build().perform();
    }

    @Override
    public void contextClick() {

        final Actions actions = new Actions(driver);
        actions.moveToElement(getWebElement()).contextClick().build().perform();
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

        // TODO getTextsFromChildren
        return null;
    }

    @Override
    public void doubleClick() {
        // TODO doubleClick
    }

    @Override
    public void highlight() {
        // TODO highlight
    }

    @Override
    public void highlight(Color color) {
        // TODO highlight
    }

    @Override
    public void swipe(int offsetX, int offSetY) {
        // TODO swipe
    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {

        // TODO getLengthOfValueAfterSendKeys
        return 0;
    }

    @Override
    public int getNumberOfFoundElements() {

        // TODO getNumberOfFoundElements
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
        // TODO doubleClickJS
    }

    @Override
    public File takeScreenshot() {

        // TODO takeScreenshot
        return null;
    }

    @Override
    public boolean isPresent() {

        find();
        return this.guiElementData.webElement != null;
    }

    @Override
    public boolean isEnabled() {

        return getWebElement().isEnabled();
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {

        // TODO anyFollowingTextNodeContains
        return false;
    }

    @Override
    public boolean isDisplayed() {

        return getWebElement().isDisplayed();
    }

    @Override
    public boolean isVisible(boolean complete) {

        // TODO isVisible
        return false;
    }

    @Override
    public boolean isSelected() {

        return this.getWebElement().isSelected();
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
    public boolean isSelectable() {

        // TODO isSelectable
        return false;
    }

    private void find() {

        try {
            this.guiElementData.webElement = this.driver.findElement(this.by);
        } catch (Exception e) {
            throw new ElementNotFoundException("GuiElement not found: " + this.toString(), e);
        }
    }

}
