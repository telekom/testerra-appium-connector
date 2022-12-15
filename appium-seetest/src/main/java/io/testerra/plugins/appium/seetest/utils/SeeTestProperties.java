package io.testerra.plugins.appium.seetest.utils;

import eu.tsystems.mms.tic.testframework.common.IProperties;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public enum SeeTestProperties implements IProperties {

    VIDEO_ON_SUCCESS("tt.appium.seetest.video.onsuccess", false),
    VIDEO_ON_FAILED("tt.appium.seetest.video.onfailed", true),
    VIDEO_DOWNLOAD_TIMEOUT("tt.appium.seetest.video.download.timeout", 20);

    private final String property;
    private final Object defaultValue;

    SeeTestProperties(String property, Object defaultValue) {
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
