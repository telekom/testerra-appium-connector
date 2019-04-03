package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MobileDriverUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileDriver.class);

    /**
     * Copy file locally or download via driver.
     *
     * @param pathFromSeeTest path to file to get.
     * @param destinationPath Destination to copy file to.
     */
    public static boolean getRemoteOrLocalFile(MobileDriver mobileDriver, String pathFromSeeTest, Path destinationPath) {
        if (StringUtils.isEmpty(pathFromSeeTest)) {
            LOGGER.error("Cannot move File with empty remoteFilePath");
            return false;
        }
        Path cleanedPathFromSeeTest = Paths.get(pathFromSeeTest.replace('\\', '/'));
        try {
            if (cleanedPathFromSeeTest.isAbsolute() && cleanedPathFromSeeTest.toFile().exists()) {
                // copy file on local filesystem
                Files.copy(cleanedPathFromSeeTest, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // download file from grid or remote executor
                mobileDriver.seeTestClient().getRemoteFile(pathFromSeeTest, 10000,
                        destinationPath.toAbsolutePath().toString());
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to move file created by SeeTest to destination. SeeTest file: "
                    + cleanedPathFromSeeTest + ". Destination: " + destinationPath, e);
            return false;
        }
    }
}
