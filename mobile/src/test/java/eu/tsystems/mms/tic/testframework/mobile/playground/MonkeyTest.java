package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.monkey.Monkey;
import org.testng.annotations.Test;

/**
 * Created by rnhb on 18.08.2016.
 */
public class MonkeyTest {

    @Test
    public void testMonkey() throws Exception {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        TestDevice testDevice = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Galaxy_S5").build();
        MobileDriverManager.deviceStore().addDevice(testDevice);
        mobileDriver.switchToDevice(testDevice);

        Monkey monkey = Monkey.giveBirthToMonkey();
        monkey.learnWhatEveryMonkeyShouldKnow();

//        mobileDriver.seeTestClient().startMonitor("Memory");
//        mobileDriver.seeTestClient().startMonitor("CPU");
//        mobileDriver.seeTestClient().setMonitorPollingInterval(1000);

        monkey.playWithDevice(mobileDriver, 30);

//        mobileDriver.seeTestClient().getMonitorsData("c:/out.csv");

    }
}
