/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, T-Systems MMS GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.tsystems.mms.tic.testframework.mobile.test.apps;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos.SettingsPage;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.nativeos.WifiSettingsPage;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.utils.AppiumUtils;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.utils.UITestUtils;
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
    public void testT01AndroidApp() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setBrowser("mobile");
        request.setDeviceQuery("contains(@name, 'Galaxy S20')");
        request.getMutableCapabilities().setCapability("appiumVersion", "2.2.2");

        request.getMutableCapabilities().setCapability(MobileCapabilityType.APP, "cloud:eu.tsystems.mms.tic.mdc.app.android/.HomeActivity");
        request.getMutableCapabilities().setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "eu.tsystems.mms.tic.mdc.app.android");
        request.getMutableCapabilities().setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".HomeActivity");
        request.setAppiumEngine("UiAutomator2");

        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);
        TimerUtils.sleep(4000);
    }

    @Test
    public void testT10NativeIOSSettings() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'iPhone X')");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.setAppiumEngine("XCUITest");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);

        WifiSettingsPage wifiSettingsPage = openWifiSettings(webDriver);
        switchWiFi(wifiSettingsPage);
        TimerUtils.sleep(5000);

        UITestUtils.takeScreenshots();
    }

    @Test
    public void testT12NativeAndWebAccessIOS() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'iPhone X')");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.setAppiumEngine("XCUITest");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);



        WifiSettingsPage wifiSettingsPage = openWifiSettings(webDriver);
        switchWiFi(wifiSettingsPage);


        UITestUtils.takeScreenshots();
    }

    @Test
    public void testT11NativeAndroidSettings() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.setDeviceQuery("contains(@name, 'Samsung Galaxy S20')");
        request.getDesiredCapabilities().setCapability("appiumVersion", "1.22.3");
        request.setAppiumEngine("UiAutomator2");
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
