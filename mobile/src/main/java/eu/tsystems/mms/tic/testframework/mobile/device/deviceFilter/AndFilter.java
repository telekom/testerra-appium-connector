package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnhb on 07.07.2016.
 */
public class AndFilter extends LogicalFilter {

    @Override
    protected List<TestDevice> initDeviceList(List<TestDevice> testDevices) {
        List<TestDevice> deviceList = new ArrayList<>();
        deviceList.addAll(testDevices);
        return deviceList;
    }

    @Override
    void applyLogicalAction(List<TestDevice> allDevices, List<TestDevice> filteredDevices) {
        allDevices.retainAll(filteredDevices);
    }

    @Override
    public String toString() {
        return "and(" + deviceFilters + ")";
    }
}
