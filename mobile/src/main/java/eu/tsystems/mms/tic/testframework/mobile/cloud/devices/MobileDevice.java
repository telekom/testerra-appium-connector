package eu.tsystems.mms.tic.testframework.mobile.cloud.devices;

import com.google.gson.annotations.SerializedName;
//TODO rework to core-interop
//import eu.tsystems.mms.tic.testframework.internal.DataContainer;
import eu.tsystems.mms.tic.testframework.mobile.cloud.Nameable;

import java.util.Objects;

/**
 * Created by rnhb on 12.12.2016.
 */
public class MobileDevice implements Nameable
        /*TODO rework to core-interop , DataContainer*/  {


    @SerializedName("iosUdid")
    public String iosUdid;

    @SerializedName("notes")
    public String notes;

    @SerializedName("deviceOs")
    public String deviceOs;

    @SerializedName("isEmulator")
    public String isEmulator;

    @SerializedName("displayStatus")
    public String displayStatus;

    @SerializedName("deviceName")
    public String deviceName;

    @SerializedName("statusTooltip")
    public String statusTooltip;

    @SerializedName("agentIp")
    public String agentIp;

    @SerializedName("agentLocation")
    public String agentLocation;

    @SerializedName("manufacturer")
    public String manufacturer;

    @SerializedName("isCleanupEnabled")
    public String isCleanupEnabled;

    @SerializedName("osVersion")
    public String osVersion;

    @SerializedName("model")
    public String model;

    @SerializedName("statusModifiedAt")
    public String statusModifiedAt;

    @SerializedName("id")
    public String id;

    @SerializedName("udid")
    public String udid;

    @SerializedName("currentStatus")
    public String currentStatus;

    @SerializedName("statusModifiedAtDateTime")
    public String statusModifiedAtDateTime;

    @SerializedName("profiles")
    public String profiles;

    @SerializedName("agentName")
    public String agentName;

    @SerializedName("uptime")
    public String uptime;

    @SerializedName("currentUser")
    public String currentUser;

    @SerializedName("deviceCategory")
    public String deviceCategory;

    @SerializedName("statusAgeInMinutes")
    public String statusAgeInMinutes;

    @SerializedName("lastUsedDateTime")
    public String lastUsedDateTime;

    @SerializedName("previousStatus")
    public String previousStatus;

    @Override
    public String name() {
        return deviceName;
    }

    @Override
    public String toString() {
        return "MobileDevice " + deviceName + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileDevice that = (MobileDevice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
