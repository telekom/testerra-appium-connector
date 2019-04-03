/*
 * Created on 31.01.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.experitest.client.Client;
import com.experitest.client.GridClient;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.DeviceTest;
import eu.tsystems.mms.tic.testframework.mobile.driver.GridMobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;

/**
 * BaseMobileDriverTest
 * 
 * @author sepr
 */
public class GridMobileDriverTest extends AbstractMockTest {

    @Test
    public void testGetDevicesInformation() throws Exception {
        GridClient gridClient = Mockito.mock(GridClient.class);
        Document document = XMLUtils.jdom()
                .createDocumentFromFile(getResourceFile("testdata/griddevices.xml").getAbsolutePath());
        String deviceString = new XMLOutputter().outputString(document);
        when(gridClient.getDevicesInformation()).thenReturn(
                deviceString);
        DeviceStore deviceStore = MobileDriverManager.deviceStore();
        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        Assert.assertTrue(deviceStore.isFilled());
        Assert.assertNotNull(deviceStore.getDevice("device 1"));
        Assert.assertEquals(mobileDriver.getDevicesInformation(), deviceString);
    }

    @Test
    public void testIsValid() throws Exception {

        GridMobileDriver mobileDriver = new GridMobileDriver(null);
        Assert.assertFalse(mobileDriver.isValid());

        GridClient gridClient = getMockedGridClientWithReservation("griddevices.xml");

        GridMobileDriver mobileDriverValid = new GridMobileDriver(gridClient);
        Assert.assertTrue(mobileDriverValid.isValid());
    }

    @Test
    public void testReserveByFilter() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        TestDevice testDevice = mobileDriver.reserveDeviceByFilter();
        Assert.assertEquals(testDevice.getName(), "device 1");
    }

    @Test(expectedExceptions = DeviceNotAvailableException.class)
    public void testReserveByFilterWithDeviceTest() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.registerDeviceTest(new DeviceTest("failed") {
            @Override
            public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {
                return false;
            }
        });
        mobileDriver.reserveDeviceByFilter();
    }

    @Test(expectedExceptions = DeviceNotAvailableException.class)
    public void testReserveByFilterWithDeviceTestException() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.registerDeviceTest(new DeviceTest("failed") {
            @Override
            public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {
                throw new Exception("bla");
            }
        });
        TestDevice testDevice = mobileDriver.reserveDeviceByFilter();
        Assert.assertEquals(testDevice.getName(), "device 1");
        Assert.assertNull(mobileDriver.getActiveDevice());
    }

    @Test
    public void testReserveByFilterNoDeviceTests() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.clearDeviceTests();
        mobileDriver.reserveDeviceByFilter();
    }

    @Test(expectedExceptions = DeviceNotAvailableException.class)
    public void testReserveByFilterLockFailed() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        when(gridClient.lockDeviceForExecution(anyString(), Mockito.contains("device 1"), anyInt(), anyLong()))
                .thenReturn(null);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.reserveDeviceByFilter();
    }

    @Test(expectedExceptions = DeviceNotAvailableException.class)
    public void testReserveByFilterNoDevice() throws JDOMException, IOException {
        System.setProperty("my.stupid.filter", "os.type=android");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);

        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.reserveDeviceByFilter("my.stupid.filter");
    }

    @Test
    public void testReserveByFilterNoShuffleMonitor() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        System.setProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_RANDOMIZE_ORDER, "false");
        System.setProperty(MobileProperties.MOBILE_MONITORING_ACTIVE, "true");
        GridClient gridClient = getMockedGridClientWithReservation("griddevices.xml");
        Client seetestClient = PowerMockito.mock(Client.class);

        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        TestDevice testDevice = mobileDriver.reserveDeviceByFilter();
        Assert.assertTrue(testDevice.getName().startsWith("device"));
    }
}
