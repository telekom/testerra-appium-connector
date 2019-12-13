/*
 * Created on 30.01.2017
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
 * Header actions in TestApp.
 *
 * @author sepr
 */
public class AndroidAppHeader extends AppHeader {

    /**
     * @param driver
     */
    public AndroidAppHeader(WebDriver driver) {

        super(driver);
    }

    private final GuiElement backButton = new GuiElement(driver, By.xpath("//*[@contentDescription='Nach oben']"));

    //    private final NativeMobileGuiElement homeButton = new NativeMobileGuiElement(By.id("title"));

    /* (non-Javadoc)
     * @see eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.AppHeader#gotoHomePage()
     */
    @Override
    public TestAppHomePage gotoHomePage() {

        if (backButton.isDisplayed()) {
            backButton.click();
        }
        return new AndroidTestAppHomePage(driver);
    }
}
