/*
 * Created on 07.02.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.test;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.mockito.Mockito;

import com.experitest.client.GridClient;

import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import eu.tsystems.mms.tic.testframework.utils.XMLUtils;

/**
 * AbstractMockTest
 * 
 * @author sepr
 */
public abstract class AbstractMockTest {

    /**
     * Get resource from classloader.
     * 
     * @return resource as file
     */
    protected File getResourceFile(String rsPath) {
        return FileUtils.getResourceFile(rsPath);
    }

    protected GridClient getMockedGridClientWithReservation(String devicesXmlFileName)
            throws JDOMException, IOException {
        GridClient gridClient = Mockito.mock(GridClient.class);

        Document document = XMLUtils.jdom()
                .createDocumentFromFile(getResourceFile("testdata/" + devicesXmlFileName).getAbsolutePath());
        String deviceString = new XMLOutputter().outputString(document);
        when(gridClient.getDevicesInformation()).thenReturn(
                deviceString);
        return gridClient;
    }
}
