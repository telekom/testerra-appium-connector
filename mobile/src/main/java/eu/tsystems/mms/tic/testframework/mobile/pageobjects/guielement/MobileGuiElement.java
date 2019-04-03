package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

import java.awt.Color;
import java.util.Map;

import org.openqa.selenium.Point;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Checkable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Nameable;

/**
 * Created by rnhb on 22.12.2015.
 */
public interface MobileGuiElement extends Checkable, Nameable {

    MobileLocator getLocator();

    Point getLocation();

    Color getColorOfPixel(int xOffset, int yOffset);

    void setName(String name);

    /**
     * Supported properties:
     * All available properties from the object spy.
     * <p/>
     * Additional supported properties:
     * index: For android and iOS pickers to  get the element in the picker in the provided index.
     * date: Works on Android and iOS date picker, to get the date value.
     * datetime: Works on iOS date time picker, to get the date and time value
     * time: Works on Android or iOS time picker, to get the time value.
     * slide: Works on Android or iOS slider to get the slider value.
     * text: Works on Android or iOS picker to get the specific selected text in the picker.
     * on: Works on Android or iOS switch to get its value (true or false)
     * InnerHTML: Works on Android or iOS. Gets the inner HTML code.
     * enabled: Check if a button is enabled or disabled. Works on Android or iOS buttons.
     * checked: Check if a checkbox is checked or not. Works on Android or iOS buttons.
     *
     * @param property Name of the property
     * @return Value of the property
     */
    String getProperty(String property);

    /**
     * Get all available properties of the element.
     *
     * @return property map, or null if element is not found
     */
    Map<String, String> getProperties();

    /**
     * Supported properties:
     * <p/>
     * NATIVE ZONE:
     * index: For pickers to selects the element in the provided index. The supported values are integers according to size of the picker
     * date: Works on Android and iOS date pickers to set the date value. The supported value format is dd.mm.yyyy: 12.09.2012
     * datetime: Works on iOS date time picker, to set the date and time value. The supported value format is dd.mm.yyyy hh.mm: 12.09.2012 13.05.
     * time: Works on Android or iOS time picker, to set the time value. The supported value format hh.mm:13.05
     * slide: Works on Android or iOS slider to set the slider value. You can set intervals like 0-10, 0-100, or 0-infinity.
     * text: Works on iOS picker to select specific text in the picker.
     * on: Works on Android and  iOS switch to change the value. The supported value is boolean - true or false
     * placeholder: Works on iOS
     * checked: Works on Android or iOS check box. The supported value is boolean - true or false
     * <p/>
     * WEB ZONE:
     * Property selection affects attributes of HTML elements. The inserted value is in  javascript.
     *
     * @param property
     * @param value
     * @link https://docs.experitest.com/display/public/SA/ElementSetProperty
     */
    void setProperty(String property, String value);

    /**
     * Click.
     *
     * @param offsetX the offset in x-direction
     * @param offsetY the offset in y-direction
     */
    void click(int offsetX, int offsetY);

    /**
     * One click on element.
     */
    void click();

    /**
     * One long click. Only available on Android.
     */
    void longClick();

    /**
     * Swipe element in specified direction.
     *
     * @param direction the direction in which the screen should be swiped
     * @param offset    the swipe offset
     * @param swipeTime the time hoe fast the swipe should be done
     */
    void swipe(Direction direction, int offset, int swipeTime);

    /**
     * Search for locator in zoneName and click.
     *
     * @param direction the direction in which the screen should be swiped
     * @param offset    the swipe offset
     * @param swipeTime the time how fast the swipe should be done
     * @param delay     the delay time
     * @param maxRounds the maximum number of search tries
     * @param click     click at element if found or not
     */
    void swipeWhileNotFound(String direction, int offset, int swipeTime, int delay, int maxRounds, boolean click);

    /**
     * Checks if the element is displayed via the onScreen property, additional to all checks from isPresent().
     *
     * @return true if the element is displayed on the screen.
     */
    boolean isDisplayed();

    /**
     * Check if the element is present.
     * If it was found by its locator and the property "hidden" and "parentHidden" are false, it is present.
     * It is not necessary for these properties to be present - if they are missing, they have no effect.
     * <p/>
     * Explanation: In some apps, GuiElement might exist but are hidden, due to the nature of native apps. Instead of removing and
     * adding elements from the Gui-Tree, they are often hidden or overlaid by other elements.
     * In GuiTest-Scenarios, we are only interested if it is actually shown somewhere (including in an area visible after scrolling) or not.
     *
     * @return true if the element is present
     */
    boolean isPresent();

    /**
     * Assert that the element has a at least a minimal size
     */
    void assertMinimalSize(int width, int height);

    /**
     * Assert the element is present.
     */
    void assertIsPresent();

    /**
     * Assert the element is not present.
     */
    void assertIsNotPresent();

    /**
     * Assert the element is displayed.
     */
    void assertIsDisplayed();

    /**
     * Assert the element is not displayed.
     */
    void assertIsNotDisplayed();

    /**
     * Assert that the property matches the given value.
     * The actual value will be trimmed.
     *
     * @param property property to check
     * @param value    value to match
     */
    void assertPropertyValue(String property, String value);

    /**
     * Assert that the property contains the given value.
     *
     * @param property property to check
     * @param value    value to contain
     */
    void assertPropertyContains(String property, String value);

    boolean waitForIsDisplayed();

    boolean waitForIsNotDisplayed();

    boolean waitForIsPresent();

    boolean waitForIsNotPresent();

    boolean waitForPropertyValue(String property, String value);

    boolean waitForPropertyContains(String property, String value);

    /**
     * Type text.
     *
     * @param text the text
     */
    void sendText(String text);

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    void setText(final String text);

    /**
     * Gets the text.
     *
     * @return the text
     */
    String getText();

    /**
     * Gets the number of elements
     *
     * @return number of elements
     */
    int getNumberOfElements();

    /**
     * Waits for an element for an specific amount of time.
     *
     * @param timeout time to wait
     * @return true if element found within timeout
     */
    boolean waitForElement(int timeout);

    /**
     * Drags an element into the specified offset direction
     *
     * @param xOffset the width
     * @param yOffset the height
     */
    void drag(int xOffset, int yOffset);

    /**
     * Creates a new NativeMobileGuiElement by appending the given locator to the locator of this element.
     * This has to result in a valid new locator.
     *
     * @param subLocator partial locator to append
     * @return new Element
     */
    NativeMobileGuiElement getSubElement(String subLocator);

    void setTimeoutInSeconds(int timeoutInSeconds);

    void setTimerSleepTimeInMs(int timerSleepTimeInMs);

    /**
     * Waits until this element is found. Does not do anything if otherwise. The found object can still be hidden.
     *
     * @return true if element found.
     */
    boolean waitForElementIsFound();

    /**
     * Waits until this element is not found. Does not do anything if otherwise.
     *
     * @return true if element is not found within timeout
     */
    boolean waitForElementIsNotFound();

    /**
     * Pick entry in list. Will use zones of the respective MobileGuiElement.
     *
     * @param listEntry Element representing the list entry
     * @param click     If TRUE then click once found
     */
    void listPick(MobileGuiElement listEntry, boolean click);

    /**
     * Pick entry in list.
     *
     * @param elementLocator locator for entry, cannot be xPath. use text=ThisText
     * @param index          index of the element
     * @param click          If TRUE then click once found
     */
    void listSelect(String elementLocator, int index, boolean click);

    /**
     * @param elementLocator locator for entry, cannot be xPath. use text=ThisText
     * @param index          index of the element
     */
    void scrollToListEntry(String elementLocator, int index);

    /**
     * To be able to cache whole pages, the element needs to access the cache of the page it is defined in.
     * With this setter, the element gets to know which page that is.
     *
     * @param mobilePage MobilePage this element is defined in.
     */
    void setContainingPage(MobilePage mobilePage);

    /**
     * returns the text of the hint (android) or placeholder (ios)
     */
    String getHint();

    /**
     * returns the object tree below this element node in the screen dump. Obviously only works for Elements that work on a screen dump.
     */
    String getSource();

    /**
     * Tries to center the GuiElement in the screen. The definition of the center is given as parameter, as fraction of the screen.
     * Will only work for elements that are found in the object tree.
     *
     * @param centerFraction the fraction of the screen considered as center. For example, a fraction of 0.6 for a screen
     *                       size of 1000 pixels means, that the GuiElement should be in between pixel 200 and 800.
     */
    void centerVertically(float centerFraction);
}
