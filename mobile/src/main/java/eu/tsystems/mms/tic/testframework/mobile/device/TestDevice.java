package eu.tsystems.mms.tic.testframework.mobile.device;

import eu.tsystems.mms.tic.testframework.exceptions.FennecSystemException;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;

/**
 * Created by rnhb on 17.04.2015.
 */
public class TestDevice {

    public static final TestDevice NONE = TestDevice.builder("none", MobileOperatingSystem.ANDROID).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDevice.class);

    private String serialNumber;
    private String name;
    private MobileOperatingSystem operatingSystem;
    private ViewOrientation viewOrientation;
    private String phoneNumber;
    private HashMap<String, Object> properties;
    private String model;
    private String operatingSystemVersion;
    private String manufacturer;
    private String simCardName;
    private DeviceType deviceType;
    private int pixelsX;
    private int pixelsY;

    private ReservationStatus reservationStatus = ReservationStatus.UNKNOWN;

    private TestDevice(String name, MobileOperatingSystem mobileOperatingSystem) {
        properties = new HashMap<String, Object>();
        this.name = name;
        this.operatingSystem = mobileOperatingSystem;
    }

    private TestDevice() {
        properties = new HashMap<String, Object>();
    }

    /**
     * Name and OS mandatory!
     *
     * @return Builder for TestDevice
     */
    public static Builder builder() {
        return new TestDevice.Builder();
    }

    public static TestDevice.Builder builder(String name, MobileOperatingSystem mobileOperatingSystem) {
        return new TestDevice.Builder(name, mobileOperatingSystem);
    }

    public TestDevice.Builder buildAdditional() {
        return new TestDevice.Builder(this);
    }

    public synchronized void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public synchronized ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public static class Builder {
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private TestDevice testDevice;

        private Builder(String name, MobileOperatingSystem mobileOperatingSystem) {
            this.testDevice = new TestDevice(name, mobileOperatingSystem);
        }

        private Builder(TestDevice testDevice) {
            this.testDevice = testDevice;
        }

        public Builder() {
            this.testDevice = new TestDevice();
        }

        public Builder serialNumber(String serialNumber) {
            testDevice.serialNumber = serialNumber;
            return this;
        }

        public Builder name(String name) {
            testDevice.name = name;
            return this;
        }

        public Builder simCardName(String simCardName) {
            testDevice.simCardName = simCardName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            testDevice.phoneNumber = phoneNumber;
            return this;
        }

        public Builder viewOrientation(ViewOrientation viewOrientation) {
            testDevice.viewOrientation = viewOrientation;
            return this;
        }

        public Builder operatingSystem(MobileOperatingSystem operatingSystem) {
            testDevice.operatingSystem = operatingSystem;
            return this;
        }

        public Builder operatingSystemVersion(String operatingSystemVersion) {
            testDevice.operatingSystemVersion = operatingSystemVersion;
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            testDevice.manufacturer = manufacturer;
            return this;
        }

        public Builder deviceType(DeviceType deviceType) {
            testDevice.deviceType = deviceType;
            return this;
        }

        public Builder model(String model) {
            testDevice.model = model;
            return this;
        }

        public Builder reservationStatus(ReservationStatus reservationStatus) {
            this.testDevice.reservationStatus = reservationStatus;
            return this;
        }

        public Builder set(HashMap<DeviceInfo, String> data) {
            //check if key is in data
            if (data.containsKey(DeviceInfo.SERIALNUMBER)) {
                testDevice.serialNumber = data.get(DeviceInfo.SERIALNUMBER);
                data.remove(DeviceInfo.SERIALNUMBER);
            }
            if (data.containsKey(DeviceInfo.NAME)) {
                testDevice.name = data.get(DeviceInfo.NAME);
                data.remove(DeviceInfo.NAME);
            }
            if (data.containsKey(DeviceInfo.PHONENUMBER)) {
                testDevice.phoneNumber = data.get(DeviceInfo.PHONENUMBER);
                data.remove(DeviceInfo.PHONENUMBER);
            }
            if (data.containsKey(DeviceInfo.VIEWORIENTATION)) {
                testDevice.viewOrientation = ViewOrientation.valueOf(data.get(DeviceInfo.VIEWORIENTATION).toUpperCase());
                data.remove(DeviceInfo.VIEWORIENTATION);
            }
            if (data.containsKey(DeviceInfo.OPERATINGSYSTEM)) {
                testDevice.operatingSystem = MobileOperatingSystem.valueOf(data.get(DeviceInfo.OPERATINGSYSTEM).toUpperCase());
                data.remove(DeviceInfo.OPERATINGSYSTEM);
            }
            if (data.containsKey(DeviceInfo.OPERATINGSYSTEMVERSION)) {
                testDevice.operatingSystemVersion = data.get(DeviceInfo.OPERATINGSYSTEMVERSION);
                data.remove(DeviceInfo.OPERATINGSYSTEMVERSION);
            }
            if (data.containsKey(DeviceInfo.MANUFACTURER)) {
                testDevice.manufacturer = data.get(DeviceInfo.MANUFACTURER);
                data.remove(DeviceInfo.MANUFACTURER);
            }
            if (data.containsKey(DeviceInfo.MODEL)) {
                testDevice.model = data.get(DeviceInfo.MODEL);
                data.remove(DeviceInfo.MODEL);
            }
            if (data.containsKey(DeviceInfo.SIMCARDNAME)) {
                testDevice.simCardName = data.get(DeviceInfo.SIMCARDNAME);
                data.remove(DeviceInfo.SIMCARDNAME);
            }
            if (data.containsKey(DeviceInfo.DEVICE_TYPE)) {
                testDevice.deviceType = DeviceType.valueOf(data.get(DeviceInfo.DEVICE_TYPE));
                data.remove(DeviceInfo.DEVICE_TYPE);
            } else {
                testDevice.deviceType = null;
            }
            // checks if a key in deviceInfo isn't save in TestDevice
            if (data.size() > 0) {
                String unusedKeys = "";
                for (DeviceInfo s : data.keySet()) {
                    unusedKeys += s.getInfo() + ", ";
                }
                LOGGER.warn("A HashMap was passed to the builder, but the" +
                        " following keys were not understood/implemented: " + unusedKeys);
            }
            return this;
        }

        public TestDevice build() {
            // make sure that the device is not modified after building
            if (testDevice.name == null || testDevice.operatingSystem == null) {
                throw new FennecSystemException("Name and operatingSystem of TestDevice have to be set.");
            }
            TestDevice deviceToReturn = testDevice;
            testDevice = null;
            return deviceToReturn;
        }
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * The Name that is chosen in SeeTest (without the operating system prefix).
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public String getNameWithOsPrefix() {
        return operatingSystem.getAppPrefix() + name;
    }

    /**
     * This should not be used for now, because it is not reliable. TODO
     *
     * @return view orientation of the device
     */
    @Deprecated
    public ViewOrientation getViewOrientation() {
        return viewOrientation;
    }

    public void setViewOrientation(ViewOrientation viewOrientation) {
        this.viewOrientation = viewOrientation;
    }

    @Override
    public String toString() {
        String serialNumberString = serialNumber != null ? "[" + serialNumber + "]" : "";
        return name + serialNumberString;
    }

    public MobileOperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getModel() {
        return model;
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * Property container to allow adding properties to the devices, that were not originally planned.
     *
     * @param propertyName propertyName
     * @param property     property
     */
    public void addProperty(String propertyName, Object property) {
        properties.put(propertyName, property);
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public TestDevice mergeWith(TestDevice otherTestDevice) {
        Builder builder = TestDevice.builder();
        builder.serialNumber(mergeProperty(serialNumber, otherTestDevice.serialNumber));
        builder.phoneNumber(mergeProperty(phoneNumber, otherTestDevice.phoneNumber));
        builder.model(mergeProperty(model, otherTestDevice.model));
        builder.operatingSystemVersion(mergeProperty(operatingSystemVersion, otherTestDevice.operatingSystemVersion));
        builder.manufacturer(mergeProperty(manufacturer, otherTestDevice.manufacturer));
        builder.operatingSystem(mergeProperty(operatingSystem, otherTestDevice.operatingSystem));
        builder.viewOrientation(mergeProperty(viewOrientation, otherTestDevice.viewOrientation));
        builder.deviceType(mergeProperty(deviceType, otherTestDevice.deviceType));
        builder.name(mergeProperty(name, otherTestDevice.name));
        builder.reservationStatus(mergeProperty(reservationStatus, otherTestDevice.reservationStatus));
        TestDevice mergedDevice = builder.build();

        for (String propertyName : properties.keySet()) {
            Object thisProperty = properties.get(propertyName);
            mergedDevice.addProperty(propertyName, thisProperty);
        }

        for (String propertyName : otherTestDevice.properties.keySet()) {
            if (!properties.containsKey(propertyName)) {
                Object otherProperty = otherTestDevice.properties.get(propertyName);
                mergedDevice.addProperty(propertyName, otherProperty);
            }
        }

        return mergedDevice;
    }

    private <T> T mergeProperty(T s1, T s2) {
        if (s1 != null) {
            if (s2 != null) {
                if (!s1.equals(s2)) {
                    LOGGER.warn("Conflict when merging TestDevices. The value \"" + s2 + "\" was already set, but will now " +
                            "be overwritten by \"" + s1 + "\".");
                }
            }
            return s1;
        } else if (s2 != null) {
            return s2;
        } else {
            return null;
        }
    }

    public void setActive(MobileDriver mobileDriver) {
        if (pixelsX == 0) {
            pixelsX = mobileDriver.seeTestClient().p2cx(100);
            pixelsY = mobileDriver.seeTestClient().p2cy(100);
        }
    }

    public int getPixelsX() {
        return pixelsX;
    }

    public int getPixelsY() {
        return pixelsY;
    }
}
