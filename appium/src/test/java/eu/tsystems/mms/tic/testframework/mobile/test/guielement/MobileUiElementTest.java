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

package eu.tsystems.mms.tic.testframework.mobile.test.guielement;

import eu.tsystems.mms.tic.testframework.annotations.Fails;
import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.mobile.driver.SafariMobileConfig;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.testing.AssertProvider;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.useragents.ChromeConfig;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Martin Großmann
 */
public class MobileUiElementTest extends AbstractAppiumTest implements UiElementFinderFactoryProvider, AssertProvider {

    @BeforeClass
    public void initBrowser()  {
        WEB_DRIVER_MANAGER.setUserAgentConfig(Browsers.mobile_chrome, (ChromeConfig) options -> {
            options.setAcceptInsecureCerts(true);
        });

        WEB_DRIVER_MANAGER.setUserAgentConfig(Browsers.mobile_safari, (SafariMobileConfig) options -> {
           options.acceptInsecureCerts();
        });
    }

    @Test
    public void testT01_isPresent() {
        TestStep.begin("");

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final TestableUiElement inputUserName = find(By.xpath("//*[@id='username']"));
        inputUserName.assertThat().present(true);
    }

    @Test
    public void testT02_isDisplayed() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final TestableUiElement inputUserName = find(By.xpath("//*[@id='username']"));
        inputUserName.assertThat().displayed(true);
    }

    @Test
    public void testT03_type() {
        final String valueExpected = "foobar";

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final UiElement inputUserName = find(By.xpath("//*[@id='username']"));

        inputUserName.type(valueExpected);
        inputUserName.assertThat().value(valueExpected);
    }

    @Test
    public void testT04_getNumberOfFoundElements() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final UiElement inputElementMatchesTwo = find(By.cssSelector("input"));
        ASSERT.assertEquals(inputElementMatchesTwo.list().size(), 2);
    }

    @Test
    public void testT05_sendKeys() {
        final String valueExpected = "foo bar";

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final UiElement inputUserName = find(By.xpath("//*[@id='username']"));

        inputUserName.sendKeys(valueExpected);
        inputUserName.assertThat().value(valueExpected);
    }

    @Test
    public void testT06_getTagName() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final TestableUiElement inputUserName = find(By.xpath("//*[@id='username']"));
        inputUserName.assertThat().tagName().is("input");
    }

    @Test
    public void testT07_click() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        final UiElement buttonAdd = find(By.xpath("//button[text()='Add Element']"));
        buttonAdd.assertThat().displayed(true);
        buttonAdd.click();

        final UiElement buttonRemove = find(By.xpath("//button[text()='Delete']"));
        buttonRemove.assertThat().displayed(true);
        buttonRemove.click();
        buttonRemove.assertThat().displayed(false);
    }

    @Test
    @Fails(description = "Hover does not work?")
    public void testT08_hover() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/hovers");

        final UiElement itemToHover = find(By.xpath("(//*[@class='figure'])[1]"));
        final UiElement itemToDisplayed = find(By.xpath("//h5[contains(text(), 'user1')]"));

        itemToHover.assertThat().displayed(true);
        itemToDisplayed.assertThat().displayed(false);

        itemToHover.hover();
        itemToDisplayed.assertThat().displayed(true);
    }

    @Test
    public void testT09_getSelectElement() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/dropdown");

        final UiElement selectDropdown = find(By.xpath("//select[@id='dropdown']"));
        selectDropdown.assertThat().displayed(true);

        selectDropdown.findWebElement(element -> {
            final Select selectElement = new Select(element);

            final List<WebElement> options = selectElement.getOptions();
            ASSERT.assertNotEquals(options.size(), 0, "One ore more options shown.");

            selectElement.selectByIndex(2);
            ASSERT.assertEquals(selectElement.getFirstSelectedOption().getText(), "Option 2", "Selected option matches.");
        });
    }

    @Test
    public void testT10_selectCheckbox() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        final UiElement checkboxOne = find(By.xpath("(//input[@type='checkbox'])[1]"));
        checkboxOne.assertThat().selected(false);

        checkboxOne.select();
        checkboxOne.assertThat().selected(true);
    }

    @Test
    public void testT11_deselectCheckbox() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        final UiElement checkboxOne = find(By.xpath("(//input[@type='checkbox'])[2]"));
        checkboxOne.assertThat().selected(true);

        checkboxOne.deselect();
        checkboxOne.assertThat().selected(false);
    }

    @Test
    public void testT12_isVisible() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/");

        final TestableUiElement link = find(By.linkText("WYSIWYG Editor"));
        link.assertThat().visible(true);
    }

    @Test
    public void testT13_getSubElement() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/forgot_password");

        final UiElement contentWrapper = find(By.xpath("//*[@id='content']"));
        contentWrapper.assertThat().displayed(true);

        final UiElement formElement = contentWrapper.find(By.xpath(".//*[@id='forgot_password']"));
        formElement.assertThat().displayed(true);

        final UiElement buttonElement = formElement.find(By.xpath(".//button[@id='form_submit']"));
        buttonElement.assertThat().displayed(true);
    }

    @Test
    public void testT14_shadowDom() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/shadowdom");

        final UiElement shadowRoot = find(By.xpath("(//my-paragraph)[2]")).shadowRoot();
        shadowRoot.assertThat().present(true);

        final UiElement shadowRootSubElement = shadowRoot.find(By.xpath(".//li[contains(text(), 'In a list!')]"));
        shadowRootSubElement.assertThat().displayed(true);
    }

    @Test
    public void testT15_withFrames() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/nested_frames");

        final UiElement frameTop = find(By.xpath("//frame[@name='frame-top']"));
        frameTop.assertThat().present(true);

        final UiElement frameBottom = find(By.xpath(".//frame[@name='frame-bottom']"));
        frameBottom.assertThat().present(true);

        final UiElement frameTopMiddle = frameTop.find(By.xpath("//frame[@name='frame-middle']"));
        frameTopMiddle.assertThat().present(true);

        final UiElement topMiddleContent = frameTopMiddle.find(By.xpath("//*[@id='content' and text()='MIDDLE']"));
        topMiddleContent.assertThat().present(true);

        final UiElement frameTopLeft = frameTop.find(By.xpath(".//frame[@name='frame-left']"));
        frameTopLeft.assertThat().present(true);
    }

    @Test
    public void testT16_contextClick() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/context_menu");

        final UiElement elementToClick = find(By.xpath("//*[@id='hot-spot']"));
        elementToClick.assertThat().displayed(true);
        elementToClick.contextClick();
    }

    @Test(enabled = false)
    public void testTXX_dragAndDropElements() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/drag_and_drop");

        final UiElement draggable = find(By.xpath("//*[@id='column-a']"));
        final UiElement dropLocation = find(By.xpath("//*[@id='column-b']"));

        draggable.assertThat().displayed(true);
        dropLocation.assertThat().displayed(true);

        // TODO action here, MouseActions are not working
        // need feature request
//        MouseActions.dragAndDropJS((GuiElement) draggable, (GuiElement) dropLocation);

        String text = dropLocation.find(By.xpath("//header")).waitFor().text().getActual();
        ASSERT.assertEquals(text, "A", "A successfully dropped.");
    }

    // scroll into view

    private UiElement find(By by) {
        UiElementFinder finder = UI_ELEMENT_FINDER_FACTORY.create(WEB_DRIVER_MANAGER.getWebDriver());
        return finder.find(by);
    }

}

