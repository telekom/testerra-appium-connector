/*
 * Testerra
 *
 * (C) 2022, Daniel Eckardt, T-Systems MMS GmbH, Deutsche Telekom AG
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

import io.appium.java_client.AppiumDriver;

/**
 * Created on 2023-03-27
 *
 * @author mgn
 */
public enum AppiumContext {

    /**
     * The context ({@link AppiumDriver#getContext()}) of the current view of the application is an operating system native.
     */
    NATIVE_APP,
    /**
     * The context ({@link AppiumDriver#getContext()}) of the current view of the application is a (embedded) browser. {@see
     * http://appium.io/docs/en/writing-running-appium/web/hybrid/index.html}
     */
    WEBVIEW;

    public static AppiumContext parse(String string) {
        // there's only two NATIVE_APP contexts (NATIVE_APP and NATIVE_APP_INSTRUMENTED),
        // but an infinite amount of WEBVIEW contexts, like WEBVIEW, SAFARI, CHROMIUM, WEBVIEW_1, WEBVIEW_2 etc.
        // that's why we just assume that any context starting with NATIVE_APP is native and the rest WEBVIEW:
        AppiumContext result = string == null || string.toLowerCase().startsWith("native_app") ? NATIVE_APP : WEBVIEW;
//        LOGGER.debug(String.format("Parsed %s as %s", string, result));
        return result;
    }

}
