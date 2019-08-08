/*
 * Created on 08.08.2019
 *
 * Copyright(c) 1995 - 2007 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.desktop.WebDriverMode;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

/**
 * MobileWebDriverRequest
 * <p>
 * Date: 08.08.2019
 * Time: 07:29
 *
 * @author erku
 */
public class MobileWebDriverRequest extends WebDriverRequest {

    public Map<String, Object> sessionCapabilities = new HashMap<>();
    public DesiredCapabilities desiredCapabilities;
    public String seleniumServerURL;
    public String seleniumServerHost;
    public String seleniumServerPort;
    public WebDriverMode webDriverMode;

}
