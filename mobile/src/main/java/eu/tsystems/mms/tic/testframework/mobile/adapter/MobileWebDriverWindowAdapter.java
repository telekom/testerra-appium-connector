package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rnhb on 10.02.2016.
 */
public class MobileWebDriverWindowAdapter implements MobileWebDriverAdapter.Window {

    private final MobileDriver mobileDriver;

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileWebDriverWindowAdapter.class);

    public MobileWebDriverWindowAdapter(MobileDriver mobileDriver) {
        this.mobileDriver = mobileDriver;
    }

    @Override
    public void setSize(Dimension dimension) {
        LOGGER.warn("setSize was called on mobile browser, will do nothing.");
    }

    @Override
    public void setPosition(Point point) {
        LOGGER.warn("setPosition was called on mobile browser, will do nothing.");
    }

    @Override
    public Dimension getSize() {
        int x = mobileDriver.seeTestClient().p2cx(100);
        int y = mobileDriver.seeTestClient().p2cy(100);
        return new Dimension(x, y);
    }

    @Override
    public Point getPosition() {
        return new Point(0, 0);
    }

    @Override
    public void maximize() {
        LOGGER.warn("maximize() was called on mobile browser, will do nothing.");
    }

    @Override
    public void fullscreen() {
        LOGGER.warn("fullscreen() was called on mobile browser, will do nothing.");
    }
}
