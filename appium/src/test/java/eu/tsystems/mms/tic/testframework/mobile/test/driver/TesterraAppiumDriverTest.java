/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package eu.tsystems.mms.tic.testframework.mobile.test.driver;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.mobile.test.AbstractAppiumTest;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import eu.tsystems.mms.tic.testframework.utils.JSUtils;
import eu.tsystems.mms.tic.testframework.utils.UITestUtils;
import eu.tsystems.mms.tic.testframework.utils.WebDriverUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AppiumDriverRequest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsRotation;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Date: 24.06.2020
 * Time: 12:26
 *
 * @author Eric Kubenka
 */
public class TesterraAppiumDriverTest extends AbstractAppiumTest implements WebDriverManagerProvider {

    @Test
    public void testT01_startDefaultSession() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        request.getMutableCapabilities().setCapability("appiumVersion", "2.2.2");
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver(request);

        WEB_DRIVER_MANAGER.unwrapWebDriver(driver, AppiumDriver.class).ifPresent(appiumDriver -> {
            ((SupportsRotation) appiumDriver).rotate(ScreenOrientation.LANDSCAPE);
        });
        driver.get("https://the-internet.herokuapp.com/dropdown");
        UITestUtils.takeScreenshots();
    }

    @Test
    public void testT02_startMultipleSessions() {

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        AppiumDriver appiumDriver = WEB_DRIVER_MANAGER.unwrapWebDriver(driver, AppiumDriver.class).get();

        ((SupportsRotation) appiumDriver).rotate(ScreenOrientation.LANDSCAPE);
        driver.get("https://the-internet.herokuapp.com/dropdown");

        final WebDriver driver2 = WEB_DRIVER_MANAGER.getWebDriver("second");
        AppiumDriver appiumDriver2 = WEB_DRIVER_MANAGER.unwrapWebDriver(driver2, AppiumDriver.class).get();

        ((SupportsRotation) appiumDriver2).rotate(ScreenOrientation.PORTRAIT);
        driver2.get("https://the-internet.herokuapp.com/checkboxes");

        Assert.assertNotEquals(driver, driver2, "Driver equals");
    }

    @Test
    public void testT03_startRequestSession() {
        AppiumDriverRequest request = new AppiumDriverRequest();
        final String device = "Apple iPhone X";
        request.setBrowser(Browsers.mobile_safari);
        request.setDeviceQuery(String.format("contains(@name, '%s')", device));
        request.getMutableCapabilities().setCapability("appium:appiumVersion", "2.2.2");

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver(request);

        WEB_DRIVER_MANAGER.getSessionContext(driver)
                .flatMap(SessionContext::getActualBrowserName)
                .ifPresentOrElse(
                        string -> Assert.assertTrue(string.contains(device)),
                        () -> Assert.fail("Cannot found requested device in user agent.")
                );
    }

    @Test
    public void testT04_startSessionTwice() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();

        driver.get("https://the-internet.herokuapp.com/dropdown");
        Assert.assertEquals(WEB_DRIVER_MANAGER.getWebDriver(), driver, "Driver equals");
    }

    @Test
    public void testT05_takeScreenshot() {

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        AppiumDriver appiumDriver = WEB_DRIVER_MANAGER.unwrapWebDriver(driver, AppiumDriver.class).get();

        driver.get("https://the-internet.herokuapp.com/");

        ((SupportsRotation) appiumDriver).rotate(ScreenOrientation.LANDSCAPE);
        final Screenshot screenshotLandScape = UITestUtils.takeScreenshot(driver, true);
        Assert.assertNotNull(screenshotLandScape, "Screenshot created.");

        ((SupportsRotation) appiumDriver).rotate(ScreenOrientation.PORTRAIT);
        final Screenshot screenshotPortrait = UITestUtils.takeScreenshot(driver, true);
        Assert.assertNotNull(screenshotPortrait, "Screenshot created.");
    }

    @Test
    public void testT06_executeJavaScript() {

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/");

        final Object returnValue = JSUtils.executeScript(driver, "var test ='test'; return test;");
        Assert.assertNotNull(returnValue, "JS returned a value.");
        Assert.assertEquals(returnValue.toString(), "test", "Returned value equals.");
    }

    @Test
    public void testT07_getViewport() {

        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        driver.get("https://the-internet.herokuapp.com/");

        Rectangle viewport = WebDriverUtils.getViewport(driver);
        Assert.assertNotNull(viewport, "JSUtils Viewport received.");

        Object x = JSUtils.executeScript(driver, "return window.pageXOffset.toString();");
        Object y = JSUtils.executeScript(driver, "return window.pageYOffset.toString();");
        Object w = JSUtils.executeScript(driver, "return window.innerWidth.toString();");
        Object h = JSUtils.executeScript(driver, "return window.innerHeight.toString();");

        // TODO throws error ... may change it in testerra.
        //        final Rectangle viewportRectangle = WebDriverUtils.getViewport(driver);
        //        Assert.assertNotNull(viewportRectangle, "WebDriver Viewport created");
    }

}
