/*
 * Testerra
 *
 * (C) 2021, Mike Reiche, T-Systems MMS GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.appium.windows;

import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.PreparedLocator;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import org.openqa.selenium.WebDriver;

public class CalculatorApp extends Page {

    private final PreparedLocator automationLocator = LOCATE.prepare("//*[@AutomationId=\"%s\"]");

    private final UiElement num1Btn = find(automationLocator.with("num1Button"));
    private final UiElement num3Btn = find(automationLocator.with("num3Button"));
    private final UiElement num7Btn = find(automationLocator.with("num7Button"));
    private final UiElement results = find(automationLocator.with("CalculatorResults"));

    public CalculatorApp(WebDriver webDriver) {
        super(webDriver);
    }

    public void typeSomething() {
        num1Btn.click();
        num3Btn.click();
        num3Btn.click();
        num7Btn.click();
    }

    public TestableUiElement getResults() {
        return results;
    }
}
