package io.testerra.plugins.appium.seetest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public class SeeTestRestClient implements Loggable {

    private final Client client;

    private final String url;

    public SeeTestRestClient(String url) {
        this.url = url;
        this.client = ClientBuilder.newClient();
    }

    public Optional<JsonArray> getAbout() {
        Response response = this.getBuilder("/applications", SeeTestApis.PM).get();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            log().debug("No SeeTest host found. (Response status {})", response.getStatus());
            return Optional.empty();
        }

        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(response.readEntity(String.class), JsonArray.class);
        if (jsonArray.isJsonArray()) {
            return Optional.of(jsonArray);
        } else {
            log().debug("Cannot read about response: {}", response.readEntity(String.class));
            return Optional.empty();
        }
    }

//    public Optional<Object> downloadVideoStream(String id) {
//        Response response = this.getBuilder("/tests/" + id + "/video", SeeTestApis.REPORTER, MediaType.WILDCARD).get();
////mobiledevicecloud.t-systems-mms.eu/reporter/api/tests/1783/video
//        return Optional.empty();
//    }

    private Invocation.Builder getBuilder(String path, SeeTestApis api) {
        return getBuilder(path, api, MediaType.APPLICATION_JSON);
    }

    private Invocation.Builder getBuilder(String path, SeeTestApis api, String mediaType) {
        WebTarget webTarget = client
                .target(this.url)
                .path(api.getValue())
                .path(path);
        log().debug(webTarget.getUri().toString());
        Invocation.Builder builder = webTarget
                .request(mediaType)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AppiumProperties.MOBILE_GRID_ACCESS_KEY.asString());
        return builder;
    }

    enum SeeTestApis {
        PM("/api/v1"),
        REPORTER("/reporter/api"),
        ;

        private String value;

        SeeTestApis(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
