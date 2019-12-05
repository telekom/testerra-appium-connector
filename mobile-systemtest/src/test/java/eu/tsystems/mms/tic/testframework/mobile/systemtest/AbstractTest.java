package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.transfer.ThrowablePackedResponse;
import eu.tsystems.mms.tic.testframework.utils.Timer;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Created by ncbe on 02.09.2016.
 */
public abstract class AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
    //    static final String MOBILE_TEST_PAGE = "http://q4deumsy0tt.mms-dresden.de";
    static final String MOBILE_TEST_PAGE = "http://google.de";
    static MobileOperatingSystem mobileOperatingSystem;

    static {
        String env = PropertyManager.getProperty("env", "local");
        LOGGER.info("Loading env: " + env);
        PropertyManager.loadProperties("env/" + env + ".properties");
    }

    @BeforeClass(alwaysRun = true)
    public void prepareDevice() throws DeviceNotAvailableException {
        //TODO how to achive that in testerra?
        //        TestRunConfiguration.getInstance().addTestFramework("xeta-mobile");
        mobileOperatingSystem = getOsByScope();
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        //        mobileDriver.registerDeviceTest(new DeviceOnlineTest("www.google.de", "xpath=//*[@id='hplogo']"));
        //        mobileDriver.registerDeviceTest(new DeviceTest("App can be installed") {
        //            @Override
        //            public boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception {
        //                installTestApp();
        //                return mobileDriver.isApplicationInstalled(getAppName());
        //            }
        //        });
        //        mobileDriver.reserveDeviceByFilter(getDeviceFilterProperty());
        //        ClientListener.addGoogleChromePromoDialogListener();
        //        ClientListener.addGoogleChromeTermsAndConditionsListener();
        //        ClientListener.addGoogleLanguagePopupRemoverListener();
        TestDevice device = mobileDriver.reserveDeviceByFilter(getDeviceFilterProperty());
        mobileDriver.switchToDevice(device);
        mobileDriver.installApplication(getAppName());

    }

    @BeforeMethod(alwaysRun = true)
    public void setProperties(Method testMethod) {

        PropertyManager.loadThreadLocalProperties(mobileOperatingSystem.lowerCaseName() + ".properties");
    }

    /**
     * Cleanup Test
     */
    @AfterClass(alwaysRun = true)
    public void releaseDevice() {

        MobileDriverManager.releaseDriverFromThread();
        TimerUtils.sleep(20000); //TODO why?
    }

    private MobileOperatingSystem getOsByScope() {
        //        String scopeProperty = PropertyManager.getProperty("scope");
        //        if (scopeProperty == null || scopeProperty.equalsIgnoreCase("int")) {
        //            return MobileOperatingSystem.IOS;
        //        } else {
        //            return MobileOperatingSystem.ANDROID;
        //        }
        return MobileOperatingSystem.IOS;
    }

    /**
     * Get device filter property for os
     */
    protected String getDeviceFilterProperty() {

        return "tt.mobile.system.test.device.filter." + mobileOperatingSystem.lowerCaseName();
    }

    protected void launchTestApp() {

        String appName = getAppName();
        MobileDriver driver = MobileDriverManager.getMobileDriver();
        if (driver.isApplicationInstalled(appName)) {
            driver.launchApplication(appName, true, false);
        } else {
            throw new TesterraRuntimeException("App war nichst installiert beim starten");
        }

    }

    protected void installTestApp() {

        String appName = getAppName();
        MobileDriver driver = MobileDriverManager.getMobileDriver();
        if (!driver.isApplicationInstalled(appName)) {
            driver.installApplication(appName);
        }
    }

    protected String getAppName() {

        return PropertyManager
                .getProperty("tt.mobile.system.test.app.name." + mobileOperatingSystem.lowerCaseName());
    }

    protected void assertTestAppIsRunning() {

        Assert.assertTrue(
                getAppName().contains(MobileDriverManager.getMobileDriver().getCurrentlyRunningApplication()),
                "Test App is not running.");
    }

    protected void assertTestAppIsNotRunning() {

        ThrowablePackedResponse<Boolean> sequence = new Timer().executeSequence(new Timer.Sequence<Boolean>() {

            @Override
            public void run() throws Throwable {

                boolean appRunning = getAppName()
                        .contains(MobileDriverManager.getMobileDriver().getCurrentlyRunningApplication());
                setPassState(!appRunning);
                setReturningObject(appRunning);
                setSkipThrowingException(true);
            }
        });

        Assert.assertFalse(sequence.getResponse(),
                "Test App is running.");
    }

    protected void assertTestAppIsInstalled() {

        Assert.assertTrue(MobileDriverManager.getMobileDriver().isApplicationInstalled(getAppName()),
                "Application is installed.");
    }

    protected void assertTestAppIsNotInstalled() {

        Assert.assertFalse(MobileDriverManager.getMobileDriver().isApplicationInstalled(getAppName()),
                "Application is installed.");
    }
}
