package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies;

import com.experitest.client.InternalException;
import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.StatusContainer;
import org.openqa.selenium.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Map;

/**
 * Created by rnhb on 17.02.2016.
 * <p/>
 * Strategy for all elements, that have access to a dump. Web, instrumented Native and not instrumented Native.
 */
public class DumpBasedMobileGuiElementStrategy extends BasicMobileGuiElementStrategy {

    private final ScreenDump.Type screenDumpType;

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpBasedMobileGuiElementStrategy.class);

    public DumpBasedMobileGuiElementStrategy(MobileDriver mobileDriver, MobileLocator mobileLocator,
                                             StatusContainer statusContainer) {

        super(mobileDriver, mobileLocator, statusContainer);
        if (mobileLocator.zone.equals(LocatorType.NATIVE.toString())) {
            screenDumpType = ScreenDump.Type.NATIVE_INSTRUMENTED;
        } else if (mobileLocator.zone.equals(LocatorType.WEB.toString())) {
            screenDumpType = ScreenDump.Type.WEB;
        } else {
            screenDumpType = null;
        }
    }

    @Override
    public String getSource() {

        final ScreenDump screenDump = driver.getScreenDump(screenDumpType);
        return screenDump.getContent(mobileLocator);
    }

    @Override
    public String getHint() {

        Map<String, String> properties = this.getProperties();
        if (properties.containsKey("hint")) {
            return properties.get("hint");
        } else if (properties.containsKey("placeholder")) {
            return properties.get("placeholder");
        } else {
            LOGGER.warn(this + " getHint(): Element has no property named 'hint' or 'placeholder'.");
            return "";
        }
    }

    @Override
    public Point getLocation() {

        Map<String, String> properties = this.getProperties();
        try {
            Integer x = Integer.valueOf(properties.get("x"));
            Integer y = Integer.valueOf(properties.get("y"));
            return new Point(x, y);
        } catch (Exception e) {
            throw new TesterraRuntimeException("Parse error when trying to get position of MobileGuiElement.", e);
        }
    }

    @Override
    public String getProperty(String property) {

        Map<String, String> attributes = this.getProperties();

        if (attributes == null || !attributes.containsKey(property)) {
            try {
                return driver.element().getProperty(mobileLocator, property);
            } catch (InternalException e) {
                throw new ElementNotFoundException(
                        mobileLocator + " not found in screen dump. Direct getProperty" + property
                                + " failed too.",
                        e);
            }
        } else {
            return attributes.get(property);
        }
    }

    @Override
    public Map<String, String> getProperties() {

        final ScreenDump screenDump = driver.getScreenDump(screenDumpType);
        Map<String, String> attributes = screenDump.getAttributes(mobileLocator);
        return attributes;
    }

    @Override
    public void setProperty(String property, String value) {

        driver.element().setProperty(mobileLocator, property, value);
    }

    @Override
    public boolean isDisplayed() {

        final boolean present = isPresent();

        if (present) {
            final Map<String, String> properties = this.getProperties();
            if (properties.containsKey("width") && properties.containsKey("height")) {
                String width = properties.get("width");
                String height = properties.get("height");
                try {
                    Integer widthInt = Integer.valueOf(width);
                    Integer heightInt = Integer.valueOf(height);
                    return widthInt > 0 && heightInt > 0;
                } catch (NumberFormatException nfe) {
                    LOGGER.debug("Could not parse properties to integers.");
                }
            }
        }
        return present;
    }

    @Override
    public boolean isPresent() {

        //        final boolean elementFound = driver.element().isElementFound(mobileLocator);

        // way much faster because of saved screendump instead of locating elements again and again
        // speed is everything. I'am speed.
        final boolean elementFound = this.getProperties() != null;
        LOGGER.info(mobileLocator + " found: " + elementFound + ".");
        statusContainer.setStatus(mobileLocator + " found: " + elementFound + ".");
        return elementFound;
    }

    @Override
    public void assertMinimalSize(int width, int height) {

        Map<String, String> properties = this.getProperties();
        String widthString = properties.get("width");
        String heightString = properties.get("height");
        int actualWidth = Integer.parseInt(widthString);
        int actualHeight = Integer.parseInt(heightString);
        Assert.assertTrue(actualWidth >= width,
                "Element has at least minimal width. Expected: " + width + ". Actual: " + actualWidth);
        Assert.assertTrue(actualHeight >= height,
                "Element has at least minimal height. Expected: " + height + ". Actual: " + actualHeight);
    }

    @Override
    public void setText(String text) {

        driver.element().setProperty(mobileLocator, "text", text);
    }

    @Override
    public String getText() {

        return driver.element().getText(mobileLocator);
    }

    @Override
    public NativeMobileGuiElement getSubElement(String subElementLocator) {

        NativeMobileGuiElement subElement = new NativeMobileGuiElement(mobileLocator.locator + subElementLocator);
        return subElement;
    }

    @Override
    public void listPick(MobileGuiElement listEntry, boolean click) {

        throw new CommandNotSupportedException("listPick() is not supported for " + this.getClass().getSimpleName());
    }

    @Override
    public void listSelect(String elementLocator, int index, boolean click) {

        throw new CommandNotSupportedException("listSelect() is not supported for " + this.getClass().getSimpleName());
    }

    @Override
    public void scrollToListEntry(String elementLocator, int index) {

        throw new CommandNotSupportedException(
                "scrollToListEntry() is not supported for " + this.getClass().getSimpleName());
    }
}
