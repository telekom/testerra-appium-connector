package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import com.experitest.client.Client;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public abstract class TestAppProgressPage extends AbstractTestAppPage {

    /**
     * Default constructor.
     */
    public TestAppProgressPage() {

        checkPage();
    }

    @Check
    protected NativeMobileGuiElement progressBar;
    protected NativeMobileGuiElement seekBar;
    protected NativeMobileGuiElement resultText;
    protected NativeMobileGuiElement resetButton;

    public void verifySeekBarProceeds(Client seeTestClient) {

        int coverageBefore = Integer.parseInt(seeTestClient.runNativeAPICall("NATIVE", "xpath=//*[@id='progress_bar']", 0, "view.getProgress();"));
        seekBar.click(50, 0);
        int coverageAfterwards = Integer.parseInt(seeTestClient.runNativeAPICall("NATIVE", "xpath=//*[@id='progress_bar']", 0, "view.getProgress();"));
        Assert.assertTrue(coverageAfterwards > coverageBefore, "Progress bar could not be accessed.");
    }

    public String getResultText() {

        return resultText.getText();
    }

    public void resetActivity() {

        resetButton.click();
    }

}
