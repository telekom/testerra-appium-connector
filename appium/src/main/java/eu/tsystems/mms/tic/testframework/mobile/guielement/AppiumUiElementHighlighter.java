/*
 * Testerra
 *
 * (C) 2024, Martin Großmann, Deutsche Telekom MMS GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.pageobjects.DefaultUiElementHighlighter;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.Color;

/**
 * Created on 2024-06-11
 *
 * @author mgn
 */
public class AppiumUiElementHighlighter extends DefaultUiElementHighlighter {

    @Override
    public void highlight(WebDriver driver, WebElement webElement, Color color) {
        IExecutionContextController instance = Testerra.getInjector().getInstance(IExecutionContextController.class);
        instance.getCurrentSessionContext().ifPresent(sessionContext -> {
            // Highlighting is only working in browsers but not in apps
            MobileOsChecker mobileOsChecker = new MobileOsChecker();
            Platform platform = mobileOsChecker.getPlatform(driver);
            if (!mobileOsChecker.isAppTest(sessionContext.getWebDriverRequest(), platform)) {
                super.highlight(driver, webElement, color);
            }
        });
    }

}
