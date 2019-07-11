package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.execution.testng.worker.MethodWorker;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileScreenshotWorker;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileVideoWorker;
import eu.tsystems.mms.tic.testframework.report.FennecListener;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by rnhb on 06.04.2016.
 */
//FIXME complete rework or move to different module
public class MobileTestListener implements ITestListener, IConfigurationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileTestListener.class);
    private static boolean registeredInXetaListener = false;

    @Override
    public void onTestStart(ITestResult result) {
        if (MobileDriverManager.hasActiveMobileDriver()) {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
            if (mobileDriver.getActiveDevice() != null) {
                LOGGER.info("Active device:" + mobileDriver.getActiveDevice().getName());
                boolean saveVideoOnFail = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO_TEST_FAILED);
                boolean alwaysSaveVideo = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, DefaultParameter.MOBILE_REPORT_SAVE_VIDEO);

                if (saveVideoOnFail || alwaysSaveVideo) {
                    mobileDriver.startVideoRecord();
                }
            }
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        if (!registeredInXetaListener) {
            registeredInXetaListener = true;
            MobileDriverManager.registerWebDriverFactory();
//            FennecListener.registerAfterMethodWorker(MobileVideoWorker.class);

            FennecListener.registerAfterMethodWorker(MobileScreenshotWorker.class);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            if (MobileDriverManager.hasActiveMobileDriver()) {
                ScreenshotTracker.stopTracking();
                MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
                mobileDriver.release();
            }
        } catch (Exception e) {
            LOGGER.error("Exception on handling test finish.", e);
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {

    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {

    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {

    }
}
