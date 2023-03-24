package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos;

import org.openqa.selenium.WebDriver;

/**
 * Created on 2023-03-13
 *
 * @author mgn
 */
public class IosWifiSettingsPage extends WifiSettingsPage{
    public IosWifiSettingsPage(WebDriver webDriver) {
        super(webDriver);
        log().info("You are using the specific IOS WifiSettings page.");
    }
}
