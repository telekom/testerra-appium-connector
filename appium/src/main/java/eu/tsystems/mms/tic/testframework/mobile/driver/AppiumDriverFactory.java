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
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.guielement.AppiumGuiElementCoreAdapter;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.options.BaseOptions;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
//            finalRequest.setBrowserVersion(webDriverRequest.getBrowserVersion());
        }

        MutableCapabilities requestCapabilities = finalRequest.getMutableCapabilities();
        BaseOptions baseOptions = new BaseOptions<>();

        IExecutionContextController executionContext = Testerra.getInjector().getInstance(IExecutionContextController.class);
        baseOptions.setCapability(AppiumDriverRequest.CAPABILITY_NAME_TEST_NAME, executionContext.getExecutionContext().getRunConfig().getReportName());

        if (requestCapabilities.getCapability(AppiumDriverRequest.DEVICE_QUERY) == null
                || StringUtils.isBlank(requestCapabilities.getCapability(AppiumDriverRequest.DEVICE_QUERY).toString())) {
            switch (webDriverRequest.getBrowser()) {
                case Browsers.mobile_safari: {
                    baseOptions.setCapability(AppiumDriverRequest.DEVICE_QUERY, AppiumProperties.MOBILE_APPIUM_DEVICE_QUERY_IOS.asString());
                    break;
                }
                case Browsers.mobile_chrome: {
                    baseOptions.setCapability(AppiumDriverRequest.DEVICE_QUERY, AppiumProperties.MOBILE_APPIUM_DEVICE_QUERY_ANDROID.asString());
                    break;
                }
            }
        }

        switch (webDriverRequest.getBrowser()) {
            case Browsers.mobile_chrome:
                baseOptions.setCapability(CapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
                break;
            case Browsers.mobile_safari:
                baseOptions.setCapability(CapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
                break;
            default:
                log().info("No mobile browser requested.");
        }

        // Any additional defined desired capabilities are merged into browser options
        baseOptions = baseOptions.merge(finalRequest.getDesiredCapabilities());
        baseOptions = baseOptions.merge(finalRequest.getMutableCapabilities());
        finalRequest.setCapabilities(baseOptions);

        return finalRequest;
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        try {
            return startNewAppiumSession(webDriverRequest, sessionContext);
        } catch (Exception e) {
            // In case of an exception there is a second retry
            int ms = Testerra.Properties.WEBDRIVER_TIMEOUT_SECONDS_RETRY.asLong().intValue() * 1000;
            log().error("Error starting WebDriver. Trying again in {} seconds", (ms / 1000), e);
            TimerUtils.sleep(ms);
            return startNewAppiumSession(webDriverRequest, sessionContext);
        }
    }

    private WebDriver startNewAppiumSession(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest) webDriverRequest;
        Capabilities requestCapabilities = appiumDriverRequest.getCapabilities();
        URL appiumUrl = appiumDriverRequest.getServerUrl().get();
//        DesiredCapabilities finalCapabilities = new DesiredCapabilities(requestCapabilities);

//        IExecutionContextController executionContextController = Testerra.getInjector().getInstance(IExecutionContextController.class);
//        DefaultCapabilityUtils utils = new DefaultCapabilityUtils();

//        utils.putIfAbsent(requestCapabilities, AppiumDriverRequest.CAPABILITY_NAME_TEST_NAME, executionContextController.getExecutionContext().getRunConfig().getReportName());

        AppiumDriver appiumDriver = null;
        Platform mobilePlatform = new MobileOsChecker().getPlatform(webDriverRequest);
        switch (mobilePlatform) {
            case IOS:
                appiumDriver = new IOSDriver(appiumUrl, requestCapabilities);
                break;
            case ANDROID:
                appiumDriver = new AndroidDriver(appiumUrl, requestCapabilities);
                break;
        }

        if (appiumDriver != null) {
            AppiumDeviceQuery appiumDeviceQuery = new AppiumDeviceQuery(appiumDriver.getCapabilities());
            sessionContext.setUserAgent(appiumDeviceQuery.toString());
            sessionContext.setActualBrowserName(appiumDeviceQuery.getBrowserName());
        } else {
            throw new RuntimeException("Cannot create new Appium session - ambiguous capabilities found:\n " + requestCapabilities.toString());
//            throw new RuntimeException("Mobile Browser not supported: " + webDriverRequest.getBrowser());
        }
        return appiumDriver;
    }

    @Override
    public void setupNewWebDriverSession(EventFiringWebDriver webDriver, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest) sessionContext.getWebDriverRequest();
        AtomicReference<String> driverString = new AtomicReference<>("AppiumDriver");
        Testerra.getInjector().getInstance(IWebDriverManager.class)
                .unwrapWebDriver(webDriver, AppiumDriver.class)
                .ifPresent(driver -> driverString.set(driver.getClass().toString()));

        // In case of app automation it es not possible to call a URL
        if (StringUtils.isNotBlank(appiumDriverRequest.getBrowser())) {
            appiumDriverRequest.getBaseUrl().ifPresent(url -> {
                log().info("Open {} on {}", url, driverString.get());
                webDriver.get(url.toString());
            });
        }
    }

    @Override
    public List<String> getSupportedBrowsers() {
        return Arrays.asList(Browsers.mobile_chrome, Browsers.mobile_safari, Browsers.mobile);
    }

    @Override
    public GuiElementCore createCore(GuiElementData guiElementData) {
        return new AppiumGuiElementCoreAdapter(guiElementData);
    }

}
