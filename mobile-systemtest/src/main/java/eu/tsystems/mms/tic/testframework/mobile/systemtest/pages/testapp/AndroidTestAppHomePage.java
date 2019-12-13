/*
 * Created on 27.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Page class for Homepage of app.
 *
 * @author sepr
 */
public class AndroidTestAppHomePage extends TestAppHomePage {

    public AndroidTestAppHomePage() {

        middleSection = new NativeMobileGuiElement(By.id("middleSection"));
    }
}
