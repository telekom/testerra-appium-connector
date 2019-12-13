package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class AdministrationEULAPage extends NavigationPage {

    @Check
    private final GuiElement pageWrapper = new GuiElement(driver, By.id("page-wrapper"));

    public AdministrationEULAPage(WebDriver driver) {

        super(driver);
        checkPage();
    }

}
