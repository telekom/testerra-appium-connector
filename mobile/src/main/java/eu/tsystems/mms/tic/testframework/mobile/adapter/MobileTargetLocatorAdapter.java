package eu.tsystems.mms.tic.testframework.mobile.adapter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rnhb on 12.02.2016.
 */
public class MobileTargetLocatorAdapter implements WebDriver.TargetLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileTargetLocatorAdapter.class);

    private final MobileWebDriverAdapter mobileWebDriverAdapter;

    public MobileTargetLocatorAdapter(MobileWebDriverAdapter mobileWebDriverAdapter) {
        this.mobileWebDriverAdapter = mobileWebDriverAdapter;
    }

    private WebDriver returnAndLog() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.warn("switchTo()." + methodName + " not implemented on mobile.");
        return mobileWebDriverAdapter;
    }

    @Override
    public WebDriver frame(int index) {
        return returnAndLog();
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return returnAndLog();
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return returnAndLog();
    }

    @Override
    public WebDriver parentFrame() {
        return returnAndLog();
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return returnAndLog();
    }

    @Override
    public WebDriver defaultContent() {
        return returnAndLog();
    }

    @Override
    public WebElement activeElement() {
        throw new NotSupportedException();
    }

    @Override
    public Alert alert() {
        throw new NotSupportedException();
    }
}
