package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnhb on 07.07.2016.
 */
public abstract class LogicalFilter implements DeviceFilter {
    final List<DeviceFilter> deviceFilters;

    public LogicalFilter() {
        this.deviceFilters = new ArrayList<>();
    }

    @Override
    public List<TestDevice> apply(List<TestDevice> testDevices) {
        List<TestDevice> devicesFilteredByAllFilters = initDeviceList(testDevices);

        for (DeviceFilter deviceFilter : deviceFilters) {
            List<TestDevice> devicesFilteredByOneFilter = deviceFilter.apply(testDevices);
            applyLogicalAction(devicesFilteredByAllFilters, devicesFilteredByOneFilter);
        }
        return devicesFilteredByAllFilters;
    }

    protected abstract List<TestDevice> initDeviceList(List<TestDevice> testDevices);

    public void addFilter(DeviceFilter deviceFilter) {
        deviceFilters.add(deviceFilter);
    }

    abstract void applyLogicalAction(List<TestDevice> allDevices, List<TestDevice> filteredDevices);
}
