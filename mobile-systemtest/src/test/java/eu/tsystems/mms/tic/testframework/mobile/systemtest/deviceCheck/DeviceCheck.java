package eu.tsystems.mms.tic.testframework.mobile.systemtest.deviceCheck;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceNotAvailableException;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by rnhb on 04.10.2016.
 */
public class DeviceCheck {

    private HashMap<String, DeviceCheckResult> results = new HashMap<>();

    /**
     * Dataprovider for Devices from Devicesources.
     *
     * @return Array of CheckResults
     */
    @DataProvider(name = "mobileDevices")
    public Object[][] collectDevicesFromSources() {

        PropertyManager.loadProperties("devicecheck.properties");
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "name=*");

        DeviceStore deviceStoreTools = new DeviceStore();
        DeviceStore deviceStoreJson = new DeviceStore();

        //read Devices from Registered Tools
        HashMap<String, TestDevice> deviceMapTools = new HashMap<>();

        for (TestDevice deviceFromTool : deviceStoreTools.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER)) {

            deviceMapTools.put(deviceFromTool.getName(), deviceFromTool);
        }

        //read Devices from Json
        HashMap<String, TestDevice> deviceMapJson = new HashMap<>();

        for (TestDevice deviceFromJson : deviceStoreJson.getTestDevicesByFilter(MobileProperties.MOBILE_DEVICE_FILTER)) {
            deviceMapJson.put(deviceFromJson.getName(), deviceFromJson);
        }

        DeviceCheckResult deviceCheckResult;
        for (String deviceName : deviceMapTools.keySet()) {

            if (!deviceMapJson.containsKey(deviceName)) {
                deviceCheckResult = new DeviceCheckResult(deviceMapTools.get(deviceName));
                deviceCheckResult.addError(DeviceCheckError.DEVICE_NOT_IN_JSON);
                results.put(deviceName, deviceCheckResult);
                continue;
            }

            TestDevice testDeviceTool = deviceMapTools.get(deviceName);
            TestDevice testDeviceJson = deviceMapJson.get(deviceName);

            deviceCheckResult = new DeviceCheckResult(testDeviceTool);

            if (!testDeviceTool.getManufacturer().equals(testDeviceJson.getManufacturer())) {
                deviceCheckResult.addError(DeviceCheckError.DIFFERENT_MANUFACTURER);
            }

            if (!testDeviceTool.getModel().equals(testDeviceJson.getModel())) {
                deviceCheckResult.addError(DeviceCheckError.DIFFERENT_MODEL);
            }

            if (!testDeviceTool.getOperatingSystem().equals(testDeviceJson.getOperatingSystem())) {
                deviceCheckResult.addError(DeviceCheckError.DIFFERENT_OS);
            }

            if (!testDeviceTool.getOperatingSystemVersion().equals(testDeviceJson.getOperatingSystemVersion())) {
                deviceCheckResult.addError(DeviceCheckError.DIFFERENT_OS_VERSION);
            }
            results.put(deviceName, deviceCheckResult);
            deviceMapJson.remove(deviceName);
        }

        for (TestDevice testDeviceJson : deviceMapJson.values()) {

            deviceCheckResult = new DeviceCheckResult(testDeviceJson);
            deviceCheckResult.addError(DeviceCheckError.DEVICE_NOT_IN_SEETEST);
            results.put(testDeviceJson.getName(), deviceCheckResult);
        }

        Object[][] providerArray = new Object[results.size()][];
        int i = 0;
        for (DeviceCheckResult deviceCheckResultFinal : results.values()) {
            providerArray[i] = new Object[]{deviceCheckResultFinal};
            i++;
        }

        return providerArray;

    }

    /**
     * Evaluates the TestCheckResults and generates error messages.
     *
     * @param deviceCheckResult Testcheck Result
     */
    //    @Test(dataProvider = "mobileDevices", priority = 1)
    //    public void t01tdccheckDeviceSourcesTest(DeviceCheckResult deviceCheckResult) {
    //        //TODO: how to achive this with testerra?
    //        TestMethodContainer testMethodContainerForCurrentTestResult = ExecutionContextController.getTestMethodContainerForCurrentTestResult();
    //        if (testMethodContainerForCurrentTestResult != null) {
    //            testMethodContainerForCurrentTestResult.setTestMethodName("t01_tdc_checkDeviceSources_" + deviceCheckResult.getDevice().getName());
    //        }
    //
    //        String errorString = "";
    //        for (DeviceCheckError dce : deviceCheckResult.getErrors()) {
    //            errorString += ": " + dce.name();
    //        }
    //
    //        Assert.assertTrue(deviceCheckResult.getErrors().isEmpty(),
    //                "There where differences in device sources of json and seetest" + errorString);
    //    }

    /**
     * Collects all Devices that are existing in seetest.
     *
     * @return Array of CheckResults
     */
    @DataProvider(name = "mobileDevicesReservationState")
    public Object[][] collectDevicesReservationState() {

        ArrayList<DeviceCheckResult> temp = new ArrayList<>();

        for (DeviceCheckResult deviceCheckResult : results.values()) {
            if (!deviceCheckResult.getErrors().contains(DeviceCheckError.DEVICE_NOT_IN_SEETEST)) {
                temp.add(deviceCheckResult);
            }
        }

        Object[][] providerArray = new Object[temp.size()][];
        int i = 0;
        for (DeviceCheckResult deviceCheckResult : temp) {
            providerArray[i] = new Object[]{deviceCheckResult};
            i++;
        }

        return providerArray;
    }

    /**
     * Checks the reservation state of a device.
     *
     * @param deviceCheckResult Testcheck Result
     */
    //    @Test(dataProvider = "mobileDevicesReservationState", priority = 2)
    //    public void t02tdccheckReservationStateTest(DeviceCheckResult deviceCheckResult) {

    //        ExecutionContextController.getTestMethodContainerForCurrentTestResult()
    //                .setTestMethodName("t02_tdc_checkReservationState_" + deviceCheckResult.getDevice().getName());
    //
    //        TestDevice testDevice = deviceCheckResult.getDevice();
    //        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
    //        mobileDriver.
    //        ReservationStatus reservationStatus = mobileDriver.getReservationStatusOfDevice(testDevice);
    //        String deviceName = testDevice.getName();
    //
    //        boolean foundError = false;
    //        if (!reservationStatus.equals(ReservationStatus.ONLINE_UNRESERVED)) {
    //            deviceCheckResult.addError(DeviceCheckError.DEVICE_ILLEGAL_RESERVATION_STATE);
    //            foundError = true;
    //        }
    //
    //        results.put(deviceName, deviceCheckResult);
    //        Assert.assertFalse(foundError,
    //                "There where an illegal reservation state: " + reservationStatus.toString());
    //
    //    }

    /**
     * Collects all Devices that are reservable in seetest.
     *
     * @return Array of CheckResults
     */
    @DataProvider(name = "mobileDevicesReservation")
    public Object[][] collectDevicesReservation() {

        ArrayList<DeviceCheckResult> temp = new ArrayList<>();

        for (DeviceCheckResult deviceCheckResult : results.values()) {
            temp.add(deviceCheckResult);
        }

        Object[][] providerArray = new Object[temp.size()][];
        int i = 0;
        for (DeviceCheckResult deviceCheckResult : temp) {
            providerArray[i] = new Object[]{deviceCheckResult};
            i++;
        }

        return providerArray;

    }

    /**
     * Checks the reservation of a device.
     *
     * @param deviceCheckResult Testcheck Result
     */
    @Test(dataProvider = "mobileDevicesReservationState", priority = 3)
    public void t03tdccheckReservationTest(DeviceCheckResult deviceCheckResult) {

        //        ExecutionContextController.getTestMethodContainerForCurrentTestResult()
        //                .setTestMethodName("t03_tdc_checkReservation_" + deviceCheckResult.getDevice().getName());

        TestDevice testDevice = deviceCheckResult.getDevice();

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        boolean succesful = true;
        try {
            mobileDriver.reserveDevice(testDevice);
            mobileDriver.switchToDevice(testDevice);
        } catch (DeviceNotAvailableException e) {
            succesful = false;
            deviceCheckResult.addError(DeviceCheckError.DEVICE_NOT_AVAILABLE);
        } finally {
            mobileDriver.releaseAllDevices();
        }

        results.put(testDevice.getName(), deviceCheckResult);
        assertTrue("ReservationTest succesful", succesful);

    }

    /**
     * Collects all Devices that are reservable in seetest.
     *
     * @return Array of CheckResults
     */
    @DataProvider(name = "mobileDevicesApp")
    public Object[][] collectDevicesAppInstall() {

        ArrayList<DeviceCheckResult> temp = new ArrayList<>();

        for (DeviceCheckResult deviceCheckResult : results.values()) {

            if (!deviceCheckResult.getErrors().contains(DeviceCheckError.DEVICE_NOT_AVAILABLE)) {
                temp.add(deviceCheckResult);
            }
        }

        Object[][] providerArray = new Object[temp.size()][];
        int i = 0;
        for (DeviceCheckResult deviceCheckResult : temp) {
            providerArray[i] = new Object[]{deviceCheckResult};
            i++;
        }

        return providerArray;

    }

    /**
     * Checks the installation of the testapp to a device.
     *
     * @param deviceCheckResult Testcheck Result
     */
    @Test(dataProvider = "mobileDevicesApp", priority = 4)
    public void t03tdccheckAppTest(DeviceCheckResult deviceCheckResult) {

        //        ExecutionContextController.getTestMethodContainerForCurrentTestResult()
        //                .setTestMethodName("t03_tdc_checkAppInstall_" + deviceCheckResult.getDevice().getName());

        boolean deviceAvailable = true;
        boolean deviceAppInstalled = true;
        boolean deviceAppStarted = true;
        boolean deviceWlanReset = true;

        TestDevice testDevice = deviceCheckResult.getDevice();
        String appFullPath;
        MobileOperatingSystem mobileOperatingSystem = testDevice.getOperatingSystem();
        if (mobileOperatingSystem.equals(MobileOperatingSystem.ANDROID)) {
            appFullPath = "cloud:eu.tsystems.mms.tic.testframework.mobile.simpleticapp/.HomeActivity";
        } else if (mobileOperatingSystem.equals(MobileOperatingSystem.IOS)) {
            appFullPath = "cloud:eu.tsystems.mms.tic.testframework.mobile.simpleticapp";
        } else {
            throw new RuntimeException(mobileOperatingSystem + " not supported in AppInstallTest()");
        }

        assertTrue("Loading App Name from properties failed", !appFullPath.isEmpty());

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

        try {
            mobileDriver.reserveDevice(testDevice);
            mobileDriver.switchToDevice(testDevice);
            boolean isInstalled = mobileDriver.installApplication(appFullPath);
            if (!isInstalled) {
                deviceCheckResult.addError(DeviceCheckError.APP_NOT_INSTALLED);
                deviceAppInstalled = false;
            }
            mobileDriver.launchApplication(appFullPath);
            //todo wlan reset is in pull request
            //deviceWlanReset = mobileDriver.resetWlan();
            if (!deviceWlanReset) {
                deviceCheckResult.addError(DeviceCheckError.DEVICE_WLAN_RESET_FAILED);
            }
        } catch (DeviceNotAvailableException e) {
            deviceCheckResult.addError(DeviceCheckError.DEVICE_NOT_AVAILABLE);
            deviceAvailable = false;
        } catch (TesterraRuntimeException xetaRuntimeException) {
            deviceCheckResult.addError(DeviceCheckError.APP_NOT_STARTED);
            deviceAppStarted = false;
        } finally {
            mobileDriver.releaseAllDevices();
        }
        results.put(testDevice.getName(), deviceCheckResult);
        assertTrue("Device was available", deviceAvailable);
        assertTrue("App was installed succesful", deviceAppInstalled);
        assertTrue("App was started succesful", deviceAppStarted);
        assertTrue("Wlan reset succesful", deviceWlanReset);
    }




    /*

        @DataProvider(name = "devices")
        public Object[][] collectDevices() throws DeviceNotAvailableException {

            PropertyManager.loadProperties("devicecheck.properties");
            String specificTestdevice = PropertyManager.getProperty("test.device");

            //ToSingleDevice
            System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "name=*");

            MobileDriverManager.deviceStore().readJsonFromResourceFile("clouddevices.json");
            Map<String, String> jsonDeviceVersions = new HashMap<>();
            List<TestDevice> testDevices = MobileDriverManager.deviceStore().getTestDevicesByFilter();
            for (TestDevice testDevice : testDevices) {
                jsonDeviceVersions.put(testDevice.getName(),testDevice.getOperatingSystemVersion());
            }
            MobileDriverManager.deviceStore().fillFromRegisteredTools();
            testDevices = MobileDriverManager.deviceStore().getTestDevicesByFilter();
            // if specific testdevice is set in properties, only this will be used
            if(specificTestdevice != null && !specificTestdevice.isEmpty()) {
                TestDevice device = MobileDriverManager.deviceStore().getDevice(specificTestdevice);
                testDevices= new ArrayList<>();
                testDevices.add(device);
            }

            List<DeviceCheckResult> deviceCheckResults = new ArrayList<>(testDevices.size());
            for (TestDevice testDevice : testDevices) {
                ReservationStatus reservationStatus = mobileDriver.getReservationStatusOfDevice(testDevice);
                String osVersJson = jsonDeviceVersions.get(testDevice.getName());
                osVersJson = testDevice.getOperatingSystemVersion();

                //DeviceCheckResult deviceCheckResult = new DeviceCheckResult(reservationStatus, testDevice, osVersJson);
                //deviceCheckResults.add(deviceCheckResult);
            }
            //zusammenbau der Dataproviders
            Object[][] providerArray = new Object[deviceCheckResults.size()][];
            int i = 0;
            for (DeviceCheckResult deviceCheckResult : deviceCheckResults) {
                providerArray[i] = new Object[]{deviceCheckResult};
                i++;
            }

            return providerArray;
        }

        @DataProvider(name = "getAvailableDevices")
        public Object[][] getAvailableDevices() throws DeviceNotAvailableException{
            if(this.availableDevices.size()<1)
                return this.collectDevices();

            Object[][] providerArray = new Object[this.availableDevices.size()][];

            int i = 0;
            for (DeviceCheckResult deviceCheckResult : this.availableDevices) {
                providerArray[i] = new Object[]{deviceCheckResult};
                i++;
            }

            return providerArray;
        }

        @DataProvider(name = "appInstallDevices")
        public Object[][] appInstallDevices() throws DeviceNotAvailableException{
            if(this.appInstallSuccessfullDevices.size()<1)
                return this.collectDevices();

            Object[][] providerArray = new Object[this.appInstallSuccessfullDevices.size()][];

            int i = 0;
            for (DeviceCheckResult deviceCheckResult : this.appInstallSuccessfullDevices) {
                providerArray[i] = new Object[]{deviceCheckResult};
                i++;
            }

            return providerArray;
        }

        //Check if Correct Wlan MMSWPMC

        public void initCurrentDevice(DeviceCheckResult deviceCheckResult) throws Exception {
            mobileDriver.reserveDevice(deviceCheckResult.testDevice);
            mobileDriver.switchToAndWakeDevice(deviceCheckResult.testDevice);
            mobileDriver.pressHomeButton();
        }

        public boolean installApplication() {
            if(mobileDriver.isApplicationInstalled(application)) {
                mobileDriver.uninstall(application);
            }
            return mobileDriver.installApplication(application);
        }

        @Test(dataProvider = "devices",priority = 1)
        public void t01_tdc_testDeviceCheck_Availability(DeviceCheckResult deviceCheckResult) throws Exception {
            ExecutionContextController.getTestMethodContainerForCurrentTestResult()
                    .setTestMethodName("t01_tdc_testDeviceCheck_Availability_" + deviceCheckResult.testDevice.getName());

            if (!(deviceCheckResult.reservationStatus.equals(ReservationStatus.ONLINE_RESERVED_BY_YOU) ||
                    deviceCheckResult.reservationStatus.equals(ReservationStatus.ONLINE_UNRESERVED) ||
                    deviceCheckResult.reservationStatus.equals(ReservationStatus.UNKNOWN))) {
                Assert.fail("Neither " + ReservationStatus.ONLINE_RESERVED_BY_YOU +
                        ", nor " + ReservationStatus.ONLINE_UNRESERVED + ".\n" +
                        "Actual Status " + deviceCheckResult.reservationStatus);
            }

            String osVersJson = deviceCheckResult.osVersionJson;
            String actualVersion = deviceCheckResult.testDevice.getOperatingSystemVersion();
            String testDevice_Name = deviceCheckResult.testDevice.getName();
            if(osVersJson!=null) {
                NonFunctionalAssert.assertEquals(actualVersion,osVersJson,
                        "Device: "+testDevice_Name+" OS-Version in Json equals Actual Version"
                );
            } else {
                NonFunctionalAssert.fail("Device "+testDevice_Name+" not in Json Device List");
            }


            availableDevices.add(deviceCheckResult);
        }

        @Test(dataProvider = "getAvailableDevices", priority = 2)
        public void t02_tdc_CheckBrowserWorks(DeviceCheckResult deviceCheckResult) throws Exception {
            ExecutionContextController.getTestMethodContainerForCurrentTestResult()
                    .setTestMethodName("t02_tdc_CheckBrowserWorks_" + deviceCheckResult.testDevice.getName());

            initCurrentDevice(deviceCheckResult);

            mobileDriver.launchApplication("http://www.google.de", true);


            String[] possibleXpathes = {
                    "xpath=//*[contains(text(),'Offline')]",
                    "xpath=//*[contains(text(),'offline')]",
                    "xpath=//*[contains(@contentDescription,'offline')]",
                    "xpath=//*[@class='android.widget.Button']",
                    "xpath=//*[@id='tsbb']"
            };

            boolean foundGuiElement=false;
            //Checks all possible Xpaths and returns true if someone matched
            for (int i = 0; !foundGuiElement && i < possibleXpathes.length; i++) {
                foundGuiElement= new WebMobileGuiElement(possibleXpathes[i]).isDisplayed();
            }

            Assert.assertTrue(foundGuiElement, "Browser is working");
        }

        @Test(dataProvider = "getAvailableDevices", priority = 2)
        public void t03_tdc_CheckAppInstallation(DeviceCheckResult deviceCheckResult)throws Exception {
            ExecutionContextController.getTestMethodContainerForCurrentTestResult()
                    .setTestMethodName("t03_tdc_CheckAppInstallation_" + deviceCheckResult.testDevice.getName());
            initCurrentDevice(deviceCheckResult);


            boolean isInstalledSuccessfully = installApplication();

            Assert.assertTrue(isInstalledSuccessfully,"Installation successfull");
            appInstallSuccessfullDevices.add(deviceCheckResult);
        }

        @Test(dataProvider = "appInstallDevices",priority = 3)
        public void t04_tdc_CheckWlan(DeviceCheckResult deviceCheckResult) throws Exception {
            ExecutionContextController.getTestMethodContainerForCurrentTestResult()
                    .setTestMethodName("t04_tdc_CheckWlan_" + deviceCheckResult.testDevice.getName());

            initCurrentDevice(deviceCheckResult);
            installApplication();

            mobileDriver.launchApplication(application);
            NativeMobileGuiElement options = new NativeMobileGuiElement("xpath=//*[@class='android.s
            upport.v7.widget.ActionMenuPresenter$OverflowMenuButton']");
            options.assertIsDisplayed();
            options.click();
            NativeMobileGuiElement getInfo = new NativeMobileGuiElement("xpath=//*[@text='Get connectivity info']");
            getInfo.assertIsDisplayed();
            getInfo.click();
            NativeMobileGuiElement check = new NativeMobileGuiElement("xpath=//*[@text='Check connectivity']");
            check.assertIsDisplayed();
            check.click();
            NativeMobileGuiElement status = new NativeMobileGuiElement("xpath=//*[@id='connectivity_info']");
            boolean gotWiFI = status.getText().contains("Connected to WIFI");
            Assert.assertTrue(gotWiFI,"Device got Wifi");
            boolean isRightSSID = status.getText().contains("MMSWPMC");
            Assert.assertTrue(isRightSSID,"Device is in Network with SSID MMSWPMC");

        }

    @AfterMethod
    public void release() {
        getMobileDriver().releaseAllDevices();
    }*/
}
