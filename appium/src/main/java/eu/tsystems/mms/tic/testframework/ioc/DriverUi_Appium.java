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
package eu.tsystems.mms.tic.testframework.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import eu.tsystems.mms.tic.testframework.appium.WinAppDriverFactory;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverFactory;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;

/**
 * Add AppiumDriverFactory and WinAppDriverFactory
 *
 * Date: 24.06.2020
 * Time: 10:29
 *
 * @author Eric Kubenka
 */
public class DriverUi_Appium extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<WebDriverFactory> webDriverFactoryBinder = Multibinder.newSetBinder(binder(), WebDriverFactory.class);
        webDriverFactoryBinder.addBinding().to(AppiumDriverFactory.class).in(Scopes.SINGLETON);
        webDriverFactoryBinder.addBinding().to(WinAppDriverFactory.class).in(Scopes.SINGLETON);
    }
}
