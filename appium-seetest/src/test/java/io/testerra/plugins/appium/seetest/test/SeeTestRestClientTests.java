package io.testerra.plugins.appium.seetest.test;

import com.google.gson.JsonArray;
import eu.tsystems.mms.tic.testframework.common.PropertyManagerProvider;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.report.model.context.SessionContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.report.utils.IExecutionContextController;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.testing.WebDriverManagerProvider;
import io.appium.java_client.AppiumDriver;
import io.testerra.plugins.appium.seetest.request.VideoRequest;
import io.testerra.plugins.appium.seetest.utils.SeeTestRestClient;
import io.testerra.plugins.appium.seetest.utils.VideoLoader;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Created on 22.07.2022
 *
 * @author mgn
 */
public class SeeTestRestClientTests extends TesterraTest implements WebDriverManagerProvider, PropertyManagerProvider {

    IExecutionContextController contextController = Testerra.getInjector().getInstance(IExecutionContextController.class);

    @Test
    public void testGetApplications() throws MalformedURLException {
        URL url = new URL(PROPERTY_MANAGER.getProperty("tt.mobile.grid.url"));
        SeeTestRestClient client = new SeeTestRestClient(String.format("%s://%s/", url.getProtocol(), url.getHost()));
        Optional<JsonArray> about = client.getAbout();
        Assert.assertTrue(about.isPresent(), "There should something as response.");
    }

    @Test
    public void testDownloadVideo() {
        final WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        String reportTestId = WEB_DRIVER_MANAGER.unwrapWebDriver(webDriver, AppiumDriver.class)
                .map(driver -> driver.getCapabilities().getCapability("reportTestId").toString())
                .orElse("na");
        SessionContext sessionContext = contextController.getCurrentSessionContext().get();

        // The session needs to be shutdown, otherwise we get no video.
        WEB_DRIVER_MANAGER.shutdownSession(webDriver);

        VideoLoader loader = new VideoLoader();
        VideoRequest videoRequest = new VideoRequest(sessionContext, sessionContext.getSessionKey() + ".mp4", reportTestId);
        Video download = loader.download(videoRequest);
        Assert.assertNotNull(download, "There should be a downloaded video file.");
        Assert.assertTrue(download.getVideoFile().getName().contains("mp4"), "The video file name should have the extension mp4.");
    }
}
