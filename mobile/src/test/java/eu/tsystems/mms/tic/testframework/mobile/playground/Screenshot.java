package eu.tsystems.mms.tic.testframework.mobile.playground;

import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.testng.annotations.Test;

public class Screenshot {

    /**
     * Check Screenshot Quality from Cloud.
     */
    @Test
    public void T01_Screenshot_Quality() {
        System.setProperty(MobileProperties.MOBILE_SERVER_HOST, "localhost");
        System.setProperty(MobileProperties.MOBILE_GRID_USER, "xeta_systemtest");
        System.setProperty(MobileProperties.MOBILE_GRID_PASSWORD, "Mas4test#");
        System.setProperty(MobileProperties.MOBILE_GRID_PROJECT, "Testing");
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=android");
        System.setProperty(MobileProperties.MOBILE_SCREENSHOT_QUALITY, "100");
        System.setProperty(TesterraProperties.PROXY_SETTINGS_LOAD, "false");
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDeviceByFilter();
        for (int i = 0; i < 20; i++) {
            mobileDriver.takeBeforeScreenshot();
            TimerUtils.sleep(5000);
        }
    }
//    TODO do we need this anywhere?
//    private void prep(MobileDriver md) {
//        String capturePath = md.seeTestClient().capture();
//        if (capturePath == null || capturePath.equals("")) {
//            return;
//        }
//        String imageFile = md.getActiveDevice().getName() + "_" + Paths.get(capturePath.replace('\\', '/')).getFileName().toString();
//        Path screenshotDestinationPath = Paths.get(ReportUtils.getScreenshotsPath() + imageFile);
//        File screenshotFolder = new File(ReportUtils.getScreenshotsPath());
//        if (!screenshotFolder.exists()) {
//            screenshotFolder.mkdirs();
//        }
//        try {
//            md.seeTestClient().getRemoteFile(capturePath, 10000, screenshotDestinationPath.toAbsolutePath().toString());
//        } catch (Exception e) {
//        }
//    }
}
