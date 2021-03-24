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

package eu.tsystems.mms.tic.testframework.appium.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.appium.pageobjects.internal.core.WinAppDriverCoreAdapter;
import eu.tsystems.mms.tic.testframework.common.IProperties;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.testing.TestControllerProvider;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WinAppDriverFactory implements WebDriverFactory, Loggable, TestControllerProvider {

    public enum Properties implements IProperties {
        WINAPP_SERVER_URL("tt.winapp.server.url", "http://localhost:4723/"),
        //WINAPP_DRIVER_PATH("tt.winapp.driver","C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe")
        ;
        private final String property;
        private final Object defaultValue;

        Properties(String property, Object defaultValue) {
            this.property = property;
            this.defaultValue = defaultValue;
        }

        public String toString() {
            return this.property;
        }

        public Object getDefault() {
            return this.defaultValue;
        }
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
//        URL winappServerUrl = null;
//        try {
//            winappServerUrl = new URL(Properties.WINAPP_SERVER_URL.asString());
//        } catch (MalformedURLException e) {
//            log().error("Invalid " + Properties.WINAPP_SERVER_URL.toString(), e);
//        }
        WinAppDriverRequest winAppDriverRequest = (WinAppDriverRequest)webDriverRequest;
        DesiredCapabilities desiredCapabilities = winAppDriverRequest.getDesiredCapabilities();
        desiredCapabilities.setCapability("deviceName", "WindowsPC");
        // https://github.com/microsoft/WinAppDriver/issues/1092
//        desiredCapabilities.setCapability("ms:waitForAppLaunch", UiElement.Properties.ELEMENT_TIMEOUT_SECONDS.asLong());
//        desiredCapabilities.setCapability("ms:experimental-webdriver", true);
        AtomicReference<WindowsDriver<WindowsElement>> atomicWebDriver = new AtomicReference<>();
        CONTROL.waitFor(UiElement.Properties.ELEMENT_TIMEOUT_SECONDS.asLong().intValue(), () -> {
            URL winappServerUrl = new URL(Properties.WINAPP_SERVER_URL.asString());;
            atomicWebDriver.set(new WindowsDriver<>(winappServerUrl, desiredCapabilities));
        });
        return atomicWebDriver.get();
    }

    @Override
    public List<String> getSupportedBrowsers() {
        return Arrays.asList(Browsers.windows);
    }

    @Override
    public GuiElementCore createCore(GuiElementData guiElementData) {
        return new WinAppDriverCoreAdapter(guiElementData);
    }
}
