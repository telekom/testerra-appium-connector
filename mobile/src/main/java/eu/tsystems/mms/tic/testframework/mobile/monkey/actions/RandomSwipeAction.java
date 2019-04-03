package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 18.08.2016.
 */
public class RandomSwipeAction extends MonkeyAction {

    private final Direction direction;
    private final int time;
    private final int offset;

    public RandomSwipeAction(Direction direction, int time, int offset) {
        this.direction = direction;
        this.time = time;
        this.offset = offset;
    }

    @Override
    public void perform(MobileDriver mobileDriver) {
        mobileDriver.swipe(direction, offset, time);
    }
}
