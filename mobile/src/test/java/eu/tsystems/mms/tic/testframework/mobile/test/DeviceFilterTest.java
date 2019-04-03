package eu.tsystems.mms.tic.testframework.mobile.test;

import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.AndFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.DeviceNameFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.ManufacturerFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSVersionFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OrFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rnhb on 23.11.2016.
 */
public class DeviceFilterTest {

    private final List<TestDevice> testDevices = Collections.unmodifiableList(createTestDevices());

    private List<TestDevice> createTestDevices() {
        List<TestDevice> testDevices = new ArrayList<TestDevice>();
        TestDevice.Builder builder1 = TestDevice.builder();
        builder1.operatingSystem(MobileOperatingSystem.ANDROID);
        builder1.name("nexus_5");
        builder1.manufacturer("LG");
        builder1.operatingSystemVersion("6.0.1");
        TestDevice testDevice1 = builder1.build();
        testDevices.add(testDevice1);

        TestDevice.Builder builder2 = TestDevice.builder();
        builder2.operatingSystem(MobileOperatingSystem.IOS);
        builder2.name("iPhone_5");
        builder2.manufacturer("Apple");
        builder2.operatingSystemVersion("9.0");
        TestDevice testDevice2 = builder2.build();
        testDevices.add(testDevice2);

        TestDevice.Builder builder3 = TestDevice.builder();
        builder3.operatingSystem(MobileOperatingSystem.IOS);
        builder3.name("iPhone_6");
        builder3.manufacturer("Apple");
        builder3.operatingSystemVersion("9.1");
        TestDevice testDevice3 = builder3.build();
        testDevices.add(testDevice3);

        return testDevices;
    }

    @Test
    public void applyOSFilter() {
        OSFilter osFilter = new OSFilter("iOS");
        List<TestDevice> filteredDevices = osFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two Devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");
    }

    @Test
    public void applyOSVersionFilter() {
        OSVersionFilter osVersionFilter = new OSVersionFilter("9.0");
        List<TestDevice> filteredDevices = osVersionFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 1, "More than one testDevice in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)), "Result does not match filter.");
    }

    @Test
    public void applyManufacturerFilter() {
        ManufacturerFilter manufacturerFilter = new ManufacturerFilter("Apple");
        List<TestDevice> filteredDevices = manufacturerFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");
    }

    @Test
    public void applyDeviceNameFilter() {
        DeviceNameFilter deviceNameFilter = new DeviceNameFilter("nexus_5");
        List<TestDevice> filteredDevices = deviceNameFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 1, "More than one testDevice in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(0)), "Result does not match filter.");
    }

    @Test
    public void applyOrFilter() {
        List<TestDevice> testDevices = createTestDevices();
        OSVersionFilter osVersionFilter1 = new OSVersionFilter("6.0.1");
        OSVersionFilter osVersionFilter2 = new OSVersionFilter("9.0");

        OrFilter orFilter = new OrFilter();
        orFilter.addFilter(osVersionFilter1);
        orFilter.addFilter(osVersionFilter2);
        List<TestDevice> filteredDevices = orFilter.apply(testDevices);

        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(0)) && filteredDevices.contains(testDevices.get(1)), "Result does not match filter.");
    }

    @Test
    public void applyAndFilter() {
        OSFilter osFilter = new OSFilter("iOS");
        ManufacturerFilter manufacturerFilter = new ManufacturerFilter("Apple");

        AndFilter andFilter = new AndFilter();
        andFilter.addFilter(osFilter);
        andFilter.addFilter(manufacturerFilter);

        List<TestDevice> filteredDevices = andFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");
    }
}
