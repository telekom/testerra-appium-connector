/*
 * Created on 19.02.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import eu.tsystems.mms.tic.testframework.constants.Browser;
import eu.tsystems.mms.tic.testframework.mobile.adapter.MobileWebDriverAdapter;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;

public class WebDriverAdapterFactory implements WebDriverFactory {
    @Override
    public WebDriver getRawWebDriver(WebDriverRequest webDriverRequest) {
        // you can implement your own WebDriverRequest "next to" DesktopWebDriverRequest with mobile options you
        // need. - pele 19.07.2017
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        return new MobileWebDriverAdapter(mobileDriver);
    }

    @Override
    public void setupSession(EventFiringWebDriver eventFiringWebDriver, String sessionId, Browser browser) {
        // you CAN do something, but you don't need to . :) - pele 19.07.2017
    }
}