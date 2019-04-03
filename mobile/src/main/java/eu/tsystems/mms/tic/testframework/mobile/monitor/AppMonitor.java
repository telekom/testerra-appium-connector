package eu.tsystems.mms.tic.testframework.mobile.monitor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.tsystems.mms.tic.testframework.internal.ConsumptionMeasurementsCollector;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.report.utils.ReportUtils;

/**
 * Created by sbke on 29.11.2017.
 */
//TODO implement as listener ?
public class AppMonitor {

    private MobileDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMonitor.class);

    public AppMonitor(MobileDriver driver) {
        this.driver = driver;
    }

    public void createReportTab() throws IOException {
        String monitorDataString = driver.seeTestClient().getMonitorsData("");
        if (checkMonitoringDataIsEmpty(monitorDataString)) {
            LOGGER.error("No monitoring data has been recorded.");
            return;
        }
        String deviceName = getDeviceNameFromMonitorsData(monitorDataString);
        List<CSVRecord> csvRecords = getCsvRecords(monitorDataString);
        ConsumptionMeasurementsCollector appMeasurementsCollector = new ConsumptionMeasurementsCollector();
        addDeviceMeasurements(appMeasurementsCollector, csvRecords);
        ReportUtils.addExtraTopLevelConsumptionMeasurementsTab(deviceName + " monitor", deviceName + ".html", true, appMeasurementsCollector);
    }

    private void addDeviceMeasurements(ConsumptionMeasurementsCollector cmc, List<CSVRecord> csvRecords) {
        ConsumptionMeasurementsCollector.ContextMeasurement deviceCpuUsage =
                new ConsumptionMeasurementsCollector.ContextMeasurement("CPU", "CPU", "%");
        ConsumptionMeasurementsCollector.ContextMeasurement deviceMemUsage =
                new ConsumptionMeasurementsCollector.ContextMeasurement("Memory Usage", "Mem", "MB");
        for (CSVRecord csvRecord : csvRecords) {
            addDeviceRecordsToMeasurementsCollector(csvRecord, cmc, deviceCpuUsage, deviceMemUsage);
        }
    }

    private void addDeviceRecordsToMeasurementsCollector(CSVRecord csvRecord, ConsumptionMeasurementsCollector cmc,
                                                         ConsumptionMeasurementsCollector.ContextMeasurement appCpuUsage, ConsumptionMeasurementsCollector.ContextMeasurement appMemUsage) {
        Long timestamp = getTimeStamp(csvRecord);
        long cpuValue = getDeviceCpuValue(csvRecord);
        cmc.addValue(appCpuUsage, cpuValue, timestamp);
        long memoryValue = getDeviceMemoryValue(csvRecord);
        cmc.addValue(appMemUsage, memoryValue, timestamp);
    }

    private String getDeviceNameFromMonitorsData(String monitorData) {
        if (monitorData.indexOf("\n") > 0) {
            return monitorData.substring(0, monitorData.indexOf("\n"));
        } else {
            return "";
        }
    }

    private List<CSVRecord> getCsvRecords(String monitorDataString) throws IOException {
        if (monitorDataString.indexOf("\n") > 0) {
            monitorDataString = monitorDataString.substring(monitorDataString.indexOf("\n") + 1);
            Reader in = new StringReader(monitorDataString);
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(in);
            return parser.getRecords();
        } else {
            return new ArrayList<>();
        }
    }

    private Long getTimeStamp(CSVRecord csvRecord) {
        String dateString = csvRecord.get("Date");
        Locale timeStampLocale;
        if(dateString.contains("MESZ")) {
            timeStampLocale = Locale.GERMAN;
        } else {
            timeStampLocale = Locale.ENGLISH;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss z", timeStampLocale);
        Instant time = Instant.from(dtf.parse(dateString));
        return time.getEpochSecond() * 1000;
    }

    private long getDeviceCpuValue(CSVRecord csvRecord) {
        return getValueFromCsv(csvRecord, "cpu", "");
    }

    private long getDeviceMemoryValue(CSVRecord csvRecord) {
        return getValueFromCsv(csvRecord, "memory", "");
    }

    private long getValueFromCsv(CSVRecord csvRecord, String prefix, String packageName) {
        String value = csvRecord.get(prefix + packageName);
        if (value.isEmpty()) {
            return 0;
        } else {
            return Math.round(Double.parseDouble(value));
        }
    }

    private boolean checkMonitoringDataIsEmpty(String monitoringData) {
        if (monitoringData != null) {
            monitoringData = monitoringData.replaceAll("\\s*", "");
            return monitoringData.isEmpty();
        }
        return true;
    }

    public void startMonitoring() {
        // sbke: with empty string it does not work properly on all devices (seetest 11.2)
        driver.seeTestClient().startMonitor("cpu");
    }
}
