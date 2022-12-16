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

import org.openqa.selenium.Capabilities;

/**
 * Represent a device query
 * <p>
 * Date: 17.09.2020
 * Time: 10:25
 *
 * @author Eric Kubenka
 */
public class AppiumDeviceQuery {
    private String os;
    private String version;
    private String manufacture;
    private String model;
    private String browserName;

    // TODO: Extend this with app name, if app is tested
    public AppiumDeviceQuery() {

    }

    public AppiumDeviceQuery(Capabilities capabilities) {
        this.setOs((String) capabilities.getCapability("platformName"));
        this.setVersion((String) capabilities.getCapability("platformVersion"));
        this.setManufacture((String) capabilities.getCapability("deviceManufacture"));
        this.setModel((String) capabilities.getCapability("deviceModel"));
        this.setBrowserName(capabilities.getBrowserName());
    }

    public String getOs() {

        return os;
    }

    public void setOs(String os) {

        this.os = os;
    }

    public String getVersion() {

        return version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public String getManufacture() {

        return manufacture;
    }

    public void setManufacture(String manufacture) {

        this.manufacture = manufacture;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    @Override
    public String toString() {

        return "AppiumDevice {" +
                "os='" + os + '\'' +
                ", version='" + version + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", model='" + model + '\'' +
                ", browser='" + browserName + '\'' +
                '}';
    }
}
