package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.testng.Assert;

import java.awt.Color;

/**
 * Created by nkfa on 01.02.2017.
 */
public class TestAppCameraPage extends AbstractTestAppPage {

    private final String PATH_TO_PICTURE = "C:\\BuildAgent\\work\\mobile_systemtest\\res\\simulate-capture.jpg";

    @Check
    private final NativeMobileGuiElement cameraPreview = new NativeMobileGuiElement(By.id("camera_preview"));

    /**
     * Default constructor.
     */
    public TestAppCameraPage() {

        checkPage();
    }

    public void verifySimulateCaputre() {

        driver.seeTestClient().simulateCapture(PATH_TO_PICTURE);
        Color color = cameraPreview.getColorOfPixel(400, 400);
        Assert.assertTrue(color.getRed() > 190 && color.getRed() < 235);
        Assert.assertTrue(color.getGreen() < 15);
        Assert.assertTrue(color.getBlue() > 105 && color.getBlue() < 130);
    }

}
