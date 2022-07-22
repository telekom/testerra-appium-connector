package io.testerra.plugins.appium.seetest.test;

import com.google.gson.JsonArray;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import io.testerra.plugins.appium.seetest.utils.SeeTestRestClient;
import io.testerra.plugins.appium.seetest.utils.VideoLoader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Optional;

/**
 * Created on 22.07.2022
 *
 * @author mgn
 */
public class SeeTestRestClientTests extends TesterraTest implements WebDriverManagerProvider {

    IExecutionContextController contextController = Testerra.getInjector().getInstance(IExecutionContextController.class);

    @Test
    public void testGetApplications() {
        SeeTestRestClient client = new SeeTestRestClient("https://mobiledevicecloud.t-systems-mms.eu");
        Optional<JsonArray> about = client.getAbout();
    }

//    @Test
//    public void testGetVideoFile() {
//        SeeTestRestClient client = new SeeTestRestClient("https://mobiledevicecloud.t-systems-mms.eu");
//        Optional<Object> o = client.downloadVideoStream("1783");
//    }

    @Test
    public void testDownloadVideo() {
        final WebDriver driver = WEB_DRIVER_MANAGER.getWebDriver();
        SessionContext sessionContext = contextController.getCurrentSessionContext().get();
        VideoLoader loader = new VideoLoader();
        VideoRequest videoRequest = new VideoRequest(sessionContext, sessionContext.getSessionKey() + ".mp4", "1423");
        Video download = loader.download(videoRequest);
    }
}
