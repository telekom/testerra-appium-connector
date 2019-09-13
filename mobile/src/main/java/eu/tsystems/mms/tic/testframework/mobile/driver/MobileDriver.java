package eu.tsystems.mms.tic.testframework.mobile.driver;

import com.experitest.client.Client;
import com.experitest.client.MobileListener;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceLog;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.device.ViewOrientation;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import java.io.File;

/**
 * Created by rnhb on 12.07.2017.
 */
public interface MobileDriver extends TakesScreenshot {

    void registerDeviceTest(DeviceTest deviceTest);

    void clearDeviceTests();

    void setFiringKeyEvents(boolean b);

    boolean closeApplication(String packageName);

    void reserveDevice(TestDevice testDevice);

    TestDevice reserveDeviceByFilter(String filterProperty);

    TestDevice reserveDeviceByFilter();

    void takeBeforeScreenshot();

    void takeAfterScreenshot();

    File prepareNewScreenshot();

    void releaseDevice(TestDevice testDevice);

    @Deprecated
    boolean isDeviceConnected(TestDevice testDevice);

    void closeKeyboard();

    void swipe(Direction direction, float offsetInPixelOrScreenFraction, int timeInMs);

    void clearApplicationData(String packageName);

    void clearDeviceLog();

    DeviceLog getDeviceLog();

    boolean installApplication(String fullPathOfApp);

    boolean installApplication(String fullPathOfApp, boolean instrument);

    boolean uninstall(String application);

    void uninstallAllInstalledApps();

    void switchToDevice(TestDevice testDevice);

    @Deprecated
    void switchToAndWakeDevice(TestDevice testDevice);

    void openDevice();

    void closeDevice();

    void wakeDevice();

    void browserBack();

    void back();

    void pressHomeButton();

    void pressEnterOnKeyboard();

    void changeOrientationTo(ViewOrientation viewOrientation);

    ViewOrientation getOrientation();

    int getNumberOfElements(LocatorType locatorType, String identifier);

    Dimension getScreenResolution();

    void closeAllDevices();

    void performDragOnCoordinates(int startX, int startY, int endX, int endY, int timeToDrag);

    ElementAccessor element();

    TestDevice getActiveDevice();

    void pressBackspace();

    void launchApplication(String applicationString);

    void launchApplication(String applicationString, boolean instrumentApplication);

    void launchApplication(String applicationString, boolean instrumentApplication, boolean closeAppIfRunning);

    boolean isApplicationInstalled(String applicationString);

    void setGpsLocation(String latitude, String longitude);

    void clearGpsLocation();

    Client seeTestClient();

    void startVideoRecord();

    String stopVideoRecord();

    void shakeDevice();

    void setNetWorkConditionsProfile(String netWorkConditionsProfile);

    void addRecoveryListener(LocatorType locatorType, String xPath, MobileListener listener);

    void type(String textToType);

    void uninstallAllApps(String regexMatchingPackageName);

    String getCurrentlyRunningApplication();

    @Deprecated
    void report(String message);

    @Deprecated
    void report(String message, boolean takeScreenshot);

    @Deprecated
    void report(String message, boolean takeScreenshot, boolean status);

    @Deprecated
    void report(String message, String imagePath, boolean status);

    @Deprecated
    void setReporting(boolean reporting);

    /* (non-Javadoc)
     * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
     */
    @SuppressWarnings("unchecked")
    @Override
    <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException;

    void releaseAllDevices();

    void disableScreenshots(boolean disable);

    void closeSystemPopUps();

    void release();

    boolean isValid();

    boolean resetWlanOfActiveDevice();

    String getDevicesInformation();
}
