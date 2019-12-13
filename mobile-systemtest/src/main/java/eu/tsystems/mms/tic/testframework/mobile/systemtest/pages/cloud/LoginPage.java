package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class LoginPage extends Page {

    @Check
    private final GuiElement userInput = new GuiElement(driver, By.name("username"));

    @Check
    private final GuiElement passwordInput = new GuiElement(driver, By.name("password"));

    @Check
    private final GuiElement loginButton = new GuiElement(driver, By.name("login"));

    public LoginPage(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DashboardPage login(String username, String password) {

        userInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
        return new DashboardPage(driver);
    }

}
