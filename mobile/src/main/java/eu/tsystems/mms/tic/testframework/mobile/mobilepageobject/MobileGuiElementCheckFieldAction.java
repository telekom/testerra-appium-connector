package eu.tsystems.mms.tic.testframework.mobile.mobilepageobject;

import eu.tsystems.mms.tic.testframework.enums.CheckRule;
import eu.tsystems.mms.tic.testframework.exceptions.PageNotFoundException;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.CheckFieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldWithActionConfig;

//TODO rework to core-interop
//import eu.tsystems.mms.tic.testframework.report.utils.MethodAccessUtils;

/**
 * Created by rnhb on 17.12.2015.
 */
public class MobileGuiElementCheckFieldAction extends CheckFieldAction {

    public MobileGuiElementCheckFieldAction(FieldWithActionConfig field, AbstractPage declaringPage) {
        super(field, declaringPage);
    }

    @Override
    protected void checkField(Check check, boolean fast) {
        MobileGuiElement mobileGuiElement;
        try {
            mobileGuiElement = (MobileGuiElement) checkableInstance;
        } catch (ClassCastException e) {
            logger.error("Cannot check element " + checkableInstance + ", as it is not of class "
                    + MobileGuiElement.class.getSimpleName() + ".");
            return;
        }

        try {
            CheckRule checkRule = check.checkRule();
            switch (checkRule) {
                case IS_PRESENT:
                    mobileGuiElement.assertIsPresent();
                    break;
                default:
                    mobileGuiElement.assertIsDisplayed();
                    break;
            }
        } catch (AssertionError e) {
            //TODO rework to core-interop
            //MethodAccessUtils.setThrowable(readableMessage, e);
            throw new PageNotFoundException(readableMessage, e);
        }
    }

    @Override
    protected void additionalBeforeCheck() {
        // nothing to check
    }
}
