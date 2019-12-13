/*
 * Created on 27.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;

/**
 * Abstract page class for all pages of testapp.
 *
 * @author sepr
 */
public class AbstractTestAppPage extends MobilePage {

    private final AppHeader header = PageFactory.getNew(AppHeader.class);

    /**
     * Default constructor.
     */
    public AbstractTestAppPage() {

        super();
    }

    public AppHeader getHeader() {

        return header;
    }
}
