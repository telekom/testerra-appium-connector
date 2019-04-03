package eu.tsystems.mms.tic.testframework.mobile.monkey.actions;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;

/**
 * Created by rnhb on 19.08.2016.
 */
public class ClickRandomPointAbility extends MonkeyAbility{
    @Override
    MonkeyAction thinkOfAnAction(MobileDriver mobileDriver) {
        TestDevice activeDevice = mobileDriver.getActiveDevice();
        int x = (int) (Math.random() * activeDevice.getPixelsX());
        int y = (int) (Math.random() * activeDevice.getPixelsY());
        int clickCount = (int) (Math.random() * 3);
        return new ClickRandomPointAction(x,y,clickCount);
    }
}
