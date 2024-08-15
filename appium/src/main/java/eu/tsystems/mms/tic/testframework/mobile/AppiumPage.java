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
package eu.tsystems.mms.tic.testframework.mobile;

import eu.tsystems.mms.tic.testframework.mobile.guielement.CreateAppiumGuiElementAction;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.AbstractFieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.SetNameFieldAction;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Created on 2023-02-09
 *
 * @author mgn
 */
public class AppiumPage extends Page {

    public AppiumPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected Optional<List<AbstractFieldAction>> addCustomFieldActions(Field field, AbstractPage declaringPage) {
        log().debug("Custom field action for {}", field.getName());
        CreateAppiumGuiElementAction action = new CreateAppiumGuiElementAction(field, declaringPage);
        SetNameFieldAction nameFieldAction = new SetNameFieldAction(field, declaringPage);
        return Optional.of(List.of(action, nameFieldAction));
    }

}
