package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.AndFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.DeviceNameFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.ManufacturerFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OSVersionFilter;
import eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter.OrFilter;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class DeviceTest extends AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileGuiElement.class);

    private String testAppPackage;
    private String pathToTestApp;

    private final String PROJECT_BASE_DIRECTORY = "C:\\BuildAgent\\work\\mobile_systemtest\\base";

    private final String PATH_TO_PICTURE = "C:\\BuildAgent\\work\\mobile_systemtest\\res\\img\\simulate_capture.jpg";


    public DeviceTest(MobileOperatingSystem operatingSystem) {

        System.setProperty("tt.mobile.device.filter", "os.type=" + operatingSystem.getClassPrefix());
    }

    @BeforeGroups(groups = {Groups.SINGLE})
    public void getDriverAndReserveDevice() throws DeviceNotAvailableException {

        MobileDriverManager.getMobileDriver().reserveDeviceByFilter();
    }

    @BeforeGroups(groups = {Groups.DRIVER, Groups.GUIELEMENTS, Groups.SEETEST})
    public void reserveDeviceOnce() throws DeviceNotAvailableException {

        MobileDriverManager.getMobileDriver().reserveDeviceByFilter();
    }

    @AfterGroups(groups = {Groups.SINGLE})
    public void releaseAfterSingleTest() {

        MobileDriverManager.getMobileDriver().releaseAllDevices();
    }

    @AfterGroups(groups = {Groups.DRIVER, Groups.GUIELEMENTS, Groups.SEETEST})
    public void releaseDeviceOnce() throws DeviceNotAvailableException {

        MobileDriverManager.getMobileDriver().releaseAllDevices();
    }

    @AfterMethod
    public void sleep() {

        TimerUtils.sleep(5 * 1000);
    }

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

    public List<TestDevice> applyOSFilter(List<TestDevice> testDevices) {
        // os.type=iOS
        OSFilter osFilter = new OSFilter("iOS");
        List<TestDevice> filteredDevices = osFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two Devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");

        return filteredDevices;
    }

    public List<TestDevice> applyOSVersionFilter(List<TestDevice> testDevices) {
        // os.verion=9.0
        OSVersionFilter osVersionFilter = new OSVersionFilter("9.0");
        List<TestDevice> filteredDevices = osVersionFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 1, "More than one testDevice in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)), "Result does not match filter.");

        return filteredDevices;
    }

    public List<TestDevice> applyManufacturerFilter(List<TestDevice> testDevices) {
        // manufacturer=Apple
        ManufacturerFilter manufacturerFilter = new ManufacturerFilter("Apple");
        List<TestDevice> filteredDevices = manufacturerFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");

        return filteredDevices;
    }

    public List<TestDevice> applyDeviceNameFilter(List<TestDevice> testDevices) {
        // name=nexus_5
        DeviceNameFilter deviceNameFilter = new DeviceNameFilter("nexus_5");
        List<TestDevice> filteredDevices = deviceNameFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 1, "More than one testDevice in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(0)), "Result does not match filter.");

        return filteredDevices;
    }

    public List<TestDevice> applyOrFilter(List<TestDevice> testDevices) {
        // os.version=6.0.1|9.0
        OSVersionFilter osVersionFilter1 = new OSVersionFilter("6.0.1");
        OSVersionFilter osVersionFilter2 = new OSVersionFilter("9.0");

        OrFilter orFilter = new OrFilter();
        orFilter.addFilter(osVersionFilter1);
        orFilter.addFilter(osVersionFilter2);
        List<TestDevice> filteredDevices = orFilter.apply(testDevices);

        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(0)) && filteredDevices.contains(testDevices.get(1)), "Result does not match filter.");

        return filteredDevices;
    }

    public List<TestDevice> applyAndFilter(List<TestDevice> testDevices) {
        // manufacturer=Apple&os.type=iOS
        OSFilter osFilter = new OSFilter("iOS");
        ManufacturerFilter manufacturerFilter = new ManufacturerFilter("Apple");

        AndFilter andFilter = new AndFilter();
        andFilter.addFilter(osFilter);
        andFilter.addFilter(manufacturerFilter);

        List<TestDevice> filteredDevices = andFilter.apply(testDevices);
        Assert.assertTrue(filteredDevices.size() == 2, "More than two devices in filtered list.");
        Assert.assertTrue(filteredDevices.contains(testDevices.get(1)) && filteredDevices.contains(testDevices.get(2)), "Result does not match filter.");

        return filteredDevices;
    }

    @Test(groups = {Groups.BASIC})
    public void testT52_DeviceStore_getTestDevicesByFilter() {

        DeviceStore deviceStore = new DeviceStore();
        Comparator<TestDevice> comparator = new Comparator<TestDevice>() {
            @Override
            public int compare(TestDevice td1, TestDevice td2) {

                return td1.getName().compareTo(td2.getName());
            }
        };
        List<TestDevice> testDevices = createTestDevices();
        List<TestDevice> filteredDevices = new ArrayList<>();

        for (TestDevice t : testDevices) {
            deviceStore.addDevice(t);
        }

        System.setProperty("tt.mobile.device.filter", "manufacturer=Apple&os.type=iOS");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByAnd = applyAndFilter(testDevices);
        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByAnd, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByAnd, "List of filtered devices by DeviceStore function does not match AndFilter.");


        System.setProperty("tt.mobile.device.filter", "os.version=6.0.1|9.0");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByOr = applyOrFilter(testDevices);

        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByOr, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByOr, "List of filtered devices by DeviceStore function does not match OrFilter.");

        System.setProperty("tt.mobile.device.filter", "name=nexus_5");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByName = applyDeviceNameFilter(testDevices);

        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByName, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByName, "List of filtered devices by DeviceStore function does not match DeviceNameFilter.");

        System.setProperty("tt.mobile.device.filter", "manufacturer=Apple");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByManufacturer = applyManufacturerFilter(testDevices);

        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByManufacturer, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByManufacturer, "List of filtered devices by DeviceStore function does not match ManufacturerFilter.");

        System.setProperty("tt.mobile.device.filter", "os.version=9.0");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByOSVersion = applyOSVersionFilter(testDevices);

        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByOSVersion, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByOSVersion, "List of filtered devices by DeviceStore function does not match OSVersionFilter.");

        System.setProperty("tt.mobile.device.filter", "os.type=iOS");
        filteredDevices = deviceStore.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER);
        List<TestDevice> filteredDevicesByOS = applyOSFilter(testDevices);

        Collections.sort(filteredDevices, comparator);
        Collections.sort(filteredDevicesByOS, comparator);

        Assert.assertEquals(filteredDevices, filteredDevicesByOS, "List of filtered devices by DeviceStore function does not match OSFilter.");
    }
}