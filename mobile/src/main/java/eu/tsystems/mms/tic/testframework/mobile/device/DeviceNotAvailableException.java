package eu.tsystems.mms.tic.testframework.mobile.device;

import eu.tsystems.mms.tic.testframework.mobile.driver.ReservationStatus;

/**
 * Created by rnhb on 18.12.2015.
 */
public class DeviceNotAvailableException extends RuntimeException {
    public DeviceNotAvailableException(TestDevice testDevice, ReservationStatus reservationStatus) {
        super((testDevice == TestDevice.NONE ? "No device" : testDevice.getName() + " not") + " available. Status: " + reservationStatus);
    }

    public DeviceNotAvailableException(TestDevice testDevice, String message) {
        super((testDevice == TestDevice.NONE ? "No device" : testDevice.getName() + " not") + " available. " + message);
    }
}
