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

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Startpage
 * <p>
 * Date: 17.09.2020
 * Time: 13:06
 *
 * @author Eric Kubenka
 */
public class StartPage extends AbstractInternetPage {

    @Check
    private UiElement linkLogin = find(By.linkText("Form Authentication"));

    @Check
    private UiElement linkTables = find(By.linkText("Sortable Data Tables"));

    /**
     * Constructor for existing sessions.
     *
     * @param driver .
     */
    public StartPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage goToLoginPage() {
        this.linkLogin.scrollIntoView();
        this.linkLogin.click();
        return createPage(LoginPage.class);
    }

    public TablePage goToTablePage() {
        this.linkTables.scrollIntoView();
        this.linkTables.click();
        return createPage(TablePage.class);
    }
}
