package eu.tsystems.mms.tic.testframework.mobile.device;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;

/**
 * Created by rnhb on 30.04.2015.
 */
public class DeviceLog {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<String> log;

    public DeviceLog(Path deviceLogPath) {
        log = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(deviceLogPath.toFile()));) {
            String line = br.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    log.add(line);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new FennecRuntimeException("No log found at " + deviceLogPath);
        } catch (IOException e) {
            throw new FennecRuntimeException("Error when reading the log at " + deviceLogPath);
        }
    }

    public boolean containsEntry(String entry) {
        for (String l : log) {
            if (l.equals(entry)) {
                logger.info("Found entry " + entry);
                return true;
            }
        }
        return false;
    }

    public boolean containsString(String string) {
        for (String l : log) {
            if (l.contains(string)) {
                logger.info("Found entry containing " + string + ": " + l);
                return true;
            }
        }
        return false;
    }

    public String getEntryContainingString(String string) {
        for (String l : log) {
            if (l.contains(string)) {
                return l;
            }
        }
        return "";
    }

    public boolean hasEntryMatching(String regex) {
        for (String l : log) {
            if (l.matches(regex)) {
                logger.info("Found entry matching " + regex + ": " + l);
                return true;
            }
        }
        return false;
    }
}
