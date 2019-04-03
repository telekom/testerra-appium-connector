package eu.tsystems.mms.tic.testframework.mobile.driver;

/**
 * Created by rnhb on 20.11.2017.
 */
public enum DeviceReservationPolicy {
    /**
     * Will only use devices that are online and not reserved by anyone.
     */
    ONLY_UNRESERVED_DEVICES {
        @Override
        public boolean isAllowed(ReservationStatus reservationStatus) {
            return reservationStatus == ReservationStatus.ONLINE_UNRESERVED;
        }
    },
    /**
     * Will only use devices, that are online and either unreserved or reserved by you.
     */
    UNRESERVED_OR_OWN_DEVICES {
        @Override
        public boolean isAllowed(ReservationStatus reservationStatus) {
            return reservationStatus == ReservationStatus.ONLINE_UNRESERVED
                    || reservationStatus == ReservationStatus.ONLINE_RESERVED_BY_YOU;
        }
    },
    /**
     * Will try to use any device that is online, even killing other reservations if necessary.
     */
    EXCLUSIVE_DEVICE_PRIORITY {
        @Override
        public boolean isAllowed(ReservationStatus reservationStatus) {
            return reservationStatus == ReservationStatus.ONLINE_UNRESERVED
                    || reservationStatus == ReservationStatus.ONLINE_RESERVED_BY_YOU
                    || reservationStatus == ReservationStatus.ONLINE_RESERVED_BY_OTHER;
        }
    };

    public static final DeviceReservationPolicy DEFAULT_POLICY = ONLY_UNRESERVED_DEVICES;

    public abstract boolean isAllowed(ReservationStatus reservationStatus);

    public static DeviceReservationPolicy get(String stringValue) {
        if (stringValue == null) {
            return null;
        }
        for (DeviceReservationPolicy deviceReservationPolicy : values()) {
            String cleanedPolicyName = deviceReservationPolicy.name().toLowerCase().replace("_", "");
            String cleanedValueName = stringValue.toLowerCase()
                    .replace("_", "")
                    .replace(".", "")
                    .replace(" ", "");
            if (cleanedPolicyName.equals(cleanedValueName)) {
                return deviceReservationPolicy;
            }
        }
        return null;
    }
}
