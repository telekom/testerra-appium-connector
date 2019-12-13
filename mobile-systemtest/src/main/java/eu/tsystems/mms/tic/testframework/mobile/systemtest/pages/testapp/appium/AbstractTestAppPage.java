/*
 * Created on 27.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.appium;

import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import org.openqa.selenium.WebDriver;

/**
 * Abstract page class for all pages of testapp.
 *
 * @author sepr
 */
public class AbstractTestAppPage extends Page {

    private final AppHeader header = PageFactory.create(AppHeader.class, driver);

    /**
     * Default constructor.
     */
    public AbstractTestAppPage(WebDriver driver) {

        super(driver);
    }

    public AppHeader getHeader() {

        return header;
    }
}
