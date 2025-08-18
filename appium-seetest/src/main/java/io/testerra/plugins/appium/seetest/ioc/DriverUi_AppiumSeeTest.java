/*
 * Testerra
 *
 * (C) 2022, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
package io.testerra.plugins.appium.seetest.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import io.testerra.plugins.appium.seetest.collector.SeeTestVideoCollector;
import io.testerra.plugins.appium.seetest.webdriver.SeeTestAppiumDriverFactory;
import io.testerra.plugins.appium.seetest.webdriver.SeeTestVideoFactory;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public class DriverUi_AppiumSeeTest extends AbstractModule implements
        ModuleHook,
        Loggable,
        WebDriverManagerProvider {

    private final boolean VIDEO_ACTIVE = Testerra.Properties.SCREENCASTER_ACTIVE.asBool();

    @Override
    protected void configure() {
        Multibinder<WebDriverFactory> webDriverFactoryBinder = Multibinder.newSetBinder(binder(), WebDriverFactory.class);
        webDriverFactoryBinder.addBinding().to(SeeTestAppiumDriverFactory.class).in(Scopes.SINGLETON);
    }

    @Override
    public void init() {
        // VIDEO disabled by properties. Not doing anything here.
        if (!VIDEO_ACTIVE) {
            log().warn("SeeTest video download disabled. {} is set to false.", Testerra.Properties.SCREENCASTER_ACTIVE);
            return;
        }

        WEB_DRIVER_MANAGER.registerWebDriverAfterStartupHandler(new SeeTestVideoFactory());

        // Adding Video Handlers
        if (VIDEO_ACTIVE) {
            // Register a shutdown handler to get informed about closing WebDriver sessions
            WEB_DRIVER_MANAGER.registerWebDriverAfterShutdownHandler(new SeeTestVideoCollector());
        }

    }

    @Override
    public void terminate() {
        // Do nothing
    }
}
