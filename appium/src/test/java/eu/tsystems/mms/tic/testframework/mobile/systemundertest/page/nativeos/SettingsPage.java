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
