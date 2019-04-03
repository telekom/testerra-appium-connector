package eu.tsystems.mms.tic.testframework.mobile.device;

/**
 * Created by matz on 23.10.2015.
 */
public enum DeviceInfo {
        NAME ("Name"),
        SERIALNUMBER ("SerialNumber"),
        OPERATINGSYSTEM ("OperatingSystem"),
        VIEWORIENTATION ("ViewOrientation"),
        REPOSITORYNAME ("RepositoryName"),
        PHONENUMBER ("PhoneNumber"),
        MODEL ("Model"),
        OPERATINGSYSTEMVERSION ("OperatingSystemVersion"),
        MANUFACTURER ("Manufacturer"),
        SIMCARDNAME ("SimCardName"),
        INVALID ("no key matched"),
        DEVICE_TYPE("");

        private String info;

        DeviceInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }