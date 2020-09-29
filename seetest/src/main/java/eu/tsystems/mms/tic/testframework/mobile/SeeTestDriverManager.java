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
import com.experitest.client.Client;
import com.experitest.client.GridClient;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;

/**
 * Wrap an {@link io.appium.java_client.AppiumDriver}
 * <p>
 * Date: 17.09.2020
 * Time: 14:05
 *
 * @author Eric Kubenka
 */
public class SeeTestDriverManager implements Loggable {

    String accessKey = PropertyManager.getProperty("tt.mobile.grid.access.key");

    private final AppiumDriverManager appiumDriverManager = new AppiumDriverManager();

    /**
     * Transfer a {@link WebDriver} to {@link SeeTestClient} by using {@link AppiumDriver}
     * See here for more https://docs.experitest.com/pages/viewpage.action?pageId=55281084
     *
     * @param driver {@link WebDriver}
     * @return SeeTestClient
     */
    public SeeTestClient fromWebDriver(WebDriver driver) {

        final AppiumDriver<MobileElement> appiumDriver = appiumDriverManager.fromWebDriver(driver);
        return fromAppiumDriver(appiumDriver);
    }

    public SeeTestClient fromAppiumDriver(AppiumDriver driver) {

        return new SeeTestClient(driver);
    }


}
