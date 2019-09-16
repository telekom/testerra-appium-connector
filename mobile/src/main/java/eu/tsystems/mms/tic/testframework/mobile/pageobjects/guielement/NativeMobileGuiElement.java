/*
 * Created on 09.01.2014
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;


import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.CachedNativeMobileGuiElementStrategy;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.NativeMobileGuiElementStrategy;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.LoggerFactory;

//import eu.tsystems.mms.tic.testframework.restriction.XetaLicense;

/**
 * @author rnhb
 */
public class NativeMobileGuiElement extends AbstractMobileGuiElement {

    private static final boolean cacheGuiElements;

    static {
        cacheGuiElements = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_CACHE_GUI_ELEMENTS, false);
        LoggerFactory.getLogger("MobileGuiElement").info("Using cache: " + cacheGuiElements);
    }

    public NativeMobileGuiElement(By by) {
        this(by.toString());
    }

    public NativeMobileGuiElement(By by, MobilePage mobilePage) {
        this(by.toString(), mobilePage);
    }

    /**
     * Instantiates a new mobile native gui element.
     *
     * @param elementLocatorString the element locator string
     */
    public NativeMobileGuiElement(String elementLocatorString) {
        this(elementLocatorString, null);
    }

    /**
     * Instantiates a new mobile native gui element.
     *
     * @param elementLocatorString the element locator string
     * @param mobilePage           Page this element is contained in. Necessary if this element should use the page cache.
     */
    public NativeMobileGuiElement(String elementLocatorString, MobilePage mobilePage) {
        if (StringUtils.isStringEmpty(elementLocatorString)) {
            throw new TesterraSystemException("Locator for GuiElement is empty, this is not allowed.");
        } else if (!elementLocatorString.startsWith("xpath=")) {
            throw new TesterraSystemException("Locator for NativeMobileGuiElement did not start with 'xpath=', this is not allowed.");
        }
        MobileLocator mobileLocator = new MobileLocator(LocatorType.NATIVE.toString(), elementLocatorString, 0);
        mobileDriver = MobileDriverManager.getMobileDriver();
        if (mobilePage != null || cacheGuiElements) {
            setStrategy(new CachedNativeMobileGuiElementStrategy(mobileDriver, mobileLocator, statusContainer));
        } else {
            setStrategy(new NativeMobileGuiElementStrategy(mobileDriver, mobileLocator, statusContainer));
        }
        setContainingPage(mobilePage);
    }
}
