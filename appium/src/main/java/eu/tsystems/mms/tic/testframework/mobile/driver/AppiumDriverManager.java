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

import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverProxy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Date: 08.09.2020
 * Time: 11:43
 *
 * @author Eric Kubenka
 */
public class AppiumDriverManager {


    public AppiumDriver<MobileElement> fromWebDriver(WebDriver driver) {

        final WebDriver rawDriver = ((EventFiringWebDriver) driver).getWrappedDriver();
        final InvocationHandler invocationHandler = Proxy.getInvocationHandler(rawDriver);
        final WebDriver rawAppiumDriver = ((WebDriverProxy) invocationHandler).getWrappedWebDriver();
        return (AppiumDriver<MobileElement>) rawAppiumDriver;
    }
}
