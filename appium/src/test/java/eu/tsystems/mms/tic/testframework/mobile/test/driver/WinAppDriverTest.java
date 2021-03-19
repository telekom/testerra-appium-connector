/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
 *
 */

package eu.tsystems.mms.tic.testframework.mobile.test.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.appium.driver.WinAppDriverRequest;
import eu.tsystems.mms.tic.testframework.appium.windows.CalculatorApp;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.testing.AssertProvider;
import eu.tsystems.mms.tic.testframework.testing.PageFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.appium.java_client.windows.WindowsDriver;
import java.util.Optional;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Date: 24.06.2020
 * Time: 12:26
 *
 * @author Eric Kubenka
 */
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
        calculatorApp.getResults().expect().text().contains("1337");
    }

    @Test
    public void test_calculate_works() {
        WinAppDriverRequest winAppDriverRequest = new WinAppDriverRequest();
        winAppDriverRequest.setApplication("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(winAppDriverRequest);
        webDriver.findElement(By.xpath("//Button[@Name=\"Eins\"]")).click();
        webDriver.findElement(By.xpath("//*[@AutomationId=\"num1Button\"]")).click();
        Optional<WindowsDriver> windowsDriver1 = WEB_DRIVER_MANAGER.unwrapWebDriver(webDriver, WindowsDriver.class);
        Assert.assertTrue(windowsDriver1.isPresent());
        WindowsDriver windowsDriver = windowsDriver1.get();
        WebElement num2Button = windowsDriver.findElementByAccessibilityId("num2Button");
        num2Button.click();

        WebElement calculatorResults = windowsDriver.findElementByAccessibilityId("CalculatorResults");
        ASSERT.assertContains(calculatorResults.getText(), "112");
    }

    @AfterMethod
    public void tearDown() {

    }
}
