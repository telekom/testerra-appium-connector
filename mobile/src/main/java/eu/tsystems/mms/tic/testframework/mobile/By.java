package eu.tsystems.mms.tic.testframework.mobile;

import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;

import java.util.ResourceBundle;

/**
 * Created by rnhb on 05.02.2016.
 */
public class By {

    public final String locator;
    private static ResourceBundle resourceBundle;

    public By(String locator) {
        this.locator = locator;
    }

    public static void setResourceBundle(ResourceBundle resourceBundle) {
        By.resourceBundle = resourceBundle;
    }

    public static By accessibilityLabel(String accessibilityLabelOrKey) {
        if (resourceBundle != null && resourceBundle.containsKey(accessibilityLabelOrKey)) {
            accessibilityLabelOrKey = resourceBundle.getString(accessibilityLabelOrKey);
        }
        return new By("xpath=//*[normalize-space(@accessibilityLabel)='" + accessibilityLabelOrKey + "']");
    }

    public static By accessibilityLabelContains(String accessibilityLabelOrKey) {
        if (resourceBundle != null && resourceBundle.containsKey(accessibilityLabelOrKey)) {
            accessibilityLabelOrKey = resourceBundle.getString(accessibilityLabelOrKey);
        }
        return new By("xpath=//*[contains(normalize-space(@accessibilityLabel),'" + accessibilityLabelOrKey + "')]");
    }

    public static By text(String textOrKey) {
        if (resourceBundle != null && resourceBundle.containsKey(textOrKey)) {
            textOrKey = resourceBundle.getString(textOrKey);
        }
        return new By("xpath=//*[normalize-space(@text)='" + textOrKey + "']");
    }

    public static By textContains(String textOrKey) {
        if (resourceBundle != null && resourceBundle.containsKey(textOrKey)) {
            textOrKey = resourceBundle.getString(textOrKey);
        }
        return new By("xpath=//*[contains(normalize-space(@text),'" + textOrKey + "')]");
    }

    public static By xPath(String xpath) {
        return new By("xpath=" + xpath);
    }

    /**
     * takes id of android or accessibilityIdentifier of iOS
     *
     * @return valid locator for the current OS
     */
    public static By id(String id) {
        TestDevice activeDevice = MobileDriverManager.getMobileDriver().getActiveDevice();
        MobileOperatingSystem mobileOperatingSystem = activeDevice.getOperatingSystem();
        switch (mobileOperatingSystem) {
            case ANDROID:
                return new By("xpath=//*[@id='" + id + "']");
            case IOS:
                String version = activeDevice.getOperatingSystemVersion();
                String additionalXpath = ""; // <- workaround for MDC-83
                if (version.contains("11") || version.contains("10.3")) {
                    additionalXpath = " or @accessibilityLabel='" + id + "'";
                }
                return new By("xpath=//*[@accessibilityIdentifier='" + id + "'" + additionalXpath + "]");
            default:
                throw new TesterraRuntimeException(mobileOperatingSystem + " not supported for By.id locator");
        }
    }

    public static By twoId(String parentId, String childId) {
        MobileOperatingSystem mobileOperatingSystem = MobileDriverManager.getMobileDriver().getActiveDevice().getOperatingSystem();
        switch (mobileOperatingSystem) {
            case ANDROID:
                return new By("xpath=//*[@id='" + parentId + "']//*[@id='" + childId + "']");
            case IOS:
                return new By("xpath=//*[@accessibilityIdentifier='" + parentId + "']//*[@accessibilityIdentifier='" + childId + "']");
            default:
                throw new TesterraRuntimeException(mobileOperatingSystem + " not supported for By.id locator");
        }
    }

    @Override
    public String toString() {
        return locator;
    }
}
