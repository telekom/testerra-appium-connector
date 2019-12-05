package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabApplicationsPageInstallPopup extends Page {

    private final GuiElement toastBox = new GuiElement(driver, By.className("toast-message"));

    @Check
    private final GuiElement okButton = new GuiElement(driver, By.name("action-application-ok"));

    public DeviceLabApplicationsPageInstallPopup(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public void chooseDevice(String deviceName) {

        GuiElement tableEntryCheckBox = new GuiElement(driver, By.xpath("//*[contains(text(), '" + deviceName + "')]/preceding-sibling::*/div")); //*[@id="DevicesTable"]/tbody/tr[2]/td[1]/div
        tableEntryCheckBox.click();
    }

    public DeviceLabApplicationsPage confirmInstallation(String deviceName) {

        chooseDevice(deviceName);
        okButton.click();

        Assert.assertEquals(toastBox.getText(), "Application was installed successfully");
        driver.navigate().refresh();
        return new DeviceLabApplicationsPage(driver);
    }

}
