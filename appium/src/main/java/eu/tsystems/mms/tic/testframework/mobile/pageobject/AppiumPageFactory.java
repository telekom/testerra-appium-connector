/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, Deutsche Telekom MMS GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.mobile.pageobject;

import eu.tsystems.mms.tic.testframework.enums.CheckRule;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.DefaultPageFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.PageFactory;
import org.openqa.selenium.WebDriver;

/**
 * Created on 2023-03-13
 *
 * @author mgn
 */
public class AppiumPageFactory extends DefaultPageFactory {

    @Override
    public <T extends Page> T createPageWithCheckRule(Class<T> pageClass, WebDriver webDriver, CheckRule checkRule) {
        return super.createPageWithCheckRule(AppiumClassFinder.getInstance().getBestMatchingClass(pageClass, webDriver), webDriver, checkRule);
    }

    @Override
    public PageFactory clearThreadLocalPagesPrefix() {
        AppiumClassFinder.getInstance().clearCache();
        return super.clearThreadLocalPagesPrefix();
    }

}
