package eu.tsystems.mms.tic.testframework.mobile.cloud.devices;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.AbstractCloudApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevice;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevices;

/**
 * Created by rnhb on 09.12.2016.
 */
public class DevicesApi extends AbstractCloudApi {

    private final String prefix = "devices/";

    public MobileDevices getDevices() {
        JsonObject jsonObject = get(prefix);
        return parseFromJson(jsonObject);
    }

    public static MobileDevices parseFromJson(JsonObject jsonObject) {
        Gson gson = new Gson();
        MobileDevices mobileDevices = new MobileDevices();
        for (JsonElement jsonElement : jsonObject.get("data").getAsJsonArray()) {
            MobileDevice mobileDevice = gson.fromJson(jsonElement, MobileDevice.class);
            mobileDevices.add(mobileDevice);
        }
        return mobileDevices;
    }
}
