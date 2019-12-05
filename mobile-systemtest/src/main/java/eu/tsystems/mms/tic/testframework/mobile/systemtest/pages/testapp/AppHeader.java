/*
 * Created on 30.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;

/**
 * Header for test app.
 *
 * @author sepr
 */
public abstract class AppHeader extends MobilePage {

    /**
     * Go to first page of app.
     *
     * @return {@link TestAppHomePage}
     */
    public abstract TestAppHomePage gotoHomePage();

}
