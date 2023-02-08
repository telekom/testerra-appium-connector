/*
 * Testerra
 *
 * (C) 2021, Mike Reiche, T-Systems MMS GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class AppiumDriverRequest extends SeleniumWebDriverRequest {

    public static final String DEVICE_QUERY = "deviceQuery";
    public static final String ACCESS_KEY = "accessKey";
    public static final String CAPABILITY_NAME_TEST_NAME = "testName";

    public AppiumDriverRequest() {
        setAccessKey(AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString());
        this.setBrowser(Browsers.mobile);
    }

    @Override
    public Optional<URL> getServerUrl() {
        if (!super.getServerUrl().isPresent()) {
            try {
                this.setServerUrl(AppiumProperties.MOBILE_GRID_URL.asString());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unable to retrieve default Appium URL from properties", e);
            }
        }
        return super.getServerUrl();
    }

    public void setDeviceQuery(String deviceQuery) {
        this.getDesiredCapabilities().setCapability(DEVICE_QUERY, deviceQuery);
    }

    public void setAccessKey(String accessKey) {
        this.getDesiredCapabilities().setCapability(ACCESS_KEY, accessKey);
    }
}
