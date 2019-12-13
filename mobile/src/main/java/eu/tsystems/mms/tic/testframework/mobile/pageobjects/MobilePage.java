package eu.tsystems.mms.tic.testframework.mobile.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.mobilepageobject.MobileGuiElementCheckFieldAction;
import eu.tsystems.mms.tic.testframework.mobile.mobilepageobject.SetContainingPageFieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldWithActionConfig;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.SetNameFieldAction;

/**
 * Created by rnhb on 29.01.2015.
 *
 * @author rnhb
 */
public class MobilePage extends AbstractPage {

    protected MobileDriver driver;

    public MobilePage() {
        driver = MobileDriverManager.getMobileDriver();
    }

    @Override
    protected final void handleDemoMode(WebDriver webDriver) {
    }

    @Override
    protected List<FieldAction> getFieldActions(List<FieldWithActionConfig> fields, AbstractPage declaringPage) {
        List<FieldAction> fieldActions = new ArrayList<FieldAction>();
        for (FieldWithActionConfig field : fields) {
            SetNameFieldAction setNameFieldAction = new SetNameFieldAction(field.field, declaringPage);
            SetContainingPageFieldAction setContainingPageFieldAction = new SetContainingPageFieldAction(field.field,
                    declaringPage);
            MobileGuiElementCheckFieldAction mobileGuiElementCheckFieldAction = new MobileGuiElementCheckFieldAction(
                    field, declaringPage);

            fieldActions.add(setNameFieldAction);
            fieldActions.add(setContainingPageFieldAction);
            fieldActions.add(mobileGuiElementCheckFieldAction);
        }
        return fieldActions;
    }

    @Override
    public void waitForPageToLoad() {
    }

    @Override
    protected void checkPagePreparation() {

    }
}
