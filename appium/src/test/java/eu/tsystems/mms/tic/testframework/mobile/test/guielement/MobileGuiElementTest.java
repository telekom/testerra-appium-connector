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

package eu.tsystems.mms.tic.testframework.mobile.test.guielement;

import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Date: 16.09.2020
 * Time: 12:45
 *
 * @author Eric Kubenka
 */
public class MobileGuiElementTest extends AbstractAppiumTest {

    @Test
    public void testT01_isPresent() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputUserName = new GuiElement(driver, Locate.by(By.xpath("//*[@id='username']")));
        Assert.assertTrue(inputUserName.isPresent(), "Element present.");
        inputUserName.asserts().assertIsPresent();
    }

    @Test
    public void testT02_isDisplayed() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputUserName = new GuiElement(driver, Locate.by(By.xpath("//*[@id='username']")));
        Assert.assertTrue(inputUserName.isDisplayed(), "Element displayed.");
        inputUserName.asserts().assertIsDisplayed();
    }

    @Test
    public void testT03_type() {

        final String valueExpected = "foobar";

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputUserName = new GuiElement(driver, Locate.by(By.xpath("//*[@id='username']")));
        inputUserName.asserts().assertIsDisplayed();

        inputUserName.type(valueExpected);
        final String valueActual = inputUserName.getAttribute("value");
        Assert.assertEquals(valueActual, valueExpected, "Typed text equals.");
    }

    @Test
    public void testT04_getNumberOfFoundElements() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputElementMatchesTwo = new GuiElement(driver, Locate.by(By.cssSelector("input")));
        Assert.assertEquals(inputElementMatchesTwo.getNumberOfFoundElements(), 2, "Input elements found");
    }

    @Test
    public void testT05_sendKeys() {

        final String valueExpected = "foo bar";

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputUserName = new GuiElement(driver, Locate.by(By.xpath("//*[@id='username']")));
        inputUserName.asserts().assertIsDisplayed();

        inputUserName.sendKeys(valueExpected);
        Assert.assertEquals(inputUserName.getAttribute("value"), valueExpected, "Typed text equals.");
    }

    @Test
    public void testT06_getTagName() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final GuiElement inputUserName = new GuiElement(driver, Locate.by(By.xpath("//*[@id='username']")));
        inputUserName.asserts().assertIsDisplayed();

        Assert.assertEquals(inputUserName.getTagName(), "input", "Tag name equals.");
    }

    @Test
    public void testT07_click() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        final GuiElement buttonAdd = new GuiElement(driver, Locate.by(By.xpath("//button[text()='Add Element']")));
        buttonAdd.asserts().assertIsDisplayed();

        buttonAdd.click();
        final GuiElement buttonRemove = new GuiElement(driver, Locate.by(By.xpath("//button[text()='Delete']")));
        buttonRemove.asserts().assertIsDisplayed();

        buttonRemove.click();
        buttonAdd.asserts().assertIsNotDisplayed();
    }

    @Test
    public void testT08_hover() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/hovers");

        final GuiElement itemToHover = new GuiElement(driver, Locate.by(By.xpath("(//*[@class='figure'])[1]")));
        final GuiElement itemToDisplayed = new GuiElement(driver, Locate.by(By.xpath("//h5[contains(text(), 'user1')]")));

        itemToHover.asserts().assertIsDisplayed();
        itemToDisplayed.asserts().assertIsNotDisplayed();

        itemToHover.hover();
        Assert.assertTrue(itemToDisplayed.isDisplayed(), "Item displayed");
        itemToDisplayed.asserts().assertIsDisplayed();
    }

    @Test
    public void testT09_getSelectElement() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/dropdown");

        final GuiElement selectDropdown = new GuiElement(driver, Locate.by(By.xpath("//select[@id='dropdown']")));
        selectDropdown.asserts().assertIsDisplayed();

        final Select selectElement = selectDropdown.getSelectElement();
        final List<WebElement> options = selectElement.getOptions();
        Assert.assertNotEquals(options.size(), 0, "One ore more options shown.");

        selectElement.selectByVisibleText("Option 2");
        Assert.assertEquals(selectElement.getFirstSelectedOption().getText(), "Option 2", "Selected option matches.");
    }

    @Test
    public void testT10_selectCheckbox() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        final GuiElement checkboxOne = new GuiElement(driver, Locate.by(By.xpath("(//input[@type='checkbox'])[1]")));
        Assert.assertFalse(checkboxOne.isSelected(), "Checkbox selected.");

        checkboxOne.select();
        Assert.assertTrue(checkboxOne.isSelected(), "Checkbox selected.");
    }

    @Test
    public void testT11_deselectCheckbox() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        final GuiElement checkboxOne = new GuiElement(driver, Locate.by(By.xpath("(//input[@type='checkbox'])[2]")));
        Assert.assertTrue(checkboxOne.isSelected(), "Checkbox selected.");

        checkboxOne.deselect();
        Assert.assertFalse(checkboxOne.isSelected(), "Checkbox selected.");
    }

}
