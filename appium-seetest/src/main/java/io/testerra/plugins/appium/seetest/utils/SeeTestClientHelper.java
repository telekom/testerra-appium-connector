package io.testerra.plugins.appium.seetest.utils;

import com.google.gson.JsonArray;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public class SeeTestClientHelper implements Loggable {

    private static final SeeTestClientHelper INSTANCE = new SeeTestClientHelper();

    private SeeTestClientHelper() {
    }

    public static SeeTestClientHelper get() {
        return INSTANCE;
    }

    /**
     * Detemines if SeeTest is used by checking the applications method of REST API
     *
     * @return true, if Seetest was found
     */
    public boolean isSeeTestUsed(SessionContext sessionContext) {
        Optional<String> seeTestUrl = this.getSeeTestUrl(sessionContext.getWebDriverRequest().getServerUrl());
        if (seeTestUrl.isEmpty()) {
            return false;
        }

        SeeTestRestClient restClient = new SeeTestRestClient(seeTestUrl.get());
        Optional<JsonArray> about = restClient.getAbout();
        return about.isPresent();
    }

    public String getVideoDownloadUrl(String testId) {
        final String url = AppiumProperties.MOBILE_GRID_URL.asString();

        try {
            Optional<String> seeTestUrl = this.getSeeTestUrl(Optional.of(new URL(url)));
            return seeTestUrl.map(s -> s + "/reporter/api/tests/" + testId + "/video").orElse("");
        } catch (MalformedURLException e) {
            log().error("Invalid SeeTest URL: {}", url);

        }
        return "";
    }

    private Optional<String> getSeeTestUrl(Optional<URL> seeTestUrl) {
        return seeTestUrl.map(url -> String.format("%s://%s/", url.getProtocol(), url.getHost()));
    }

}
