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

package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page;

import eu.tsystems.mms.tic.testframework.mobile.systemundertest.page.component.FlashMessage;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represent https://the-internet.herokuapp.com/login
 * <p>
 * Date: 16.09.2020
 * Time: 12:51
 *
 * @author Eric Kubenka
 */
public class LoginPage extends AbstractInternetPage {

    @Check
    private GuiElement inputUsername = new GuiElement(this.getWebDriver(), Locate.by(By.xpath("//input[@id='username']")));

    @Check
    private GuiElement inputPassword = new GuiElement(this.getWebDriver(), Locate.by(By.xpath("//input[@id='password']")));

    @Check
    private GuiElement inputSubmit = new GuiElement(this.getWebDriver(), Locate.by(By.xpath("//button[@type='submit']")));


    /**
     * Constructor for existing sessions.
     *
     * @param driver .
     */
    public LoginPage(WebDriver driver) {

        super(driver);
    }

    /**
     * Instantiate Error / messages
     *
     * @return FlashMessage
     */
    public FlashMessage messages() {

        return PageFactory.create(FlashMessage.class, this.getWebDriver());
    }

    /**
     * Fills form
     *
     * @param userName {@link String}
     * @param password {@link String}
     */
    public LoginPage doFillForm(final String userName, final String password) {

        this.inputUsername.type(userName);
        this.inputPassword.type(password);
        return this;
    }

    /**
     * Submits and expect error
     *
     * @return LoginPage
     */
    public LoginPage doSubmitAndExpectedError() {

        this.inputSubmit.click();
        return PageFactory.create(LoginPage.class, this.getWebDriver());
    }
}

