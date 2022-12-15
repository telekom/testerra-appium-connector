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

package eu.tsystems.mms.tic.testframework.mobile;

import com.experitest.appium.SeeTestClient;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Date: 17.09.2020
 * Time: 14:07
 *
 * @author Eric Kubenka
 */
public class SeeTestDriverTest extends AbstractSeeTestDriverTest {

    @Test
    public void testT01_instantiateDriver() {

        final WebDriver driver = WebDriverManager.getWebDriver();
        final SeeTestClient seeTestClient = this.seeTestDriverManager.fromWebDriver(driver);
    }

    @Test(enabled = false)
    public void testT02_instantiateSeeTestClient() {

    }

}
