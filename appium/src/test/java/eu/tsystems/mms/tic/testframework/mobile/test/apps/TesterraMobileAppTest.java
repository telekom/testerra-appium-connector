package eu.tsystems.mms.tic.testframework.mobile.test.apps;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos.SettingsPage;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos.WifiSettingsPage;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.utils.AppiumUtils;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

/**
 * Created on 2023-02-07
 *
 * @author mgn
 */
public class TesterraMobileAppTest extends AbstractAppiumTest {

    @Test
    public void testT01AndroidApp() throws MalformedURLException {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'Galaxy S20') and @version='13.0'");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");

        request.getDesiredCapabilities().setCapability(MobileCapabilityType.APP, "cloud:eu.tsystems.mms.tic.mdc.app.android/.HomeActivity");
        request.getDesiredCapabilities().setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "eu.tsystems.mms.tic.mdc.app.android");
        request.getDesiredCapabilities().setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".HomeActivity");

        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);
        TimerUtils.sleep(4000);

//        UITestUtils.takeScreenshot(webDriver, true);

//        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
//        Screenshot screenshot = new Screenshot();
//        screenshot.setFile(scrFile);
//        UITestUtils.takeScreenshot(webDriver, screenshot);
    }

    @Test
    public void testT10NativeIOSSettings() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'iPhone X')");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.getDesiredCapabilities().setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);

        WifiSettingsPage wifiSettingsPage = openWifiSettings(webDriver);
        switchWiFi(wifiSettingsPage);
        TimerUtils.sleep(5000);
    }

    @Test
    public void testT11NativeAndroidSettings() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'Samsung Galaxy S20')");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.getDesiredCapabilities().setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);

        WifiSettingsPage wifiSettingsPage = openWifiSettings(webDriver);
        switchWiFi(wifiSettingsPage);
    }

    private WifiSettingsPage openWifiSettings(WebDriver driver) {
        TestStep.begin("Open Wifi settings");
        MobileOsChecker checker = new MobileOsChecker();
        Platform platform = checker.getPlatform(driver);
        switch (platform) {
            case IOS:
                new AppiumUtils().launchIOSApp(driver, "com.apple.Preferences");
                SettingsPage settingsPage = PAGE_FACTORY.createPage(SettingsPage.class, driver);
                return settingsPage.gotoWifiSettings();
            case ANDROID:
                new AppiumUtils().runCommand(driver, "am", "start", "-a", "android.settings.WIFI_SETTINGS");
                return PAGE_FACTORY.createPage(WifiSettingsPage.class, driver);
        }
        throw new RuntimeException("Invalid platform");
    }

    private void switchWiFi(WifiSettingsPage wifiSettingsPage) {
        TestStep.begin("Deactivate wifi");
        wifiSettingsPage.deactivateWifi();
        wifiSettingsPage.getWlanToggle().assertThat().selected(false);

        TestStep.begin("Activate wifi");
        wifiSettingsPage.activateWifi();
        wifiSettingsPage.getWlanToggle().assertThat().selected(true);
    }

}
