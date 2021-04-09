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

package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AbstractWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.SeleniumWebDriverRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Extends {@link AbstractWebDriverRequest}
 * Date: 24.06.2020
 * Time: 13:29
 *
 * @author Eric Kubenka
 */
public class AppiumDriverRequest extends SeleniumWebDriverRequest {

    private final String DEVICE_QUERY = "deviceQuery";
    private final String ACCESS_KEY = "accessKey";

    public AppiumDriverRequest() {
        super();
        setAccessKey(PropertyManager.getProperty("tt.mobile.grid.access.key"));
    }

    @Override
    public Optional<URL> getServerUrl() {
        if (!super.getServerUrl().isPresent()) {
            try {
                this.setServerUrl(PropertyManager.getProperty("tt.mobile.grid.url"));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unable to retrieve default Appium URL from properties", e);
            }
        }
        return super.getServerUrl();
    }

    public void setDeviceQuery(String deviceQuery) {
        this.getDesiredCapabilities().setCapability(DEVICE_QUERY, deviceQuery);
    }

//    public Optional<String> getDeviceQuery() {
//        return Optional.ofNullable(this.getDesiredCapabilities().getCapability(DEVICE_QUERY).toString());
//    }

    public void setAccessKey(String accessKey) {
        this.getDesiredCapabilities().setCapability(ACCESS_KEY, accessKey);
    }
}
