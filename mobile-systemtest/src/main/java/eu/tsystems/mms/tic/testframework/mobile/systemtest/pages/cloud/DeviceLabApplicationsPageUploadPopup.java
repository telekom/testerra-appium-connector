package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabApplicationsPageUploadPopup extends Page {

    private final GuiElement toastBox = new GuiElement(driver, By.className("toast-message"));

    @Check
    private final GuiElement fileInput = new GuiElement(driver, By.name("file"));

    @Check
    private final GuiElement addButton = new GuiElement(driver, By.name("create-application-button"));

    public DeviceLabApplicationsPageUploadPopup(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabApplicationsPage chooseApk(String path) {

        fileInput.sendKeys(path);
        addButton.click();
        toastBox.waits().waitForIsDisplayed();
        driver.navigate().refresh();
        return new DeviceLabApplicationsPage(driver);
    }
}
