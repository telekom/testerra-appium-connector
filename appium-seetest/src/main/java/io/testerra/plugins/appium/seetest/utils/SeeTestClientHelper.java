package io.testerra.plugins.appium.seetest.utils;

import com.google.gson.JsonArray;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.utils.AppiumProperties;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Created on 14.06.2022
 *
 * @author mgn
 */
public class SeeTestClientHelper implements Loggable {

    /**
     * Determines if SeeTest is used by checking the applications method of REST API
     *
     * @return true, if Seetest was found
     */
    public boolean isSeeTestUsed(SessionContext sessionContext) {
        String serverUrl = sessionContext.getWebDriverRequest().getServerUrl()
                .map(url -> String.format("%s://%s/", url.getProtocol(), url.getHost()))
                .orElse(null);
        if (StringUtils.isBlank(serverUrl)) {
            return false;
        }

        SeeTestRestClient restClient = new SeeTestRestClient(serverUrl);
        Optional<JsonArray> about = restClient.getAbout();
        return about.isPresent();
    }

    /**
     * Returns the full qualified download URL based of the 'reportTestId' of a SeeTest Appium session.
     */
    public String getVideoDownloadUrl(String reportTestId) {
        final String url = AppiumProperties.MOBILE_GRID_URL.asString();
        try {
            URL seeTestUrl = new URL(url);
            return String.format("%s://%s/reporter/api/tests/%s/video", seeTestUrl.getProtocol(), seeTestUrl.getHost(), reportTestId);
        } catch (MalformedURLException e) {
            log().error("Invalid SeeTest URL: {}", url);
        }
        return "";
    }

}
