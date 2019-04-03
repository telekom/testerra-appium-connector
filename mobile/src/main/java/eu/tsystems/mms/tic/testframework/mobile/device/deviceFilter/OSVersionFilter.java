package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncbe on 28.06.2016.
 */
public class OSVersionFilter implements DeviceFilter {

    private final String versionOS;

    public OSVersionFilter(String versionOS) {
        this.versionOS = versionOS.trim();
    }

    @Override
    public List<TestDevice> apply(List<TestDevice> testDevices) {

        List<TestDevice> foundDevices = new ArrayList<>();

        if (versionOS.equalsIgnoreCase("ANY")) {
            return testDevices;
        }

        if (versionOS.endsWith("*")) {
            String modifiedVersionOs = versionOS.replaceAll("\\*", "");
            for (TestDevice td : testDevices) {
                if (td != null && td.getOperatingSystemVersion() != null && td.getOperatingSystemVersion().startsWith(modifiedVersionOs)) {
                    foundDevices.add(td);
                }
            }
            return foundDevices;
        } else {
            for (TestDevice td : testDevices) {
                if (td != null && td.getOperatingSystemVersion() != null && td.getOperatingSystemVersion().equalsIgnoreCase(versionOS)) {
                    foundDevices.add(td);
                }
            }
            return foundDevices;

        }
    }

    @Override
    public String toString() {
        return "os.version = " + versionOS;
    }
}
