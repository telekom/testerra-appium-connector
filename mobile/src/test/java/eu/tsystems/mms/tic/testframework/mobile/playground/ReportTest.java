package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 16.06.2017.
 */
@Listeners({TesterraListener.class})
public class ReportTest {

    @BeforeTest
    public void a() throws DeviceNotAvailableException {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "name=*alaxy S7*");
        mobileDriver.reserveDeviceByFilter();
    }

    @Test
    public void testName() throws Exception {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.launchApplication("http://www.google.com");
        Assert.fail();
    }
}
