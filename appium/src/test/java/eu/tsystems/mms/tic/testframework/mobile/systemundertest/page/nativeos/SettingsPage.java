package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos;

import eu.tsystems.mms.tic.testframework.mobile.AppiumPage;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created on 2023-03-08
 *
 * @author mgn
 */
public class SettingsPage extends AppiumPage {

    @Check
    private UiElement title = find(By.xpath("//XCUIElementTypeStaticText[@value='Einstellungen']"));

    @Check
    @iOSXCUITFindBy(xpath = "//*[@type='XCUIElementTypeStaticText' and @value='WLAN']")
    private UiElement buttonWLAN;

    public SettingsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WifiSettingsPage gotoWifiSettings() {
        this.buttonWLAN.click();
        return createPage(WifiSettingsPage.class);
    }
}
