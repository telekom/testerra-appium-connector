package eu.tsystems.mms.tic.testframework.mobile.device;

import eu.tsystems.mms.tic.testframework.constants.Browsers;

/**
 * Created by rnhb on 17.04.2015.
 */
public enum MobileOperatingSystem {
    ANDROID("adb:", Browsers.mobile_chrome) {
        @Override
        public String getClassPrefix() {
            return "Android";
        }
    },
    @Deprecated
    WINDOWS_PHONE("wp:", Browsers.ie) {
        @Override
        public String getClassPrefix() {
            return "Windows";
        }
    },
    IOS("ios_app:", Browsers.mobile_safari) {
        @Override
        public String getClassPrefix() {
            return "Ios";
        }
    };

    private String appPrefix;
    private String browser;

    MobileOperatingSystem(String appPrefix, String browser) {
        this.appPrefix = appPrefix;
        this.browser = browser;
    }

    public String getAppPrefix() {
        return appPrefix;
    }

    public abstract String getClassPrefix();

    public static MobileOperatingSystem get(String os) {
        if (os == null) {
            return null;
        }
        os = os.replace(" ", "_");
        for (MobileOperatingSystem mobileOperatingSystem : values()) {
            if (mobileOperatingSystem.name().equals(os.toUpperCase())) {
                return mobileOperatingSystem;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getClassPrefix();
    }

    public String getAssociatedBrowser() {
        return browser;
    }

    public String lowerCaseName() {
        return name().toLowerCase();
    }
}
