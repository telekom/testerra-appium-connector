package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies;

import com.experitest.client.InternalException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElementProperty;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.StatusContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by rnhb on 22.12.2015.
 */
@Deprecated
public class NativeMobileGuiElementStrategy extends DumpBasedMobileGuiElementStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(NativeMobileGuiElementStrategy.class);

    public NativeMobileGuiElementStrategy(MobileDriver mobileDriver, MobileLocator mobileLocator, StatusContainer statusContainer) {
        super(mobileDriver, mobileLocator, statusContainer);
    }

    @Override
    public boolean isDisplayed() {
        Map<String, String> properties = getProperties();
        if (properties == null) {
            statusContainer.setStatus(mobileLocator + " is not present. ");
            return false;
        } else {
            boolean isOnScreen = propertyHasValue(properties, MobileGuiElementProperty.ON_SCREEN, "true");
            boolean notHidden = propertyHasValue(properties, MobileGuiElementProperty.HIDDEN, "false");
            statusContainer.setStatus(mobileLocator + " found. "
                    + MobileGuiElementProperty.HIDDEN + " = " + !notHidden + ", "
                    + MobileGuiElementProperty.ON_SCREEN + " = " + isOnScreen + ". ");
            return isOnScreen && notHidden;
        }
    }

    @Override
    public boolean isPresent() {
        Map<String, String> properties = getProperties();
        if (properties == null) {
            statusContainer.setStatus(mobileLocator + " is not present. ");
            return false;
        } else {
            statusContainer.setStatus(mobileLocator + " found.");
            return true;
        }
    }

    private boolean propertyHasValue(Map<String, String> properties, MobileGuiElementProperty property, String expectedValue) {
        if (properties.containsKey(property.toString())) {
            String propertyValue = properties.get(property.toString());
            return expectedValue.equals(propertyValue);
        } else {
            return true;
        }
    }

    @Override
    public void setText(String text) {
        int length = driver.element().getText(mobileLocator).length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                driver.pressBackspace();
            }
        }
        this.sendText(text);
    }

    @Override
    public String getText() {
        String text = driver.element().getText(mobileLocator);
        if (text == null || "".equals(text) || "null".equals(text)) {
            String propertyText = null;
            try {
                propertyText = getProperty("text");
            } catch (InternalException e) {
                LOGGER.debug("Some elements won't contain text. Everything should be fine.", e);
            }
            if (propertyText != null) {
                text = propertyText;
            }
        }
        if (driver.getActiveDevice().getOperatingSystem() == MobileOperatingSystem.IOS) {
            if (text == null || "".equals(text) || "null".equals(text)) {
                String propertyText = null;
                try {
                    propertyText = getProperty("accessibilityLabel");
                } catch (InternalException e) {
                    LOGGER.debug("Some elements won't contain accessibilityLabel=JETZT ANMELDEN. Everything should be fine.", e);
                }
                if (propertyText != null) {
                    text = propertyText;
                }
            }
        }
        return text;
    }

    @Override
    public void listPick(MobileGuiElement listEntry, boolean click) {
        MobileLocator listEntryLocator = listEntry.getLocator();
        driver.element().listPick(mobileLocator.zone, mobileLocator.locator, listEntryLocator.zone,
                listEntryLocator.locator, listEntryLocator.index, click);
    }

    @Override
    public void listSelect(String elementLocator, int index, boolean click) {
        driver.element().listSelect(mobileLocator.locator, elementLocator, mobileLocator.index, click);
    }

    @Override
    public void scrollToListEntry(String elementLocator, int index) {
        driver.element().listVisible(mobileLocator.locator, elementLocator, index);
    }
}
