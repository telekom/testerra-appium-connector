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
package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.internal.NameableChild;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.AbstractFieldAction;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import eu.tsystems.mms.tic.testframework.webdrivermanager.IWebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created on 2023-02-09
 *
 * @author mgn
 */
public class CreateAppiumGuiElementAction extends AbstractFieldAction implements UiElementFinderFactoryProvider, Loggable {

    public CreateAppiumGuiElementAction(Field field, AbstractPage declaringPage) {
        super(field, declaringPage);
    }

    @Override
    protected boolean before() {
        return true;
    }

    @Override
    protected void execute() {
        try {
            // UiElements that have already been created may not be updated with new locator.
            if (field.get(this.declaringPage) != null) {
                return;
            }
        } catch (IllegalAccessException e) {
            return;
        }

        Platform mobilePlatform = new MobileOsChecker().getPlatform(this.declaringPage.getWebDriver());
        String automationName = getAutomationEngine(this.declaringPage.getWebDriver(), mobilePlatform);

        // TODO: Check if no Appium annotations are available --> should be logged because of default by
        DefaultElementByBuilder byBuilder = new DefaultElementByBuilder(mobilePlatform.toString(), automationName);
        byBuilder.setAnnotated(field);
        // Note: The DefaultElementByBuilder creates a default By 'By.id("<name of field")' if no annotation is found
        By mobileBy = byBuilder.buildBy();

        try {

            UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(this.declaringPage.getWebDriver());
            UiElement uiElement = uiElementFinder.find(mobileBy);
            if (uiElement instanceof NameableChild) {
                ((NameableChild) uiElement).setParent(this.declaringPage);
            }

            field.set(this.declaringPage, uiElement);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create new " + mobilePlatform + " element", e);
        }
    }

    @Override
    protected void after() {
    }

    private String getAutomationEngine(WebDriver driver, Platform platform) {
        IWebDriverManager instance = Testerra.getInjector().getInstance(IWebDriverManager.class);
        Optional<WebDriverRequest> optional = instance.getSessionContext(driver).map(SessionContext::getWebDriverRequest);
        if (optional.isPresent()) {
            String automationEngine = optional.get().getCapabilities().get(MobileCapabilityType.AUTOMATION_NAME).toString();
            if (StringUtils.isNotBlank(automationEngine)) {
                return automationEngine;
            } else {
                // Use default values for automation engine
                switch (platform) {
                    case ANDROID:
                        return "UiAutomator2";
                    case IOS:
                        return "XCUITest";
                    default:
                        throw new RuntimeException("Cannot get automation engine: Invalid platform " + platform);
                }
            }
        }
        throw new RuntimeException("Cannot get automation engine: Cannot get request from WebDriver session.");
    }
}
