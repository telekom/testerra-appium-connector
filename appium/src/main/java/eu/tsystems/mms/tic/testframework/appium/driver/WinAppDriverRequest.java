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

package eu.tsystems.mms.tic.testframework.appium.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AbstractWebDriverRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WinAppDriverRequest extends AbstractWebDriverRequest {
    public WinAppDriverRequest() {
        setBrowser(Browsers.windows);
    }
    public void startDesktop() {
        this.setApplication("Root");
    }

    public void setApplicationPath(String applicationPath) {
        this.setApplicationPath(Paths.get(applicationPath));
    }

    public void setApplicationPath(Path applicationPath) {
        if (!Files.exists(applicationPath)) {
            throw new RuntimeException("Application not found: " + applicationPath);
        }
        this.setApplication(applicationPath.toString());
        this.setWorkingDir(applicationPath.getParent().toString());
    }

    public void setApplication(String applicationId) {
        this.getDesiredCapabilities().setCapability("app", applicationId);
        this.setSessionKey(applicationId);
    }

    public void setWorkingDir(String workingDir) {
        this.getDesiredCapabilities().setCapability("appWorkingDir", workingDir);
    }

    public void setApplicationArguments(String ... argv) {
        this.getDesiredCapabilities().setCapability("appArguments", String.join(" ", argv));
    }

    public void setWindowHandle(String windowHandle) {
        this.getDesiredCapabilities().setCapability("appTopLevelWindow", windowHandle);
    }
}
