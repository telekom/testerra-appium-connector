package eu.tsystems.mms.tic.testframework.mobile.device.deviceFilter;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.List;

/**
 * Created by ncbe on 27.06.2016.
 * <p>
 * known attributes:
 * - manufacturer
 * - os.type
 * - name
 * - os.version
 * <p>
 * Example:
 * deviceFilter = manufacturer = samsung |sony & os.type = android & os.version = 5.* | 6*
 */
public interface DeviceFilter {

    public List<TestDevice> apply(List<TestDevice> testDevices);

}