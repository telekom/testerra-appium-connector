package eu.tsystems.mms.tic.testframework.utils;

import eu.tsystems.mms.tic.testframework.common.IProperties;

/**
 * Created on 21.07.2022
 *
 * @author mgn
 */
public enum AppiumProperties implements IProperties {

    MOBILE_GRID_URL("tt.mobile.grid.url", ""),
    MOBILE_GRID_ACCESS_KEY("tt.mobile.grid.access.key", ""),
//    MOBILE_APPIUM_DEVICE_QUERY("tt.mobile.device.query", ""),
//    MOBILE_APPIUM_DEVICE_QUERY_IOS("tt.mobile.device.query.ios", "@os='ios' and @category='PHONE'"),
    MOBILE_APPIUM_DEVICE_QUERY_IOS("tt.mobile.device.query.ios", ""),
    MOBILE_APPIUM_DEVICE_QUERY_ANDROID("tt.mobile.device.query.android", ""),
    ;

    private final String property;

    private final Object defaultValue;

    AppiumProperties(String property, Object defaultValue) {
        this.property = property;
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return this.property;
    }

    @Override
    public Object getDefault() {
        return this.defaultValue;
    }
}
