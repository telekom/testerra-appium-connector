package eu.tsystems.mms.tic.testframework.mobile.cloud.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by rnhb on 22.12.2016.
 */
public abstract class AbstractCloudApi {

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private void assertRequestSuccess(JsonObject jsonObject) {
        JsonElement status = jsonObject.get("status");
        if (!"SUCCESS".equalsIgnoreCase(status.getAsString())) {
            throw new RuntimeException("Request to cloud was not successful: " + jsonObject);
        }
    }

    protected JsonObject get(String apiPath) {
        LOGGER.debug("Executing Get: " + apiPath);
        String result = CloudApiClient.getInstance().get(apiPath);
        LOGGER.debug("Get Result: " + result);
        return parseJson(result);
    }

    protected JsonObject post(String apiPath, String... params) {
        LOGGER.debug("Executing Post: " + apiPath + ", params: " + Arrays.toString(params));
        String result = CloudApiClient.getInstance().post(apiPath, params);
        LOGGER.debug("Post Result: " + result);
        return parseJson(result);
    }

    protected JsonObject put(String apiPath, String... params) {
        LOGGER.debug("Executing Put: " + apiPath + ", params: " + Arrays.toString(params));
        String result = CloudApiClient.getInstance().put(apiPath, params);
        LOGGER.debug("Put Result: " + result);
        return parseJson(result);
    }

    protected JsonObject delete(String apiPath, String... params) {
        LOGGER.debug("Executing Delete: " + apiPath + ", params: " + Arrays.toString(params));
        String result = CloudApiClient.getInstance().delete(apiPath, params);
        LOGGER.debug("Delete Result: " + result);
        return parseJson(result);
    }

    private JsonObject parseJson(String result) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();
        assertRequestSuccess(jsonObject);
        return jsonObject;
    }
}
