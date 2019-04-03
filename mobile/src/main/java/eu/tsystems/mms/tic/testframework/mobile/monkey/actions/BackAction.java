package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 18.08.2016.
 */
public class BackAction extends MonkeyAction {

    @Override
    public void perform(MobileDriver mobileDriver) {
        mobileDriver.back();
    }
}
