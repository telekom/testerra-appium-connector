/*
 * Created on 07.02.2018
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jdom2.JDOMException;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.experitest.client.Client;
import com.experitest.client.GridClient;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceLog;
import eu.tsystems.mms.tic.testframework.mobile.driver.GridMobileDriver;

/**
 * BaseMobileDriverTest
 * 
 * @author sepr
 */
public class BaseMobileDriverTest extends AbstractMockTest {
    @Test
    public void testGetDeviceLogLocal() throws JDOMException, IOException {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);
        File deviceLogFile = getResourceFile("testdata/device.log");
        when(seetestClient.getDeviceLog()).thenReturn(deviceLogFile.getAbsolutePath());

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.reserveDeviceByFilter();
        DeviceLog deviceLog = mobileDriver.getDeviceLog();
        Assert.assertTrue(deviceLog.containsEntry("first"));
        Assert.assertFalse(deviceLog.containsEntry(""));

        Assert.assertTrue(deviceLog.hasEntryMatching("[a-z]+"));
        Assert.assertFalse(deviceLog.hasEntryMatching("[0-9]+"));
    }

    @Test
    public void testGetDeviceLogRemote() throws Exception {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=ios");
        GridClient gridClient = getMockedGridClientWithReservation("singledevice.xml");
        Client seetestClient = PowerMockito.mock(Client.class);
        when(gridClient.lockDeviceForExecution(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(seetestClient);
        String logFile = "testdata/device.log";
        File deviceLogFile = getResourceFile(logFile);
        when(seetestClient.getDeviceLog()).thenReturn(logFile);

        Mockito.doAnswer((Answer) invocation -> {
            String target = invocation.getArgument(2);
            Files.copy(deviceLogFile.toPath(), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
            return null;
        }).when(seetestClient).getRemoteFile(anyString(), anyInt(), anyString());

        GridMobileDriver mobileDriver = new GridMobileDriver(gridClient);
        mobileDriver.reserveDeviceByFilter();
        DeviceLog deviceLog = mobileDriver.getDeviceLog();

        Assert.assertTrue(deviceLog.containsString("la"));
        Assert.assertFalse(deviceLog.containsString("blablub"));

        Assert.assertEquals(deviceLog.getEntryContainingString("ird"), "third");
        Assert.assertEquals(deviceLog.getEntryContainingString("blablub"), "");
    }
}
