package eu.tsystems.mms.tic.testframework.mobile.cloud.devicegroups;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.AbstractCloudApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevice;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevices;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Projects;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.ProjectsApi;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rnhb on 09.12.2016.
 */
public class DeviceGroupsApi extends AbstractCloudApi {

    private final String prefix = "device-groups/";

    public DeviceGroups getDeviceGroups() {
        JsonObject jsonObject = get(prefix);
        JsonObject data = jsonObject.getAsJsonObject("data");

        DeviceGroups deviceGroups = new DeviceGroups();
        for (Map.Entry<String, JsonElement> jsonElement : data.entrySet()) {
            DeviceGroup deviceGroup = new DeviceGroup();
            deviceGroup.name = jsonElement.getValue().getAsString();
            deviceGroup.id = jsonElement.getKey();
            deviceGroups.add(deviceGroup);
        }
        return deviceGroups;
    }

    public Projects getProjectsOfDeviceGroup(DeviceGroup deviceGroup) {
        return getProjectsOfDeviceGroup(deviceGroup.id);
    }

    public Projects getProjectsOfDeviceGroup(String groupId) {
        JsonObject jsonObject = get(prefix + groupId + "/projects");
        return ProjectsApi.parseFromJson(jsonObject);
    }

    public MobileDevices getDevicesOfDeviceGroup(DeviceGroup deviceGroup) {
        return getDevicesOfDeviceGroup(deviceGroup.id);
    }

    public MobileDevices getDevicesOfDeviceGroup(String groupId) {
        JsonObject jsonObject = get(prefix + groupId + "/devices");
        Gson gson = new Gson();
        MobileDevices mobileDevices = new MobileDevices();
        for (JsonElement jsonElement : jsonObject.get("data").getAsJsonArray()) {
            MobileDevice mobileDevice = gson.fromJson(jsonElement, MobileDevice.class);
            mobileDevices.add(mobileDevice);
        }
        return mobileDevices;
    }

    public void addDevice(DeviceGroup deviceGroup, MobileDevice mobileDevice) {
        JsonObject jsonObject = put(prefix + deviceGroup.id + "/devices", "deviceIdList=" + mobileDevice.id);
    }

    public void addDevices(DeviceGroup deviceGroup, MobileDevices mobileDevices) {
        List<String> deviceIdList = mobileDevices.elements.values().stream().map(mobileDevice -> mobileDevice.id).collect(Collectors.toList());
        String deviceIds = deviceIdList.toString().replace("[", "").replace("]", "");
        JsonObject jsonObject = put(prefix + deviceGroup.id + "/devices", "deviceIdList=" + deviceIds);
    }

    public void removeDevice(DeviceGroup deviceGroup, MobileDevice mobileDevice) {
        JsonObject jsonObject = delete(prefix + deviceGroup.id + "/devices", "deviceIdList=" + mobileDevice.id);
    }

    public void removeDevices(DeviceGroup deviceGroup, MobileDevices mobileDevices) {
        List<String> deviceIdList = mobileDevices.elements.values().stream().map(mobileDevice -> mobileDevice.id).collect(Collectors.toList());
        String deviceIds = deviceIdList.toString().replace("[", "").replace("]", "");
        JsonObject jsonObject = delete(prefix + deviceGroup.id + "/devices", "deviceIdList=" + deviceIds);
    }
}
