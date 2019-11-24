package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.StatusContainer;
import eu.tsystems.mms.tic.testframework.transfer.ThrowablePackedResponse;
import eu.tsystems.mms.tic.testframework.utils.Timer;
import org.openqa.selenium.Point;
import org.testng.Assert;

import java.awt.*;
import java.util.Map;

/**
 * Created by rnhb on 22.12.2015.
 * <p>
 * Strategy for any element, including Image or Text
 */
public class BasicMobileGuiElementStrategy implements MobileGuiElementStrategy {

    final MobileLocator mobileLocator;
    final StatusContainer statusContainer;
    final MobileDriver driver;
    private MobilePage mobilePage;
    private String name;

    int timeoutInSeconds = PropertyManager.getIntProperty(TesterraProperties.ELEMENT_TIMEOUT_SECONDS, 30);

    protected int timerSleepTimeInMs = 1000;

    public BasicMobileGuiElementStrategy(MobileDriver mobileDriver, MobileLocator mobileLocator, StatusContainer statusContainer) {
        this.mobileLocator = mobileLocator;
        this.statusContainer = statusContainer;
        driver = mobileDriver;
    }

    @Override
    public void setContainingPage(MobilePage mobilePage) {
        this.mobilePage = mobilePage;
    }

    @Override
    public String getHint() {
        throw new CommandNotSupportedException("getHint() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public String getSource() {
        throw new CommandNotSupportedException("getSource() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void centerVertically(float centerFraction) {
        throw new CommandNotSupportedException("centerVertically() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return getName();
    }

    protected boolean waitForDisplayedStatus(final boolean shouldBeDisplayed) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                setReturningObject(!shouldBeDisplayed);
                setSkipThrowingException(true);
                setPassState(false);

                boolean displayed = isDisplayed();
                displayed = displayed == shouldBeDisplayed;
                setPassState(displayed);
                setReturningObject(displayed);
            }
        });
        return response.logThrowableAndReturnResponse();
    }

    protected boolean waitForPresentStatus(final boolean shouldBePresent) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                setReturningObject(!shouldBePresent);
                setSkipThrowingException(true);
                setPassState(false);

                boolean present = isPresent();
                present = present == shouldBePresent;
                setPassState(present);
                setReturningObject(present);
            }
        });
        return response.logThrowableAndReturnResponse();
    }


    <T> ThrowablePackedResponse<T> executeSequence(Timer.Sequence<T> sequence) {
        Timer timer = new Timer(timerSleepTimeInMs, timeoutInSeconds * 1000);
        sequence.setSkipThrowingException(true);
        return timer.executeSequence(sequence);
    }

    @Override
    public MobileLocator getLocator() {
        return mobileLocator;
    }

    @Override
    public Point getLocation() {
        throw new CommandNotSupportedException("getLocation() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public Color getColorOfPixel(int xOffset, int yOffset) {
        Point location = getLocation();
        Color colorOfPixel = driver.element().getColorOfPixel(location.x + xOffset, location.y + yOffset);
        return colorOfPixel;
    }

    @Override
    public BasicMobileGuiElementStrategy setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        if (name == null) {
            return getLocator().toString();
        } else {
            return name;
        }
    }

    @Override
    public String getProperty(String property) {
        throw new CommandNotSupportedException("getProperty() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public Map<String, String> getProperties() {
        throw new CommandNotSupportedException("getProperties() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void setProperty(String property, String value) {
        throw new CommandNotSupportedException("setProperty() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void click(int offsetX, int offsetY) {
        driver.element().click(mobileLocator, 1, offsetX, offsetY);
    }

    @Override
    public void click() {
        driver.element().click(mobileLocator, 1);
    }

    @Override
    public void longClick() {
        driver.element().longClick(mobileLocator, 1, 0, 0);
    }

    @Override
    public void swipe(Direction direction, int offset, int swipeTime) {
        driver.element().elementSwipe(mobileLocator, direction.toString(), offset, swipeTime);
    }

    @Override
    public void swipeWhileNotFound(String direction, int offset, int swipeTime, int delay, int maxRounds, boolean click) {
        driver.element().swipeWhileNotFound(direction, offset, swipeTime, mobileLocator,
                delay, maxRounds, click);
    }

    @Override
    public boolean isDisplayed() {
        return isPresent();
    }

    @Override
    public boolean isPresent() {
        boolean elementFound = driver.element().isElementFound(mobileLocator);
        statusContainer.setStatus(mobileLocator + " found: " + elementFound + ".");
        return elementFound;
    }

    @Override
    public void assertMinimalSize(int width, int height) {
        throw new CommandNotSupportedException("assertMinimalSize() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void assertIsPresent() {
        Assert.assertTrue(waitForIsPresent(), this + " is Present." + statusContainer);
    }

    @Override
    public void assertIsNotPresent() {
        Assert.assertTrue(waitForIsNotPresent(), this + " is not Present." + statusContainer);
    }

    @Override
    public void assertIsDisplayed() {
        Assert.assertTrue(waitForIsDisplayed(), this + " is Displayed." + statusContainer);
    }

    @Override
    public void assertIsNotDisplayed() {
        Assert.assertTrue(waitForIsNotDisplayed(), this + " is not Displayed." + statusContainer);
    }

    @Override
    public void assertPropertyValue(String property, String value) {
        Assert.assertTrue(waitForPropertyValue(property, value), "Property " + property + " = " + value);
    }

    @Override
    public void assertPropertyContains(String property, String value) {
        Assert.assertTrue(waitForPropertyContains(property, value), "Property \"" + property + "\" contains \"" + value + "\".");
    }

    @Override
    public boolean waitForIsDisplayed() {
        boolean b = waitForDisplayedStatus(true);
        return b;
    }

    @Override
    public boolean waitForIsNotDisplayed() {
        boolean b = waitForDisplayedStatus(false);
        return b;
    }

    @Override
    public boolean waitForIsPresent() {
        boolean b = waitForPresentStatus(true);
        return b;
    }

    @Override
    public boolean waitForIsNotPresent() {
        boolean b = waitForPresentStatus(false);
        return b;
    }

    @Override
    public boolean waitForPropertyValue(String property, String value) {
        boolean b = waitForPropertyStatus(property, value, false);
        return b;
    }

    @Override
    public boolean waitForPropertyContains(String property, String value) {
        boolean b = waitForPropertyStatus(property, value, true);
        return b;
    }

    protected boolean waitForPropertyStatus(final String property, final String value, final boolean contains) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                setReturningObject(false);
                setPassState(false);
                String actualValue = getProperty(property);
                if (actualValue != null) {
                    boolean checkPassed = contains ? actualValue.contains(value) : actualValue.equals(value);
                    setPassState(checkPassed);
                    setReturningObject(checkPassed);
                }
            }
        });
        return response.logThrowableAndReturnResponse();
    }

    @Override
    public void sendText(String text) {
        driver.element().elementSendText(mobileLocator, text);
    }

    @Override
    public void setText(String text) {
        throw new CommandNotSupportedException("setText() is not supported for " + this.getClass().getSimpleName());
    }

    @Override
    public String getText() {
        throw new CommandNotSupportedException("assertMinimalSize() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public int getNumberOfElements() {
        return driver.element().getElementCount(mobileLocator);
    }

    @Override
    public boolean waitForElement(int timeout) {
        return driver.element().waitForElement(mobileLocator, timeout);
    }

    @Override
    public void drag(int xOffset, int yOffset) {
        driver.element().drag(mobileLocator, xOffset, yOffset);
    }

    @Override
    public NativeMobileGuiElement getSubElement(String subElementLocator) {
        throw new CommandNotSupportedException("getSubElement() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void setTimeoutInSeconds(int timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
    }

    @Override
    public void setTimerSleepTimeInMs(int timerSleepTimeInMs) {
        this.timerSleepTimeInMs = timerSleepTimeInMs;
    }

    @Override
    public boolean waitForElementIsFound() {
        return driver.element().waitForElement(mobileLocator, timeoutInSeconds * 1000);
    }

    @Override
    public boolean waitForElementIsNotFound() {
        return driver.element().waitForElementToVanish(mobileLocator, timeoutInSeconds * 1000);
    }

    @Override
    public void listPick(MobileGuiElement listEntry, boolean click) {
        throw new CommandNotSupportedException("listPick() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void listSelect(String elementLocator, int index, boolean click) {
        throw new CommandNotSupportedException("listSelect() is not supported for " + getClass().getSimpleName());
    }

    @Override
    public void scrollToListEntry(String elementLocator, int index) {
        throw new CommandNotSupportedException("scrollToListEntry() is not supported for " + getClass().getSimpleName());
    }

    MobilePage getMobilePage() {
        return mobilePage;
    }

}
