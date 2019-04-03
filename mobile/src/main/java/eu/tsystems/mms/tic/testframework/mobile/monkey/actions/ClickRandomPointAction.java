package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 18.08.2016.
 */
public class ClickRandomPointAction extends MonkeyAction {

    private final int x;
    private final int y;
    private final int clickCount;

    public ClickRandomPointAction(int x, int y, int clickCount) {
        this.x = x;
        this.y = y;
        this.clickCount = clickCount;
    }

    @Override
    public void perform(MobileDriver mobileDriver) {
        mobileDriver.seeTestClient().clickCoordinate(x, y, clickCount);
    }
}
