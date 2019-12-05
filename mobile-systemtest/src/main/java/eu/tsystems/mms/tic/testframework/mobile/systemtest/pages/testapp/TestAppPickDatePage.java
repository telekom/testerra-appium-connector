package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 31.01.2017.
 */
public abstract class TestAppPickDatePage extends AbstractTestAppPage {

    @Check
    protected NativeMobileGuiElement datePicker;
    protected NativeMobileGuiElement confirmButton;

    public TestAppDatePickerPage setDate(String date) {

        setDateProperty(date);
        confirmButton.click();
        return new TestAppDatePickerPage();
    }

    /**
     * @return Property date of datepicker
     */
    public String getDate() {

        return datePicker.getProperty("date");
    }

    /**
     * @param date Date string to set.
     */
    public void setDateProperty(String date) {

        datePicker.setProperty("date", date);
    }

    /**
     * @param selectedDate Date string to check
     * @return boolean whether propertyValue is correct
     */
    public boolean waitForCheckDatePropertyValue(String selectedDate) {

        return datePicker.waitForPropertyValue("date", selectedDate);
    }

    /**
     * @param selectedDate Date string to check
     * @return boolean whether propertyContains is correct
     */
    public boolean waitForCheckDatePropertyContains(String selectedDate) {

        return datePicker.waitForPropertyContains("date", selectedDate);
    }

    /**
     * Performing an action on the datePicker GuiElement
     *
     * @param selectedDate Date string to check
     */
    public String actionOnDatePicker(TestAppEnumerations.methodsOnGuiElement action, String selectedDate) {

        switch (action) {

            case ASSERT_PROPERTY_VALUE:
                datePicker.assertPropertyValue("date", selectedDate);
                return null;
            case ASSERT_PROPERTY_CONTAINS:
                datePicker.assertPropertyContains("date", selectedDate);
                return null;
            case GET_PROPERTY:
                return datePicker.getProperty("datee2");
            case SET_PROPERTY:
                datePicker.setProperty("datee2", selectedDate);
                return null;
            default:
                throw new IllegalArgumentException("Action " + action + " not accepted for this method.");
        }

    }
}
