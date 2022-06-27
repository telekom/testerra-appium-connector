/*
 * Testerra
 *
 * (C) 2021, Mike Reiche, T-Systems MMS GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.core;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.AbstractWebDriverCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import java.awt.Color;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Implements {@link GuiElementCore} to fulfill Testerra {@link GuiElement} functionality.
 */
public class WinAppDriverCoreAdapter extends AbstractWebDriverCore implements Loggable {

    public WinAppDriverCoreAdapter(GuiElementData guiElementData) {
        super(guiElementData);
    }

    @Override
    protected void switchToDefaultContent(WebDriver webDriver) {
        // not supported
    }

    @Override
    protected void switchToFrame(WebDriver webDriver, WebElement webElement) {
        // not supported
    }

    @Override
    public void highlight(Color color) {
        // Not supported
    }

    @Override
    protected void highlightWebElement(WebElement webElement, Color color) {
        // Not supported
    }

    @Override
    public void swipe(int offsetX, int offSetY) {
        // Not supported
    }
}
