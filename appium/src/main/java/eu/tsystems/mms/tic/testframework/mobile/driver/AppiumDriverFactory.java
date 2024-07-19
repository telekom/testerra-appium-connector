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

import eu.tsystems.mms.tic.testframework.appium.AppiumCapabilityHelper;
import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.internal.metrics.MetricsController;
import eu.tsystems.mms.tic.testframework.internal.metrics.MetricsType;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.guielement.AppiumGuiElementCoreAdapter;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.EventLoggingDriverListener;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.VisualEventDriverListener;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.options.BaseOptions;
import io.appium.java_client.safari.options.SafariOptions;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.TestException;

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
public class AppiumDriverFactory implements
        WebDriverFactory,
        Loggable,
        AppiumCapabilityHelper,
        WebDriverManagerProvider {

    @Override
    public WebDriverRequest prepareWebDriverRequest(WebDriverRequest webDriverRequest) {
        AppiumDriverRequest finalRequest;

        if (webDriverRequest instanceof AppiumDriverRequest) {
            finalRequest = (AppiumDriverRequest) webDriverRequest;
        } else {
            finalRequest = new AppiumDriverRequest();
            finalRequest.setSessionKey(webDriverRequest.getSessionKey());
            finalRequest.setBrowser(webDriverRequest.getBrowser());
        }

        Platform platform = new MobileOsChecker().getPlatform(webDriverRequest);
        Capabilities userAgentCapabilities = null;

        // Configure mobile options
        switch (platform) {

            case ANDROID: {
                UiAutomator2Options androidOptions = new UiAutomator2Options();
                if (Browsers.mobile_chrome.equals(webDriverRequest.getBrowser())) {
                    androidOptions.setCapability(CapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    WEB_DRIVER_MANAGER.getUserAgentConfig(Browsers.mobile_chrome)
                            .ifPresent(userAgentConfig -> userAgentConfig.configure(chromeOptions));
                    androidOptions = androidOptions.merge(chromeOptions);
                }
                UiAutomator2Options customAndroidOptions = new UiAutomator2Options();
                WEB_DRIVER_MANAGER.getUserAgentConfig(Browsers.android)
                        .ifPresent(userAgentConfig -> userAgentConfig.configure(customAndroidOptions));
                androidOptions = androidOptions.merge(customAndroidOptions);
                userAgentCapabilities = androidOptions;
                break;
            }

            case IOS: {
                XCUITestOptions iosOptions = new XCUITestOptions();
                if (Browsers.mobile_safari.equals(webDriverRequest.getBrowser())) {
                    iosOptions.setCapability(CapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
                    SafariOptions safariOptions = new SafariOptions();

                    WEB_DRIVER_MANAGER.getUserAgentConfig(Browsers.mobile_safari)
                            .ifPresent(userAgentConfig -> userAgentConfig.configure(safariOptions));
                    iosOptions = iosOptions.merge(safariOptions);
                }
                XCUITestOptions customIosOptions = new XCUITestOptions();
                WEB_DRIVER_MANAGER.getUserAgentConfig(Browsers.ios)
                        .ifPresent(userAgentConfig -> userAgentConfig.configure(customIosOptions));
                iosOptions = iosOptions.merge(customIosOptions);
                userAgentCapabilities = iosOptions;
                break;
            }

            default:
                throw new TestException("Cannot find platform for your webdriver request.");
        }

        BaseOptions otherOptions = new BaseOptions<>();

        if (finalRequest.getDeviceQuery() == null || StringUtils.isEmpty(finalRequest.getDeviceQuery())) {
            switch (platform) {
                case ANDROID:
                    final String androidQuery = AppiumProperties.MOBILE_APPIUM_DEVICE_QUERY_ANDROID.asString();
                    if (StringUtils.isNotEmpty(androidQuery)) {
                        otherOptions.setCapability(APPIUM_DEVICE_QUERY, androidQuery);
                    }
                    break;
                case IOS:
                    final String iosQuery = AppiumProperties.MOBILE_APPIUM_DEVICE_QUERY_IOS.asString();
                    if (StringUtils.isNotEmpty(iosQuery)) {
                        otherOptions.setCapability(APPIUM_DEVICE_QUERY, iosQuery);
                    }
                    break;
            }
        }

        IExecutionContextController executionContext = Testerra.getInjector().getInstance(IExecutionContextController.class);
        otherOptions.setCapability(APPIUM_CAPABILITY_NAME_TEST_NAME, executionContext.getExecutionContext().getRunConfig().getReportName());

        // TODO: Handle app capabilities from test.properties
//                // case iOS
//                XCUITestOptions iosAppOptions = new XCUITestOptions();
//                iosAppOptions.setApp("");
//                iosAppOptions.setBundleId("");
//                // case Android
//                UiAutomator2Options androidAppOptions = new UiAutomator2Options();
//                androidAppOptions.setApp("");
//                androidAppOptions.setAppPackage("");
//                androidAppOptions.setAppActivity("");
//

        // Any additional defined desired capabilities are merged into base options
        userAgentCapabilities = userAgentCapabilities.merge(otherOptions);

        userAgentCapabilities = userAgentCapabilities.merge(finalRequest.getDesiredCapabilities());
        userAgentCapabilities = userAgentCapabilities.merge(finalRequest.getMutableCapabilities());

        if (finalRequest.getCapabilities() != null) {
            finalRequest.setCapabilities(finalRequest.getCapabilities().merge(userAgentCapabilities));
        } else {
            finalRequest.setCapabilities(userAgentCapabilities);
        }

        return finalRequest;
    }

    @Override
    public WebDriver createWebDriver(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        try {
            return startNewAppiumSession(webDriverRequest, sessionContext);
        } catch (Exception e) {
            // In case of an exception there is a second retry
            int ms = Testerra.Properties.SELENIUM_WEBDRIVER_CREATE_RETRY.asLong().intValue() * 1000;
            log().error("Error starting WebDriver. Trying again in {} seconds", (ms / 1000), e);
            TimerUtils.sleep(ms);
            return startNewAppiumSession(webDriverRequest, sessionContext);
        }
    }

    private WebDriver startNewAppiumSession(WebDriverRequest webDriverRequest, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest) webDriverRequest;
        Capabilities requestCapabilities = appiumDriverRequest.getCapabilities();
        URL appiumUrl = appiumDriverRequest.getServerUrl().get();

        AppiumDriver appiumDriver = null;
        Platform mobilePlatform = requestCapabilities.getCapability(CapabilityType.PLATFORM_NAME) != null ?
                Platform.extractFromSysProperty(requestCapabilities.getCapability(CapabilityType.PLATFORM_NAME).toString())
                : new MobileOsChecker().getPlatform(webDriverRequest);

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
        }
        return appiumDriver;
    }

    @Override
    public WebDriver setupNewWebDriverSession(WebDriver webDriver, SessionContext sessionContext) {
        AppiumDriverRequest appiumDriverRequest = (AppiumDriverRequest) sessionContext.getWebDriverRequest();
        AtomicReference<String> driverString = new AtomicReference<>("AppiumDriver");
        Testerra.getInjector().getInstance(IWebDriverManager.class)
                .unwrapWebDriver(webDriver, AppiumDriver.class)
                .ifPresent(driver -> driverString.set(driver.getClass().toString()));

        VisualEventDriverListener visualListener = new VisualEventDriverListener();
        WebDriver decoratedDriver = new EventFiringDecorator(
                new EventLoggingDriverListener(),
                visualListener
        ).decorate(webDriver);
        visualListener.driver = decoratedDriver;    // Needed to interact with current session

        // In case of app automation it es not possible to call a URL
        if (StringUtils.isNotBlank(appiumDriverRequest.getBrowser())) {
            appiumDriverRequest.getBaseUrl().ifPresent(baseUrl -> {
                try {
                    log().info("Open {} on {}", baseUrl, driverString.get());
                    sessionContext.setBaseUrl(baseUrl.toString());
                    MetricsController metricsController = Testerra.getInjector().getInstance(MetricsController.class);
                    metricsController.start(sessionContext, MetricsType.BASEURL_LOAD);
                    decoratedDriver.get(baseUrl.toString());
                    metricsController.stop(sessionContext, MetricsType.BASEURL_LOAD);
                } catch (Exception e) {
                    log().error("Unable to open baseUrl", e);
                }
            });
        }

        return decoratedDriver;
    }

    @Override
    public List<String> getSupportedBrowsers() {
        return Arrays.asList(
                Browsers.mobile_chrome,
                Browsers.mobile_safari,
                Browsers.mobile,
                Browsers.android,
                Browsers.ios
        );
    }

    @Override
    public GuiElementCore createCore(GuiElementData guiElementData) {
        return new AppiumGuiElementCoreAdapter(guiElementData);
    }

}
