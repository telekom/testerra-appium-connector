/*
 * Created on 09.01.2014
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;


import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.BasicMobileGuiElementStrategy;

/**
 * An element that is identified via OCR (graphical text recognition), similar to the ImageMobileGuiElement.
 *
 * @author rnhb
 */
public class TextMobileGuiElement extends AbstractMobileGuiElement {

    /**
     * Instantiates a new mobile text gui element.
     *
     * @param elementLocatorString the element locator string
     */
    public TextMobileGuiElement(String elementLocatorString) {
        this(elementLocatorString, 0);
    }

    /**
     * Instantiates a new mobile text gui element.
     *
     * @param elementLocatorString the element locator string
     * @param index                the element index
     */
    public TextMobileGuiElement(String elementLocatorString, int index) {
        MobileLocator mobileLocator = new MobileLocator(LocatorType.TEXT.toString(), elementLocatorString, index);
        mobileDriver = MobileDriverManager.getMobileDriver();
        setStrategy(new BasicMobileGuiElementStrategy(mobileDriver, mobileLocator, statusContainer));
    }

    @Override
    public TextMobileGuiElement setName(final String name) {

        return (TextMobileGuiElement) super.setName(name);
    }
}