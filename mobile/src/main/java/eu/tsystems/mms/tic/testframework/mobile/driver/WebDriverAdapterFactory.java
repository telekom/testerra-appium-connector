/*
 * Created on 19.02.2018
 *
 * Copyright(c) 2013 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.mobile.adapter.MobileWebDriverAdapter;
import eu.tsystems.mms.tic.testframework.webdrivermanager.UnspecificWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class WebDriverAdapterFactory extends WebDriverFactory {

    @Override
    protected WebDriverRequest buildRequest(WebDriverRequest request) {

        MobileWebDriverRequest r;

        if (request instanceof MobileWebDriverRequest) {
            r = (MobileWebDriverRequest) request;
        } else if (request instanceof UnspecificWebDriverRequest) {
            r = new MobileWebDriverRequest();
            r.copyFrom(request);
        } else {
            throw new TesterraSystemException(request.getClass().getSimpleName() + " is not allowed here");
        }


        return r;
    }

    @Override
    protected DesiredCapabilities buildCapabilities(DesiredCapabilities desiredCapabilities, WebDriverRequest webDriverRequest) {
        // TODO - maybe we need to do something here?
        return desiredCapabilities;
    }

    @Override
    protected WebDriver getRawWebDriver(WebDriverRequest webDriverRequest, DesiredCapabilities desiredCapabilities) {

        final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        return new MobileWebDriverAdapter(mobileDriver);
    }

    @Override
    protected void setupSession(EventFiringWebDriver eventFiringWebDriver, WebDriverRequest webDriverRequest) {

    }
}