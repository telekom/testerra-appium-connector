package eu.tsystems.mms.tic.testframework.mobile.driver;

/**
 * Created by rnhb on 20.03.2015.
 */
public interface DefaultParameter {
    String MOBILE_SERVER_HOST = "localhost";
    String MOBILE_SERVER_PORT = "8889";
    String MOBILE_LOCAL_REPORT_PATH = "target\\surefire-reports\\mobile-report";
    String MOBILE_REMOTE_REPORT_PATH = MOBILE_LOCAL_REPORT_PATH;
    boolean MOBILE_OPEN_REFLECTION_SCREEN = true;
    int MOBILE_MONITORING_INTERVAL = 500;
    boolean MOBILE_MONITORING_ACTIVE = false;
    String MOBILE_REPORT_TYPE = "xml";
    boolean MOBILE_STITCH_SCREENS = true;
    boolean MOBILE_REPORT_TAKE_SCREENSHOTS = true;
    boolean MOBILE_REPORT_TAKE_ONLY_BEFORE_SCREENSHOTS = false;
    boolean MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED = false;
    boolean MOBILE_REPORT_SAVE_VIDEO = false;
    int MOBILE_REPORT_DELAY_BETWEEN_SCREENSHOTS_MS = 0;
    boolean MOBILE_SKIP_BROWSER_HTTPS_WARNING = true;
    int MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS = 60;
    int MOBILE_DEVICE_GRID_RESERVATION_DURATION_IN_MINUTES = 120;
    boolean MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER = true;
    boolean MOBILE_DEVICE_TEST_ON_RESERVATION = true;
    boolean MOBILE_REPORT_SHOW_ALL_SCREENSHOTS_IN_SLIDER = false;
    boolean MOBILE_SKIP_LAUNCH_ERROR = false;
    boolean MOBILE_APPLICATIONNAME_MATCH_STRICT = false;
    boolean MOBILE_SHOW_FAILED_DEVICE_TEST_WARNING = true;
    String MOBILE_SCREENSHOT_QUALITY = "100";
}

