package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

import java.io.Serializable;

/**
 * Created by rnhb on 19.08.2016.
 */
public abstract class MonkeyAbility implements Serializable{

    public MonkeyAction performMonkeyAction(MobileDriver mobileDriver) {
        MonkeyAction monkeyAction = thinkOfAnAction(mobileDriver);
        monkeyAction.perform(mobileDriver);
        return monkeyAction;
    }

    abstract MonkeyAction thinkOfAnAction(MobileDriver mobileDriver);
}
