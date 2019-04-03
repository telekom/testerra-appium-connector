package eu.tsystems.mms.tic.testframework.mobile.mobilepageobject;

import eu.tsystems.mms.tic.testframework.exceptions.FennecSystemException;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldAction;

import java.lang.reflect.Field;

/**
 * Created by rnhb on 02.02.2016.
 */
public class SetContainingPageFieldAction extends FieldAction {

    public SetContainingPageFieldAction(Field field, AbstractPage declaringPage) {
        super(field, declaringPage);
    }

    public boolean before() {
        return true;
    }

    public void execute() {
        Class<?> typeOfField = field.getType();
        if (MobileGuiElement.class.isAssignableFrom(typeOfField)) {
            MobileGuiElement mobileGuiElement = null;
            try {
                mobileGuiElement = (MobileGuiElement) field.get(declaringPage);
            } catch (IllegalAccessException e) {
                logger.error("Failed to assign description to " + field + ". Make sure the field was made accessible in the" +
                        " AbstractPage class before calling this method.");
            }
            if (mobileGuiElement != null) {
                if (MobilePage.class.isAssignableFrom(declaringPage.getClass())) {
                    mobileGuiElement.setContainingPage((MobilePage) declaringPage);
                } else {
                    // Another Page added this FieldAction, that should only be used by MobileGuiElement
                    throw new FennecSystemException("Internal Error.");
                }
            }
        }
    }

    public void after() {

    }
}
