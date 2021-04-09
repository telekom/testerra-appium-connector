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

package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.guielement.AppiumGuiElementCoreAdapter;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * Creates {@link WebDriver} sessions for {@link io.appium.java_client.AppiumDriver} based on {@link AppiumDriverRequest}
 * Date: 24.06.2020
 * Time: 13:16
 *
 * @author Eric Kubenka
 */
public class AppiumDriverFactory implements WebDriverFactory, Loggable {

    @Override
    public WebDriverRequest prepareWebDriverRequest(WebDriverRequest webDriverRequest) {
        AppiumDriverRequest finalRequest;

        if (webDriverRequest instanceof AppiumDriverRequest) {
            finalRequest = (AppiumDriverRequest) webDriverRequest;
        } else {
            finalRequest = new AppiumDriverRequest();
            finalRequest.setSessionKey(webDriverRequest.getSessionKey());
            finalRequest.setBrowser(webDriverRequest.getBrowser());
            finalRequest.setBrowserVersion(webDriverRequest.getBrowserVersion());
        }

        DesiredCapabilities requestCapabilities = finalRequest.getDesiredCapabilities();

        // general caps
        requestCapabilities.setCapability("testName", ExecutionContextController.getCurrentExecutionContext().runConfig.getReportName());

        switch (webDriverRequest.getBrowser()) {
            case Browsers.mobile_safari: {
                finalRequest.setDeviceQuery(PropertyManager.getProperty("tt.mobile.device.query.ios", "@os='ios' and @category='PHONE'"));
                break;
            }
            case Browsers.mobile_chrome: {
                finalRequest.setDeviceQuery(PropertyManager.getProperty("tt.mobile.device.query.android", "@os='android' and @category='PHONE'"));
                break;
            }
        }

        return finalRequest;
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest)webDriverRequest;
        DesiredCapabilities requestCapabilities = appiumDriverRequest.getDesiredCapabilities();
        URL appiumUrl = appiumDriverRequest.getServerUrl().get();

        DesiredCapabilities finalCapabilities = new DesiredCapabilities(requestCapabilities);

        AppiumDriver appiumDriver = null;
        switch (webDriverRequest.getBrowser()) {
            case Browsers.mobile_safari: {
                finalCapabilities.setBrowserName(MobileBrowserType.SAFARI);
                appiumDriver = new IOSDriver<>(appiumUrl, finalCapabilities);
                break;
            }
            case Browsers.mobile_chrome: {
                finalCapabilities.setBrowserName(MobileBrowserType.CHROMIUM);
                appiumDriver = new AndroidDriver<>(appiumUrl, finalCapabilities);
                break;
            }
        }
        if (appiumDriver != null) {
            AppiumDeviceQuery appiumDeviceQuery = new AppiumDeviceQuery(appiumDriver.getCapabilities());
            sessionContext.setActualBrowserName(appiumDeviceQuery.toString());
        } else {
            throw new RuntimeException("Mobile Browser not supported: " + webDriverRequest.getBrowser());
        }
        return appiumDriver;
    }

    @Override
    public void setupNewWebDriverSession(EventFiringWebDriver webDriver, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest)sessionContext.getWebDriverRequest();
        appiumDriverRequest.getBaseUrl().ifPresent(url -> webDriver.get(url.toString()));
    }

    @Override
    public List<String> getSupportedBrowsers() {
        return Arrays.asList(Browsers.mobile_chrome, Browsers.mobile_safari);
    }

    @Override
    public GuiElementCore createCore(GuiElementData guiElementData) {
        return new AppiumGuiElementCoreAdapter(guiElementData);
    }
}
