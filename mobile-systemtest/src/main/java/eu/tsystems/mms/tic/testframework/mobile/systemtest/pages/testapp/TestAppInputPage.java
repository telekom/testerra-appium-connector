package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public abstract class TestAppInputPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement editText = new NativeMobileGuiElement(By.id("editText"));
    private final NativeMobileGuiElement sendTextButton = new NativeMobileGuiElement(By.id("input_sendText_button"));
    protected NativeMobileGuiElement resultText;
    protected NativeMobileGuiElement radioButton;
    protected NativeMobileGuiElement radioResult;
    private final NativeMobileGuiElement uncheckButton = new NativeMobileGuiElement(By.id("input_radio_reset_button"));

    public void verifySendTextToInput(String text) {

        sendTextToInput(text);
        Assert.assertEquals(editText.getText(), text, "String was not sent to input.");
        sendTextButton.click();
        Assert.assertEquals(getResultText(), text, "Text could not be set.");
    }

    /**
     * @param text text to type.
     */
    public void sendTextToInput(String text) {

        editText.sendText(text);
    }

    /**
     * @return text from input.
     */
    public String getTextFromInput() {

        return editText.getText();
    }

    public void verifySetTextForInput(String text) {

        editText.setText(text);
        Assert.assertEquals(editText.getText(), text, "String was not sent to input.");
        sendTextButton.click();
        String result = getResultText();
        Assert.assertEquals(result, text, "Text could not be set.");
    }

    /**
     * @return
     */
    public String getResultText() {

        String result = resultText.getText();
        if (result.contains("\n")) {
            result = result.split("\n")[1];
        }
        return result;
    }

    public abstract void verifyRadioButton();

    public void uncheckRadioGroup() {

        uncheckButton.click();
    }

    /**
     * Focus input field
     */
    public void focustTextInput() {

        editText.click();
    }

}
