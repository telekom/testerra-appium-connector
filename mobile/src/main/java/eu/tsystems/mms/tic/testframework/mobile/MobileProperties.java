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
 * @author sepr
 */
//FIXME property names for fennec
public interface MobileProperties {
    String MOBILE_SERVER_HOST = "xeta.mobile.server.host";
    String MOBILE_SERVER_PORT = "xeta.mobile.server.port";
    String MOBILE_PROJECT_DIR = "xeta.mobile.project.dir";
    String MOBILE_REMOTE_REPORT_PATH = "xeta.mobile.remote.report.path";
    String MOBILE_LOCAL_REPORT_PATH = "xeta.mobile.local.report.path";
    String MOBILE_REPORT_TYPE = "xeta.mobile.report.type";
    String MOBILE_REPORT_ACTIVATED = "xeta.mobile.report.activated";
    /** Interval that the phone state should be monitored in. In Milliseconds. */
    String MOBILE_MONITORING_INTERVAL = "xeta.mobile.monitoring.interval";
    String MOBILE_REPORT_TAKE_SCREENSHOTS = "xeta.mobile.report.take.screenshots";
    String MOBILE_REPORT_TAKE_ONLY_BEFORE_SCREENSHOTS = "xeta.mobile.report.take.only.before.screenshots";
    String MOBILE_REPORT_DELAY_BETWEEN_SCREENSHOTS_MS = "xeta.mobile.report.delay.between.screenshots.ms";
    String MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED = "xeta.mobile.report.save.video.on.failed";
    String MOBILE_REPORT_SAVE_VIDEO = "xeta.mobile.report.save.video";
    String MOBILE_REPORT_SHOW_ALL_SCREENSHOTS_IN_SLIDER = "xeta.mobile.report.show.all.screenshots.in.slider";
    String MOBILE_STITCH_SCREENS = "xeta.mobile.layout.stitch.screenshots";
    String MOBILE_DEVICE_FILTER = "xeta.mobile.device.filter";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_TIMEOUT = "xeta.mobile.screenshot.tracker.timeout";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_ACTIVATED = "xeta.mobile.screenshot.tracker.activated";
    @Deprecated
    String MOBILE_SCREENSHOTTRACKER_URL = "xeta.mobile.screenshot.tracker.url";
    String MOBILE_SCREENSHOT_QUALITY = "xeta.mobile.screenshot.quality";
    String MOBILE_SKIP_BROWSER_HTTPS_WARNING = "xeta.mobile.skip.browser.https.warning";
    String MOBILE_SKIP_LAUNCH_ERROR = "xeta.mobile.skip.browser.launch.error";
    String MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS = "xeta.mobile.device.reservation.timeout.seconds";
    String MOBILE_DEVICE_GRID_RESERVATION_DURATION_IN_MINUTES = "xeta.mobile.device.grid.reservation.duration.minutes";
    String MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER = "xeta.mobile.device.reservation.randomize.order";
    String MOBILE_GRID_USER = "xeta.mobile.grid.user";
    String MOBILE_GRID_PASSWORD = "xeta.mobile.grid.password";
    String MOBILE_GRID_ACCESS_KEY = "xeta.mobile.grid.access.key";
    String MOBILE_GRID_PROJECT = "xeta.mobile.grid.project";
    String MOBILE_DEVICE_RESERVATION_RETRIES = "xeta.mobile.device.reservation.retries";
    String MOBILE_DEVICE_RESERVATION_POLICY = "xeta.mobile.device.reservation.policy";
    String MOBILE_APPLICATIONNAME_MATCH_STRICT = "xeta.mobile.appname.match.strict";
    String MOBILE_SHOW_FAILED_DEVICE_TEST_WARNING = "xeta.mobile.show.failed.device.test.warning";
    /** Can activate the monitoring of cpu and memory by MobileTestListener (mobile only). */
    String MOBILE_MONITORING_ACTIVE = "xeta.mobile.monitoring.active";
    /** Sets if the device reflection screen should be opened by SeeTest when working on it. */
    String MOBILE_OPEN_REFLECTION_SCREEN = "xeta.mobile.open.reflection.screen";
    String MOBILE_CACHE_GUI_ELEMENTS = "xeta.mobile.cache.gui.elements";
}
