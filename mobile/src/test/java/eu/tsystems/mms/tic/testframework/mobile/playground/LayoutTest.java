package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.SeeTestReportListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 13.01.2016.
 */
@Listeners(SeeTestReportListener.class)
public class LayoutTest {

    static{
        MobileDriverManager.getMobileDriver();
    }

    @Test
    public void testName() throws Exception {

        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR,"C:\\Users\\rnhb\\workspace\\project10");
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        TestDevice testDevice = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Galaxy_S6").build();
        MobileDriverManager.deviceStore().addDevice(testDevice);

        mobileDriver.switchToDevice(testDevice);

        //TODO remove?!
//        mobileDriver.runLayoutTest("src/test/resources/1");
    }
}
