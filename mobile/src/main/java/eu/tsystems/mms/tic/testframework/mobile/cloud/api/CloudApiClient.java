package eu.tsystems.mms.tic.testframework.mobile.cloud.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.common.TesterraCommons;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * The CloudApiClient can be used to build Requests for the Cloud API REST API
 * and send them via HTTP. Any project and user specific data is read from the
 * cloudapi.properties resource file. This class is implemented as singleton.
 *
 * @author mibu
 */
public class CloudApiClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";

    static {
        TesterraCommons.init();
    }

    private Map<String, Integer> deviceNameToIdMap;

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudApiClient.class);
    /**
     * Charset UTF-8.
     */
    private static final String CHARSET = StandardCharsets.UTF_8.name();
    /**
     * Singleton instance.
     */
    private static CloudApiClient instance;


    /**
     * Get instance of CloudApiClient.
     *
     * @return {@link CloudApiClient}.
     */
    public synchronized static CloudApiClient getInstance() {
        if (instance == null) {
            instance = new CloudApiClient();
        }
        return instance;
    }

    /**
     * Returns the Cloud API connection properties. Initializes the connection
     * properties using the file <code>cloudapi.properties</code> in the
     * resources folder if not initialized already.
     * <p>
     * The file <code>cloudapi.properties</code> must include the worths for:
     * <ul>
     * <li>cloudapi.server</li>
     * <li>cloudapi.user</li>
     * <li>cloudapi.password</li>
     * <li>cloudapi.projectid</li>
     * </ul>
     *
     * @return The connection data as <code>LoginData</code> object.
     */
    public static CloudApiConfiguration getConnectionProperties() {
        return pGetConnectionProperties();
    }

    /**
     * Returns the Cloud API connection properties. Initializes the connection
     * properties using the file <code>cloudapi.properties</code> in the
     * resources folder if not initialized already.
     * <p>
     * The file <code>cloudapi.properties</code> must include the worths for:
     * <ul>
     * <li>cloudapi.server</li>
     * <li>cloudapi.user</li>
     * <li>cloudapi.password</li>
     * <li>cloudapi.projectid</li>
     * </ul>
     *
     * @return The connection data as <code>LoginData</code> object.
     */
    private static CloudApiConfiguration pGetConnectionProperties() {
        final CloudApiConfiguration cloudApiConf = new CloudApiConfiguration(
                PropertyManager.getProperty("cloudapi.server", null),
                PropertyManager.getProperty("cloudapi.user", null),
                PropertyManager.getProperty("cloudapi.password", null));

        LOGGER.trace("Cloud API Server: " + cloudApiConf.getServer());
        LOGGER.trace("Cloud API User: " + cloudApiConf.getUser());

        return cloudApiConf;
    }

    /**
     * Configuration to use.
     */
    private final CloudApiConfiguration cloudApiConf;

    /**
     * Constructor.
     */
    private CloudApiClient() {
        cloudApiConf = getConnectionProperties();
        deviceNameToIdMap = new HashMap<String, Integer>();

        LOGGER.info("Connecting to Mobile Device Cloud at " + cloudApiConf.getServer() + " as " + cloudApiConf.getUser());
    }

    /**
     * Get screenshot from device
     *
     * @param deviceName name of device to get screenshot from.
     * @param time       Time to get screeshot from, null for current.
     *
     * @return absolut path to screenshot file.
     *
     * @deprecated
     */
    @Deprecated
    public String getImageOfDevice(String deviceName, Long time) {
        return doGetImageOfDevice(deviceName, time);
    }

    public String get(String entity) {
        try {
            return doGet(entity);
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * @param entity can be "/users" / "/projects" / "/devices" etc
     */
    private String doGet(String entity) throws IOException {
        URL url = new URL(cloudApiConf.getServer() + entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty(AUTHORIZATION, BASIC + cloudApiConf.getAuthStringEnc());
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        logGet(url, (HttpURLConnection) urlConnection, result);
        if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
            return result;
        } else {
            throw new RuntimeException(result);
        }
    }

    private String createQuery(String... parameters) throws UnsupportedEncodingException {
        String query = "";
        for (String parameter : parameters) {
            if (query.length() > 0) {
                query += "&";
            }
            String[] split = parameter.split("=");
            query += String.format("%s=%s", split[0], URLEncoder.encode(String.valueOf(split[1]), CHARSET));
        }
        return query;
    }

    String post(String entity, String... parameters) {
        try {
            return doPost(entity, createQuery(parameters), "POST");
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }

    String put(String entity, String... parameters) {
        try {
            return doPost(entity, createQuery(parameters), "PUT");
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }

    String delete(String entity, String... parameters) {
        try {
            return doPost(entity, createQuery(parameters), "DELETE");
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }

    /**
     * @param entity can be "/users" / "/projects" / "/devices" etc String query =
     *               String.format("param1=%s&param2=%s", URLEncoder.encode(param1,
     *               charset), URLEncoder.encode(param2, charset));
     */
    private String doPost(String entity, String query, String method) throws IOException {
        URL url = new URL(cloudApiConf.getServer() + entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
        urlConnection.setRequestProperty(AUTHORIZATION, BASIC + cloudApiConf.getAuthStringEnc());
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        httpURLConnection.setRequestMethod(method);

        OutputStream output = urlConnection.getOutputStream();
        output.write(query.getBytes(StandardCharsets.UTF_8.name()));

        logPost(url, httpURLConnection);

        InputStream stream;

        if (httpURLConnection.getResponseCode() >= 400) {
            stream = httpURLConnection.getErrorStream();
        } else {
            stream = httpURLConnection.getInputStream();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuilder responseBuffer = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        in.close();

        if (httpURLConnection.getResponseCode() < 300) {
            return responseBuffer.toString();
        } else {
            throw new RuntimeException(responseBuffer.toString());
        }
    }

    private void logPost(URL url, HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        LOGGER.info("Sending 'POST' request to URL : " + url);
        LOGGER.info("Response Code : " + responseCode);
    }

    private void logGet(URL url, HttpURLConnection httpURLConnection, String result) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        LOGGER.debug("Sending 'GET' request to URL : " + url);
        LOGGER.debug("Response Code : " + responseCode);
        LOGGER.debug(result);
    }

    /**
     * Get screenshot from device
     *
     * @param deviceName name of device to get screenshot from.
     * @param time       Time to get screeshot from
     *
     * @return path to screenshot file.
     */
    @Deprecated
    private String doGetImageOfDevice(String deviceName, Long time) {
        try {
            Integer device = getDeviceIdByName(deviceName);
            if (device == null) {
                return null;
            }
            if (time == null) {
                time = System.currentTimeMillis();
            }
            String urlString = cloudApiConf.getServer() + "devices/" + device + "?time=" + time;
            if (urlString.contains("/v1/")) {
                urlString = urlString.replace("/v1/", "/v2/");
            }
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(AUTHORIZATION, BASIC + cloudApiConf.getAuthStringEnc());
            InputStream is = urlConnection.getInputStream();
            logGet(url, urlConnection, "[Binary]");
            BufferedImage read = ImageIO.read(is);
            final File output = Files.createTempFile("screen_", ".png").toFile();
            ImageIO.write(read, "png", output);
            return output.getAbsolutePath();
        } catch (IOException e) {
            LOGGER.error("Error reading screenshot from api.", e);
            return null;
        }
    }

    /**
     * Get device info from api for device name.
     *
     * @param deviceName Name to lookup
     *
     * @return device info as json object.
     */
    @Deprecated
    private Integer getDeviceIdByName(String deviceName) {
        if (deviceNameToIdMap.get(deviceName) != null) {
            return deviceNameToIdMap.get(deviceName);
        }
        try {
            final String devices = doGet("devices");
            if (StringUtils.isEmpty(devices)) {
                LOGGER.error("No devices found via cloud api.");
                return null;
            }
            JsonArray deviceArray = new JsonParser().parse(devices).getAsJsonArray();
            JsonObject device = null;
            for (JsonElement deviceJson : deviceArray) {
                JsonObject deviceObject = deviceJson.getAsJsonObject();
                if (deviceObject.get("name").getAsString().equals(deviceName)) {
                    device = deviceObject;
                    break;
                }
            }
            if (device == null) {
                LOGGER.error("No device with name " + deviceName + " found via cloud api.");
                return null;
            }
            final int deviveId = device.get("id").getAsInt();
            deviceNameToIdMap.put(deviceName, deviveId);
            return deviveId;
        } catch (IOException exception) {
            LOGGER.error("Error reading devices from api.", exception);
            return null;
        }
    }
}
