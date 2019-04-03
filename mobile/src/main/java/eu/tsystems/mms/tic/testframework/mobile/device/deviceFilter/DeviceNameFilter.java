package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncbe on 28.06.2016.
 */
public class DeviceNameFilter implements DeviceFilter {

    private final String deviceName;

    public DeviceNameFilter(String deviceName) {
        this.deviceName = deviceName.trim();
    }

    @Override
    public List<TestDevice> apply(List<TestDevice> testDevices) {

        if (deviceName.equalsIgnoreCase("ANY")) {
            return testDevices;
        }

        final String deviceNameWithoutStar = deviceName.replaceAll("\\*", "");
        List<TestDevice> foundDevices = new ArrayList<>();
        for (TestDevice testDevice : testDevices) {
            if (testDevice != null && testDevice.getName() != null) {
                String deviceNameLowerCase = testDevice.getName().toLowerCase();
                boolean endsWithStar = deviceName.endsWith("*");
                boolean startsWithStar = deviceName.startsWith("*");

                boolean nameMatches;
                if (startsWithStar && endsWithStar) {
                    nameMatches = deviceNameLowerCase.contains(deviceNameWithoutStar);
                } else if (startsWithStar) {
                    nameMatches = deviceNameLowerCase.endsWith(deviceNameWithoutStar);
                } else if (endsWithStar) {
                    nameMatches = deviceNameLowerCase.startsWith(deviceNameWithoutStar);
                } else {
                    nameMatches = deviceNameLowerCase.equalsIgnoreCase(deviceNameWithoutStar);
                }

                if (nameMatches) {
                    foundDevices.add(testDevice);
                }
            }
        }
        return foundDevices;
    }

    @Override
    public String toString() {
        return "name = " + deviceName;
    }
}
