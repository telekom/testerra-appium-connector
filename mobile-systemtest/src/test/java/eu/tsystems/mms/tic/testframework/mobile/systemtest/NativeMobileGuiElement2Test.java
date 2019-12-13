package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.AbstractTestAppPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextMenuPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppDatePickerPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppEnumerations;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppHomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Tests verifying Native Element functions.
 *
 * @author sepr
 */
public class NativeMobileGuiElement2Test extends AbstractTest {

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
    public void testT47_NativeMobileGuiElement_waitForIsDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        Assert.assertTrue(contextPage.waitForIsListEntryIsDisplayed("Control"), "Entry is displayed.");
    }

    @Test
    public void testT47N_NativeMobileGuiElement_waitForIsDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(!(contextPage.waitForIsListEntryIsDisplayed("Control")), "Entry is not displayed.");
    }

    @Test
    public void testT48_NativeMobileGuiElement_waitForIsNotDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(contextPage.waitForIsListEntryIsNotDisplayed("Control"), "Entry is not displayed.");
    }

    @Test
    public void testT48N_NativeMobileGuiElement_waitForIsNotDisplayed() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        Assert.assertTrue(!(contextPage.waitForIsListEntryIsNotDisplayed("Control")), "Entry is displayed.");
    }

    @Test
    public void testT49_NativeMobileGuiElement_waitForIsPresent() {

        TestAppContextPage testAppContextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        Assert.assertTrue(testAppContextPage.waitForIsListEntryIsPresent("Control"), "Entry is present.");
    }

    @Test
    public void testT49N_NativeMobileGuiElement_waitForIsPresent() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(!(contextPage.waitForIsListEntryIsPresent("Control")), "Entry is not present.");
    }

    @Test
    public void testT50_NativeMobileGuiElement_waitForIsNotPresent() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage menuPage = contextPage.doLongClick();
        contextPage = menuPage.clickDelete();
        Assert.assertTrue(contextPage.waitForIsListEntryIsNotPresent("Control"), "Entry is present.");
    }

    @Test
    public void testT50N_NativeMobileGuiElement_waitForIsNotPresent() {

        TestAppContextPage testAppContextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        Assert.assertTrue(!(testAppContextPage.waitForIsListEntryIsNotPresent("Control")), "Entry is not present.");
    }

    @Test
    public void testT51_NativeMobileGuiElement_setName() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.SET_NAME);
        Assert.assertEquals(testAppHomePage.getNamePropertyOfInputButton(), "tester",
                "The guielement should have the new name");
    }

    @Test
    public void testT51N_NativeMobileGuiElement_setNameOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.SET_NAME);
    }

    @Test
    public void testT52_NativeMobileGuiElement_waitForIsFound() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage
                .actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT_IS_FOUND), "true");
    }

    @Test
    public void testT52N_NativeMobileGuiElement_waitForIsFound() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage
                .actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT_IS_FOUND), "false");
    }

    @Test
    public void testT53_NativeMobileGuiElement_waitForIsNotFound() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage.actionOnInvalidElement(
                TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT_IS_NOT_FOUND), "true");
    }

    @Test
    public void testT53N_NativeMobileGuiElement_waitForIsNotFound() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage
                .actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT_IS_NOT_FOUND), "false");
    }

    @Test
    public void testT54_NativeMobileGuiElement_swipeWhileNotFound() {

        TestAppContextPage testAppContextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        testAppContextPage.swipeWhileNotFoundListEntry("Pixel");
        Assert.assertTrue(testAppContextPage.isListEntryIsDisplayed("Pixel"));
    }

    @Test(groups = Groups.SMOKE)
    public void testT55_NativeMobileGuiElement_waitForElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage.actionOnValidElement(
                TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT), "true");
    }

    @Test
    public void testT55N_NativeMobileGuiElement_waitForElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage.actionOnInvalidElement(
                TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_ELEMENT), "false");
    }

    @Test
    public void testT56_NativeMobileGuiElement_getHint() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDatePickerPage.class);
        datePickerPage.getHint();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testT56N1_NativeMobileGuiElement_getHintOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_HINT);
    }

    @Test
    public void testT56N2_NativeMobileGuiElement_getHintOnElementWithoutHint() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        Assert.assertEquals(testAppHomePage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.GET_HINT),
                "");
    }

}
