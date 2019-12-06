package eu.tsystems.mms.tic.testframework.mobile.driver;

import com.experitest.client.Client;
import com.experitest.client.InternalException;
import com.experitest.client.MobileListener;
import com.google.common.base.Stopwatch;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.info.ReportInfo;
import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceLog;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.ViewOrientation;
import eu.tsystems.mms.tic.testframework.mobile.monitor.AppMonitor;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStepController;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rnhb on 17.04.2015.
 */
public abstract class BaseMobileDriver implements MobileDriver {

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName() + " " + this.hashCode());
    final String cloudPrefix = "cloud:";
    final String keyEventProperty = "ios.elementsendtext.action.fire";
    TestDevice activeDevice;
    Set<TestDevice> reservedDevices;
    Set<String> reportedDeviceNames;
    Set<String> installedApplications = new HashSet<>();
    Set<TestDevice> openDevices;
    ElementAccessor elementAccessor;
    Client seeTestClient;
    AppMonitor appMonitor = null;
    boolean openReflectionScreen = PropertyManager.getBooleanProperty(
            MobileProperties.MOBILE_OPEN_REFLECTION_SCREEN,
            DefaultParameter.MOBILE_OPEN_REFLECTION_SCREEN);
    File beforeScreenshot;

    int activeDeviceIndex;

    // always true for now
    private boolean reporting = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_ACTIVATED, false);
    private boolean monitoring = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_MONITORING_ACTIVE,
            DefaultParameter.MOBILE_MONITORING_ACTIVE);
    private boolean takeScreenshots = PropertyManager.getBooleanProperty(
            MobileProperties.MOBILE_REPORT_TAKE_SCREENSHOTS,
            DefaultParameter.MOBILE_REPORT_TAKE_SCREENSHOTS);
    private boolean takeOnlyBeforeScreenshots = PropertyManager.getBooleanProperty(
            MobileProperties.MOBILE_REPORT_TAKE_ONLY_BEFORE_SCREENSHOTS,
            DefaultParameter.MOBILE_REPORT_TAKE_ONLY_BEFORE_SCREENSHOTS);
    private int delayBetweenScreenshotsInMs = PropertyManager.getIntProperty(
            MobileProperties.MOBILE_REPORT_DELAY_BETWEEN_SCREENSHOTS_MS,
            DefaultParameter.MOBILE_REPORT_DELAY_BETWEEN_SCREENSHOTS_MS);
    private boolean showAllScreenshotsInSlider = PropertyManager.getBooleanProperty(
            MobileProperties.MOBILE_REPORT_SHOW_ALL_SCREENSHOTS_IN_SLIDER,
            DefaultParameter.MOBILE_REPORT_SHOW_ALL_SCREENSHOTS_IN_SLIDER);
    private boolean screenshotsDisabled = false;

    private List<DeviceTest> deviceTests = new ArrayList<>();

    private DeviceReservationPolicy deviceReservationPolicy;

    private final HashMap<ScreenDump.Type, ScreenDump> screenDumpHashMap = new HashMap<>();

    public BaseMobileDriver() {

        activeDevice = null;
        reservedDevices = new HashSet<>();
        openDevices = new HashSet<>();
        elementAccessor = new ElementAccessor(this);
        reportedDeviceNames = new HashSet<>();

        deviceTests.add(new DeviceTest("reservation") {
            @Override
            public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {

                reserveDevice(testDevice);
                seeTestClient().capture();
                return true;
            }
        });

        String policyPropertyValue = PropertyManager.getProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_POLICY,
                "");
        deviceReservationPolicy = DeviceReservationPolicy.get(policyPropertyValue);
        if (deviceReservationPolicy == null) {
            deviceReservationPolicy = DeviceReservationPolicy.DEFAULT_POLICY;
        }
        if (deviceReservationPolicy == DeviceReservationPolicy.EXCLUSIVE_DEVICE_PRIORITY) {
            LOGGER.error(DeviceReservationPolicy.EXCLUSIVE_DEVICE_PRIORITY + " not implemented yet. Will use "
                    + DeviceReservationPolicy.UNRESERVED_OR_OWN_DEVICES + " instead.");
            deviceReservationPolicy = DeviceReservationPolicy.UNRESERVED_OR_OWN_DEVICES;
        }
        LOGGER.info("Current device Reservation Policy:" + deviceReservationPolicy);
    }

    @Override
    public ScreenDump getScreenDump(ScreenDump.Type type) {

        ScreenDump nativeScreenDump = screenDumpHashMap.get(type);

        if (nativeScreenDump == null) {
            nativeScreenDump = new ScreenDump(seeTestClient().getVisualDump(type.toString()));
            screenDumpHashMap.put(type, nativeScreenDump);
            LOGGER.debug("Screendump cached.");
        }

        return nativeScreenDump;
    }

    @Override
    public void clearScreenDumpCaches() {

        screenDumpHashMap.clear();
        LOGGER.debug("Screendump Cache cleared.");
    }

    @Override
    public void registerDeviceTest(DeviceTest deviceTest) {

        if (deviceTests.contains(deviceTest)) {
            if (DeviceTest.DEFAULT_NAME.equals(deviceTest.name)) {
                LOGGER.warn(
                        "Registering device test with default name, but there is already a device test with that name. Please consider giving appropriate names to avoid confusion, or check if you accidentally add the test multiple times.");
            } else {
                LOGGER.warn("Registering device test \"" + deviceTest
                        + "\", but there is already a device test with the same name registered. Please check if you add it multiple times by accident, or if you can find a more appropriate name for each test.");
            }
        }
        deviceTests.add(deviceTest);
    }

    @Override
    public void clearDeviceTests() {

        deviceTests.clear();
    }

    /**
     * When using sendText(String) on MobileGuiElements, key events are not fired by default. This means that gui
     * reactions triggered by such events will not be caused by default ( like activating a search button only after at
     * least one character was typed).
     */
    @Override
    public void setFiringKeyEvents(boolean b) {

        seeTestClient().setProperty(keyEventProperty, String.valueOf(b));
    }

    private void afterLog(String msg, boolean takeScreenshot) {

        if (takeScreenshot) {
            takeAfterScreenshot();
        }
        LOGGER.info(msg);
    }

    private void beforeLog(String msg, boolean takeScreenshot) {

        LOGGER.info(msg);
        if (takeScreenshot) {
            takeBeforeScreenshot();
        }
    }

    @Override
    public boolean closeApplication(String packageName) {

        packageName = cutCloudPrefix(packageName);
        beforeLog("Closing application " + packageName, true);
        boolean applicationClosed = seeTestClient().applicationClose(packageName);
        clearScreenDumpCaches();

        beforeLog("Closed " + packageName + ": " + applicationClosed, true);
        return applicationClosed;
    }

    private void ensureDeviceIsActive() {

        if (activeDevice == null) {
            throw new TesterraRuntimeException(
                    "A method tried to send a command to a device, but no device was focused before.");
        }
    }

    void handleSeeTestClientCreation() {

        if (seeTestClient == null) {
            throw new RuntimeException("SeeTestClient was not successfully created. Cannot execute tests.");
        }
        seeTestClient.setLogger(new ExperitestLogger());

        seeTestClient.setWebAutoScroll(true);
        seeTestClient.setInKeyDelay(1);
        seeTestClient.setKeyToKeyDelay(1);

        String clientVersion = "unknown";
        try {
            Object result;
            Method versionMethod;
            versionMethod = Client.class.getDeclaredMethod("getVersion");
            if (versionMethod != null) {
                versionMethod.setAccessible(true);
                result = versionMethod.invoke(seeTestClient);
                clientVersion = (String) result;
            }
        } catch (Exception e) {
            LOGGER.error(
                    "Failed to retrieve client version through reflection. Will not have a functional impact, but client version is missing from report.",
                    e);
        }

        LOGGER.info("SeeTest Java Client Version: " + clientVersion);
        ReportInfo.getRunInfo().addInfo("Client Version:", clientVersion);
    }

    @Override
    public TestDevice reserveDeviceByFilter(String filterProperty) {

        List<TestDevice> foundDevices = MobileDriverManager.deviceStore().getTestDevicesByFilter(filterProperty);

        boolean randomizeDeviceOrder = PropertyManager.getBooleanProperty(
                MobileProperties.MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER,
                DefaultParameter.MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER);
        if (randomizeDeviceOrder) {
            Collections.shuffle(foundDevices);
        }

        LOGGER.info("The following device tests will be performed to check, if the device is ready for execution: "
                + deviceTests);

        for (TestDevice testDevice : foundDevices) {
            updateReservationStatusOfDevice(testDevice);
            ReservationStatus reservationStatusOfDevice = testDevice.getReservationStatus();

            if (!deviceReservationPolicy.isAllowed(reservationStatusOfDevice)) {
                continue;
            }
            try {
                for (DeviceTest deviceTest : deviceTests) {
                    TestStep.begin("Device Test " + deviceTest);
                    LOGGER.info("Starting Device Test " + deviceTest);
                    boolean pass = deviceTest.doDeviceTest(this, testDevice);
                    LOGGER.info("Device Test " + deviceTest + " has pass status: " + pass);
                    if (!pass) {
                        String errorMessage = String.format(
                                "DeviceTest \"%s\" failed on device \"%s\". Please check device or report to administrator.",
                                deviceTest.toString(), testDevice.getName());
                        if (PropertyManager.getBooleanProperty(MobileProperties.MOBILE_SHOW_FAILED_DEVICE_TEST_WARNING, DefaultParameter.MOBILE_SHOW_FAILED_DEVICE_TEST_WARNING)) {
                            ReportInfo.getDashboardWarning().addInfoWithDefaultPrio(errorMessage);
                        }
                        throw new DeviceNotAvailableException(testDevice, errorMessage);
                    }
                }
                if (monitoring) {
                    appMonitor = new AppMonitor(this);
                    appMonitor.startMonitoring();
                }
                return testDevice;
            } catch (Exception e) {
                LOGGER.warn("Device " + testDevice
                        + " seems to be problematic and does not work for automation. Trying to find an alternative. Reason: "
                        + e.getMessage());
                try {
                    releaseDevice(testDevice);
                } catch (Exception e2) {
                    // do not log an error on release - we have no idea if the device is reserved or not, exception
                    // on release is irrelevant
                }
            }
        }
        throw new DeviceNotAvailableException(TestDevice.NONE, "No device of allowed devices available. Allowed devices: " + foundDevices);
    }

    @Override
    public TestDevice reserveDeviceByFilter() throws DeviceNotAvailableException {

        return reserveDeviceByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
    }

    @Override
    public void takeBeforeScreenshot() {

        if (takeScreenshots && !screenshotsDisabled) {
            beforeScreenshot = prepareNewScreenshot();
        }
    }

    private void takeScreenshot() {

        if (takeScreenshots && !screenshotsDisabled) {
            final File screenshotFile = prepareNewScreenshot();
            final Screenshot screenshot = this.publishScreenshotToMethodContext(screenshotFile, null);
            TestStepController.addScreenshotsToCurrentAction(screenshot, null);
        }
    }

    public Screenshot publishScreenshotToMethodContext(final File screenshotFile, final File visualDumpFile) {

        if (screenshotFile == null) {
            LOGGER.debug("Called publishScreenshotToMethodContext with null: No screenshot to publish.");
            return null;
        }

        try {
            final Screenshot screenshot = Report.provideScreenshot(screenshotFile, visualDumpFile, Report.Mode.MOVE);
            MethodContext currentMethodContext = ExecutionContextController.getCurrentMethodContext();
            screenshot.errorContextId = currentMethodContext.id;
            currentMethodContext.screenshots.add(screenshot);
            return screenshot;

        } catch (IOException e) {
            LOGGER.error("Error providing screenshot to report.");
        }

        return null;
    }

    @Override
    public void takeAfterScreenshot() {

        if (takeScreenshots && !screenshotsDisabled) {
            if (takeOnlyBeforeScreenshots) {
                if (beforeScreenshot != null) {
                    final Screenshot screenshot = this.publishScreenshotToMethodContext(beforeScreenshot, null);
                    TestStepController.addScreenshotsToCurrentAction(screenshot, null);
                }
            } else {
                if (delayBetweenScreenshotsInMs > 0) {
                    TimerUtils.sleep(delayBetweenScreenshotsInMs);
                }

                // Providing screenshots to method context...
                final File afterScreenshotFile = prepareNewScreenshot();
                final Screenshot screenshotBefore = this.publishScreenshotToMethodContext(beforeScreenshot, null);
                final Screenshot screenshotAfter = this.publishScreenshotToMethodContext(afterScreenshotFile, null);

                // provide screenshot to current test step...
                TestStepController.addScreenshotsToCurrentAction(screenshotBefore, screenshotAfter);
            }

            beforeScreenshot = null;
        }
    }

    @Override
    public File prepareNewScreenshot() {

        //TODO Workaround: Though initially set screenshot quality still varyies, but not if it is set right before capturing the screen, somehow.
        //TODO When this is fixed, with the next seetest version, please remove it again.
        seeTestClient().setProperty("screen.quality", PropertyManager.getProperty(MobileProperties.MOBILE_SCREENSHOT_QUALITY, DefaultParameter.MOBILE_SCREENSHOT_QUALITY));
        final String capturePath = seeTestClient().capture();

        if (capturePath == null) {
            LOGGER.warn("SeeTestClient: Capture Path was empty. No screenshot saved.");
            return null;
        }

        final String imageFile = activeDevice.getName() + "_" + Paths.get(capturePath.replace('\\', '/')).getFileName().toString();
        final Path tempScreenshotPath = Paths.get(System.getProperty("java.io.tmpdir"), imageFile);

        if (!MobileDriverUtils.getRemoteOrLocalFile(this, capturePath, tempScreenshotPath)) {
            LOGGER.warn("MobileDriverUtils: Could not receive remote or local screenshot file on capture path: " + capturePath);
            return null;
        }

        ScreenshotTracker.setCurrentScreenshot(tempScreenshotPath);
        return tempScreenshotPath.toFile();
    }

    @Override
    @Deprecated
    public boolean isDeviceConnected(TestDevice testDevice) {

        String connectedDevices = seeTestClient().getConnectedDevices();
        return connectedDevices.contains(testDevice.getName());
    }

    @Override
    public void closeKeyboard() {

        ensureDeviceIsActive();
        seeTestClient().closeKeyboard();
        clearScreenDumpCaches();
    }

    /**
     * Swipes on the screen with a gesture. Swipes are always centered with an offset determining how many pixels away
     * from the border the swipe should start.
     *
     * @param direction                     Direction in which should be scrolled (inverse of where the finger actually moves). Swipe UP
     *                                      means moving the finger down, so the screen moves up.
     * @param offsetInPixelOrScreenFraction If below 0, it is interpreted as fraction of the vertical screensize.
     *                                      Otherwise, the offset is directly used to determine at which position to start the swipe.
     * @param timeInMs                      How long the swipe should take. Short swipes can overshoot because of the fast movement.
     */
    public void swipe(Direction direction, float offsetInPixelOrScreenFraction, int timeInMs) {

        ensureDeviceIsActive();
        beforeLog("Swiping in " + direction + " for " + timeInMs + " ms, " + offsetInPixelOrScreenFraction + " pixel",
                true);
        int pixelOffset;
        int screenSize = direction.isVertical() ? activeDevice.getPixelsY() : activeDevice.getPixelsX();
        if (offsetInPixelOrScreenFraction < 1) {
            pixelOffset = (int) (screenSize * offsetInPixelOrScreenFraction);
        } else {
            pixelOffset = (int) offsetInPixelOrScreenFraction;
        }
        elementAccessor.swipe(direction.toString(), pixelOffset, timeInMs);

        afterLog("Swiped.", true);
    }

    @Override
    public void clearApplicationData(String packageName) {

        if (packageName == null) {
            throw new TesterraRuntimeException("PackageName cannot be null!");
        }
        ensureDeviceIsActive();
        if (MobileOperatingSystem.ANDROID == activeDevice.getOperatingSystem()) {
            packageName = cutCloudPrefix(packageName);
            seeTestClient().applicationClearData(packageName);
        } else {
            LOGGER.error("clearApplicationData(String) is only available for " + MobileOperatingSystem.ANDROID
                    + ", but was called for " + activeDevice.getOperatingSystem() + ". Will do nothing.");
        }
    }

    @Override
    public void clearDeviceLog() {

        ensureDeviceIsActive();
        seeTestClient().clearDeviceLog();
    }

    @Override
    public DeviceLog getDeviceLog() {

        ensureDeviceIsActive();
        String deviceLogPath = seeTestClient().getDeviceLog();
        try {
            Path tempLogPath = Files.createTempFile(null, ".log");
            MobileDriverUtils.getRemoteOrLocalFile(this, deviceLogPath, tempLogPath);
            DeviceLog deviceLog = new DeviceLog(tempLogPath);
            return deviceLog;
        } catch (IOException e) {
            LOGGER.error("Error getting deviceLog", e);
            return null;
        }
    }


    @Override
    public boolean installApplication(String fullPathOfApp) {

        return installApplication(fullPathOfApp, true);
    }

    @Override
    public boolean installApplication(String fullPathOfApp, boolean instrument) {

        if (fullPathOfApp == null) {
            throw new TesterraRuntimeException("Application String cannot be null!");
        }
        LOGGER.info("Installing Application " + fullPathOfApp + ".");
        Stopwatch started = Stopwatch.createStarted();
        ensureDeviceIsActive();
        boolean installationSuccessful = seeTestClient().install(fullPathOfApp, instrument, false);
        LOGGER.info("Installed App: " + installationSuccessful + ". Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
        takeScreenshot();

        if (!installedApplications.contains(fullPathOfApp)) {
            String suffix = fullPathOfApp.isEmpty() ? "" : " " + (installedApplications.size() + 1);
            ReportInfo.getRunInfo().addInfo("Application" + suffix + ":", fullPathOfApp);
            installedApplications.add(fullPathOfApp);
        }

        return installationSuccessful;
    }

    /**
     * @param application String of application. Prefix "cloud:" will be cut.
     * @return status of success of uninstall
     */
    @Override
    public boolean uninstall(String application) {

        if (application == null) {
            throw new TesterraRuntimeException("Application String cannot be null!");
        }
        Stopwatch started = Stopwatch.createStarted();
        application = cutCloudPrefix(application);
        LOGGER.info("Uninstalling Application " + application + ".");
        ensureDeviceIsActive();
        boolean uninstallSuccessful = seeTestClient().uninstall(application);
        LOGGER.info("Uninstalling App: " + uninstallSuccessful + ". Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
        return uninstallSuccessful;
    }

    @Override
    public void uninstallAllInstalledApps() {

        for (String installedApplication : installedApplications) {
            try {
                uninstall(installedApplication);
            } catch (Exception e) {
                LOGGER.error("Failed ot uninstall previously installed application \"" + installedApplication + "\".",
                        e);
            }
        }
    }

    private String cutCloudPrefix(String application) {

        if (application.startsWith(cloudPrefix)) {
            application = application.substring(cloudPrefix.length());
        }
        return application;
    }

    private String appendSemicolon(String application) {

        if (PropertyManager.getBooleanProperty(MobileProperties.MOBILE_APPLICATIONNAME_MATCH_STRICT,
                DefaultParameter.MOBILE_APPLICATIONNAME_MATCH_STRICT)) {
            return application + ";";
        }
        return application;
    }

    private String cutActivity(String applicationString) {

        if (applicationString.contains("/")) {
            return applicationString.substring(0, applicationString.indexOf("/"));
        } else {
            return applicationString;
        }
    }

    @Override
    public final void reserveDevice(TestDevice testDevice) throws DeviceNotAvailableException {

        if (testDevice == null) {
            throw new TesterraRuntimeException("TestDevice cannot be null!");
        }
        LOGGER.info("Trying to reserve device " + testDevice + ", last known status is "
                + testDevice.getReservationStatus());

        updateReservationStatusOfDevice(testDevice);
        ReservationStatus reservationStatusOfDevice = testDevice.getReservationStatus();

        if (reservationStatusOfDevice == ReservationStatus.UNKNOWN
                || reservationStatusOfDevice == ReservationStatus.OFFLINE) {
            throw new TesterraRuntimeException(
                    testDevice + " not available, cannot reserve it. Status: " + reservationStatusOfDevice);
        } else {
            if (deviceReservationPolicy.isAllowed(reservationStatusOfDevice)) {
                waitForDevice(testDevice);
                handleDeviceReservation(testDevice);
            } else {
                throw new RuntimeException("Reservation policy is " + deviceReservationPolicy + ", but device "
                        + testDevice + " has the status " + reservationStatusOfDevice
                        + ", which is not allowed for this policy.");
            }
        }
    }

    protected abstract void handleDeviceReservation(TestDevice testDevice);

    protected abstract void waitForDevice(TestDevice testDevice);

    private void updateReservationStatusOfDevice(TestDevice testDevice) {

        LOGGER.info("Trying to get update reservation status for " + testDevice);
        String devicesInformation = getDevicesInformation();
        Document deviceInformation = XMLUtils.jsoup().createJsoupDocument(devicesInformation);

        Element correctDeviceInfo = null;
        for (Element device : deviceInformation.getElementsByTag("device")) {
            String name = device.attr("name");
            if (testDevice.getName().equals(name)) {
                correctDeviceInfo = device;
                break;
            }
        }
        LOGGER.info("Finished getting device info. For " + testDevice + " it is: " + correctDeviceInfo);

        testDevice.setReservationStatus(parseReservationStatus(correctDeviceInfo));
        LOGGER.info("Parsed device status for " + testDevice + ", it is " + testDevice.getReservationStatus());
    }

    protected abstract ReservationStatus parseReservationStatus(Element deviceInfo);

    @Override
    public abstract void switchToDevice(TestDevice testDevice);

    @Override
    @Deprecated
    public void switchToAndWakeDevice(TestDevice testDevice) {

        switchToDevice(testDevice);
        wakeDevice();
    }

    @Override
    public void openDevice() {

        ensureDeviceIsActive();
        seeTestClient().openDevice();
        openDevices.add(activeDevice);
    }

    @Override
    public void closeDevice() {

        ensureDeviceIsActive();
        seeTestClient().closeDevice();
        openDevices.remove(activeDevice);
    }

    @Override
    public void wakeDevice() {

        ensureDeviceIsActive();
        Stopwatch started = Stopwatch.createStarted();

        // only on android, both operations do something different in SeeTest
        if (activeDevice.getOperatingSystem() == MobileOperatingSystem.ANDROID) {
            seeTestClient().sendText("{WAKE}");
            LOGGER.info("Woke Device. Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
            started.reset();
        }

        seeTestClient().sendText("{UNLOCK}");
        LOGGER.info("Unlocked Device. Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
    }

    @Override
    public void browserBack() {

        ensureDeviceIsActive();
        beforeLog("Going back in Browser.", true);
        seeTestClient().sendText("{WEBBACK}");
        clearScreenDumpCaches();
        afterLog("Went back in Browser.", true);
    }

    @Override
    public void back() {

        ensureDeviceIsActive();
        beforeLog("Pressing back on device.", true);
        seeTestClient().sendText("{BACK}");
        clearScreenDumpCaches();
        afterLog("Pressed back on device.", true);
    }

    @Override
    public void pressHomeButton() {

        ensureDeviceIsActive();
        beforeLog("Pressing home on device.", true);
        seeTestClient().sendText("{HOME}");
        clearScreenDumpCaches();
        afterLog("Pressed home on device.", true);
    }

    @Override
    public void pressEnterOnKeyboard() {

        ensureDeviceIsActive();
        beforeLog("Pressing enter on keyboard.", true);
        seeTestClient().sendText("{ENTER}");
        clearScreenDumpCaches();
        afterLog("Pressed enter on keyboard.", true);
    }

    @Override
    public void changeOrientationTo(ViewOrientation viewOrientation) {

        ensureDeviceIsActive();
        beforeLog("Change orientation to " + viewOrientation, true);
        seeTestClient().sendText("{" + viewOrientation.name() + "}");
        activeDevice.setViewOrientation(viewOrientation);
        clearScreenDumpCaches();
        afterLog("Changed orientation", true);
    }

    @Override
    public ViewOrientation getOrientation() {

        ensureDeviceIsActive();
        beforeLog("Get screen orientation", false);
        String orientation = seeTestClient().getDeviceProperty("orientation");
        afterLog("Got screen orientation: \"" + orientation + "\"", false);
        return ViewOrientation.valueOf(orientation.toUpperCase());
    }

    @Override
    public int getNumberOfElements(LocatorType locatorType, String identifier) {

        return seeTestClient().getElementCount(locatorType.name(), identifier);
    }

    @Override
    public Dimension getScreenResolution() {

        ensureDeviceIsActive();
        int x = seeTestClient().p2cx(100);
        int y = seeTestClient().p2cy(100);
        return new Dimension(x, y);
    }

    @Override
    public void closeAllDevices() {

        for (TestDevice openDevice : openDevices) {
            seeTestClient().setDevice(openDevice.getName());
            closeDevice();
        }
    }

    @Override
    public void performDragOnCoordinates(int startX, int startY, int endX, int endY, int timeToDrag) {

        clearScreenDumpCaches();
        seeTestClient().dragCoordinates(startX, startY, endX, endY, timeToDrag);
    }

    @Override
    public ElementAccessor element() {

        return elementAccessor;
    }

    @Override
    public TestDevice getActiveDevice() {

        return activeDevice;
    }

    @Override
    public void pressBackspace() {

        ensureDeviceIsActive();
        beforeLog("Pressing backspace on keyboard.", false);
        seeTestClient().sendText("{BKSP}");
        clearScreenDumpCaches();
        afterLog("Pressed backspace on keyboard.", false);
    }

    @Override
    public void launchApplication(String applicationString) {

        launchApplication(applicationString, true);
    }

    @Override
    public void launchApplication(String applicationString, boolean instrumentApplication) {

        launchApplication(applicationString, instrumentApplication, true);
    }

    @Override
    public void launchApplication(String applicationString, boolean instrumentApplication, boolean closeAppIfRunning) {

        ensureDeviceIsActive();
        clearScreenDumpCaches();

        if (applicationString == null) {
            throw new TesterraRuntimeException("Application String cannot be null!");
        }

        Stopwatch started = Stopwatch.createStarted();

        applicationString = cutCloudPrefix(applicationString);
        beforeLog("Launching application " + applicationString, true);

        try {
            seeTestClient().launch(applicationString, instrumentApplication, closeAppIfRunning);
        } catch (InternalException e) {
            if (PropertyManager.getBooleanProperty(MobileProperties.MOBILE_SKIP_LAUNCH_ERROR,
                    DefaultParameter.MOBILE_SKIP_LAUNCH_ERROR) &&
                    e.getMessage().startsWith("Exception caught while executing launch: Failed to navigate to")) {
                LOGGER.warn("Ignoring launch exception:", e);
            } else {
                afterLog("Failed to launch app", true);
                throw new TesterraRuntimeException("Error when launching application \"" + applicationString + "\" "
                        + (instrumentApplication ? "" : "not ") + "instrumented).", e);
            }
        }

        // An Element is apparently found before it is visible, so the following click can fail.
        // This is probably related to lag in transmitting the reflection screen.
        afterLog("Launched App: " + applicationString + ". Took " + started.elapsed(TimeUnit.SECONDS)
                + "s.", true);

        if (applicationString.contains("safari:") || applicationString.contains("chrome:")) {
            ReportInfo.getRunInfo().addInfo("Browser:", applicationString.substring(0, applicationString.indexOf(":")));
        } else if (applicationString.startsWith("http")) {
            if (activeDevice.getOperatingSystem() == MobileOperatingSystem.ANDROID) {
                ReportInfo.getRunInfo().addInfo("Browser:", "Chrome");
            } else if (activeDevice.getOperatingSystem() == MobileOperatingSystem.IOS) {
                ReportInfo.getRunInfo().addInfo("Browser:", "Safari");
            }
        }

        clearScreenDumpCaches();
    }

    @Override
    public boolean isApplicationInstalled(String applicationString) {

        if (applicationString == null) {
            throw new TesterraRuntimeException("Application String cannot be null!");
        }
        String installedApplications = seeTestClient().getInstalledApplications();
        applicationString = appendSemicolon(cutCloudPrefix(cutActivity(applicationString)));
        return installedApplications.contains(applicationString);
    }

    @Override
    public void setGpsLocation(String latitude, String longitude) {

        beforeLog("Setting GPS location to " + latitude + " lat, " + longitude + " long.", true);
        seeTestClient().setLocation(latitude, longitude);
        afterLog("Set GPS location to " + latitude + " lat, " + longitude + " long.", true);
    }

    @Override
    public void clearGpsLocation() {

        beforeLog("Clearing GPS location.", true);
        seeTestClient().clearLocation();
        afterLog("Cleared GPS location.", true);
    }

    @Override
    public Client seeTestClient() {

        return seeTestClient;
    }

    @Override
    public void startVideoRecord() {

        ensureDeviceIsActive();
        LOGGER.info("Starting video record.");
        try {
            seeTestClient().startVideoRecord();
        } catch (Exception e) {
            LOGGER.error("Failed to start video record.", e);
        }
    }

    @Override
    public String stopVideoRecord() {

        ensureDeviceIsActive();
        LOGGER.info("Stopping video record.");
        try {
            return seeTestClient().stopVideoRecord();
        } catch (Exception e) {
            LOGGER.error("Failed to stop video record.", e);
        }
        return null;
    }

    /**
     * Performs a virtual shake on the device.
     */
    @Override
    public void shakeDevice() {

        ensureDeviceIsActive();
        beforeLog("Shaking device.", true);
        seeTestClient().shake();
        clearScreenDumpCaches();
        afterLog("Shaked device", true);
    }

    /**
     * Sets the profile for network conditions. Needs the Network Virtualization Tool.
     *
     * @param netWorkConditionsProfile Name of the profile to use.
     */
    @Override
    public void setNetWorkConditionsProfile(String netWorkConditionsProfile) {

        ensureDeviceIsActive();
        seeTestClient().setNetworkConditions(netWorkConditionsProfile);
    }

    /**
     * A recovery listener is used to deal with pop ups, that can occur at any time, for example an incoming phone call
     * pop up. The listener is triggered, if the specified xPath is found on the screen. Its recover method is then
     * called, allowing a logic to be executed, that handles the unexpected pop up. Neat!
     *
     * @param locatorType Method of detecting the element
     * @param xPath       xPath that triggers the recover method, if found
     * @param listener    class that offers the recover method
     */
    @Override
    public void addRecoveryListener(LocatorType locatorType, String xPath, MobileListener listener) {

        seeTestClient().addMobileListener(locatorType.name(), xPath, listener);
    }

    /**
     * Types the given text on a keyboard. If no text can be typed at this point (for example because no text field was
     * clicked), nothing happens.
     *
     * @param textToType Text to type.
     */
    @Override
    public void type(String textToType) {

        beforeLog("Typing \"" + textToType + "\" on keyboard.", true);
        seeTestClient().sendText(textToType);
        clearScreenDumpCaches();
        afterLog("Typed \"" + textToType + "\" on keyboard.", true);
    }

    /**
     * Uninstalls all applications whose package name matches the given regex.
     *
     * @param regexMatchingPackageName regex to match the package name
     */
    @Override
    public void uninstallAllApps(String regexMatchingPackageName) {

        try {
            String installedApplications = seeTestClient().getInstalledApplications();
            String[] applications = installedApplications.replace("\n", "").split(";");
            Pattern pattern = Pattern.compile(regexMatchingPackageName);
            for (String application : applications) {
                Matcher matcher = pattern.matcher(application);
                if (matcher.find()) {
                    seeTestClient().uninstall(application);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to uninstall app.", e);
        }
    }

    /**
     * This command will retrieve the name \ bundle \ package name of the application that's currently run in the
     * foreground of the active device.
     *
     * @return name \ bundle \ package name of the application that's currently run in the foreground of the active
     * device
     */
    @Override
    public String getCurrentlyRunningApplication() {

        return seeTestClient().getCurrentApplicationName();
    }

    @Override
    @Deprecated
    public void report(String message) {

        report(message, false, true);
    }

    @Override
    @Deprecated
    public void report(String message, boolean takeScreenshot) {

        report(message, takeScreenshot, true);
    }

    @Override
    @Deprecated
    public void report(String message, boolean takeScreenshot, boolean status) {

        LOGGER.info("reporting: " + reporting + "  " + message);
        if (reporting) {
            if (takeScreenshot) {
                String imagePath = seeTestClient().capture();
                seeTestClient().setShowReport(true);
                seeTestClient().report(imagePath, message, status);
            } else {
                seeTestClient().setShowReport(true);
                seeTestClient().report(message, status);
            }
            seeTestClient().setShowReport(false);
        }
    }

    @Override
    @Deprecated
    public void report(String message, String imagePath, boolean status) {

        if (reporting) {
            seeTestClient().setShowReport(true);
            seeTestClient().report(imagePath, message, status);
            seeTestClient().setShowReport(false);
        }
    }

    @Override
    @Deprecated
    public void setReporting(boolean reporting) {

        this.reporting = reporting;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
     */
    @SuppressWarnings("unchecked")

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {

        final boolean stitchScreenshotsProperty = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_STITCH_SCREENS, DefaultParameter.MOBILE_STITCH_SCREENS);
        return getScreenshotAs(target, stitchScreenshotsProperty);
    }

    public <X> X getScreenshotAs(OutputType<X> target, boolean stitchScreenshots) throws WebDriverException {

        if (target != OutputType.FILE) {
            throw new TesterraRuntimeException("Mobile Driver only allows files as getScreenShotAs return type.");
        }

        String isBottom;
        List<Path> screens = new LinkedList<>();
        int maxTries = 30;

        do {
            Path tempFile;
            try {
                tempFile = Files.createTempFile(null, ".png");
            } catch (IOException e) {
                LOGGER.error("Failed to create temp. file for screenshots.", e);
                return null;
            }

            final String pathFromSeeTest = seeTestClient().capture();
            final boolean transferred = MobileDriverUtils.getRemoteOrLocalFile(this, pathFromSeeTest, tempFile);

            if (transferred) {
                screens.add(tempFile);
            }

            // TODO erku - switch on application name... :)... only stitch on web view.
            String currentApplicationName = this.seeTestClient().getCurrentApplicationName();

            if (stitchScreenshots) {
                this.seeTestClient().hybridRunJavascript("", 0, "var result = document.documentElement.scrollTop;");
                this.seeTestClient().hybridRunJavascript("", 0, "var result = document.documentElement.clientHeight;");
                this.seeTestClient().hybridRunJavascript("", 0, "var result = document.documentElement.offsetHeight;");
                isBottom = this.seeTestClient().hybridRunJavascript("", 0, "var result = document.documentElement.scrollTop+document.documentElement.clientHeight>=document.documentElement.offsetHeight;");
                this.seeTestClient().hybridRunJavascript("", 0, "window.scrollBy(0,document.documentElement.clientHeight);");
                TimerUtils.sleep(1000);
                maxTries--;
            } else {
                break;
            }

        } while ("false".equals(isBottom) && maxTries > 0);
        Path out;
        try {
            out = stitchScreenshots(screens);
        } catch (IOException e) {
            LOGGER.error("Failed to stitch screenshots.", e);
            return null;
        }
        //        TODO remove since unnecessary with testerra?
        //        TestStepController.addScreenshotsToCurrentAction(out.toAbsolutePath().toString(), null);
        return (X) out.toFile();
    }

    /**
     * Stitch set of screenshots together.
     *
     * @param screens List of screenshots to stitch.
     * @return Path of new stitched screenshot.
     */
    private Path stitchScreenshots(List<Path> screens) throws IOException {

        if (screens.size() == 1) {
            return screens.get(0);
        }
        int height = 0;
        int width = 0;
        for (Path screenshot : screens) {
            BufferedImage image;
            image = ImageIO.read(screenshot.toFile());
            height += image.getHeight();
            width = Math.max(width, image.getWidth());
        }
        BufferedImage stitched = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster fullImageRaster = stitched.getRaster();
        int y = 0;
        for (Path screenshot : screens) {
            BufferedImage image;
            image = ImageIO.read(screenshot.toFile());
            WritableRaster raster = image.getRaster();
            fullImageRaster.setDataElements(0, y, raster);
            y += image.getHeight();
        }
        // TODO this screenshot should probably not sit around in temp and be linked to the report from there
        Path pathOut = Files.createTempFile("stiched_", "_mobile.png");
        ImageIO.write(stitched, "png", pathOut.toFile());

        // clean up
        for (Path screen : screens) {
            Files.delete(screen);
        }
        return pathOut;
    }

    @Override
    public void releaseAllDevices() {

        if (monitoring && appMonitor != null) {
            try {
                appMonitor.createReportTab();
            } catch (IOException e) {
                LOGGER.error("Exception on creating report tab for device monitoring", e);
            }
        }
        LOGGER.info("Releasing all devices: {}",
                reservedDevices.stream()
                        .map(device -> device.getName())
                        .collect(Collectors.joining(", ")));
        for (TestDevice reservedDevice : new HashSet<>(reservedDevices)) {
            releaseDevice(reservedDevice);
        }
    }

    @Override
    public void disableScreenshots(boolean disable) {

        this.screenshotsDisabled = disable;
    }

    @Override
    public void closeSystemPopUps() {

        if (activeDevice != null && activeDevice.getOperatingSystem() == MobileOperatingSystem.IOS) {
            disableScreenshots(true);
            try {
                // no backup - happens when apple id is logged in. After clicking it away, a "Später" popup can appear
                NativeMobileGuiElement okButton = new NativeMobileGuiElement(By.text("OK"));
                if (new NativeMobileGuiElement(By.text("iPhone ohne Backup")).isDisplayed()) {
                    okButton.click();
                }

                // "Später popup happens when an update is known or when the backup popup was closed
                NativeMobileGuiElement installUpdateLaterButton = new NativeMobileGuiElement(By.text("Später"));
                if (installUpdateLaterButton.isDisplayed()) {
                    installUpdateLaterButton.click();
                }

                // a second update popup can appear after closing the first
                installUpdateLaterButton = new NativeMobileGuiElement(By.text("Später erinnern"));
                if (installUpdateLaterButton.isDisplayed()) {
                    installUpdateLaterButton.click();
                }

                // popup that no sim card is inserted can appear
                if (new NativeMobileGuiElement(By.text("Keine SIM-Karte eingelegt")).isDisplayed()) {
                    okButton.click();
                }

                NativeMobileGuiElement appleIdCheck = new NativeMobileGuiElement(By.text("Später"));
                if (appleIdCheck.isDisplayed()) {
                    appleIdCheck.click();
                }
            } catch (Exception e) {
                // Logging this way is intentional, the stacktrace should not clutter the log and provides no extra
                // information.
                LOGGER.warn("Failed to handle iOS PopUps: " + e.getMessage());
            }
            disableScreenshots(false);
        }
    }

    @Override
    public boolean resetWlanOfActiveDevice() {

        boolean success = false;
        Exception thrownException = null;
        try {
            MobileOperatingSystem mobileOperatingSystem = activeDevice.getOperatingSystem();
            if (mobileOperatingSystem.equals(MobileOperatingSystem.ANDROID)) {
                success = resetAndroidWifi();
            } else if (mobileOperatingSystem.equals(MobileOperatingSystem.IOS)) {
                success = resetIosWifi();
            } else {
                throw new RuntimeException(mobileOperatingSystem + " not supported in resetWlanOfActiveDevice()");
            }
        } catch (Exception e) {
            thrownException = e;
        }
        if (!success) {
            LOGGER.error("Failed to reset Wifi.", thrownException);
        }
        return success;
    }

    private boolean resetIosWifi() {

        closeApplication("com.apple.Preferences");
        launchApplication("com.apple.Preferences");
        NativeMobileGuiElement wifiButton = new NativeMobileGuiElement(By.xPath(".//*[@text='WLAN']"));
        wifiButton.setTimeoutInSeconds(5);
        if (wifiButton.waitForIsDisplayed()) {
            wifiButton.click();
            NativeMobileGuiElement backButton = new NativeMobileGuiElement(
                    By.xPath(".//*[@knownSuperClass='_UINavigationBarBackIndicatorView']"));
            backButton.setTimeoutInSeconds(5);
            if (backButton.waitForIsDisplayed()) {
                NativeMobileGuiElement wifiSwitch = new NativeMobileGuiElement(By.xPath(".//*[@class='UIASwitch']"));
                String value = wifiSwitch.getProperty("value");
                if ("1".equals(value)) {
                    wifiSwitch.click();
                    // wait a little for the switch animation to finish
                    TimerUtils.sleep(1000);
                }
                wifiSwitch.click();
                // wait some seconds so the device has the chance to reconnect to the wifi before continuing
                TimerUtils.sleep(4000);
                return true;
            }
        }
        return false;
    }

    private boolean resetAndroidWifi() {

        String packageName = "cloud:eu.tsystems.mms.tic.mdc.app.android/.HomeActivity";

        if (!this.seeTestClient().getInstalledApplications().contains("mdc.app.android")) {
            this.installApplication(packageName);
        }

        this.launchApplication(packageName);

        new NativeMobileGuiElement("xpath=//*[@contentDescription='Weitere Optionen']").click();
        // goto Connectivity Screen
        NativeMobileGuiElement connectivityEntry = new NativeMobileGuiElement(
                "xpath=//*[@text='Get connectivity info']");
        connectivityEntry.click();

        // click Info Button
        NativeMobileGuiElement connectionInfoButton = new NativeMobileGuiElement(
                "xpath=//*[@text='Check connectivity']");
        connectionInfoButton.click();

        // check info text
        NativeMobileGuiElement connectionInfoText = new NativeMobileGuiElement(
                "xpath=//*[@text=concat('Connected to WIFI ', '\"', 'MMSWPMC', '\"', '.\\nStatus: 200 OK')]");
        boolean connectionInfoVisible = connectionInfoText.waitForIsDisplayed();

        if (connectionInfoVisible) {
            return true;
        }

        NativeMobileGuiElement reconnectButton = new NativeMobileGuiElement("xpath=//*[@text='Reconnect']");
        reconnectButton.click();

        return connectionInfoText.waitForIsDisplayed();
    }

    public void publishScreenshotToReport(File screenshotFile, File visualDumpFile) {

        Screenshot screenshot = publishScreenshotToMethodContext(screenshotFile, visualDumpFile);
        TestStepController.addScreenshotsToCurrentAction(screenshot, null);
    }
}
