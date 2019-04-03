package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ncbe on 05.07.2016.
 */
public class OrFilter extends LogicalFilter {

    @Override
    protected List<TestDevice> initDeviceList(List<TestDevice> testDevices) {
        List<TestDevice> deviceList = new ArrayList<>();
        return deviceList;
    }

    @Override
    void applyLogicalAction(List<TestDevice> allDevices, List<TestDevice> filteredDevices) {
        for (TestDevice testDevice : filteredDevices) {
            if (!allDevices.contains(testDevice)) {
                allDevices.add(testDevice);
            }
        }
    }

    @Override
    public String toString() {
        return "or(" + deviceFilters + ")";
    }
}
