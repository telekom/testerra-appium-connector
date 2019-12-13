/*
 * Created on 10.02.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * TestAppDragnDropPage
 *
 * @author sepr
 */
public class IosTestAppDragnDropPage extends TestAppDragnDropPage {

    public IosTestAppDragnDropPage() {

        resetButton = new NativeMobileGuiElement(By.accessibilityLabel("Reset"));
        resetButton.click();
        checkPage();
    }
}
