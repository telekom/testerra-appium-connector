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

package eu.tsystems.mms.tic.testframework.mobile.test.guielement;

import eu.tsystems.mms.tic.testframework.exceptions.PageNotFoundException;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.LoginPage;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.StartPage;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.TablePage;
import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.component.FooterComponent;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Date: 17.09.2020
 * Time: 12:58
 *
 * @author Eric Kubenka
 */
public class MobilePageTest extends AbstractAppiumTest {

    @Test
    public void testT01_instantiatePage() {

        final WebDriver driver = WebDriverManager.getWebDriver();

        StartPage startPage = PageFactory.create(StartPage.class, driver);
        FooterComponent footerComponent = PageFactory.create(FooterComponent.class, driver);
    }

    @Test(expectedExceptions = PageNotFoundException.class)
    public void testT02_instantiatePageFailed() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        StartPage startPage = PageFactory.create(StartPage.class, driver);
    }

    @Test
    public void testT03_sendForms() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/");

        StartPage startPage = PageFactory.create(StartPage.class, driver);
        LoginPage loginPage = startPage.goToLoginPage();

        loginPage = loginPage.doFillForm("user", "pass");
        loginPage = loginPage.doSubmitAndExpectedError();
        loginPage.messages().isErrorShown("your username is invalid");
    }

    @Test
    public void testT04_entryNotPresentOnTable() {

        TestStep.begin("1. Init driver");
        final WebDriver driver = WebDriverManager.getWebDriver();
        StartPage startPage = PageFactory.create(StartPage.class, driver);

        TestStep.begin("2. Navigate to tables");
        TablePage tablePage = startPage.goToTablePage();

        TestStep.begin("3. Assert user not shown.");
        Assert.assertFalse(tablePage.isUserShown("Test", "Michael"));
    }

    @Test
    public void testT05_entryPresentOnTable() {

        TestStep.begin("1. Init driver");
        final WebDriver driver = WebDriverManager.getWebDriver();
        StartPage startPage = PageFactory.create(StartPage.class, driver);

        TestStep.begin("2. Navigate to tables");
        TablePage tablePage = startPage.goToTablePage();

        TestStep.begin("3. Assert user shown.");
        Assert.assertTrue(tablePage.isUserShown("Smith", "John"));
    }


}
