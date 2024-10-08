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
import eu.tsystems.mms.tic.testframework.webdrivermanager.AbstractWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * Created on 2023-03-02
 *
 * @author mgn
 */
public class MobileOsChecker implements AppiumCapabilityHelper {

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
        Optional<WebDriverRequest> optional = instance.getSessionContext(driver).map(SessionContext::getWebDriverRequest);
        if (optional.isPresent()) {
            return getPlatform(optional.get());
        } else {
            return Platform.ANY;
        }
    }

    /**
     * Returns true if WebDriverRequest contains typical app capabilities.
     *
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
