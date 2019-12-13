package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 21.07.2016.
 */
public class DevicesLabDevicesPageReleaseDevicePopup extends Page {

    private final GuiElement toastBox = new GuiElement(driver, By.className("toast-message"));

    @Check
    private final GuiElement releaseButton = new GuiElement(driver, By.name("action-device-ok"));

    public DevicesLabDevicesPageReleaseDevicePopup(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabDevicesPage confirmRelease() {

        releaseButton.click();
        toastBox.waits().waitForIsDisplayed();
        driver.navigate().refresh();
        return new DeviceLabDevicesPage(driver);
    }

}
