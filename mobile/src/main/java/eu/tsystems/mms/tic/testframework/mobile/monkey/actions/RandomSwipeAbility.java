package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 19.08.2016.
 */
public class RandomSwipeAbility extends MonkeyAbility {
    @Override
    MonkeyAction thinkOfAnAction(MobileDriver mobileDriver) {
        Direction direction;
        double random = Math.random();
        if (random < 0.25) {
            direction = Direction.DOWN;
        } else if (random < 0.5) {
            direction = Direction.LEFT;
        } else if (random < 0.75) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.UP;
        }

        int time = (int) (100 + Math.random() * 2000);
        int offset = (int) (100 + Math.random() * 500);

        return new RandomSwipeAction(direction, time, offset);
    }
}
