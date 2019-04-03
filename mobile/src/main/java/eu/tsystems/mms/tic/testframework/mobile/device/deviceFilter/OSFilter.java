package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncbe on 27.06.2016.
 */
public class OSFilter implements DeviceFilter {

    private final String os;

    public OSFilter(String os) {
        this.os = os.trim();
    }

    @Override
    public List<TestDevice> apply(List<TestDevice> testDevices) {

        if (os.equalsIgnoreCase("ANY")) {
            return testDevices;
        }

        List<TestDevice> foundDevices = new ArrayList<>();
        for (TestDevice td : testDevices) {
            if (td != null && td.getOperatingSystem() != null && td.getOperatingSystem().toString().equalsIgnoreCase(os)) {
                foundDevices.add(td);
            }
        }
        return foundDevices;
    }

    @Override
    public String toString() {
        return "os.type = " + os;
    }
}
