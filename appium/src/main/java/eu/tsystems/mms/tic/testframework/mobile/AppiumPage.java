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
        log().info("Custom field action");
        CreateAppiumGuiElementAction action = new CreateAppiumGuiElementAction(field, declaringPage);
        SetNameFieldAction nameFieldAction = new SetNameFieldAction(field, declaringPage);
        return Optional.of(List.of(action, nameFieldAction));
    }

}
