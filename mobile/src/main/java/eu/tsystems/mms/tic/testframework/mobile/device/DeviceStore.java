/*
 * Created on 10.05.2015
 *
 * Copyright(c) 2013 - 2013 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.device;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.AndFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.DeviceFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.DeviceNameFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.ManufacturerFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSVersionFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OrFilter;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to manage the handling of the TestDevice instances.
 *
 * @author rnhb
 */
public class DeviceStore {
    private Map<String, TestDevice> devices = new ConcurrentHashMap<>();
    private boolean isFilled;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Fills the device store with devices that are online in the cloud. Those devices could be reserved by others.
     * Devices that are offline in the cloud, will not be added. This can lead to inexact exceptions, so it is advised
     * to use this method after reading devices from a json File.
     * <p>
     * Will fill name, serialnumber, OS, OS-Version, model and manufacturer, if possible.
     *
     * @deprecated device store is filled when creating a new {@link MobileDriver} instance.
     */
    @Deprecated
    public void fillFromRegisteredTools() {

    }

    /**
     * Adds the Device to the store, organized by name. If there is already a device with that name, it is tried to
     * merge both. In case the existing device already contains values for a property, those will be overwritten.
     *
     * @param device The Device to add.
     */
    public void addDevice(TestDevice device) {
        String deviceName = device.getName();
        if (!devices.containsKey(deviceName)) {
            devices.put(deviceName, device);
        }
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled() {
        isFilled = true;
    }

    /**
     * Returns the device in the store with the given name.
     *
     * @param deviceName Device with that name will be returned.
     * @return TestDevice
     */
    public TestDevice getDevice(String deviceName) {
        deviceName = deviceName.replace("adb:", "").replace("ios_app:", "");
        if (!devices.containsKey(deviceName)) {
            throw new TesterraRuntimeException(
                    deviceName + " is not contained in DeviceStore. Make sure to add it first before " +
                            "trying to use it and that you use the correct name.");
        }
        return devices.get(deviceName);
    }

    /**
     * @deprecated not supported any more. DeviceStore is always filled from cloud devices.
     */
    @Deprecated
    public void readJsonFromResourceFile(String resourceFileName) {

    }

    private DeviceFilter getDeviceFilter(String filterProperty) {
        if (StringUtils.isEmpty(filterProperty)) {
            throw new TesterraRuntimeException("Property " + filterProperty
                    + " was not set when requesting to reserve a device by filter. This is not possible.");
        }
        String filter = PropertyManager.getProperty(filterProperty);
        if (StringUtils.isEmpty(filter)) {
            throw new TesterraRuntimeException("Property " + filterProperty + " was set but its value is \"" + filter
                    + "\". This should not happen.");
        }

        List<String> andFilterStrings = Arrays.asList(filter.split("&"));

        AndFilter andFilter = new AndFilter();

        andFilterStrings.forEach(andFilterString -> {
            OrFilter orFilter = new OrFilter();
            andFilter.addFilter(orFilter);

            String[] orExpressionWithCategory = andFilterString.toLowerCase().split("=");
            String filterCategory = orExpressionWithCategory[0].trim();
            String[] orFilterStrings = orExpressionWithCategory[1].split("\\|");
            for (String orFilterString : orFilterStrings) {
                orFilterString = orFilterString.trim().toLowerCase();
                switch (filterCategory) {
                    case "manufacturer":
                        orFilter.addFilter(new ManufacturerFilter(orFilterString));
                        break;
                    case "os.type":
                        orFilter.addFilter(new OSFilter(orFilterString));
                        break;
                    case "name":
                        orFilter.addFilter(new DeviceNameFilter(orFilterString));
                        break;
                    case "os.version":
                        orFilter.addFilter(new OSVersionFilter(orFilterString));
                        break;
                }
            }
        });


        return andFilter;
    }

    /**
     * Get a list of devices that match the filter stored in the given property.
     *
     * @param filterProperty Property that holds filter.
     * @return List of devices matching the filter.
     */
    public List<TestDevice> getTestDevicesByFilter(String filterProperty) {
        List<TestDevice> testDevices = new ArrayList<TestDevice>();
        for (String s : devices.keySet()) {
            if (s.equals("none")) {
                continue;
            }
            testDevices.add(devices.get(s));
        }
        DeviceFilter deviceFilter = getDeviceFilter(filterProperty);
        LOGGER.info("Using DeviceFilter " + deviceFilter);

        List<TestDevice> filteredDevices = deviceFilter.apply(testDevices);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Devices matching current filter:\n");
        for (TestDevice filteredDevice : filteredDevices) {
            stringBuilder.append(filteredDevice.toString());
            stringBuilder.append(" - ");
            stringBuilder.append(filteredDevice.getReservationStatus().toString());
            stringBuilder.append("\n");
        }
        LOGGER.info(stringBuilder.toString());
        return filteredDevices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isFilled) {
            StringBuilder sb = new StringBuilder();
            sb.append(devices.size()).append(" devices stored: ");
            devices.keySet().stream().forEach(name -> sb.append(name).append(", "));
            return sb.toString();
        } else {
            return "DeviceStore not filled yet!";
        }
    }
}
