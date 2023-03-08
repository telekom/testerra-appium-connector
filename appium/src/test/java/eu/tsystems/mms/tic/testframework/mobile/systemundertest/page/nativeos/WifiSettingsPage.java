package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos;

import eu.tsystems.mms.tic.testframework.mobile.AppiumPage;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebDriver;

/**
 * Created on 2023-03-08
 *
 * @author mgn
 */
public class WifiSettingsPage extends AppiumPage {

    @Check
    @AndroidFindBy(xpath = "//*[@text='WLAN' or @text='Wi-Fi']") // this is language specific (english + german)
    @iOSXCUITFindBy(xpath = "//*[@type='XCUIElementTypeNavigationBar']//*[@label='WLAN']") // native Appium identifier
    protected UiElement headline;

    @Check
    @AndroidFindBy(xpath = "//*[@class='android.widget.Switch' or @id='switch_widget' or @id='checkbox' or @id='switchWidget']")
    // this is language specific (english + german)
    @iOSXCUITFindBy(xpath = "//*[@label='WLAN' and @type='XCUIElementTypeSwitch']")
    protected UiElement wlanToggle;

    public WifiSettingsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void deactivateWifi() {
        this.wlanToggle.select(false);
    }

    public void activateWifi() {
        this.wlanToggle.select(true);
    }

    public TestableUiElement getWlanToggle() {
        return this.wlanToggle;
    }
}
