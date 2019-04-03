/* 
 * Created on 27.07.2016
 * 
 * Copyright(c) 2011 - 2016 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Client to send stuff to tracking service.
 *  
 * @author sepr
 */
public class TrackingClient {
    private static final String PATH_RESPONSE_STATUS = "Path: {}; Response Status: {}";
    private static final String MESSAGE = "Message: {}";
    private String url;
    private final String hash = "f836f307bb3ce081be5e22c2be2d1547";

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingClient.class);

    /**
     * Default constructor.
     *
     * @param url Url to instantiate client with.
     */
    public TrackingClient(final String url) {
        this.url = url;
        if (this.url.endsWith("/")) {
            this.url = this.url + "/";
        }
    }

    /**
     * Set title on tracking window.
     * 
     * @param title title to set.
     * @return Reponse of request.
     */
    public String setTitle(String title) {
        if (title == null) {
            title = "";
        }
        return executeGetRequest("setTitle", title);
    }

    /**
     * Start tracking
     * 
     * @return Reponse of request.
     */
    public String startTracking() {
        return executeGetRequest("startTracking", null);
    }

    /**
     * Stop tracking 
     * 
     * @return Reponse of request.
     */
    public String stopTracking() {
        return executeGetRequest("stopTracking", null);
    }

    private String executeGetRequest(String path, String title) {
        final Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url)
                .path(path);
        target = target.queryParam("hash", hash);
        if (title != null) {
            target = target.queryParam("title", title);
        }
        try {
            final Response out = target.request().get();
            String bodyString = (String) out.getEntity();
            if (out.getStatus() < 300) {
                LOGGER.debug(PATH_RESPONSE_STATUS, path, out.getStatus());
            } else {
                LOGGER.error(PATH_RESPONSE_STATUS + "; " + MESSAGE, new Object[]{path, out.getStatus(), bodyString});
            }
            return bodyString;
        } catch (ProcessingException e) {
            LOGGER.error("Error on request to tracking service", e);
            return null;
        }
    }

    /**
     * Send screenshot to tracking window.
     * 
     * @param path Path to file to display.
     * @return Reponse of request or null, if file is null.
     */
    public void sendScreenshot(final Path path, final String source) {
        final Client client = ClientBuilder.newClient();
        final Response out;
        if (path != null) {
            if (!path.toFile().exists()) {
                LOGGER.error("File to send does not exist.");
                return;
            }
            WebTarget webTarget = client.target(url)
                    .path("displayScreenshot").queryParam("hash", hash);
            if (source != null) {
                webTarget = webTarget.queryParam("source", source);
            }
            try {
                out = webTarget.request().post(Entity.entity(Files.readAllBytes(path), MediaType.APPLICATION_OCTET_STREAM));
            } catch (IOException e) {
                LOGGER.error("File to send does not exist.", e);
                return;
            } catch (ProcessingException e) {
                LOGGER.error("Error on request to tracking service", e);
                return;
            }
            final String bodyString = (String) out.getEntity();
            if (out.getStatus() < 300) {
                LOGGER.debug(PATH_RESPONSE_STATUS, path, out.getStatus());
            } else {
                LOGGER.error(PATH_RESPONSE_STATUS + "; " + MESSAGE, new Object[]{path, out.getStatus(), bodyString});
            }
        } else {
            LOGGER.error("No file given to send.");
        }
    }
}
