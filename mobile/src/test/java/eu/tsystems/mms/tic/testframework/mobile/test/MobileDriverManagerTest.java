/*
 * Created on 31.01.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.test;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import eu.tsystems.mms.tic.testframework.annotations.Fails;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.experitest.client.Client;
import com.experitest.client.GridClient;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.ExecutorMobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.GridMobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;

/**
 * BaseMobileDriverTest
 * 
 * @author sepr
 */
//@PrepareForTest(MobileDriverManager.class)
public class MobileDriverManagerTest extends PowerMockTestCase {

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }

    /**
     * TODO is failing due IllegalArgumentException
     * @throws Exception
     */
    @Test(enabled = true)
    public void testGridDriver() throws Exception {
        System.setProperty(MobileProperties.MOBILE_SERVER_HOST, "https://");

        GridClient gridClient = PowerMockito.mock(GridClient.class);
        Document document = XMLUtils.jdom()
                .createDocumentFromFile(FileUtils.getResourceFile("testdata/griddevices.xml").getAbsolutePath());
        String deviceString = new XMLOutputter().outputString(document);
        when(gridClient.getDevicesInformation()).thenReturn(deviceString);

        PowerMockito.whenNew(GridClient.class).withArguments(
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.anyString()).thenReturn(gridClient);
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        Assert.assertTrue(mobileDriver instanceof GridMobileDriver);
    }

    /**
     * TODO is failing due ConnectivityException
     * @throws Exception
     */
    @Test(enabled = true)
    public void testExecutorDriver() throws Exception {
        System.setProperty(MobileProperties.MOBILE_SERVER_HOST, "localhost");

        Client seetestClient = PowerMockito.mock(Client.class);
        Document document = XMLUtils.jdom()
                .createDocumentFromFile(FileUtils.getResourceFile("testdata/griddevices.xml").getAbsolutePath());
        String deviceString = new XMLOutputter().outputString(document);
        when(seetestClient.getDevicesInformation()).thenReturn(deviceString);

        PowerMockito.whenNew(Client.class).withArguments(
                Mockito.anyString(),
                Mockito.anyInt())
                .thenReturn(seetestClient);
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        Assert.assertTrue(mobileDriver instanceof ExecutorMobileDriver);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup(Method m) {
        MobileDriverManager.releaseDriverFromThread();
    }
}
