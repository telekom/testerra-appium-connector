package eu.tsystems.mms.tic.testframework.mobile.guielement;

import eu.tsystems.mms.tic.testframework.internal.NameableChild;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.AbstractFieldAction;
import eu.tsystems.mms.tic.testframework.testing.UiElementFinderFactoryProvider;
import org.openqa.selenium.By;

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
        // TODO: Handle iOS locator
        AndroidLocator androidLocator = field.getAnnotation(AndroidLocator.class);
        if (androidLocator != null) {
            try {
//                log().info(androidLocator.xpath());
//                log().info(field.getName());
//                Class<? extends Field> aClass = field.getClass();
//                log().info(aClass.toString());

                UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(this.declaringPage.getWebDriver());
                UiElement uiElement = uiElementFinder.find(By.xpath(androidLocator.xpath()));
                if (uiElement instanceof NameableChild) {
                    ((NameableChild) uiElement).setParent(this.declaringPage);
                }

                field.set(this.declaringPage, uiElement);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create new Android element", e);
            }
        }
    }

    @Override
    protected void after() {

    }
}
