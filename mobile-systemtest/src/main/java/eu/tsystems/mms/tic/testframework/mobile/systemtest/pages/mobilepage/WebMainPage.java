package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.mobilepage;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by aero on 09.11.2017.
 */
public class WebMainPage extends Page {

    private GuiElement invalidGuiElement = new GuiElement(driver, By.xpath("//*[@id='sectionnn_0']"));
    @Check
    private GuiElement heading = new GuiElement(driver, By.xpath("//*[@id='section_0']"));
    private GuiElement logo = new GuiElement(driver, By.xpath("//*[@nodeName='IMG']"));
    private GuiElement learnMoreButton = new GuiElement(driver, By.xpath("//*[@id='learn_more']"));
    private GuiElement navBurger = new GuiElement(driver, By.xpath("//*[@id='burger']"));
    private GuiElement signInButton = new GuiElement(driver, By.xpath("//*[@id='sign_in']"));


    public WebMainPage(WebDriver driver) {

        super(driver);
    }

}
