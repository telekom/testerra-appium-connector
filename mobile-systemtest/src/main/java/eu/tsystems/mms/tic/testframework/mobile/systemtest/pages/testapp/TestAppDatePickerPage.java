package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 31.01.2017.
 */
public class TestAppDatePickerPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement dateInput = new NativeMobileGuiElement(By.id("datepicker_date_input"));
    private final NativeMobileGuiElement timeInput = new NativeMobileGuiElement(By.id("datepicker_time_input"));
    private final NativeMobileGuiElement dateButton = new NativeMobileGuiElement(By.id("datepicker_pick_date_button"));
    private final NativeMobileGuiElement timeButton = new NativeMobileGuiElement(By.id("datepicker_pick_time_button"));
    private final NativeMobileGuiElement resetButton = new NativeMobileGuiElement(By.id("datepicker_reset_button"));

    /**
     * Default constructor.
     */
    public TestAppDatePickerPage() {

        checkPage();
    }

    /**
     * @return pickDatePage
     */
    public TestAppPickDatePage pickDate() {

        dateButton.click();
        return PageFactory.getNew(TestAppPickDatePage.class);
    }

    /**
     * @return pickTimePage
     */
    public TestAppPickTimePage pickTime() {

        timeButton.click();
        return PageFactory.getNew(TestAppPickTimePage.class);
    }

    public String getDate() {

        return dateInput.getText();
    }

    public String getTime() {

        return timeInput.getText();
    }

    public void resetActivity() {

        resetButton.click();
    }

    /**
     * Using the getHint() Method on the time input guiElement
     *
     * @return String hint
     */
    public String getHint() {

        String hint = timeInput.getHint();

        return hint;
    }



}
