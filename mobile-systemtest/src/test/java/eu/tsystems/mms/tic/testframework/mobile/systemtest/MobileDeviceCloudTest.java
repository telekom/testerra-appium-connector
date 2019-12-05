package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DashboardPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabApplicationsPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabApplicationsPageDeletePopup;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabApplicationsPageInstallPopup;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabApplicationsPageUploadPopup;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabDevicesPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DeviceLabDevicesPageAddReservationPopup;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.DevicesLabDevicesPageReleaseDevicePopup;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud.LoginPage;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 21.06.2016.
 */
public class MobileDeviceCloudTest extends AbstractTest {

    private final String USERNAME_1 = "xeta";
    private final String PASSWORD_1 = "Xeta_mms1*";

    private final String USERNAME_2 = "nkfl";
    private final String PASSWORD_2 = "Xeta_mms1*";

    private final String PATH_TO_APK = "C:\\BuildAgent\\work\\mobile_systemtest\\res\\testingApps\\tic_companion.apk";
    private final String PATH_TO_UPLOAD_TEST_APK = "C:\\BuildAgent\\work\\mobile_systemtest\\res\\testingApps\\nextsms.apk";
    private final String UPLOAD_TESTING_APP_PACKAGE_NAME = "com.handcent.nextsms/com.handcent.sms.ui.ConversationExList";
    private final String PATH_TO_IPA = "path";
    private final String ANDROID_PACKAGE_NAME = "eu.tsystems.mms.tic.testframework.mobile.simpleticapp";
    private final String IOS_PACKAGE_NAME = "ios.package.name";
    private String packageName, pathToApp, deviceName;

    public void initializeDevice(TestDevice testDevice) {

        this.deviceName = testDevice.getName();
        switch (testDevice.getOperatingSystem()) {
            case ANDROID:
                this.packageName = ANDROID_PACKAGE_NAME;
                this.pathToApp = PATH_TO_APK;
                break;
            case IOS:
                this.packageName = IOS_PACKAGE_NAME;
                this.pathToApp = PATH_TO_IPA;
                break;
            default:
                this.packageName = ANDROID_PACKAGE_NAME;
                this.pathToApp = PATH_TO_APK;
        }
    }

    private void setWindowSize(WebDriver driver) {

        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1920, 1080));
    }

    public void reserveDeviceByAnotherUser(String deviceToBeReserved) {

        WebDriver webDriver = WebDriverManager.getWebDriver();
        setWindowSize(webDriver);
        LoginPage loginPage = new LoginPage(webDriver);
        final DashboardPage dashboardPage = loginPage.login(USERNAME_2, PASSWORD_2);
        DeviceLabDevicesPage devicesPage = dashboardPage.navigateToDeviceLabDevices();
        DeviceLabDevicesPageAddReservationPopup addReservationPopup = devicesPage.reserveDevice(deviceToBeReserved);
        devicesPage = addReservationPopup.confirmReservation();
        devicesPage.logout();
    }

    public void releaseReservedDevice(String deviceToBeReleased) {

        WebDriver webDriver = WebDriverManager.getWebDriver();
        setWindowSize(webDriver);
        LoginPage loginPage = new LoginPage(webDriver);
        final DashboardPage dashboardPage = loginPage.login(USERNAME_2, PASSWORD_2);
        DeviceLabDevicesPage devicesPage = dashboardPage.navigateToDeviceLabDevices();
        DevicesLabDevicesPageReleaseDevicePopup releaseDevicePopup = devicesPage.releaseDevice(deviceToBeReleased);
        devicesPage = releaseDevicePopup.confirmRelease();
        devicesPage.logout();
    }

    public void uploadAppToCloud() {

        WebDriver webDriver = WebDriverManager.getWebDriver();
        setWindowSize(webDriver);
        LoginPage loginPage = new LoginPage(webDriver);
        final DashboardPage dashboardPage = loginPage.login(USERNAME_1, PASSWORD_1);
        DeviceLabApplicationsPage deviceLabApplicationsPage = dashboardPage.navigateToDeviceLabApplications();
        if (deviceLabApplicationsPage.appIsListed(this.packageName)) {
            DeviceLabApplicationsPageDeletePopup deletePopup = deviceLabApplicationsPage.clickDelete();
            deviceLabApplicationsPage = deletePopup.confirmDeletion();
        }
        DeviceLabApplicationsPageUploadPopup uploadPopup = deviceLabApplicationsPage.clickUpload();
        deviceLabApplicationsPage = uploadPopup.chooseApk(this.pathToApp);
        deviceLabApplicationsPage.assertAppIsListedInTable(this.packageName);
    }

    public void installAppOnCloudDevice() {

        WebDriver webDriver = WebDriverManager.getWebDriver();
        setWindowSize(webDriver);
        LoginPage loginPage = new LoginPage(webDriver);
        final DashboardPage dashboardPage = loginPage.login(USERNAME_1, PASSWORD_1);
        DeviceLabApplicationsPage deviceLabApplicationsPage = dashboardPage.navigateToDeviceLabApplications();
        GuiElement listEntry = new GuiElement(webDriver, By.xpath("//*[contains(text(), '" + this.packageName + "')]"));
        if (!listEntry.isDisplayed()) {
            deviceLabApplicationsPage.logout();
            uploadAppToCloud();
        }
        listEntry.click();
        DeviceLabApplicationsPageInstallPopup deviceLabApplicationsPageInstallPopup = deviceLabApplicationsPage.clickInstall();
        deviceLabApplicationsPage = deviceLabApplicationsPageInstallPopup.confirmInstallation(this.deviceName);
        deviceLabApplicationsPage.logout();
    }

    //@Test funktioniert nie Überarbeiten
    public void testT01_uploadApp() {

        WebDriver webDriver = WebDriverManager.getWebDriver();
        setWindowSize(webDriver);
        LoginPage loginPage = new LoginPage(webDriver);
        final DashboardPage dashboardPage = loginPage.login(USERNAME_1, PASSWORD_1);
        DeviceLabApplicationsPage deviceLabApplicationsPage = dashboardPage.navigateToDeviceLabApplications();
        if (deviceLabApplicationsPage.appIsListed(UPLOAD_TESTING_APP_PACKAGE_NAME)) {
            DeviceLabApplicationsPageDeletePopup deletePopup = deviceLabApplicationsPage.clickDelete();
            deviceLabApplicationsPage = deletePopup.confirmDeletion();
        }
        DeviceLabApplicationsPageUploadPopup uploadPopup = deviceLabApplicationsPage.clickUpload();
        deviceLabApplicationsPage = uploadPopup.chooseApk(PATH_TO_UPLOAD_TEST_APK);
        deviceLabApplicationsPage.assertAppIsListedInTable(UPLOAD_TESTING_APP_PACKAGE_NAME);
    }
}
