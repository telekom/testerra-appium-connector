/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, T-Systems MMS GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

/**
 * Created on 2023-03-08
 *
 * @author mgn
 */
public class AppiumUtils implements Loggable {

    public String runCommand(WebDriver driver, String command, String... args) {
        log().info("Shell command (native): {}, {}", command, Arrays.toString(args)); // ImmutableList does NOT work here...
        return String.valueOf(JSUtils.executeScript(driver, "mobile: shell", ImmutableMap.of("command", command, "args", Lists.newArrayList(args))));
    }

    public void launchIOSApp(WebDriver driver, String bundleId) {
        log().info("Start application '{}'", bundleId);
        MobileOsChecker checker = new MobileOsChecker();
        Platform platform = checker.getPlatform(driver);
        if (platform == Platform.IOS) {
            JSUtils.executeScript(driver, "mobile: launchApp", ImmutableMap.of("bundleId", bundleId));
            JSUtils.executeScript(driver, "mobile: activateApp", ImmutableMap.of("bundleId", bundleId));
        } else {
            throw new RuntimeException("Cannot start application " + bundleId + " at " + platform);
        }
    }

}
