package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by rnhb on 10.02.2016.
 */
public class MobileWebDriverNavigationAdapter implements WebDriver.Navigation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileWebDriverWindowAdapter.class);

    private final MobileDriver mobileDriver;

    public MobileWebDriverNavigationAdapter(MobileDriver mobileDriver) {
        this.mobileDriver = mobileDriver;
    }

    @Override
    public void back() {
        mobileDriver.browserBack();
    }

    @Override
    public void forward() {
        throw new NotSupportedException();
    }

    @Override
    public void to(String s) {
        mobileDriver.launchApplication(s);
    }

    @Override
    public void to(URL url) {
        mobileDriver.launchApplication(url.toString());
    }

    @Override
    public void refresh() {
        LOGGER.warn("refresh was called on mobile browser, will do nothing.");
    }
}
