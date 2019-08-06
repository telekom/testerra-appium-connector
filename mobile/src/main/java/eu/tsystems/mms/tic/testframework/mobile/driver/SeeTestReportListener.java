package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.common.TesterraCommons;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by rnhb on 13.05.2015.
 */
public class SeeTestReportListener implements ITestListener, IConfigurationListener {

    private boolean monitoringActivated = PropertyManager.getBooleanProperty(MobileProperties.MOBILE_MONITORING_ACTIVE,
            DefaultParameter.MOBILE_MONITORING_ACTIVE);
    private int monitoringInterval = PropertyManager.getIntProperty(MobileProperties.MOBILE_MONITORING_INTERVAL,
            DefaultParameter.MOBILE_MONITORING_INTERVAL);

    private String reportType = PropertyManager.getProperty(MobileProperties.MOBILE_REPORT_TYPE, DefaultParameter.MOBILE_REPORT_TYPE);

    private static final Logger LOGGER = LoggerFactory.getLogger(SeeTestReportListener.class);


    /**
     * The report directory
     */
    private Path LOCAL_REPORT_DIRECTORY;
    private Path REMOTE_REPORT_DIRECTORY;
    private boolean reportDirectoryCreationResult;

    {
        //FIXME where to do init?
        TesterraCommons.init();

        String localReportPathProperty =
                PropertyManager.getProperty(MobileProperties.MOBILE_LOCAL_REPORT_PATH, DefaultParameter.MOBILE_LOCAL_REPORT_PATH);
        String remoteReportPathProperty =
                PropertyManager.getProperty(MobileProperties.MOBILE_REMOTE_REPORT_PATH, DefaultParameter.MOBILE_REMOTE_REPORT_PATH);


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        Calendar cal = Calendar.getInstance();
        String reportName = "\\report_" + dateFormat.format(cal.getTime());

        Path localReportPath = Paths.get(localReportPathProperty + reportName);
        Path remoteReportPath = Paths.get(remoteReportPathProperty + reportName);
        LOCAL_REPORT_DIRECTORY = localReportPath.toAbsolutePath();
        REMOTE_REPORT_DIRECTORY = remoteReportPath.toAbsolutePath();
        Path PARENT_FOLDER = Paths.get(localReportPathProperty);

        try {
            FileUtils.cleanDirectory(PARENT_FOLDER.toFile());
        } catch (Exception e) {
            LOGGER.error("Failed to clean directory for report \"" + PARENT_FOLDER + "\".", e);
        }

        try {
            reportDirectoryCreationResult = LOCAL_REPORT_DIRECTORY.toFile().mkdirs();
            LOGGER.info("Created directory " + LOCAL_REPORT_DIRECTORY + "for report: " + reportDirectoryCreationResult);
        } catch (Exception e) {
            LOGGER.error("Failed to create path for report \"" + LOCAL_REPORT_DIRECTORY + "\".", e);
        }
    }

    /**
     * Sets if mobile devices should be monitored. Currently, only the memory monitor is used.
     *
     * @param monitoringActivated activation state of the monitoring
     */

    public void setMonitoringActivated(boolean monitoringActivated) {
        this.monitoringActivated = monitoringActivated;
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

            String testName = result.getName();
            if (testName == null) {
                testName = "UNKNOWN";
            }

            if (!Files.isDirectory(LOCAL_REPORT_DIRECTORY)) {
                LOGGER.warn("Can't find report directory " + LOCAL_REPORT_DIRECTORY + ". Will still try to generate report.");
            }
            String returnValue = mobileDriver.seeTestClient().setReporter(reportType, REMOTE_REPORT_DIRECTORY.toString(), testName);
            LOGGER.info("Setting report directory to " + REMOTE_REPORT_DIRECTORY + ". Report name is " + testName
                    + ". Report will be created in " + returnValue + ".");

            if (monitoringActivated) {
                mobileDriver.seeTestClient().startMonitor("Memory");
                mobileDriver.seeTestClient().setMonitorPollingInterval(monitoringInterval);
                mobileDriver.seeTestClient().setMonitorTestState(testName);
            }
        } catch (Exception e) {
            LOGGER.error("Exception on handling test start.", e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        generateReport();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

            // If the test failed, report details of the last seetest command
            Map<String, Object> lastCommandResultMap = mobileDriver.seeTestClient().getLastCommandResultMap();
            mobileDriver.seeTestClient().report("Last Command: \n" + lastCommandResultMap.toString(), true);

            // also provide the screendump
            String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
            mobileDriver.seeTestClient().report("Screen Dump: \n" + visualDump, true);

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                String stackTraceSuffix = "";
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                if (stackTrace != null) {
                    stackTraceSuffix = Arrays.toString(stackTrace);
                }
                mobileDriver.report("StackTrace: " + stackTraceSuffix);
                mobileDriver.report("Screen on failure: " + throwable.toString(), true, false);
            } else {
                mobileDriver.seeTestClient().report("Test failed because of a TestNG assertion, but the throwable of the result was " +
                        "null. Check the Xeta Report!", false);
            }
        } catch (Exception e) {
            LOGGER.error("Exception on handling test failure.", e);
        }
        generateReport();
    }

    private void generateReport() {
        try {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
            String pathToMobileReport = mobileDriver.seeTestClient().generateReport(false);
            LOGGER.info("Generated Mobile Report: " + pathToMobileReport);
        } catch (Exception e) {
            LOGGER.error("Failed to generate report in directory " + LOCAL_REPORT_DIRECTORY, e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
            if (monitoringActivated) {
                mobileDriver.seeTestClient().getMonitorsData("MonitorData");
            }
            TestDevice activeDevice = mobileDriver.getActiveDevice();
            if (activeDevice != null) {
                mobileDriver.releaseDevice(activeDevice);
            }
            mobileDriver.seeTestClient().releaseClient();
        } catch (Exception e) {
            LOGGER.error("Exception on handling test finish.", e);
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {

    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        try {
            Throwable throwable = itr.getThrowable();
            if (throwable != null) {
                LOGGER.info("Error on configuration method.", throwable);
                MobileDriverManager.getMobileDriver().seeTestClient().report("Error on configuration method: " + throwable.getMessage(), false);
            } else {
                LOGGER.info("Unknown error on configuration method.");
                MobileDriverManager.getMobileDriver().seeTestClient().report("Unknown error on configuration method.", false);
            }
            generateReport();
        } catch (Exception e) {
            LOGGER.error("Exception on handling configuration failure.", e);
        }
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        onConfigurationFailure(itr);
    }
}
