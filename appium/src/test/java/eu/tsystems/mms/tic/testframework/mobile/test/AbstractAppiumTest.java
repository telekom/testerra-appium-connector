/*
 * Testerra
 *
 * (C) 20205 Martin Großmann, Deutsche Telekom MMS GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.mobile.test;

import eu.tsystems.mms.tic.testframework.appium.AppiumCapabilityHelper;
import eu.tsystems.mms.tic.testframework.common.PropertyManagerProvider;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.testing.PageFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import org.testng.annotations.BeforeSuite;

/**
 * Abstract Representation for appioum based testerra test
 * <p>
 * Date: 16.09.2020
 * Time: 12:46
 *
 * @author Eric Kubenka
 */
public class AbstractAppiumTest extends TesterraTest implements
        WebDriverManagerProvider,
        PageFactoryProvider,
        PropertyManagerProvider,
        Loggable,
        AppiumCapabilityHelper,
        UiElementFinderFactoryProvider {

    @BeforeSuite
    public void init() {
        // Add a `local.properties` to your resources folder to setup secrets, URLs, ...
        PROPERTY_MANAGER.loadProperties("local.properties");
    }

}
