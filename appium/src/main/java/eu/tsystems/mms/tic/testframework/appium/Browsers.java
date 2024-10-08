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

package eu.tsystems.mms.tic.testframework.appium;

/**
 * A list ob supported browsers
 */
public class Browsers {
    public static final String windows = "windows";
    public static final String mobile_chrome = "mobile_chrome";
    public static final String mobile_safari = "mobile_safari";

    // Value for app tests
    public static final String android = "android";
    public static final String ios = "ios";

    // Default value for AppiumDriverRequest
    public static final String mobile = "mobile";
}
