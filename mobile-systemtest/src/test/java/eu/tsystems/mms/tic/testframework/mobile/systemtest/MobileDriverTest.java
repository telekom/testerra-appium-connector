package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.annotations.Fails;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.ViewOrientation;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.NeedsAppInstalled;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppHomePage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppInputPage;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.openqa.selenium.OutputType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by rnhb on 23.11.2016.
 */
public class MobileDriverTest extends AbstractTest {

    /**
     * Make sure app is installed
     *
     * @throws DeviceNotAvailableException
     */
    @BeforeMethod(alwaysRun = true)
    public void setup(Method m) throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();

        if (driver.getActiveDevice() == null) {
            driver.reserveDeviceByFilter(getDeviceFilterProperty());
        }

        if (m.getAnnotation(NeedsAppInstalled.class) != null) {
            installTestApp();
        }
    }

    @Test
    @NeedsAppInstalled
    public void testT02N_MobileDriver_isApplicationInstalled() throws DeviceNotAvailableException {

        boolean applicationInstalled = MobileDriverManager.getMobileDriver()
                .isApplicationInstalled("not.existing.app");
        Assert.assertFalse(applicationInstalled, "Application is installed");
    }

    @Test(groups = Groups.SMOKE)
    @NeedsAppInstalled
    public void testT03_MobileDriver_launchApplication() throws DeviceNotAvailableException {

        launchTestApp();
        assertTestAppIsRunning();
    }

    @Test
    @NeedsAppInstalled
    public void testT04_MobileDriver_closeApplication() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        launchTestApp();
        driver.closeApplication(driver.getCurrentlyRunningApplication());
        assertTestAppIsNotRunning();
    }

    @Test
    @NeedsAppInstalled
    public void testT05_MobileDriver_uninstall() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        Assert.assertTrue(driver.uninstall(getAppName()), "uninstall succesful");
        TimerUtils.sleep(10000);
        assertTestAppIsNotInstalled();
    }

    @Test
    public void testT06_MobileDriver_reserveDevice() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        TestDevice device = driver.getActiveDevice();
        Assert.assertNotNull(device, "ActiveDevice is not present.");
        driver.releaseDevice(device);
        TimerUtils.sleep(20000);
        Assert.assertNull(driver.getActiveDevice(), "Device should be released.");
        driver.reserveDevice(device);
        driver.switchToDevice(device);
        Assert.assertNotNull(driver.getActiveDevice(), "Device should be reserved.");
    }

    @Test
    public void testT07_MobileDriver_releaseDevice() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        TestDevice testDevice = driver.getActiveDevice();
        driver.releaseDevice(testDevice);
        Assert.assertNull(driver.getActiveDevice(), "Device should be released.");
        TimerUtils.sleep(20000);
    }

    @Test
    public void testT08_MobileDriver_seeTestClient() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        Assert.assertNotNull(driver.seeTestClient(), "Seetest client is null.");
    }

    @Test(enabled = false)
    public void testT11_Mobiledriver_releaseAllDevices() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        TestDevice testDevice = driver.reserveDeviceByFilter();
        TestDevice alternateTestDevice = driver.reserveDeviceByFilter();
        Assert.assertTrue(driver.isDeviceConnected(testDevice), "device should be connected.");
        Assert.assertTrue(driver.isDeviceConnected(alternateTestDevice), "device should be connected.");
        TimerUtils.sleep(5 * 1000);
        try {
            driver.releaseAllDevices();
            TimerUtils.sleep(10 * 1000);
            Assert.assertFalse(driver.isDeviceConnected(testDevice), "device should be released.");
            Assert.assertFalse(driver.isDeviceConnected(alternateTestDevice), "device should be released.");
        } finally {
            driver.releaseDevice(testDevice);
            driver.releaseDevice(alternateTestDevice);
        }
        TimerUtils.sleep(20000);
    }

    @Test(priority = 101)
    public void testT12_MobileDriver_reserveDeviceByFilter() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        driver.releaseAllDevices();
        TimerUtils.sleep(20000);
        TestDevice deviceByFilter = driver.reserveDeviceByFilter();
        Assert.assertEquals(driver.getActiveDevice().getOperatingSystem(),
                driver.getActiveDevice().getOperatingSystem());
        driver.releaseDevice(deviceByFilter);
        TimerUtils.sleep(20000);
    }

    @Test(priority = 100)
    public void testT12N_MobileDriver_reserveDeviceByFilter() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        driver.releaseAllDevices();
        TimerUtils.sleep(20000);
        TestDevice deviceByFilter = driver.reserveDeviceByFilter();
        Assert.assertNotEquals(driver.getActiveDevice().getOperatingSystem(), MobileOperatingSystem.WINDOWS_PHONE);
        driver.releaseDevice(deviceByFilter);
        TimerUtils.sleep(20000);
    }

    @Test
    public void testT13_twoMinutesTest() throws DeviceNotAvailableException {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 2 * 60 * 1000;
        while (System.currentTimeMillis() < endTime) {
            if (driver.getScreenResolution().getHeight() > driver.getScreenResolution().getWidth()) {
                driver.changeOrientationTo(ViewOrientation.LANDSCAPE);
            } else {
                driver.changeOrientationTo(ViewOrientation.PORTRAIT);
            }
        }
        driver.changeOrientationTo(ViewOrientation.PORTRAIT);
    }

    @Test(groups = Groups.SMOKE)
    public void testT14_MobileDriver_takeScreenshot() {

        final MobileDriver driver = MobileDriverManager.getMobileDriver();
        File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
        //        String screenshotPath = driver.prepareNewScreenshot();
        //        screenshotPath = screenshotPath.replace("../../screenshots/", ReportUtils.getScreenshotsPath());
        Assert.assertTrue(screenshotFile.exists(), "Screen could not be recorded.");
    }

    @Test(groups = Groups.SMOKE)
    public void testT15_MobileDriver_changeOrientation() {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        launchTestApp();
        if (driver.getScreenResolution().getWidth() < driver.getScreenResolution().getHeight()) {
            driver.changeOrientationTo(ViewOrientation.LANDSCAPE);
            Assert.assertTrue(driver.getScreenResolution().getWidth() > driver.getScreenResolution().getHeight(),
                    "Screen could not be rotated.");
        } else {
            driver.changeOrientationTo(ViewOrientation.PORTRAIT);
            Assert.assertTrue(driver.getScreenResolution().getHeight() > driver.getScreenResolution().getWidth(),
                    "Screen could not be rotated.");
        }
        driver.changeOrientationTo(ViewOrientation.PORTRAIT);
    }

    @Fails(description = "shake device not supported by most devices")
    @Test
    public void testT16_MobileDriver_shakeDevice() {

        launchTestApp();
        MobileDriverManager.getMobileDriver().shakeDevice();
    }

    @Fails(ticketString = "MDC-128", validFor = "os.unter.test=ios")
    @Test(groups = Groups.SMOKE)
    public void testT18_MobileDriver_pressHomeButton() {

        launchTestApp();
        MobileDriverManager.getMobileDriver().pressHomeButton();
        assertTestAppIsNotRunning();
    }

    @Fails(description = "ios has no back button", validFor = "os.unter.test=ios")
    @Test
    public void testT19_MobileDriver_back() {

        launchTestApp();
        TestAppHomePage appHomePage = PageFactory.getNew(TestAppHomePage.class);
        appHomePage.goToActivity(TestAppInputPage.class);
        MobileDriverManager.getMobileDriver().back();
        appHomePage.checkPage();
    }

    @Test
    public void testT20_MobileDriver_getScreenResolution() {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        launchTestApp();
        driver.changeOrientationTo(ViewOrientation.PORTRAIT);
        Assert.assertTrue(driver.getScreenResolution().getHeight() > driver.getScreenResolution().getWidth());
        driver.changeOrientationTo(ViewOrientation.LANDSCAPE);
        Assert.assertTrue(driver.getScreenResolution().getHeight() < driver.getScreenResolution().getWidth());
        driver.changeOrientationTo(ViewOrientation.PORTRAIT);
    }

    @Test
    public void testT21_MobileDriver_pressBackspace() {

        launchTestApp();
        TestAppHomePage appHomePage = PageFactory.getNew(TestAppHomePage.class);
        TestAppInputPage appInputPage = appHomePage.goToActivity(TestAppInputPage.class);
        appInputPage.sendTextToInput("TIC");
        Assert.assertEquals(appInputPage.getTextFromInput(), "TIC", "Could not set text of text input.");
        appInputPage.focustTextInput();
        MobileDriverManager.getMobileDriver().pressBackspace();
        Assert.assertEquals(appInputPage.getTextFromInput(), "TI", "pressBackspace() could not be performed.");
    }

    // @Test(groups = {"driver"})
    // public void testT22_MobileDriver_wakeDevice() {
    // MobileDriver driver = MobileDriverManager.getMobileDriver();
    // Client client = driver.seeTestClient();
    // driver.openDevice();
    // client.sendText("{UNLOCK}");
    // client.sendText("{POWER}");
    // for (int i = 1; i <= 10; i++) {
    // Assert.assertEquals(client.getCoordinateColor(10 * i, 10 * i), 0);
    // }
    // driver.wakeDevice();
    // launchTestApp();
    // for (int i = 1; i <= 10; i++) {
    // Assert.assertTrue(client.getCoordinateColor(10 * i, 10 * i) > 0);
    // }
    // }

    @Test
    public void testT24_MobileDriver_getScreenshotAs() {

        MobileDriver driver = MobileDriverManager.getMobileDriver();
        driver.openDevice();
        driver.launchApplication(MOBILE_TEST_PAGE);
        File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
        Assert.assertNotNull(screenshotFile, "Screenshot file is null.");
    }

    // @Test(groups = {"seetest"})
    // public void testT51_SeeTestClient_simulateCapture() {
    // MobileDriver driver = MobileDriverManager.getMobileDriver();
    // launchTestApp(TestingAppActivity.CAMERA);
    // driver.openDevice();
    // driver.seeTestClient().simulateCapture(PATH_TO_PICTURE);
    // NativeMobileGuiElement preview = new NativeMobileGuiElement("xpath=//*[@id='camera_preview']");
    // Color color = preview.getColorOfPixel(400, 400);
    // Assert.assertTrue(color.getRed() > 190 && color.getRed() < 235);
    // Assert.assertTrue(color.getGreen() < 15);
    // Assert.assertTrue(color.getBlue() > 105 && color.getBlue() < 130);
    // }
}
