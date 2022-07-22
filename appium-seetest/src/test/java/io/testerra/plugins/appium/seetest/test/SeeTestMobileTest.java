package io.testerra.plugins.appium.seetest.test;

import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created on 22.07.2022
 *
 * @author mgn
 */
public class SeeTestMobileTest extends TesterraTest implements WebDriverManagerProvider, UiElementFinderFactoryProvider {

    private final UiElementFinder finder = UI_ELEMENT_FINDER_FACTORY.create(WEB_DRIVER_MANAGER.getWebDriver());

    @Test
    public void testT01_isPresent() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final TestableUiElement inputUserName = find(By.xpath("//*[@id='username']"));
        inputUserName.assertThat().present(true);
    }

    private UiElement find(By by) {
        return finder.find(by);
    }
}
