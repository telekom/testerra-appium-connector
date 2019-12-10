package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.annotations.Fails;
import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.AbstractTestAppPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextMenuPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppDatePickerPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppEnumerations;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppHomePage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppPickDatePage;
import org.openqa.selenium.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Tests verifying Native Element functions.
 *
 * @author sepr
 */
public class NativeMobileGuiElement1Test extends AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NativeMobileGuiElement1Test.class);

    /**
     * Always go back to startpage before test.
     *
     * @param method reference to testmethod.
     */
    @BeforeMethod(alwaysRun = true)
    public void gotoStart(Method method) {

        super.launchTestApp();
        AbstractTestAppPage testAppPage = new AbstractTestAppPage();
        testAppPage.getHeader().gotoHomePage();
    }

    @Test
    public void testT34_NativeMobileGuiElement_isDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        Assert.assertTrue(contextPage.isListEntryIsDisplayed("Control"), "Entry is displayed.");
    }

    @Test(groups = {Groups.SMOKE})
    public void testT34N_NativeMobileGuiElement_isDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(!(contextPage.isListEntryIsDisplayed("Control")), "Entry is not displayed.");
    }

    @Test(groups = {Groups.SMOKE})
    public void testT35_NativeMobileGuiElement_isPresent() {

        TestAppContextPage testAppContextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        Assert.assertTrue(testAppContextPage.isListEntryIsPresent("Control"), "Entry is present.");
    }

    @Test
    public void testT35N_NativeMobileGuiElement_isPresent() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(!(contextPage.isListEntryIsPresent("Control")), "Entry is not present.");
    }

    @Test
    public void testT36_NativeMobileGuiElement_getLocation() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Point location = testAppHomePage.doGetInputButtonLocation();
        Assert.assertTrue(location.getX() > 0 && location.getY() > 0, "Location could not be read.");
    }

    @Test(expectedExceptions = TesterraRuntimeException.class)
    public void testT36N_NativeMobileGuiElement_getLocationOfInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_LOCATION);
    }

    @Test
    public void testT37_NativeMobileGuiElement_getLocator() {

        String xpath = "xpath=//*[@id='imageButton_textEdit']";
        NativeMobileGuiElement inputTestButton = new NativeMobileGuiElement(xpath);
        Assert.assertEquals(inputTestButton.getLocator().locator, xpath, "Locator should be correct");
    }

    @Test
    public void testT37N_NativeMobileGuiElement_getLocatorOfInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(
                testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_LOCATOR),
                TestAppHomePage.INVALIDLOCATOR);
    }

    @Fails(description = "IOS App needs locator for magenta section", validFor = "os.unter.test=ios")
    @Test
    public void testT38_NativeMobileGuiElement_getColorOfPixel() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Color color = testAppHomePage.doGetMainColor();
        Assert.assertTrue(color.getRed() > 190 && color.getRed() < 235, "Red part of color is fine for magenta");
        Assert.assertTrue(color.getGreen() < 15, "Green part of color is fine for magenta");
        Assert.assertTrue(color.getBlue() > 105 && color.getBlue() < 130, "Blue part of color is fine for magenta");
    }

    @Fails(description = "IOS App needs locator for magenta section", validFor = "os.unter.test=ios")
    @Test
    public void testT39_NativeMobileGuiElement_getProperties() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Map<String, String> properties = testAppHomePage.doGetMainProperties();
        Assert.assertEquals(properties.get("backColor"), "0xE20074", "BackColor should be correct");
    }

    @Test(groups = {Groups.SMOKE})
    public void testT40_NativeMobileGuiElement_getProperty() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);

        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";

        datePickerPage = pickDatePage.setDate(dateString);
        final String date = datePickerPage.getDate();

        if (mobileOperatingSystem == MobileOperatingSystem.IOS) {
            Assert.assertTrue(date.contains("2012-12-12"), "Could verify date property.");
        } else {
            Assert.assertTrue(date.contains("2012/12/12"), "Could verify date property.");
        }
    }

    @Test(expectedExceptions = ElementNotFoundException.class)
    public void testT40N2_NativeMobileGuiElement_getPropertyOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_PROPERTY);
    }

    @Test
    public void testT41_NativeMobileGuiElement_checkChangedPropertyValue() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);

        Assert.assertEquals(datePickerPage.getDate(), "2012/12/12");
    }

    @Test
    public void testT41N_NativeMobileGuiElement_checkChangedPropertyValue() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);

        Assert.assertNotEquals(datePickerPage.getDate(), "2013/12/06");
    }

    @Test
    public void testT42_NativeMobileGuiElement_checkChangedPropertyContains() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);

        Assert.assertTrue(datePickerPage.getDate().contains("12/12"), "Date contains 12/12");
    }

    @Test
    public void testT42N_NativeMobileGuiElement_checkChangedPropertyContains() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);

        Assert.assertFalse(datePickerPage.getDate().contains("12/11"));
    }

    @Test
    public void testT43_NativeMobileGuiElement_checkWaitForChangedPropertyValue() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);
        pickDatePage = datePickerPage.pickDate();

        Assert.assertTrue(pickDatePage.waitForCheckDatePropertyValue("12.12.2012"), "Date should be correct");
    }

    @Test
    public void testT43N_NativeMobileGuiElement_checkWaitForChangedPropertyValue() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);
        pickDatePage = datePickerPage.pickDate();

        Assert.assertFalse(pickDatePage.waitForCheckDatePropertyValue("06.03.2012"), "Date should not be correct");
    }

    @Test
    public void testT44_NativeMobileGuiElement_checkWaitForChangedPropertyContains() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);
        pickDatePage = datePickerPage.pickDate();

        Assert.assertTrue(pickDatePage.waitForCheckDatePropertyContains("12.12"), "Date should be correct");
    }

    @Test
    public void testT44N_NativeMobileGuiElement_checkWaitForChangedPropertyContains() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        final String dateString = "12.12.2012";
        datePickerPage = pickDatePage.setDate(dateString);
        pickDatePage = datePickerPage.pickDate();

        Assert.assertFalse(pickDatePage.waitForCheckDatePropertyContains("11.12"), "Date should not be correct");
    }

    @Fails(description = "IOS App needs a fix.", validFor = "os.unter.test=ios")
    @Test
    public void testT45_NativeMobileGuiElement_listSelect() {

        TestAppContextPage appContextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        final String textToSelect = "Remote";
        appContextPage.doListSelectAndClick(textToSelect);
        Assert.assertEquals(appContextPage.doGetClickedItems()[0], textToSelect, "List item has been clicked.");
    }

    @Test
    public void testT46_NativeMobileGuiElement_scrollToListEntry() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppContextPage.class);
        final String entryText = "Pixel";

        Assert.assertFalse(contextPage.isListEntryIsDisplayed(entryText), "Entry is displayed.");
        contextPage.scrollToListEntry(entryText);
        Assert.assertTrue(contextPage.isListEntryIsDisplayed(entryText), "Entry is displayed.");
    }

}
