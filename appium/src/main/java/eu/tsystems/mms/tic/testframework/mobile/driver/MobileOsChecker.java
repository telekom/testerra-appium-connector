/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, T-Systems MMS GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.AppiumCapabilityHelper;
import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AbstractWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2023-03-02
 *
 * @author mgn
 */
public class MobileOsChecker implements AppiumCapabilityHelper, WebDriverManagerProvider {

    public Platform getPlatform(WebDriverRequest webDriverRequest) {
        if (Browsers.mobile_chrome.equals(webDriverRequest.getBrowser()) || isAppTest(webDriverRequest, Platform.ANDROID)) {
            return Platform.ANDROID;
        }
        if (Browsers.mobile_safari.equals(webDriverRequest.getBrowser()) || isAppTest(webDriverRequest, Platform.IOS)) {
            return Platform.IOS;
        }
        return Platform.ANY;
    }

    public Platform getPlatform(WebDriver driver) {
        IWebDriverManager instance = Testerra.getInjector().getInstance(IWebDriverManager.class);
        WebDriver originDriver = instance.getOriginalFromDecorated(driver);
        if (originDriver instanceof AndroidDriver) {
            return Platform.ANDROID;
        }
        if (originDriver instanceof IOSDriver) {
            return Platform.IOS;
        }
        // When checks before are not working under any circumstances the WebDriverRequest is used.
        Optional<WebDriverRequest> optional = instance.getSessionContext(driver).map(SessionContext::getWebDriverRequest);
        if (optional.isPresent()) {
            return getPlatform(optional.get());
        } else {
            return Platform.ANY;
        }
    }

    /**
     * Returns true if current driver session was created with typical app capabilities.
     */
    public boolean isAppTest(WebDriver driver) {
        AtomicBoolean appTest = new AtomicBoolean(false);
        IExecutionContextController instance = Testerra.getInjector().getInstance(IExecutionContextController.class);
        WebDriver originDriver = WEB_DRIVER_MANAGER.getOriginalFromDecorated(driver);

        // WEB_DRIVER_MANAGER.getSessionContext(driver) does not work because in AppiumGuiElementCoreAdapter the origin non-decorated driver is used.
        // WEB_DRIVER_MANAGER only stores the decorated one!
        // There we have to iterate through linked SessionContexts of current MethodContext and find a matching driver
        instance.getCurrentMethodContext().ifPresent(methodContext -> {
            methodContext.readSessionContexts()
                    .map(sessionContext -> WEB_DRIVER_MANAGER.getWebDriver(sessionContext)
                            .map(driverOfSession -> new AbstractMap.SimpleEntry<>(sessionContext, driverOfSession)))
                    .flatMap(Optional::stream)
                    .filter(entry -> WEB_DRIVER_MANAGER.getOriginalFromDecorated(entry.getValue()) == originDriver)
                    .findFirst()    // There could only be one or no result after driver check
                    .ifPresent(entry -> {
                        Platform platform = this.getPlatform(driver);
                        appTest.set(this.isAppTest(entry.getKey().getWebDriverRequest(), platform));
                    });
        });

        return appTest.get();
    }

    /**
     * Returns true if WebDriverRequest contains typical app capabilities.
     * <p>
     * The method checks all possible caps and there values to find out the platform.
     */
    public boolean isAppTest(WebDriverRequest webDriverRequest, Platform platform) {
        Capabilities capabilities = webDriverRequest.getCapabilities();
        MutableCapabilities mutableCapabilities = ((AbstractWebDriverRequest) webDriverRequest).getMutableCapabilities();

        switch (platform) {
            case ANDROID:
                return capabilities.getPlatformName() == Platform.ANDROID // if instance of UiAutomator2Options --> platform already set
                        || mutableCapabilities.getPlatformName() == Platform.ANDROID
                        || webDriverRequest.getBrowser().equalsIgnoreCase(Browsers.android)
                        || capabilities.getBrowserName().equalsIgnoreCase(Browsers.android)
                        || "Espresso".equalsIgnoreCase(getCap(capabilities, APPIUM_AUTOMATION_NAME))
                        || "UiAutomator2".equalsIgnoreCase(getCap(capabilities, APPIUM_AUTOMATION_NAME))
                        || getCap(capabilities, APPIUM_APP_PACKAGE) != null
                        || getCap(capabilities, APPIUM_APP_ACTIVITY) != null
                        || "Espresso".equalsIgnoreCase(getCap(mutableCapabilities, APPIUM_AUTOMATION_NAME))
                        || "UiAutomator2".equalsIgnoreCase(getCap(mutableCapabilities, APPIUM_AUTOMATION_NAME))
                        || getCap(mutableCapabilities, APPIUM_APP_PACKAGE) != null
                        || getCap(mutableCapabilities, APPIUM_APP_ACTIVITY) != null;
            case IOS:
                return capabilities.getPlatformName() == Platform.IOS // if instance of XCUITestOptions --> platform already set
                        || mutableCapabilities.getPlatformName() == Platform.IOS
                        || webDriverRequest.getBrowser().equalsIgnoreCase(Browsers.ios)
                        || capabilities.getBrowserName().equalsIgnoreCase(Browsers.ios)
                        || "XCUITest".equalsIgnoreCase(getCap(capabilities, APPIUM_AUTOMATION_NAME))
                        || getCap(capabilities, APPIUM_BUNDLE_ID) != null
                        || "XCUITest".equalsIgnoreCase(getCap(mutableCapabilities, APPIUM_AUTOMATION_NAME))
                        || getCap(mutableCapabilities, APPIUM_BUNDLE_ID) != null;
            default:
                return false;
        }
    }


}
