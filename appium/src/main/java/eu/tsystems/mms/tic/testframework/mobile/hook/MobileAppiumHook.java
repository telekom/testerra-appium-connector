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
package eu.tsystems.mms.tic.testframework.mobile.hook;

import eu.tsystems.mms.tic.testframework.common.TesterraCommons;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverFactory;
import eu.tsystems.mms.tic.testframework.mobile.guielement.AppiumGuiElementCoreFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;

/**
 * Implementes Testerra {@link ModuleHook}
 * <p>
 * Date: 24.06.2020
 * Time: 10:29
 *
 * @author Eric Kubenka
 */
public class MobileAppiumHook implements ModuleHook {

    @Override
    public void init() {

        TesterraCommons.init();
        WebDriverManager.registerWebDriverFactory(new AppiumDriverFactory(), "mobile_safari");
        GuiElement.registerGuiElementCoreFactory(new AppiumGuiElementCoreFactory(), "mobile_safari");
    }

    @Override
    public void terminate() {

    }
}
