package eu.tsystems.mms.tic.testframework.mobile.driver;

import com.experitest.client.Client;
import com.experitest.client.InternalException;
import com.google.common.base.Stopwatch;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceType;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import eu.tsystems.mms.tic.testframework.utils.Timer;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

//        TODO rework with jfennec
//        import eu.tsystems.mms.tic.testframework.report.info.ReportInfo;

/**
 * Created by rnhb on 12.07.2017.
 */
public class ExecutorMobileDriver extends BaseMobileDriver {

    public ExecutorMobileDriver(Client seeTestClient) {
        this.seeTestClient = seeTestClient;
        handleSeeTestClientCreation();

        fillDeviceStore(MobileDriverManager.deviceStore());

        String projectDirectoryProperty = PropertyManager.getProperty(MobileProperties.MOBILE_PROJECT_DIR, null);
        if (!StringUtils.isEmpty(projectDirectoryProperty)) {
            seeTestClient().setProjectBaseDirectory(projectDirectoryProperty);
        }
    }

    @Override
    public void release() {
        if (seeTestClient != null) {
            releaseAllDevices();
            seeTestClient().releaseClient();
            seeTestClient = null;
        }
    }

    @Override
    public boolean isValid() {
        return seeTestClient() != null;
    }

    @Override
    public String getDevicesInformation() {
        return seeTestClient.getDevicesInformation();
    }

    private void waitForDeviceIsConnected(final TestDevice testDevice) throws DeviceNotAvailableException {
        Timer.Sequence<Object> sequence = new Timer.Sequence<Object>() {
            @Override
            public void run() throws Throwable {
                String connectedDevices = seeTestClient().getConnectedDevices();
                if (!connectedDevices.contains(testDevice.getName())) {
                    LOGGER.warn(testDevice + " not connected. Connected Devices: " + connectedDevices);
                    throw new DeviceNotAvailableException(testDevice,
                            "Device is not connected after reservation attempt. Connected Devices: " + connectedDevices);
                }
            }
        };
        sequence.setSkipThrowingException(true);
        int timeoutInSeconds = PropertyManager.getIntProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS, DefaultParameter.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS);
        LOGGER.info("Checking if " + testDevice + " is actually connected after reservation, timeout is " + timeoutInSeconds + " seconds.");
        new Timer(5000, 1000 * timeoutInSeconds).executeSequence(sequence);
        LOGGER.info(testDevice + " is connected after reservation..");
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
                if (attributes.hasKey("manufacture")) {
                    builder.manufacturer(attributes.get("manufacture"));
                }
                if (attributes.hasKey("os")) {
                    String os = attributes.get("os");
                    builder.operatingSystem(MobileOperatingSystem.get(os));
                }
                if (attributes.hasKey("serialNumber")) {
                    builder.serialNumber(attributes.get("serialNumber"));
                }
                if (attributes.hasKey("versionnumber")) {
                    builder.operatingSystemVersion(attributes.get("versionnumber"));
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
    protected void handleDeviceReservation(TestDevice testDevice) throws DeviceNotAvailableException {
        waitForDeviceIsConnected(testDevice);
        testDevice.setReservationStatus(ReservationStatus.ONLINE_RESERVED_BY_YOU);
        reservedDevices.add(testDevice);
        switchToDevice(testDevice);
    }

    @Override
    protected ReservationStatus parseReservationStatus(Element correctDeviceInfo) {
        if (correctDeviceInfo == null) {
            return ReservationStatus.OFFLINE;
        } else {
            String reservedToYou = correctDeviceInfo.attr("reservedToYou");
            if ("".equals(reservedToYou) || "true".equals(reservedToYou)) {
                return ReservationStatus.ONLINE_RESERVED_BY_YOU;
            } else {
                String status = correctDeviceInfo.attr("status");
                boolean unreserved = status.contains("unreserved");
                boolean reserved = status.contains("reserved");
                if (unreserved) {
                    return ReservationStatus.ONLINE_UNRESERVED;
                } else {
                    if (reserved) {
                        return ReservationStatus.ONLINE_RESERVED_BY_OTHER;
                    } else {
                        return ReservationStatus.UNKNOWN;
                    }
                }
            }
        }
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

        String testDeviceNameWithOsPrefix = testDevice.getNameWithOsPrefix();
        seeTestClient().setDevice(testDeviceNameWithOsPrefix);

        activeDevice = testDevice;

        String deviceName = activeDevice.getName();
        if (!reportedDeviceNames.contains(deviceName)) {
            String propertySuffix = "";
            if (activeDeviceIndex >= 1) {
                propertySuffix = "_" + (activeDeviceIndex + 1);
            }
            activeDeviceIndex++;
            String osString = activeDevice.getOperatingSystem().toString() + " " + activeDevice.getOperatingSystemVersion();
            //        TODO rework with jfennec
            //        ReportInfo.getRunInfo().addInfo("Device:" + propertySuffix, deviceName + " " + osString);

            reportedDeviceNames.add(deviceName);
        }
        ScreenshotTracker.setDevice(testDevice);
        activeDevice.setActive(this);
        PageFactory.setGlobalPagesPrefix(activeDevice.getOperatingSystem().getClassPrefix());
        LOGGER.info("Switched to Device " + testDevice + ". Took " + started.elapsed(TimeUnit.SECONDS) + "s.");
        if (openReflectionScreen) {
            try {
                openDevice();
            } catch (InternalException e) {
                LOGGER.warn("Failed to open device " + testDevice + " reflection screen after trying to reserve it.", e);
            }
        }
    }

    @Override
    protected void waitForDevice(TestDevice testDevice) {
        String query = "@name='" + testDevice.getName() + "'";
        int timeoutInSeconds = PropertyManager.getIntProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS, DefaultParameter.MOBILE_DEVICE_RESERVATION_TIMEOUT_IN_SECONDS);
        LOGGER.info("Wait for Device by SeeTest Filter \"" + query + "\" with " + timeoutInSeconds + " seconds timeout.");
        String reservedDevice = "none";
        try {
            reservedDevice = seeTestClient.waitForDevice(query, timeoutInSeconds * 1000);
        } catch (Exception e) {
            LOGGER.error("SeeTest threw Exception on device reservation. Might still have been successful, will try to continue.", e);
        }
        LOGGER.info("Finished wait for Device. Reserved: " + reservedDevice);
    }

    @Override
    public void releaseDevice(TestDevice testDevice) {
        if (testDevice == null) {
            throw new TesterraRuntimeException("TestDevice cannot be null!");
        }

        try {
            LOGGER.info("Trying to release Device " + testDevice);
            /**
             https://docs.experitest.com/display/public/SA/ReleaseDevice
             ReleaseAgent: Deprecated Boolean parameter. If set to true or false will release the execution agent the device was using.
             RemoveFromDeviceList: Boolean parameter. If set to true, will remove the device from the connected devices menu.
             ReleaseFromCloud: Boolean parameter. If set to true, will release the device from the current user in the Cloud.
             */

            // release will only work for the currently active device, so a switch is necessary
            switchToDevice(testDevice);
            seeTestClient().releaseDevice(testDevice.getNameWithOsPrefix(), true, true, true);
            if (testDevice == activeDevice) {
                activeDevice = null;
            }
        } catch (Exception e) {
            LOGGER.warn("Releasing Device " + testDevice + " failed. Maybe it was not reserved.");
        }

        reservedDevices.remove(testDevice);
        if (reservedDevices.isEmpty()) {
            ScreenshotTracker.stopTracking();
        }
    }
}
