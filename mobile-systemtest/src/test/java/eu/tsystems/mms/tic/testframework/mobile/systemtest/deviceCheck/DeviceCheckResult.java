package eu.tsystems.mms.tic.testframework.mobile.systemtest.deviceCheck;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

import java.util.ArrayList;

/**
 * Created by ncbe on 26.01.2017.
 */
public class DeviceCheckResult {

    private final TestDevice testDevice;
    private ArrayList<DeviceCheckError> errors;

    /**
     * Constructor.
     *
     * @param testDevice Testdevice
     */
    public DeviceCheckResult(TestDevice testDevice) {

        this.testDevice = testDevice;
        this.errors = new ArrayList<>();
    }

    /**
     * Adds Error to ErrorList.
     *
     * @param deviceCheckError DeviceCheckError
     */
    public void addError(DeviceCheckError deviceCheckError) {

        errors.add(deviceCheckError);
    }

    public TestDevice getDevice() {

        return testDevice;
    }

    public ArrayList<DeviceCheckError> getErrors() {

        return errors;
    }
}
