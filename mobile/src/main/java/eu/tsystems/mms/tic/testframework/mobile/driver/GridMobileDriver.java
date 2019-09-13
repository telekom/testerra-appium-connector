package eu.tsystems.mms.tic.testframework.mobile.driver;

import com.experitest.client.GridClient;
import com.google.common.base.Stopwatch;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.info.ReportInfo;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceType;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

/**
 * Created by rnhb on 12.07.2017.
 */
public class GridMobileDriver extends BaseMobileDriver {

    private GridClient gridClient;

    public GridMobileDriver(GridClient gridClient) {
        this.gridClient = gridClient;
        fillDeviceStore(MobileDriverManager.deviceStore());
        if (PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, false)
                || PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, false)) {
            gridClient.enableVideoRecording();
        }
    }

    /**
     * Dev Commentary by rnhb:
     * This method is duplicated in both driver implementations because there are slight differences between grid and executor.
     * It is not refactored in a way to minimize LOC, but in a way to reduce coupling of both options through common functionality.
     * This is done because it is expected, that we are currently in a buggy situation and would build abstractions that fit the bugs, but won't fit when they get resolved.
     */
    private void fillDeviceStore(DeviceStore deviceStore) {
        if (deviceStore.isFilled()) {
            return;
        }

        String devicesInformationString = getDevicesInformation();
        LOGGER.info("Got the following devices from Grid: {}", devicesInformationString);
        Document jsoupDocument = XMLUtils.jsoup().createJsoupDocument(devicesInformationString);
        Elements deviceElements = jsoupDocument.getElementsByTag("device");
        for (Element deviceElement : deviceElements) {
            TestDevice.Builder builder = TestDevice.builder();
            Attributes attributes = deviceElement.attributes();
            if (attributes.hasKey("name")) {
                String deviceName = attributes.get("name");
                builder.name(deviceName);
                if (attributes.hasKey("manufacturer")) {
                    builder.manufacturer(attributes.get("manufacturer"));
                }
                if (attributes.hasKey("os")) {
                    String os = attributes.get("os");
                    builder.operatingSystem(MobileOperatingSystem.get(os));
                }
                if (attributes.hasKey("serialNumber")) {
                    builder.serialNumber(attributes.get("serialNumber"));
                }
                if (attributes.hasKey("version")) {
                    builder.operatingSystemVersion(attributes.get("version"));
                }
                if (attributes.hasKey("model")) {
                    builder.model(attributes.get("model"));
                }
                if (attributes.hasKey("category")) {
                    builder.deviceType(DeviceType.valueOfIfExisting(attributes.get("category")));
                }
                try {
                    TestDevice testDevice = builder.build();
                    deviceStore.addDevice(testDevice);
                    ReservationStatus reservationStatus = parseReservationStatus(deviceElement);
                    testDevice.setReservationStatus(reservationStatus);
                } catch (TesterraSystemException e) {
                    LOGGER.error("Failed to create testDevice from attributes " + attributes);
                }
            }
        }
        deviceStore.setFilled();
        LOGGER.info(deviceStore.toString());
    }

    @Override
    protected void handleDeviceReservation(TestDevice testDevice) {
        testDevice.setReservationStatus(ReservationStatus.ONLINE_RESERVED_BY_YOU);
        reservedDevices.add(testDevice);
        switchToDevice(testDevice);
    }

    @Override
    public boolean isValid() {
        return gridClient != null;
    }

    @Override
    protected void waitForDevice(TestDevice testDevice) {

        String query = "@name='" + testDevice.getName() + "'";
        int timeoutInSeconds = PropertyManager.getIntProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS, DefaultParameter.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS);
        int durationInMinutes = PropertyManager.getIntProperty(MobileProperties.MOBILE_DEVICE_GRID_RESERVATION_DURATION_IN_MINUTES, DefaultParameter.MOBILE_DEVICE_GRID_RESERVATION_DURATION_IN_MINUTES);

        final String projectName = PropertyManager.getProperty(MobileProperties.MOBILE_GRID_PROJECT, "T");
        final MethodContext currentMethodContext = ExecutionContextController.getCurrentMethodContext();

        String testName;
        if (currentMethodContext == null) {
            testName = projectName + " Test";
        } else {
            testName = currentMethodContext.getName();
        }

        int maximumLockingAttempts = Math.max(1, 1 + PropertyManager.getIntProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_RETRIES, 1));
        LOGGER.info("Trying to lock device with " + query + " for grid execution of " + testName + ". Timeout: "
                + timeoutInSeconds + "s. Duration: " + durationInMinutes + "m. Maxmimal locking attempts: "
                + (maximumLockingAttempts));
        for (int i = 1; i <= maximumLockingAttempts; i++) {
            LOGGER.info("Lock attempt " + i + " of " + maximumLockingAttempts + ".");
            try {
                seeTestClient = gridClient.lockDeviceForExecution(testName, query, durationInMinutes, TimeUnit.SECONDS.toMillis(timeoutInSeconds));
                if (seeTestClient == null) {
                    LOGGER.info("SeeTestClient generated in lock attempt is null.");
                } else {
                    handleSeeTestClientCreation();
                    return;
                }
            } catch (Exception e) {
                LOGGER.error("Got Exception on lock device.", e);
            }
        }
        throw new RuntimeException("Failed to lock device and create a valid SeeTestClient.");
    }

    @Override
    public void switchToDevice(TestDevice testDevice) {
        if (testDevice == null) {
            throw new TesterraRuntimeException("TestDevice cannot be null!");
        }

        if (testDevice == activeDevice) {
            LOGGER.info("Switch to Device " + testDevice + ", not necessary, already active.");
            return;
        }

        if (!reservedDevices.contains(testDevice)) {
            throw new TesterraRuntimeException("Cannot switch to Device " + testDevice +
                    ". It was not successfully reserved before.");
        }

        LOGGER.info("Switching to Device " + testDevice + ".");
        Stopwatch started = Stopwatch.createStarted();

        activeDevice = testDevice;

        String deviceName = activeDevice.getName();
        if (!reportedDeviceNames.contains(deviceName)) {
            String propertySuffix = "";
            if (activeDeviceIndex >= 1) {
                propertySuffix = "_" + (activeDeviceIndex + 1);
            }
            activeDeviceIndex++;
            String osString = activeDevice.getOperatingSystem().toString() + " " + activeDevice.getOperatingSystemVersion();
            ReportInfo.getRunInfo().addInfo("Device:" + propertySuffix, deviceName + " " + osString);

            reportedDeviceNames.add(deviceName);
        }
        ScreenshotTracker.setDevice(testDevice);
        activeDevice.setActive(this);
        PageFactory.setGlobalPagesPrefix(activeDevice.getOperatingSystem().getClassPrefix());
        LOGGER.info("Switched to Device " + testDevice + ". Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
    }

    @Override
    public void release() {
        if (seeTestClient != null) {
            releaseAllDevices();
        }
    }

    @Override
    protected ReservationStatus parseReservationStatus(Element deviceInfo) {
        if (deviceInfo == null || "offline".equals(deviceInfo.attr("status"))) {
            return ReservationStatus.OFFLINE;
        } else {
            if ("true".equals(deviceInfo.attr("reservedtoyou"))) {
                return ReservationStatus.ONLINE_RESERVED_BY_YOU;
            } else {
                if ("unreserved online".equals(deviceInfo.attr("status"))) {
                    return ReservationStatus.ONLINE_UNRESERVED;
                } else {
                    return ReservationStatus.ONLINE_RESERVED_BY_OTHER;
                }
            }
        }
    }

    @Override
    public void releaseDevice(TestDevice testDevice) {
        String s = gridClient.releaseDeviceFromExecution(seeTestClient);
        seeTestClient = null;
        activeDevice = null;

        reservedDevices.remove(testDevice);
        if (reservedDevices.isEmpty()) {
            ScreenshotTracker.stopTracking();
        }

        LOGGER.info("Released device and client: " + s);
    }

    @Override
    public String getDevicesInformation() {
        return gridClient.getDevicesInformation();
    }
}
