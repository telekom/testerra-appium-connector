package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabApplicationsPageDeletePopup extends Page {

    private final GuiElement toastBox = new GuiElement(driver, By.className("toast-message"));

    @Check
    private final GuiElement okButton = new GuiElement(driver, By.name("delete-application-ok"));

    public DeviceLabApplicationsPageDeletePopup(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabApplicationsPage confirmDeletion() {

        okButton.click();
        toastBox.waits().waitForIsDisplayed();
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.navigate().refresh();
        return new DeviceLabApplicationsPage(driver);
    }

}
