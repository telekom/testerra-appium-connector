package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by rnhb on 18.08.2016.
 */
public class ClickRandomElementAction extends MonkeyAction {

    private final String xPath;

    public ClickRandomElementAction(String xPath) {
        this.xPath = xPath;
    }

    @Override
    public void perform(MobileDriver mobileDriver) {
        NativeMobileGuiElement randomElement = new NativeMobileGuiElement(xPath);
        randomElement.click();
    }
}
