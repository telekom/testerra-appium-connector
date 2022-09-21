/*
 * Testerra
 *
 * (C) 2022, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
package io.testerra.plugins.appium.seetest.request;

import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;

public class VideoRequest {
    public final SessionContext sessionContext;
    public final String videoName;

    public final String reportTestId;

    public VideoRequest(SessionContext sessionContext, String videoName, String reportTestId) {
        this.sessionContext = sessionContext;
        this.videoName = videoName;
        this.reportTestId = reportTestId;
    }
}
