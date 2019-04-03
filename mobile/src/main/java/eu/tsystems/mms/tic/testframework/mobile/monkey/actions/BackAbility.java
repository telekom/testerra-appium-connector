package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 19.08.2016.
 */
public class BackAbility extends MonkeyAbility {
    @Override
    MonkeyAction thinkOfAnAction(MobileDriver mobileDriver) {
        return new BackAction();
    }
}
