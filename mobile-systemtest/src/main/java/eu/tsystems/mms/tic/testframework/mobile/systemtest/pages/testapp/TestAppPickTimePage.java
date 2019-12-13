package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public abstract class TestAppPickTimePage extends AbstractTestAppPage {

    @Check
    protected NativeMobileGuiElement timePicker;
    protected NativeMobileGuiElement confirmButton;

    /**
     * Default constructor.
     */
    public TestAppPickTimePage() {

        checkPage();
    }

    public TestAppDatePickerPage setTime(String time) {

        timePicker.setProperty("time", time);
        confirmButton.click();
        Assert.assertTrue(new NativeMobileGuiElement(By.id("datepicker_title")).isDisplayed(), "Could not find back to DatePickerActivity.");
        return new TestAppDatePickerPage();
    }
}
