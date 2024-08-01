/*
 * Testerra
 *
 * (C) 2024, Martin Großmann, Deutsche Telekom MMS GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.appium;

import org.openqa.selenium.Capabilities;

/**
 * Created on 2024-06-11
 * <p>
 * The Appium client convert all Appium specific capabilities and adds the prefix 'appium:'.
 * This should help to read a capability.
 */
public interface AppiumCapabilityHelper {

    String APPIUM_AUTOMATION_NAME = "automationName";
    String APPIUM_DEVICE_NAME = "deviceName";
    String APPIUM_APP = "app";
    String APPIUM_APP_PACKAGE = "appPackage";
    String APPIUM_APP_ACTIVITY = "appActivity";
    String APPIUM_BUNDLE_ID = "bundleId";
    String APPIUM_PLATFORM_VERSION = "platformVersion";
    String APPIUM_UDID = "udid";

    // This cap belongs to SeeTest platform
    String APPIUM_DEVICE_QUERY = "deviceQuery";
    String APPIUM_ACCESS_KEY = "accessKey";
    String APPIUM_CAPABILITY_NAME_TEST_NAME = "testName";

    default String getAppiumCap(final String capabilityName) {
        return "appium:" + capabilityName;
    }

    default String getCap(Capabilities caps, final String name) {
        return (caps == null || name == null) ? null :
                caps.getCapability(name) != null ? caps.getCapability(name).toString() :
                caps.getCapability(getAppiumCap(name)) != null ? caps.getCapability(getAppiumCap(name)).toString() : null;
    }

}
