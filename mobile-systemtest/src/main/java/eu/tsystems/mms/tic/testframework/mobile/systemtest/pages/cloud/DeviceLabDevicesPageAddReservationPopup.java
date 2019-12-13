package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabDevicesPageAddReservationPopup extends Page {

    private final GuiElement toastBox = new GuiElement(driver, By.className("toast-message"));

    @Check
    private final GuiElement reserveButton = new GuiElement(driver, By.name("reserve-device-ok"));

    public DeviceLabDevicesPageAddReservationPopup(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabDevicesPage confirmReservation() {

        reserveButton.click();
        toastBox.waits().waitForIsDisplayed();
        driver.navigate().refresh();
        return new DeviceLabDevicesPage(driver);
    }

}
