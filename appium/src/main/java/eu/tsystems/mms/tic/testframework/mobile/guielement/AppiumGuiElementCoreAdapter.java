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

import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

/**
 * Implements {@link GuiElementCore} to fullfill Testerra {@link GuiElement} functionality.
 * Date: 24.06.2020
 * Time: 13:11
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreAdapter implements GuiElementCore {

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

    private void find() {

        this.guiElementData.webElement = this.driver.findElement(this.by);
    }

    @Override
    public By getBy() {

        return this.by;
    }

    @Override
    public void scrollToElement() {

    }

    @Override
    public void scrollToElement(int yOffset) {

    }

    @Override
    public void select() {

    }

    @Override
    public void deselect() {

    }

    @Override
    public void type(String text) {

        find();
        this.clear();
        this.guiElementData.webElement.sendKeys(text);
    }

    @Override
    public void click() {

    }

    @Override
    public void clickJS() {

    }

    @Override
    public void clickAbsolute() {

    }

    @Override
    public void mouseOverAbsolute2Axis() {

    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... charSequences) {

    }

    @Override
    public void clear() {

        find();
        this.guiElementData.webElement.clear();
    }

    @Override
    public String getTagName() {

        return null;
    }

    @Override
    public GuiElement getSubElement(By byLocator, String description) {

        return null;
    }

    @Override
    public GuiElement getSubElement(Locate locator) {

        return null;
    }

    @Override
    public GuiElement getSubElement(By by) {

        return null;
    }

    @Override
    public Point getLocation() {

        return null;
    }

    @Override
    public Dimension getSize() {

        return null;
    }

    @Override
    public String getCssValue(String cssIdentifier) {

        return null;
    }

    @Override
    public void mouseOver() {

    }

    @Override
    public void mouseOverJS() {

    }

    @Override
    public Select getSelectElement() {

        return null;
    }

    @Override
    public List<String> getTextsFromChildren() {

        return null;
    }

    @Override
    public void doubleClick() {

    }

    @Override
    public void highlight() {

    }

    @Override
    public void swipe(int offsetX, int offSetY) {

    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {

        return 0;
    }

    @Override
    public int getNumberOfFoundElements() {

        return 0;
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void rightClickJS() {

    }

    @Override
    public void doubleClickJS() {

    }

    @Override
    public File takeScreenshot() {

        return null;
    }

    @Override
    public boolean isPresent() {

        return false;
    }

    @Override
    public boolean isEnabled() {

        return false;
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {

        return false;
    }

    @Override
    public boolean isDisplayed() {

        find();
        return this.guiElementData.webElement.isDisplayed();
    }

    @Override
    public boolean isVisible(boolean complete) {

        return false;
    }

    @Override
    public boolean isSelected() {

        return false;
    }

    @Override
    public String getText() {

        return null;
    }

    @Override
    public String getAttribute(String attributeName) {

        return null;
    }

    @Override
    public boolean isSelectable() {

        return false;
    }
}
