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

package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.creation.GuiElementCoreFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Implements {@link GuiElementCoreFactory} to create Appium base {@link GuiElementCore} implementations
 * Date: 24.06.2020
 * Time: 13:18
 *
 * @author Eric Kubenka
 */
public class AppiumGuiElementCoreFactory implements GuiElementCoreFactory {

    @Override
    public GuiElementCore create(By by, WebDriver webDriver, GuiElementData guiElementData) {

        return new AppiumGuiElementCoreAdapter(by, webDriver, guiElementData);
    }
}
