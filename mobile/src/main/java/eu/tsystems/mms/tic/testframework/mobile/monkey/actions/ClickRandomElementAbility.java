package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by rnhb on 19.08.2016.
 */
public class ClickRandomElementAbility extends MonkeyAbility {
    @Override
    MonkeyAction thinkOfAnAction(MobileDriver mobileDriver) {
        NativeMobileGuiElement anyElement = new NativeMobileGuiElement("xpath=.//*[@onScreen='true']");
        int index = 1 + (int) (Math.random() * (anyElement.getNumberOfElements() - 1));

        String xPath = "xpath=(.//*[@onScreen='true'])[" + index + "]";
        return new ClickRandomElementAction(xPath);
    }
}
