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

package eu.tsystems.mms.tic.testframework.appium;

import eu.tsystems.mms.tic.testframework.core.WinAppDriverCoreAdapter;
import eu.tsystems.mms.tic.testframework.common.IProperties;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.testing.TestControllerProvider;
import eu.tsystems.mms.tic.testframework.utils.Sleepy;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WinAppDriverRequest;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WinAppDriverFactory implements WebDriverFactory, Loggable, TestControllerProvider, Sleepy {

    public enum Properties implements IProperties {
        WINAPP_SERVER_URL("tt.winapp.server.url", "http://localhost:4723/"),
        REUSE_TIMEOUT_SECONDS("tt.winapp.reuse.timeout.seconds", 2),
        STARTUP_TIMEOUT_SECONDS("tt.winapp.startup.timeout.seconds", 8),
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

    private WindowsDriver<WindowsElement> startNewWindowsDriver(WinAppDriverRequest appDriverRequest, SessionContext sessionContext) {
        final URL finalWinAppServerUrl = appDriverRequest.getServerUrl().get();
        sessionContext.setNodeUrl(finalWinAppServerUrl);

        DesiredCapabilities desiredCapabilities = appDriverRequest.getDesiredCapabilities();
        desiredCapabilities.setCapability(WinAppDriverRequest.DEVICE_NAME, "WindowsPC");
//        desiredCapabilities.setCapability("ms:experimental-webdriver", true);
        sessionContext.setActualBrowserName("WindowsPC");
        appDriverRequest.getApplicationId().ifPresent(appId -> {
            desiredCapabilities.setCapability(WinAppDriverRequest.APP_ID, appId);
        });

        WindowsDriver<WindowsElement> windowsDriver = new WindowsDriver<>(finalWinAppServerUrl, desiredCapabilities);
        CONTROL.retryFor(appDriverRequest.getStartupTimeoutSeconds(), windowsDriver::getTitle, this::sleep);
        //windowsDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
        return windowsDriver;
    }

    @Override
    public WebDriverRequest prepareWebDriverRequest(WebDriverRequest webDriverRequest) {
        if (!(webDriverRequest instanceof WinAppDriverRequest)) {
            throw new RuntimeException("Unsupported " + webDriverRequest.getClass().getSimpleName());
        }
        return webDriverRequest;
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext)  {
        WinAppDriverRequest appDriverRequest = (WinAppDriverRequest)webDriverRequest;

        /**
         * Try to reuse an already opened application
         * @see https://github.com/Microsoft/WinAppDriver/blob/v1.0-RC2/README.md#attaching-to-an-existing-app-window
         * @see https://github.com/microsoft/WinAppDriver/issues/1092
         */
        appDriverRequest.getReusableApplicationWindowTitle().ifPresent(reuseableWindowTitle -> {
            DesiredCapabilities desiredCapabilities = appDriverRequest.getDesiredCapabilities();
            WinAppDriverRequest desktopDriverRequest;
            if (WinAppDriverRequest.APP_ID_DESKTOP.equals(desiredCapabilities.getCapability(WinAppDriverRequest.APP_ID))) {
                desktopDriverRequest = appDriverRequest;
            } else {
                desktopDriverRequest = new WinAppDriverRequest();
                desktopDriverRequest.setDesktopApplication();
                appDriverRequest.getServerUrl().ifPresent(desktopDriverRequest::setServerUrl);
            }

            WindowsDriver<WindowsElement> desktopDriver = startNewWindowsDriver(desktopDriverRequest, sessionContext);

            log().info(String.format("Try to create driver on running application by window title \"%s\" with %d seconds timeout", reuseableWindowTitle, appDriverRequest.getReuseTimeoutSeconds()));
            CONTROL.waitFor(appDriverRequest.getReuseTimeoutSeconds(), () -> {
                WebElement elementByName = desktopDriver.findElementByName(reuseableWindowTitle);
                String nativeWindowHandle = elementByName.getAttribute("NativeWindowHandle");
                int nativeWindowHandleId = Integer.parseInt(nativeWindowHandle);
                if (nativeWindowHandleId > 0) {
                    log().info("Found running application window handle: " + nativeWindowHandle);
                    desiredCapabilities.setCapability(WinAppDriverRequest.TOP_LEVEL_WINDOW, Integer.toHexString(nativeWindowHandleId));
                    appDriverRequest.unsetApplication();
                } else {
                    log().warn("Ignore invalid application window handle: " + nativeWindowHandle);
                }
            });

            if (desktopDriverRequest != appDriverRequest) {
                desktopDriver.quit();
            }
        });

        return startNewWindowsDriver(appDriverRequest, sessionContext);
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
