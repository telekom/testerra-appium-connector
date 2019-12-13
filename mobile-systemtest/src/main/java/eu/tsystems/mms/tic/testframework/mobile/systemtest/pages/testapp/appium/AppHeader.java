/*
 * Created on 30.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.appium;

import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.WebDriver;

/**
 * Header for test app.
 *
 * @author sepr
 */
public abstract class AppHeader extends Page {

    /**
     * @param driver
     */
    public AppHeader(WebDriver driver) {

        super(driver);
    }

    /**
     * Go to first page of app.
     *
     * @return {@link TestAppHomePage}
     */
    public abstract TestAppHomePage gotoHomePage();

}
