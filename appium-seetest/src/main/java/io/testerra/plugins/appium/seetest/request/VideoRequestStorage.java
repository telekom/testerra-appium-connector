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
package io.testerra.plugins.appium.seetest.request;

import eu.tsystems.mms.tic.testframework.logging.Loggable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Stores {@link VideoRequest} and associated {@link eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest}
 * <p>
 * Date: 15.04.2020
 * Time: 11:30
 *
 * @author Eric Kubenka
 */
public class VideoRequestStorage implements Loggable {

    private final static VideoRequestStorage INSTANCE = new VideoRequestStorage();

    private static final Queue<VideoRequest> GLOBAL_VIDEO_WEBDRIVER_REQUESTS = new ConcurrentLinkedQueue<>();

    private VideoRequestStorage() {

    }

    public static VideoRequestStorage get() {
        return INSTANCE;
    }

    /**
     * Returns thread-local list of current valid {@link VideoRequest}
     *
     * @return List
     */
    public Queue<VideoRequest> list() {
        return GLOBAL_VIDEO_WEBDRIVER_REQUESTS;
    }

    /**
     * Stores a {@link VideoRequest} in thread local and global list.
     *
     * @param request {@link VideoRequest}
     */
    public void store(VideoRequest request) {

        // adding
        GLOBAL_VIDEO_WEBDRIVER_REQUESTS.add(request);
    }

    /**
     * Removes {@link VideoRequest} from storage. Called when video grabbing is done.
     *
     * @param request {@link VideoRequest}
     */
    public void remove(VideoRequest request) {
        GLOBAL_VIDEO_WEBDRIVER_REQUESTS.remove(request);
    }
}
