/*
 * Testerra
 *
 * (C) 2022, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
package io.testerra.plugins.appium.seetest.test;

import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created on 22.07.2022
 *
 * @author mgn
 */
public class SeeTestMobileTest extends AbstractAppiumTest {

    private final UiElementFinder finder = UI_ELEMENT_FINDER_FACTORY.create(WEB_DRIVER_MANAGER.getWebDriver());

    @Test
    public void testT01_isPresent() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        final TestableUiElement inputUserName = find(By.xpath("//*[@id='username']"));
        inputUserName.assertThat().present(true);
    }

    private UiElement find(By by) {
        return finder.find(by);
    }
}
