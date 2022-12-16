package io.testerra.plugins.appium.seetest.webdriver;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.AppiumDriverFactory;
import eu.tsystems.mms.tic.testframework.webdriver.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.testerra.plugins.appium.seetest.utils.SeeTestProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 2022-12-16
 *
 * @author mgn
 */
public class SeeTestAppiumDriverFactory extends AppiumDriverFactory implements WebDriverFactory, Loggable {

    @Override
    public WebDriverRequest prepareWebDriverRequest(WebDriverRequest webDriverRequest) {
        AppiumDriverRequest request = (AppiumDriverRequest) super.prepareWebDriverRequest(webDriverRequest);

        final String appiumServer = SeeTestProperties.SEETEST_APPIUM_VERSION.asString();
        if (StringUtils.isNotBlank(appiumServer)) {
            request.getDesiredCapabilities().setCapability("appiumVersion", appiumServer);
        }

        return request;
    }

}
