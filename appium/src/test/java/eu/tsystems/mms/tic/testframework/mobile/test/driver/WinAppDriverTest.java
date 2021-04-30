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

package eu.tsystems.mms.tic.testframework.mobile.test.driver;

import eu.tsystems.mms.tic.testframework.webdrivermanager.WinAppDriverRequest;
import eu.tsystems.mms.tic.testframework.appium.windows.CalculatorApp;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.testing.AssertProvider;
import eu.tsystems.mms.tic.testframework.testing.PageFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class WinAppDriverTest implements
        WebDriverManagerProvider,
        PageFactoryProvider,
        UiElementFinderFactoryProvider,
        Loggable,
        AssertProvider
{

    @Test
    public void test_calculate() {
        WinAppDriverRequest winAppDriverRequest = new WinAppDriverRequest();
        winAppDriverRequest.setApplication("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");

        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(winAppDriverRequest);
        CalculatorApp calculatorApp = PAGE_FACTORY.createPage(CalculatorApp.class, webDriver);
        calculatorApp.typeSomething();
        calculatorApp.getResults().expect().text().contains("1.337").is(true);
    }

    @AfterMethod
    public void tearDown() {

    }
}
