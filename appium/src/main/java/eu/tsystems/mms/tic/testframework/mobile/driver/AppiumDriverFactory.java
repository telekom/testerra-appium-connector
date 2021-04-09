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

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.guielement.AppiumGuiElementCoreAdapter;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileBrowserType;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;

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
        return finalRequest;
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        final String GRID_ACCESS_KEY = PropertyManager.getProperty("tt.mobile.grid.access.key");
        final String APPIUM_DEVICE_QUERY_IOS = PropertyManager.getProperty("tt.mobile.device.query.ios", "@os='ios' and @category='PHONE'");
        final String APPIUM_DEVICE_QUERY_ANDROID = PropertyManager.getProperty("tt.mobile.device.query.android", "@os='android' and @category='PHONE'");
        // early exit.
        if (webDriverRequest.getBrowser() == null) {
            throw new RuntimeException("DriverRequest was null.");
        }

        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest)webDriverRequest;

        DesiredCapabilities desiredCapabilities = appiumDriverRequest.getDesiredCapabilities();

        // general caps
        desiredCapabilities.setCapability("testName", ExecutionContextController.getCurrentExecutionContext().runConfig.getReportName());
        desiredCapabilities.setCapability("accessKey", GRID_ACCESS_KEY);

        URL appiumUrl = appiumDriverRequest.getServerUrl().get();
        AppiumDeviceQuery appiumDeviceQuery;

        switch (webDriverRequest.getBrowser()) {
            case MobileBrowsers.mobile_safari:

                desiredCapabilities.setCapability("deviceQuery", APPIUM_DEVICE_QUERY_IOS);
                desiredCapabilities.setBrowserName(MobileBrowserType.SAFARI);

                final IOSDriver<IOSElement> iosDriver = new IOSDriver<>(appiumUrl, desiredCapabilities);
                appiumDeviceQuery = new AppiumDeviceQuery(iosDriver.getCapabilities());
                log().info("iOS Session created for: " + appiumDeviceQuery.toString());
                appiumDriverRequest.getBaseUrl().ifPresent(url -> iosDriver.get(url.toString()));
                return iosDriver;

            case MobileBrowsers.mobile_chrome:

                desiredCapabilities.setCapability("deviceQuery", APPIUM_DEVICE_QUERY_ANDROID);
                desiredCapabilities.setBrowserName(MobileBrowserType.CHROMIUM);

                final AndroidDriver<AndroidElement> androidDriver = new AndroidDriver<>(appiumUrl, desiredCapabilities);
                appiumDeviceQuery = new AppiumDeviceQuery(androidDriver.getCapabilities());
                log().info("Android Session created for: " + appiumDeviceQuery.toString());
                appiumDriverRequest.getBaseUrl().ifPresent(url -> androidDriver.get(url.toString()));
                return androidDriver;

            default:
                throw new RuntimeException("Mobile Browser not supported.");
        }
    }

    @Override
    public List<String> getSupportedBrowsers() {
        return Arrays.asList(MobileBrowsers.mobile_chrome, MobileBrowsers.mobile_safari);
    }

    @Override
    public GuiElementCore createCore(GuiElementData guiElementData) {
        return new AppiumGuiElementCoreAdapter(guiElementData);
    }
}
