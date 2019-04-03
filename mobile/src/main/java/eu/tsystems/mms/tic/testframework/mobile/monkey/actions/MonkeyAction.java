package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

import java.io.Serializable;

/**
 * Created by rnhb on 18.08.2016.
 */
public abstract class MonkeyAction implements Serializable {
    public abstract void perform(MobileDriver mobileDriver);
}
