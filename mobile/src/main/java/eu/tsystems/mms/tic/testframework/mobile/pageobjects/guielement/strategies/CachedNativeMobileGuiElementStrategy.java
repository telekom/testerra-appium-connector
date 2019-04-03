package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElementProperty;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.StatusContainer;
import eu.tsystems.mms.tic.testframework.transfer.ThrowablePackedResponse;
import eu.tsystems.mms.tic.testframework.utils.Timer;

import java.util.Map;

/**
 * Created by rnhb on 22.12.2015.
 */
public class CachedNativeMobileGuiElementStrategy extends DumpBasedMobileGuiElementStrategy {

    public CachedNativeMobileGuiElementStrategy(MobileDriver mobileDriver, MobileLocator mobileLocator, StatusContainer statusContainer) {
        super(mobileDriver, mobileLocator, statusContainer);
    }

    @Override
    public Map<String, String> getProperties() {
        ScreenDump nativeScreenDump = getMobilePage().getScreenDump(ScreenDump.Type.NATIVE_INSTRUMENTED);
        Map<String, String> attributes = nativeScreenDump.getAttributes(mobileLocator);
        return attributes;
    }

    @Override
    public String getText() {
        return getProperty(MobileGuiElementProperty.TEXT.toString());
    }

    @Override
    public int getNumberOfElements() {
        ScreenDump nativeScreenDump = getMobilePage().getScreenDump(ScreenDump.Type.NATIVE_INSTRUMENTED);
        int numberOfElements = nativeScreenDump.getNumberOfElements(mobileLocator);
        return numberOfElements;
    }

    @Override
    public void click(int offsetX, int offsetY) {
        super.click(offsetX, offsetY);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void click() {
        super.click();
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void longClick() {
        super.longClick();
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void swipe(Direction direction, int offset, int swipeTime) {
        super.swipe(direction, offset, swipeTime);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void swipeWhileNotFound(String direction, int offset, int swipeTime, int delay, int maxRounds, boolean click) {
        super.swipeWhileNotFound(direction, offset, swipeTime, delay, maxRounds, click);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void sendText(String text) {
        super.sendText(text);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void drag(int xOffset, int yOffset) {
        super.drag(xOffset, yOffset);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void listPick(MobileGuiElement listEntry, boolean click) {
        super.listPick(listEntry, click);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void listSelect(String elementLocator, int index, boolean click) {
        super.listSelect(elementLocator, index, click);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    public void scrollToListEntry(String elementLocator, int index) {
        super.scrollToListEntry(elementLocator, index);
        getMobilePage().clearCachedMobileGuiElements();
    }

    @Override
    protected boolean waitForDisplayedStatus(final boolean shouldBeDisplayed) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                boolean displayed = isDisplayed();
                displayed = displayed == shouldBeDisplayed;
                if (!displayed) {
                    getMobilePage().clearCachedMobileGuiElements();
                }
                setPassState(displayed);
                setReturningObject(displayed);
            }
        });
        return response.logThrowableAndReturnResponse();
    }

    @Override
    protected boolean waitForPresentStatus(final boolean shouldBePresent) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                boolean present = isPresent();
                present = present == shouldBePresent;
                if (!present) {
                    getMobilePage().clearCachedMobileGuiElements();
                }
                setPassState(present);
                setReturningObject(present);
            }
        });
        return response.logThrowableAndReturnResponse();
    }

    @Override
    protected boolean waitForPropertyStatus(final String property, final String value, final boolean contains) {
        final ThrowablePackedResponse<Boolean> response = executeSequence(new Timer.Sequence<Boolean>() {
            @Override
            public void run() {
                boolean passState = false;
                String actualValue = getProperty(property);
                if (actualValue != null) {
                    actualValue = actualValue.trim();
                    if (contains) {
                        passState = actualValue.contains(value);
                    } else {
                        passState = actualValue.equals(value);
                    }
                }
                if (!passState) {
                    getMobilePage().clearCachedMobileGuiElements();
                }
                setPassState(passState);
                setReturningObject(passState);
            }
        });
        return response.logThrowableAndReturnResponse();
    }


    @Override
    public String toString() {
        return super.toString() + ". Page = " + getMobilePage();
    }
}
