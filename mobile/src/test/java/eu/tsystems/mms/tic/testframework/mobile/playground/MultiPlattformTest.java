package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.constants.MobileBrowsers;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileWebDriverRequest;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 12.02.2016.
 */
@Listeners({TesterraListener.class})
public class MultiPlattformTest {

    @Test
    public void testT01() throws Exception {

        MobileWebDriverRequest mobileWebDriverRequest = new MobileWebDriverRequest();
        mobileWebDriverRequest.browser = MobileBrowsers.mobile_chrome;
        //        WebDriverManager.config().browser = MobileBrowsers.mobile_chrome;
        //WebDriverManager.config().setBrowser(Browser.firefox);

        WebDriver webDriver = WebDriverManager.getWebDriver();
        webDriver.get("http://www.google.com");

        GuiElement searchField = new GuiElement(webDriver, By.xpath(".//*[@id='lst-ib']"));
        searchField.type("working");

        GuiElement searchButton = new GuiElement(webDriver, By.tagName("button"));
        searchButton.click();

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver(webDriver);
    }

    @Test
    public void testT02() throws Exception {

        class GooglePage extends Page {

            GuiElement searchField = new GuiElement(driver, By.xpath(".//*[@id='lst-ib']"));
            GuiElement searchButton = new GuiElement(driver, By.tagName("button"));

            public GooglePage(WebDriver driver) {
                super(driver);
            }

            public void search(String term) {
                searchField.type(term);
                searchButton.click();
            }
        }

        MobileWebDriverRequest mobileWebDriverRequest = new MobileWebDriverRequest();
        mobileWebDriverRequest.browser = MobileBrowsers.mobile_chrome;
        //        WebDriverManager.config().browser() = MobileBrowsers.mobile_chrome;
        //WebDriverManager.config().setBrowser(Browser.firefox);

        WebDriver webDriver = WebDriverManager.getWebDriver();
        webDriver.get("http://www.google.com");

        GooglePage googlePage = new GooglePage(webDriver);
        googlePage.search("working with page");
    }

    @Test
    public void testT03() throws Exception {

        class GooglePage extends Page {

            @Check
            GuiElement searchField = new GuiElement(driver, By.xpath(".//*[@id='lst-ib']"));
            @Check
            GuiElement searchButton = new GuiElement(driver, By.tagName("button"));

            public GooglePage(WebDriver driver) {
                super(driver);
                checkPage();
            }

            public void search(String term) {
                searchField.type(term);
                searchButton.click();
            }
        }

        MobileWebDriverRequest mobileWebDriverRequest = new MobileWebDriverRequest();
        mobileWebDriverRequest.browser = MobileBrowsers.mobile_chrome;
        //        WebDriverManager.config().browser = MobileBrowsers.mobile_chrome;
        //WebDriverManager.config().setBrowser(Browser.firefox);

        WebDriver webDriver = WebDriverManager.getWebDriver();
        webDriver.get("http://www.google.com");

        GooglePage googlePage = new GooglePage(webDriver);
        googlePage.search("working with page");
    }

    @Test
    public void testT04() throws Exception {
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER + ".chrome", "os.type=android");

        MobileWebDriverRequest mobileWebDriverRequest = new MobileWebDriverRequest();
        mobileWebDriverRequest.browser = MobileBrowsers.mobile_chrome;
        //        WebDriverManager.config().browser = MobileBrowsers.mobile_chrome;
        //WebDriverManager.config().setBrowser(Browser.firefox);

        WebDriver webDriver = WebDriverManager.getWebDriver();
        webDriver.get("http://www.google.com");

        Assert.fail();
    }
}
