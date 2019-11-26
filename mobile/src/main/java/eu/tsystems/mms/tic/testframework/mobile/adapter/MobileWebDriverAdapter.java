package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.constants.MobileBrowsers;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.DefaultParameter;
import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.TextMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rnhb on 09.02.2016.
 */
public class MobileWebDriverAdapter implements WebDriver, JavascriptExecutor, TakesScreenshot {

    private final MobileDriver mobileDriver;
    private final MobileWebDriverNavigationAdapter navigationAdapter;
    private final MobileWebDriverOptionsAdapter optionsAdapter;
    private final MobileTargetLocatorAdapter targetLocatorAdapter;

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileWebDriverAdapter.class);

    private final boolean skipHttpsWarning = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_SKIP_BROWSER_HTTPS_WARNING, DefaultParameter.MOBILE_SKIP_BROWSER_HTTPS_WARNING);

    public MobileWebDriverAdapter(MobileDriver mobileDriver) {
        this.mobileDriver = mobileDriver;
        navigationAdapter = new MobileWebDriverNavigationAdapter(mobileDriver);
        optionsAdapter = new MobileWebDriverOptionsAdapter(mobileDriver);
        targetLocatorAdapter = new MobileTargetLocatorAdapter(this);
    }

    @Override
    public void get(String urlToLaunch) {
        TestDevice activeDevice = mobileDriver.getActiveDevice();
        String browserName = PropertyManager.getProperty(TesterraProperties.BROWSER);
        if (browserName == null ||
                !(MobileBrowsers.mobile_chrome.equals(browserName)
                        || MobileBrowsers.mobile_safari.equals(browserName))) {
            throw new TesterraSystemException("Internal error: Somehow tried to start mobile browser test without properly set mobile browser.");
        }
        String filterProperty = MobileProperties.MOBILE_DEVICE_FILTER + "." + browserName.substring(browserName.lastIndexOf("_") + 1);

        if (activeDevice == null) {
            try {
                mobileDriver.reserveDeviceByFilter(filterProperty);
            } catch (DeviceNotAvailableException e) {
                throw new TesterraRuntimeException(e);
            }
        }

        mobileDriver.launchApplication(urlToLaunch);
        if (skipHttpsWarning) {
            skipUntrustedWebsiteWarning();
        }
    }

    private void skipUntrustedWebsiteWarning() {
        TestDevice activeDevice = mobileDriver.getActiveDevice();
        if (activeDevice != null) {
            String browser = activeDevice.getOperatingSystem().getAssociatedBrowser();
            switch (browser) {
                case MobileBrowsers.mobile_chrome:
                    TextMobileGuiElement expandButton = new TextMobileGuiElement("ERWEITERT");
                    if (expandButton.isDisplayed()) {
                        TextMobileGuiElement continueButton = new TextMobileGuiElement("WEITER ZU");
                        continueButton.setTimeoutInSeconds(5);
                        if (continueButton.waitForIsDisplayed()) {
                            continueButton.click();
                            return;
                        }
                    }
                    break;
                case MobileBrowsers.mobile_safari:
                    TextMobileGuiElement ignoreWarning = new TextMobileGuiElement("Warnhinweis ignorieren");

                    if (ignoreWarning.isDisplayed()) {
                        ignoreWarning.click();
                        return;
                    }
                    TextMobileGuiElement showDetails = new TextMobileGuiElement("Details einblenden");
                    if (showDetails.isDisplayed()) {
                        showDetails.click();
                        TextMobileGuiElement openPage = new TextMobileGuiElement("Ã¶ffne diese Website");
                        TextMobileGuiElement openPage2 = new TextMobileGuiElement("öffne diese Website");
                        if (openPage.isDisplayed()) {
                            openPage.click();
                        } else if (openPage2.isDisplayed()) {
                            openPage2.click();
                        }
                    }
                    break;
                default:
                    LOGGER.error("Browser " + browser + " is not recognized for skipUntrustedWebsiteWarning(). Will not do anything.");
            }
            LOGGER.error("skipUntrustedWebsiteWarning() did not recognize the expected elements. Will not do anything.");
        }
    }

    @Override
    public String getCurrentUrl() {
        String browser = PropertyManager.getProperty(TesterraProperties.BROWSER);
        switch (browser) {
            case MobileBrowsers.mobile_chrome: {
                NativeMobileGuiElement urlInputField = new NativeMobileGuiElement("xpath=//*[@id='url_bar']");
                if (urlInputField.waitForIsPresent()) {
                    return urlInputField.getText();
                } else {
                    return "url_field_not_found";
                }
            }
            case MobileBrowsers.mobile_safari: {
                NativeMobileGuiElement urlInputField = new NativeMobileGuiElement("xpath=//*[@accessibilityLabel='URL']");
                if (urlInputField.waitForIsPresent()) {
                    return urlInputField.getProperty("value");
                } else {
                    return "url_field_not_found";
                }
            }
            default:
                throw new NotSupportedException(browser + " not supported for getCurrentUrl().");
        }
    }

    @Override
    public String getTitle() {
        return (String) executeScript("result=document.title");
    }

    @Override
    public List<WebElement> findElements(By by) {
        ScreenDump screenDump = mobileDriver.getScreenDump(ScreenDump.Type.WEB);
        String seeTestXpath = ByTranslator.translateForSeeTest(by);
        MobileLocator mobileLocator = new MobileLocator(LocatorType.WEB.toString(), seeTestXpath, 0);
        NodeList elements = screenDump.findElements(mobileLocator);
        List<WebElement> webElements = new ArrayList<WebElement>();
        if (elements == null) {
            return webElements;
        }
        for (int i = 0; i < elements.getLength(); i++) {
            WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(mobileDriver,
                    seeTestXpath + "[" + i + "]");
            MobileWebElementAdapter mobileWebElementAdapter = new MobileWebElementAdapter(webMobileGuiElement);
            webElements.add(mobileWebElementAdapter);
        }
        return webElements;
    }

    @Override
    public WebElement findElement(By by) {
        String translatedBy = ByTranslator.translateForSeeTest(by);
        MobileLocator mobileLocator = new MobileLocator(LocatorType.WEB.toString(), translatedBy, 0);
        if (mobileDriver.element().isElementFound(mobileLocator)) {
            WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(mobileDriver, translatedBy);
            MobileWebElementAdapter mobileWebElementAdapter = new MobileWebElementAdapter(webMobileGuiElement);
            return mobileWebElementAdapter;
        } else {
            throw new ElementNotFoundException(translatedBy + " not found.");
        }
    }

    @Override
    public String getPageSource() {
        return mobileDriver.getScreenDump(ScreenDump.Type.WEB).getRawXmlString();
    }

    @Override
    public void close() {
        try {
            String currentlyRunningApplication = mobileDriver.getCurrentlyRunningApplication();
            mobileDriver.closeApplication(currentlyRunningApplication);
        } catch (Exception e) {
            LOGGER.error("Failed to close currently running Application.", e);
        }
    }

    @Override
    public void quit() {
        /* Do not release mobile driver here! In any case, MobileTestListener should be running too, which takes care
        of releasing anything at the appropriate point.
        Closing a browser is not the same thing as releaseing a deivce, since we might not get that same device
        again, but we get the equivalent browser again.
        */
        // mobileDriver.release();
    }

    @Override
    public Set<String> getWindowHandles() {
        HashSet<String> strings = new HashSet<String>();
        strings.add(getWindowHandle());
        return strings;
    }

    @Override
    public String getWindowHandle() {
        return "browser";
    }

    @Override
    public TargetLocator switchTo() {
        return targetLocatorAdapter;
    }

    @Override
    public Navigation navigate() {
        return navigationAdapter;
    }

    @Override
    public Options manage() {
        return optionsAdapter;
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        if (s.startsWith("return")) {
            s = s.replace("return", "result=");
        }

        String returnedString = mobileDriver.seeTestClient().hybridRunJavascript("", 0, s);

        // Selenium expects Boolean objects for return types that equal boolean, while SeeTest always returns String.
        // This would cause a ClassCastException, so we have to circumvent this.
        if ("true".equalsIgnoreCase(returnedString) || "false".equalsIgnoreCase(returnedString)) {
            return Boolean.parseBoolean(returnedString);
        } else {
            return returnedString;
        }
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return executeScript(s, objects);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return this.mobileDriver.getScreenshotAs(target);
    }

    public boolean adaptsMobileDriver(MobileDriver mobileDriver) {
        return this.mobileDriver.equals(mobileDriver);
    }
}
