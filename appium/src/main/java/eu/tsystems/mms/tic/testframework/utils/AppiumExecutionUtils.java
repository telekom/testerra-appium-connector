/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.utils;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created on 2023-03-10
 *
 * @author mgn
 */
public class AppiumExecutionUtils implements ExecutionUtils {

    /**
     * The only change against default is the log level in the catch block.
     * This is needed to keep clean the logs because of some restrictions of Appium for native apps.
     */
    public <T> Optional<T> getFailsafe(Supplier<T> supplier) {
        try {
            T returnVal = supplier.get();
            return Optional.ofNullable(returnVal);
        } catch (Throwable e) {
            log().debug("Property is not supported at this platform\n", e);
            return Optional.empty();
        }
    }
}
