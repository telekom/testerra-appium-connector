package eu.tsystems.mms.tic.testframework.mobile.driver;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;

/**
 * Created by rnhb on 17.04.2015.
 */
public class ElementAccessor {

    private final MobileDriver mobileDriver;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ElementAccessor(MobileDriver mobileDriver) {
        this.mobileDriver = mobileDriver;
    }

    public String getProperty(MobileLocator mobileLocator, String property) {
        return mobileDriver.seeTestClient().elementGetProperty(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, property);
    }

    public void click(MobileLocator mobileLocator, int clickCount, int offsetX, int offsetY) {
        mobileDriver.seeTestClient().click(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, clickCount, offsetX, offsetY);
    }

    public void click(MobileLocator mobileLocator, int clickCount) {
        mobileDriver.seeTestClient().click(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, clickCount);
    }

    public void longClick(MobileLocator mobileLocator, int clickCount, int x, int y) {
        mobileDriver.seeTestClient().longClick(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, clickCount, x, y);
    }

    public void elementSwipe(MobileLocator mobileLocator, String direction, int offset, int swipeTime) {
        mobileDriver.seeTestClient().elementSwipe(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, direction, offset, swipeTime);
    }

    public void swipeWhileNotFound(String direction, int offset, int swipeTime, MobileLocator mobileLocator, int delay, int maxRounds, boolean click) {
        mobileDriver.seeTestClient().swipeWhileNotFound(direction, offset, swipeTime, mobileLocator.zone, mobileLocator.locator, mobileLocator.index, delay, maxRounds, click);
    }

    public void verifyElementFound(MobileLocator mobileLocator) {
        mobileDriver.seeTestClient().verifyElementFound(mobileLocator.zone, mobileLocator.locator, mobileLocator.index);
    }

    public void verifyElementNotFound(MobileLocator mobileLocator) {
        mobileDriver.seeTestClient().verifyElementNotFound(mobileLocator.zone, mobileLocator.locator, mobileLocator.index);
    }

    public void elementSendText(MobileLocator mobileLocator, String text) {
        mobileDriver.seeTestClient().elementSendText(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, text);
    }

    public String getText(MobileLocator mobileLocator) {
        return mobileDriver.seeTestClient().elementGetText(mobileLocator.zone, mobileLocator.locator, mobileLocator.index);
    }

    public int getElementCount(MobileLocator mobileLocator) {
        return mobileDriver.seeTestClient().getElementCount(mobileLocator.zone, mobileLocator.locator);
    }

    public boolean isElementFound(MobileLocator mobileLocator) {
        return mobileDriver.seeTestClient().isElementFound(mobileLocator.zone, mobileLocator.locator, mobileLocator.index);
    }

    public boolean waitForElement(MobileLocator mobileLocator, int timeout) {
        return mobileDriver.seeTestClient().waitForElement(mobileLocator.zone, mobileLocator.locator,
                mobileLocator.index, timeout);
    }

    public boolean waitForElementToVanish(MobileLocator mobileLocator, int timeout) {
        return mobileDriver.seeTestClient().waitForElementToVanish(mobileLocator.zone, mobileLocator.locator,
                mobileLocator.index, timeout);
    }

    public void drag(MobileLocator mobileLocator, int xOffset, int yOffset) {
        mobileDriver.seeTestClient().drag(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, xOffset, yOffset);
    }

    public void listPick(String listZone, String listLocator, String elementZone, String elementLocator, int index, boolean click) {
        mobileDriver.seeTestClient().elementListPick(listZone, listLocator, elementZone, elementLocator, index, click);
    }

    public void listSelect(String listLocator, String elementLocator, int index, boolean click) {
        mobileDriver.seeTestClient().elementListSelect(listLocator, elementLocator, index, click);
    }

    public void listVisible(String listLocator, String elementLocator, int index) {
        mobileDriver.seeTestClient().elementListVisible(listLocator, elementLocator, index);
    }

    public void setProperty(MobileLocator mobileLocator, String property, String value) {
        mobileDriver.seeTestClient().elementSetProperty(mobileLocator.zone, mobileLocator.locator, mobileLocator.index, property, value);
    }

    public Color getColorOfPixel(int x, int y) {
        int coordinateColor = mobileDriver.seeTestClient().getCoordinateColor(x, y);
        int rOffset = 256 * 256;
        int r = coordinateColor / (rOffset);
        int gb = coordinateColor % rOffset;
        int gOffset = 256;
        int g = gb / gOffset;
        int b = gb % gOffset;
        return new Color(r, g, b);
    }
}
