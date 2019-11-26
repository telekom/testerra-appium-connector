/*
 * Created on 22.01.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile;

/**
 * MobileProperties
 *
 * @author sepr, erku
 */
public interface MobileProperties {

    String MOBILE_SERVER_HOST = "tt.mobile.server.host";
    String MOBILE_SERVER_PORT = "tt.mobile.server.port";
    String MOBILE_PROJECT_DIR = "tt.mobile.project.dir";
    String MOBILE_REMOTE_REPORT_PATH = "tt.mobile.remote.report.path";
    String MOBILE_LOCAL_REPORT_PATH = "tt.mobile.local.report.path";
    String MOBILE_REPORT_TYPE = "tt.mobile.report.type";
    String MOBILE_REPORT_ACTIVATED = "tt.mobile.report.activated";
    /**
     * Interval that the phone state should be monitored in. In Milliseconds.
     */
    String MOBILE_MONITORING_INTERVAL = "tt.mobile.monitoring.interval";
    String MOBILE_REPORT_TAKE_SCREENSHOTS = "tt.mobile.report.take.screenshots";
    String MOBILE_REPORT_TAKE_ONLY_BEFORE_SCREENSHOTS = "tt.mobile.report.take.only.before.screenshots";
    String MOBILE_REPORT_DELAY_BETWEEN_SCREENSHOTS_MS = "tt.mobile.report.delay.between.screenshots.ms";
    String MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED = "tt.mobile.report.save.video.on.failed";
    String MOBILE_REPORT_SAVE_VIDEO = "tt.mobile.report.save.video";
    String MOBILE_REPORT_SHOW_ALL_SCREENSHOTS_IN_SLIDER = "tt.mobile.report.show.all.screenshots.in.slider";
    String MOBILE_STITCH_SCREENS = "tt.mobile.layout.stitch.screenshots";
    String MOBILE_DEVICE_FILTER = "tt.mobile.device.filter";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_TIMEOUT = "tt.mobile.screenshot.tracker.timeout";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_ACTIVATED = "tt.mobile.screenshot.tracker.activated";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_URL = "tt.mobile.screenshot.tracker.url";
    String MOBILE_SCREENSHOT_QUALITY = "tt.mobile.screenshot.quality";
    String MOBILE_SKIP_BROWSER_HTTPS_WARNING = "tt.mobile.skip.browser.https.warning";
    String MOBILE_SKIP_LAUNCH_ERROR = "tt.mobile.skip.browser.launch.error";
    String MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS = "tt.mobile.device.reservation.timeout.seconds";
    String MOBILE_DEVICE_GRID_RESERVATION_DURATION_IN_MINUTES = "tt.mobile.device.grid.reservation.duration.minutes";
    String MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER = "tt.mobile.device.reservation.randomize.order";
    String MOBILE_GRID_USER = "tt.mobile.grid.user";
    String MOBILE_GRID_PASSWORD = "tt.mobile.grid.password";
    String MOBILE_GRID_ACCESS_KEY = "tt.mobile.grid.access.key";
    String MOBILE_GRID_PROJECT = "tt.mobile.grid.project";
    String MOBILE_DEVICE_RESERVATION_RETRIES = "tt.mobile.device.reservation.retries";
    String MOBILE_DEVICE_RESERVATION_POLICY = "tt.mobile.device.reservation.policy";
    String MOBILE_APPLICATIONNAME_MATCH_STRICT = "tt.mobile.appname.match.strict";
    String MOBILE_SHOW_FAILED_DEVICE_TEST_WARNING = "tt.mobile.show.failed.device.test.warning";
    /**
     * Can activate the monitoring of cpu and memory by MobileTestListener (mobile only).
     */
    String MOBILE_MONITORING_ACTIVE = "tt.mobile.monitoring.active";
    /**
     * Sets if the device reflection screen should be opened by SeeTest when working on it.
     */
    String MOBILE_OPEN_REFLECTION_SCREEN = "tt.mobile.open.reflection.screen";
}
