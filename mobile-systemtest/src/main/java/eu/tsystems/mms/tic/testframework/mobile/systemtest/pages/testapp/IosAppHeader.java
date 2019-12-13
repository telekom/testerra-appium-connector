/*
 * Created on 30.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Header actions in TestApp.
 *
 * @author sepr
 */
public class IosAppHeader extends AppHeader {

    private final NativeMobileGuiElement backButton = new NativeMobileGuiElement(By.accessibilityLabel("Back"));

    /* (non-Javadoc)
     * @see eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.AppHeader#gotoHomePage()
     */
    @Override
    public TestAppHomePage gotoHomePage() {

        if (backButton.isDisplayed()) {
            backButton.click();
        }
        return new IosTestAppHomePage();
    }

}
