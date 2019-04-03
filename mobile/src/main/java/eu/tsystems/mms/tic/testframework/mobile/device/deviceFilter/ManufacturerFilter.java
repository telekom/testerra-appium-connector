package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncbe on 27.06.2016.
 */
public class ManufacturerFilter implements DeviceFilter {

    private final String manufacturer;

    public ManufacturerFilter(String manufacturer) {
        this.manufacturer = manufacturer.trim();
    }

    @Override
    public List<TestDevice> apply(List<TestDevice> testDevices) {

        if (manufacturer.equalsIgnoreCase("ANY")) {
            return testDevices;
        }

        List<TestDevice> foundDevices = new ArrayList<>();
        for (TestDevice td : testDevices) {
            if (td != null && td.getManufacturer() != null && td.getManufacturer().equalsIgnoreCase(manufacturer)) {
                foundDevices.add(td);
            }
        }
        return foundDevices;
    }

    @Override
    public String toString() {
        return "manufacturer = " + manufacturer;
    }
}
