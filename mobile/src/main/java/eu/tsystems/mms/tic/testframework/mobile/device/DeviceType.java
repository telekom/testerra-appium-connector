package eu.tsystems.mms.tic.testframework.mobile.device;

/**
 * Created by rnhb on 18.12.2015.
 *
 * THere is no "UNKNOWN" for a reason. We want to allow filling the DeviceStor by a json file, then updating it with
 * data from the connected SeeTest, which will merge the new and old data. If the new data has DeviceType UNKNOWN,
 * it will overwrite an existing DeviceType that is correct. If the new type is null, it will not overwrite anything.
 */
public enum DeviceType {
    TABLET, PHONE;

    public static DeviceType valueOfIfExisting(String toCheck){
        for (DeviceType deviceType : DeviceType.values()) {
            if(deviceType.toString().equals(toCheck)){
                return deviceType;
            }
        }
        return null;
    }
}
