package eu.tsystems.mms.tic.testframework.appium;

/**
 * Created on 2024-06-11
 *
 * The Appium client convert all Appium specific capabilities and adds the prefix 'appium:'.
 * This should help to read a capability.
 */
public interface AppiumCapabilityHelper {

    default String getAppiumCap(final String capabilityName) {
        return "appium:" + capabilityName;
    }

}
