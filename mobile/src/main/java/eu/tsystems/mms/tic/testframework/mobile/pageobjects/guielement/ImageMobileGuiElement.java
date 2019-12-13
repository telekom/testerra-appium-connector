/**
 * Created on 09.01.2014
 * <p/>
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.strategies.BasicMobileGuiElementStrategy;

/**
 * An element that is identified by its image.
 * In the given repository in the executing SeeTest Automation instance, there has to be an element with that name.
 *
 * @author rnhb
 */
public class ImageMobileGuiElement extends AbstractMobileGuiElement {

    /**
     * Instantiates a new mobile visible gui element.
     * To work properly, the object has to exist in thew given object repository in the executing SeeTest Automation instance.
     *
     * @param elementName the element name
     */
    public ImageMobileGuiElement(String elementName, String repositoryName) {

        MobileLocator mobileLocator = new MobileLocator(repositoryName, elementName, 0);
        mobileDriver = MobileDriverManager.getMobileDriver();
        setStrategy(new BasicMobileGuiElementStrategy(mobileDriver, mobileLocator, statusContainer));
    }

}
