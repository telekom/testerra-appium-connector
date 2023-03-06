package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.appium.MobileOs;
import eu.tsystems.mms.tic.testframework.internal.NameableChild;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.AbstractFieldAction;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;

import java.lang.reflect.Field;

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

        MobileOs mobileOs = new MobileOsChecker().getOS(this.declaringPage.getWebDriver());

        Platform mobilePlatform = null;
        String automationName = null;

        switch (mobileOs) {
            case ANDROID: {
                mobilePlatform = Platform.ANDROID;
                // TODO: Read from caps or default value
                automationName = "UiAutomator2";
            }
            break;
            case IOS: {
                mobilePlatform = Platform.IOS;
                // TODO: Read from caps or default value
                automationName = "XCUITest";
                break;
            }
            case OTHER:
                throw new RuntimeException("Cannot find UiElement locator for unknown mobile platform");
        }

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
}
