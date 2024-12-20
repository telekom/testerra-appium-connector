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

import eu.tsystems.mms.tic.testframework.appium.AppiumCapabilityHelper;
import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class AppiumDriverRequest extends SeleniumWebDriverRequest implements AppiumCapabilityHelper {

    private URL mobileServerUrl;

    public AppiumDriverRequest() {
        this.setAccessKey(AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString());

        if (StringUtils.isBlank(this.getBrowser())) {
            this.setBrowser(Browsers.mobile);
        }

    }

    @Override
    public void setServerUrl(String url) throws MalformedURLException {
        mobileServerUrl = new URL(url);
    }

    @Override
    public void setServerUrl(URL url) {
        this.mobileServerUrl = url;
    }

    @Override
    public Optional<URL> getServerUrl() {
        Optional<URL> serverUrl = Optional.ofNullable(this.mobileServerUrl);
        if (serverUrl.isEmpty()) {
            try {
                this.setServerUrl(AppiumProperties.MOBILE_GRID_URL.asString());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unable to retrieve default Appium URL from properties", e);
            }
        }
        return Optional.ofNullable(this.mobileServerUrl);
    }

    public void setDeviceQuery(String deviceQuery) {
        if (StringUtils.isNotBlank(deviceQuery)) {
            this.getMutableCapabilities().setCapability(APPIUM_DEVICE_QUERY, deviceQuery);
        }
    }

    public String getDeviceQuery() {
        return (String) this.getMutableCapabilities().getCapability(APPIUM_DEVICE_QUERY);
    }

    public void setAccessKey(String accessKey) {
        this.getMutableCapabilities().setCapability(APPIUM_ACCESS_KEY, accessKey);
    }

    public void setAppiumEngine(String engine) {
        this.getMutableCapabilities().setCapability(APPIUM_AUTOMATION_NAME, engine);
    }

    public String getAppiumEngine() {
        return (String) this.getMutableCapabilities().getCapability(getAppiumCap(APPIUM_AUTOMATION_NAME));
    }

    public void setDeviceName(String deviceName) {
        this.getMutableCapabilities().setCapability(APPIUM_DEVICE_NAME, deviceName);
    }

    public String getDeviceName() {
        return (String) this.getMutableCapabilities().getCapability(getAppiumCap(APPIUM_DEVICE_NAME));
    }

    public void setPlatformVersion(String platformVersion) {
        this.getMutableCapabilities().setCapability(APPIUM_PLATFORM_VERSION, platformVersion);
    }

    public String getPlatformVersion() {
        return (String) this.getMutableCapabilities().getCapability(getAppiumCap(APPIUM_PLATFORM_VERSION));
    }

    public void setDeviceId(String id) {
        this.getMutableCapabilities().setCapability(APPIUM_UDID, id);
    }

    public String getDeviceId() {
        return (String) this.getMutableCapabilities().getCapability(getAppiumCap(APPIUM_UDID));
    }
}
