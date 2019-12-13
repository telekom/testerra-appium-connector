/*
 * Created on 27.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.appium;

import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page class for Homepage of app.
 *
 * @author sepr
 */
public class AndroidTestAppHomePage extends TestAppHomePage {

    public AndroidTestAppHomePage(WebDriver driver) {

        super(driver);
        middleSection = new GuiElement(driver, By.id("middleSection"));
    }
}
