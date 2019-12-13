package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public class AndroidTestAppInputPage extends TestAppInputPage {

    /**
     * Default constructor.
     */
    public AndroidTestAppInputPage() {

        super();
        this.resultText = new NativeMobileGuiElement(By.id("textView_inputText"));
        this.radioButton = new NativeMobileGuiElement(By.id("radioButton1"));
        this.radioResult = new NativeMobileGuiElement(By.id("textView_radioText"));
        checkPage();
    }

    /* (non-Javadoc)
     * @see eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppInputPage#verifyRadioButton()
     */
    @Override
    public void verifyRadioButton() {

        radioButton.click();
        Assert.assertEquals(radioResult.getText(), "The first radio button was checked.", "Click could not be performed.");
    }
}
