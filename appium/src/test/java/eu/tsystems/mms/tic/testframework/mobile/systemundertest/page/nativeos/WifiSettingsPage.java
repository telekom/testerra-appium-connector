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
