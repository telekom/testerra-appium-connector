package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class DeviceLabDevicesPage extends NavigationPage {

    @Check
    private final GuiElement pageWrapper = new GuiElement(driver, By.id("page-wrapper"));

    @Check
    private final GuiElement reserveButton = new GuiElement(driver, By.xpath("//*[contains(@data-original-title, 'Reserve')]"));

    @Check
    private final GuiElement releaseButton = new GuiElement(driver, By.xpath("//*[contains(@data-original-title, 'Release')]"));

    public DeviceLabDevicesPage(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabDevicesPageAddReservationPopup reserveDevice(String deviceName) {

        GuiElement deviceInList = new GuiElement(driver, By.xpath("//*[contains(text(), '" + deviceName + "')]"));
        deviceInList.click();
        reserveButton.click();
        return new DeviceLabDevicesPageAddReservationPopup(driver);
    }

    public DevicesLabDevicesPageReleaseDevicePopup releaseDevice(String deviceName) {

        GuiElement deviceInList = new GuiElement(driver, By.xpath("//*[contains(text(), '" + deviceName + "')]"));
        deviceInList.click();
        releaseButton.click();
        return new DevicesLabDevicesPageReleaseDevicePopup(driver);
    }
}
