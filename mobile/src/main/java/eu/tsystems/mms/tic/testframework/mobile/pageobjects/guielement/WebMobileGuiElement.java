/*
 * Created on 09.01.2014
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.DumpBasedMobileGuiElementStrategy;

/**
 * Mobile GuiElement in a WebContext (blue border in Object Spy).
 *
 * @author rnhb
 */
public class WebMobileGuiElement extends AbstractMobileGuiElement {

    /**
     * Instantiates a new mobile web gui element.
     *
     * @param elementLocatorString the element locator string
     */
    public WebMobileGuiElement(MobileDriver mobileDriver, String elementLocatorString) {
        MobileLocator mobileLocator = new MobileLocator(LocatorType.WEB.toString(), elementLocatorString, 0);

        if (mobileDriver == null) {
            this.mobileDriver = MobileDriverManager.getMobileDriver();
        } else {
            this.mobileDriver = mobileDriver;
        }

        setStrategy(new DumpBasedMobileGuiElementStrategy(this.mobileDriver, mobileLocator, statusContainer));
    }

    public WebMobileGuiElement(String elementLocatorString) {
        this(null, elementLocatorString);
    }

    public MobileDriver getMobileDriver() {
        return mobileDriver;
    }

    @Override
    public WebMobileGuiElement setName(final String name) {

        return (WebMobileGuiElement) super.setName(name);
    }
}