package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabApplicationsPage extends NavigationPage {

    private final Logger LOGGER = LoggerFactory.getLogger(MobileGuiElement.class);

    @Check
    private final GuiElement uploadButton = new GuiElement(driver, By.xpath("//*[contains(@class, 'icon-cloud-upload')]")); // //*[contains(text(), 'Upload')]"

    @Check
    private final GuiElement installButton = new GuiElement(driver, By.xpath("//*[contains(@class, 'icon-database-download')]"));

    @Check
    private final GuiElement deleteButton = new GuiElement(driver, By.xpath("//*[contains(@class, 'icon-trash2')]"));    // //*[contains(text(), 'Delete')]

    private GuiElement tableEntry;

    public DeviceLabApplicationsPage(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public boolean appIsListed(String appName) {

        tableEntry = new GuiElement(driver, By.xpath("//*[contains(text(), '" + appName + "')]"));
        return tableEntry.isDisplayed();
    }

    public DeviceLabApplicationsPageDeletePopup clickDelete() {

        tableEntry.click();
        deleteButton.click();
        return new DeviceLabApplicationsPageDeletePopup(driver);
    }

    public DeviceLabApplicationsPageUploadPopup clickUpload() {

        uploadButton.click();
        return new DeviceLabApplicationsPageUploadPopup(driver);
    }

    public DeviceLabApplicationsPageInstallPopup clickInstall() {

        installButton.click();
        return new DeviceLabApplicationsPageInstallPopup(driver);
    }

    public void assertAppIsListedInTable(String appName) {

        GuiElement tableEntry = new GuiElement(driver, By.xpath("//*[contains(text(), '" + appName + "')]"));
        int i = 0;
        while (!tableEntry.isDisplayed() && i < 15) {
            TimerUtils.sleep(3 * 1000);
            driver.navigate().refresh();
            i++;
        }

        Assert.assertTrue(tableEntry.isDisplayed(), "Application has been uploaded.");
    }

}