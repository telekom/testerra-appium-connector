/*
 * Created on 09.01.2014
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.MobileGuiElementStrategy;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Checkable;
import eu.tsystems.mms.tic.testframework.utils.TestUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * The Class MobileAbstractGuiElement.
 * <p>
 * TODO the whole strategy part has to be reworked. Situation changed, it does not seem useful anymore.
 *
 * @author rnhb
 */
public class AbstractMobileGuiElement implements Checkable, MobileGuiElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileGuiElement.class);

    private MobileGuiElementStrategy strategy;

    private String name;

    final StatusContainer statusContainer;

    MobileDriver mobileDriver;

    public AbstractMobileGuiElement() {
        statusContainer = new StatusContainer();
    }

    protected void setStrategy(MobileGuiElementStrategy mobileGuiElementStrategy) {
        strategy = mobileGuiElementStrategy;
    }

    @Override
    public String toString() {
        if (name == null) {
            return getLocator().toString();
        } else {
            return name;
        }
    }

    private void afterLog(String msg, boolean takeScreenshot) {
        if (takeScreenshot) {
            mobileDriver.takeAfterScreenshot();
        }
        LOGGER.info(this + ": " + msg);
    }

    private void beforeLog(String msg, boolean takeScreenshot) {
        LOGGER.info(this + ": " + msg);
        if (takeScreenshot) {
            mobileDriver.takeBeforeScreenshot();
        }
    }

    @Override
    public MobileLocator getLocator() {
        return strategy.getLocator();
    }

    @Override
    public Point getLocation() {
        beforeLog("executing getLocation()", false);
        Point location = strategy.getLocation();
        afterLog("location is " + location, false);
        return location;
    }

    @Override
    public Color getColorOfPixel(int xOffset, int yOffset) {
        beforeLog("executing getColorOfPixel(" + xOffset + ", " + yOffset + ")", false);
        Color colorOfPixel = strategy.getColorOfPixel(xOffset, yOffset);
        afterLog("color of [" + xOffset + ", " + yOffset + "] is" + colorOfPixel, false);
        return colorOfPixel;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        strategy.setName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProperty(String property) {
        beforeLog("getProperty(" + property + ")", false);
        String value = strategy.getProperty(property);
        afterLog("Property \"" + property + "\" of " + name + " = \"" + value + "\"", false);
        return value;
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> properties = strategy.getProperties();
        return properties;
    }

    @Override
    public void setProperty(String property, String value) {
        afterLog("Set property \"" + property + "\" of " + name + " to \"" + value + "\"", false);
        strategy.setProperty(property, value);
    }

    @Override
    public void click(int offsetX, int offsetY) {
        beforeLog("Click " + name + " with offset [" + offsetX + ", " + offsetY + "]", true);
        strategy.click(offsetX, offsetY);
        afterLog("Clicked", true);
    }

    @Override
    public void click() {
        beforeLog("Click " + name, true);
        strategy.click();
        afterLog("Clicked", true);
    }

    @Override
    public void longClick() {
        beforeLog("Long Click " + name, true);
        strategy.longClick();
        afterLog("Long clicked", true);
    }

    @Override
    public void swipe(Direction direction, int offset, int swipeTime) {
        beforeLog("Swipe " + name + " to " + direction + " by " + offset + "px in " + swipeTime + "ms", true);
        strategy.swipe(direction, offset, swipeTime);
        afterLog("Swiped", true);
    }

    @Override
    public void swipeWhileNotFound(String direction, int offset, int swipeTime, int delay, int maxRounds,
                                   boolean click) {
        afterLog("Swipe while not found " + name + " to " + direction + " by " + offset + "px in " + swipeTime + "ms", true);
        strategy.swipeWhileNotFound(direction, offset, swipeTime, delay, maxRounds, click);
        afterLog("Swiped", true);
    }

    @Override
    public boolean isDisplayed() {
        boolean displayed = strategy.isDisplayed();
        return displayed;
    }

    @Override
    public boolean isPresent() {
        boolean present = strategy.isPresent();
        return present;
    }

    @Override
    public void assertMinimalSize(int width, int height) {
        strategy.assertMinimalSize(width, height);
    }

    @Override
    public void assertIsPresent() {
        strategy.assertIsPresent();
    }

    @Override
    public void assertIsNotPresent() {
        strategy.assertIsNotPresent();
    }

    @Override
    public void assertIsDisplayed() {
        strategy.assertIsDisplayed();
    }

    @Override
    public void assertIsNotDisplayed() {
        strategy.assertIsNotDisplayed();
    }

    @Override
    public void assertPropertyValue(String property, String value) {
        strategy.assertPropertyValue(property, value);
    }

    @Override
    public void assertPropertyContains(String property, String value) {
        strategy.assertPropertyContains(property, value);
    }

    @Override
    public boolean waitForIsDisplayed() {
        boolean b = strategy.waitForIsDisplayed();
        return b;
    }

    @Override
    public boolean waitForIsNotDisplayed() {
        boolean b = strategy.waitForIsNotDisplayed();
        return b;
    }

    @Override
    public boolean waitForIsPresent() {
        boolean b = strategy.waitForIsPresent();
        return b;
    }

    @Override
    public boolean waitForIsNotPresent() {
        boolean b = strategy.waitForIsNotPresent();
        return b;
    }

    @Override
    public boolean waitForPropertyValue(String property, String value) {
        boolean b = strategy.waitForPropertyValue(property, value);
        return b;
    }

    @Override
    public boolean waitForPropertyContains(String property, String value) {
        boolean b = strategy.waitForPropertyContains(property, value);
        return b;
    }

    @Override
    public void sendText(String text) {
        beforeLog("Send Text \"" + text + "\" to " + name, true);
        strategy.sendText(text);
        afterLog("Text sent", true);
    }

    @Override
    public void setText(String text) {
        beforeLog("Set Text of \"" + text + "\" to " + name, true);
        strategy.setText(text);
        afterLog("Text set", true);
    }

    @Override
    public String getText() {
        beforeLog("Get Text", false);
        String text = strategy.getText();
        afterLog("Text is " + text, false);
        return text;
    }

    @Override
    public int getNumberOfElements() {
        beforeLog("Get Number of found elements for " + getLocator(), false);
        int numberOfElements = strategy.getNumberOfElements();
        afterLog(" Number of elements = " + numberOfElements, false);
        return numberOfElements;
    }

    @Override
    public boolean waitForElement(int timeout) {
        return strategy.waitForElement(timeout);
    }

    @Override
    public void drag(int offsetX, int offsetY) {
        beforeLog("Drag " + name + " by [" + offsetX + ", " + offsetY + "]", true);
        strategy.drag(offsetX, offsetY);
        afterLog("Dragged ", true);
    }

    @Override
    public NativeMobileGuiElement getSubElement(String subLocator) {
        NativeMobileGuiElement subElement = strategy.getSubElement(subLocator);
        return subElement;
    }

    @Override
    public void setTimeoutInSeconds(int timeoutInSeconds) {
        strategy.setTimeoutInSeconds(timeoutInSeconds);
    }

    @Override
    public void setTimerSleepTimeInMs(int timerSleepTimeInMs) {
        strategy.setTimerSleepTimeInMs(timerSleepTimeInMs);
    }

    @Override
    public boolean waitForElementIsFound() {
        return strategy.waitForElementIsFound();
    }

    @Override
    public boolean waitForElementIsNotFound() {
        return strategy.waitForElementIsNotFound();
    }

    @Override
    public void listPick(MobileGuiElement listEntry, boolean click) {
        beforeLog("Pick " + listEntry, true);
        strategy.listPick(listEntry, click);
        afterLog("Picked", true);
    }

    @Override
    public void listSelect(String elementLocator, int index, boolean click) {
        beforeLog("Select " + elementLocator, true);
        strategy.listSelect(elementLocator, index, click);
        afterLog("Selected", true);
    }

    @Override
    public void scrollToListEntry(String elementLocator, int index) {
        beforeLog("Scroll to " + elementLocator, true);
        strategy.scrollToListEntry(elementLocator, index);
        afterLog("Scrolled", true);
    }

    @Override
    public void setContainingPage(MobilePage mobilePage) {
        strategy.setContainingPage(mobilePage);
    }

    @Override
    public String getHint() {
        beforeLog("get Hint", false);
        String hint = strategy.getHint();
        afterLog("Hint = \"" + hint + "\"", false);
        return hint;
    }

    @Override
    public String getSource() {
        beforeLog("get element source", false);
        return strategy.getSource();
    }

    @Override
    public void centerVertically(float centerFraction) {
        int screenHeight = mobileDriver.getActiveDevice().getPixelsY();
        int border = (int) (centerFraction * 0.5f * screenHeight);

        // at most 4 swipes
        for (int i = 0; i < 4; i++) {
            int posY = Integer.valueOf(getProperty("y"));
            int height = Integer.valueOf(getProperty("height"));
            int elementEndY = posY + height;

            boolean isAboveUpperBorder = posY < border;
            boolean isBelowLowerBorder = elementEndY > screenHeight - border;

            //            float relativeOffset = Math.max(border - posY, elementEndY - screenHeight + border) / (float) screenHeight;
            float swipeStrength = 0.6f;

            if (isAboveUpperBorder) {
                mobileDriver.swipe(Direction.UP, swipeStrength, 1000);
                TestUtils.sleep(200); // short sleep to allow scrolling to finish
            } else if (isBelowLowerBorder) {
                mobileDriver.swipe(Direction.DOWN, 0.15f + swipeStrength, 1000);
                TestUtils.sleep(200); // short sleep to allow scrolling to finish
            } else {
                return;
            }
        }
        LOGGER.warn("Failed to center element vertically.");
    }

    public File takeScreenshot() {

        final boolean isSelenium4 = false;

        if (isSelenium4) {
            return mobileDriver.getScreenshotAs(OutputType.FILE);
        } else {
            try {
                final TakesScreenshot driver = mobileDriver;
                final MobileGuiElement element = this;

                File screenshot = driver.getScreenshotAs(OutputType.FILE);
                BufferedImage fullImg = ImageIO.read(screenshot);

                //                Point point = element.getLocation();

                int propertyX = Integer.parseInt(element.getProperty("x"));
                int propertyY = Integer.parseInt(element.getProperty("y"));
                int eleWidth = Integer.parseInt(element.getProperty("width"));
                int eleHeight = Integer.parseInt(element.getProperty("height"));

                BufferedImage eleScreenshot = fullImg.getSubimage(
                        propertyX,
                        propertyY,
                        eleWidth,
                        eleHeight
                );
                ImageIO.write(eleScreenshot, "png", screenshot);
                return screenshot;
            } catch (IOException e) {
                LOGGER.error(String.format("%s unable to take screenshot: %s ", this.name, e));
            }
        }

        return null;
    }
}