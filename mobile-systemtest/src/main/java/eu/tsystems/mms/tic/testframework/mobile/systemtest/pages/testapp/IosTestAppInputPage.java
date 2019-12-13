package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public class IosTestAppInputPage extends TestAppInputPage {

    /**
     * Default constructor.
     */
    public IosTestAppInputPage() {

        super();
        this.resultText = new NativeMobileGuiElement(By.accessibilityLabel("inputResultSection"));
        this.radioButton = new NativeMobileGuiElement(By.accessibilityLabel("First"));
        this.radioResult = new NativeMobileGuiElement(By.id("radioResultSection"));
        checkPage();
    }

    /* (non-Javadoc)
     * @see eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppInputPage#verifyRadioButton()
     */
    @Override
    public void verifyRadioButton() {

        radioButton.click();
        Assert.assertEquals(radioResult.getText(), "First segment", "Click could not be performed.");
    }
}
