package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import com.experitest.client.Client;
import com.experitest.client.InternalException;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.AbstractTestAppPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextMenuPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppContextPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppDatePickerPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppDragnDropPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppEnumerations;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppHomePage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppInputPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppPickDatePage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppPickTimePage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppProgressPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppTabbedFirstFragmentPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppTabbedSecondFragmentPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Tests verifying Native Element functions.
 *
 * @author sepr
 */
public class NativeMobileGuiElementTest extends AbstractTest {

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

    @Test(groups = {Groups.DEBUG})
    public void testT25_NativeMobileGuiElement_setText() {

        TestAppInputPage inputPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppInputPage.class);
        inputPage.verifySetTextForInput("tsystems");
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT25N1_NativeMobileGuiElement_setTextOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.SET_TEXT);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testT25N2_NativeMobileGuiElement_setTextOnNoneEditElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.SET_TEXT);
    }

    @Test
    public void testT26_NativeMobileGuiElement_sendText() {

        TestAppInputPage inputPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppInputPage.class);
        inputPage.verifySendTextToInput("tsystems");
    }

    @Test(groups = {Groups.SMOKE}, expectedExceptions = InternalException.class)
    public void testT26N1_NativeMobileGuiElement_sendTextOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.SEND_TEXT);
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT26N2_NativeMobileGuiElement_sendTextOnNoneEditElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.SEND_TEXT);
    }

    @Test
    public void testT27_NativeMobileGuiElement_click() {

        TestAppInputPage inputPage = PageFactory.getNew(TestAppHomePage.class).goToActivity(TestAppInputPage.class);
        inputPage.verifyRadioButton();
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT27N_NativeMobileGuiElement_clickOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.CLICK);
    }

    @Test
    public void testT28_NativeMobileGuiElement_click_offset() {

        Client seeTestClient = MobileDriverManager.getMobileDriver().seeTestClient();
        TestAppProgressPage progressPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppProgressPage.class);
        progressPage.verifySeekBarProceeds(seeTestClient); // needs ios fix
    }

    @Test
    public void testT29_NativeMobileGuiElement_drag() {

        TestAppDragnDropPage dragnDropPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDragnDropPage.class);
        dragnDropPage.verifyDragnDrop();
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT29N_NativeMobileGuiElement_dragOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.DRAG);
    }

    @Test
    public void testT30_NativeMobileGuiElement_setProperty_date() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        datePickerPage = pickDatePage.setDate("04.04.2015");
        Assert.assertTrue(datePickerPage.getDate().equals("2015/4/4") || datePickerPage.getDate().equals("2015/04/04"),
                "Date has been picked.");
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT30N_NativeMobileGuiElement_setInvalidProperty() {

        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDatePickerPage.class);
        TestAppPickDatePage pickDatePage = datePickerPage.pickDate();
        pickDatePage.actionOnDatePicker(TestAppEnumerations.methodsOnGuiElement.SET_PROPERTY, "04.04.2015");
    }

    @Test
    public void testT31_NativeMobileGuiElement_setProperty_time() {

        final String time = "17.50";
        TestAppDatePickerPage datePickerPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppDatePickerPage.class);
        TestAppPickTimePage pickTimePage = datePickerPage.pickTime();
        datePickerPage = pickTimePage.setTime(time);
        Assert.assertEquals(datePickerPage.getTime(), "17:50", "Time has been picked correctly.");
    }

    @Test
    public void testT32_NativeMobileGuiElement_swipe() {

        TestAppTabbedFirstFragmentPage firstFragmentPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppTabbedFirstFragmentPage.class);
        TestAppTabbedSecondFragmentPage secondFragmentPage = firstFragmentPage.goToSecondFrag();
        secondFragmentPage.goToFirstFrag();
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT32N_NativeMobileGuiElement_swipeOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.SWIPE);
    }

    @Test(groups = {Groups.SMOKE})
    public void testT33_NativeMobileGuiElement_longClick() {

        TestAppContextPage contextPage = PageFactory.getNew(TestAppHomePage.class)
                .goToActivity(TestAppContextPage.class);
        TestAppContextMenuPage contextMenuPage = contextPage.doLongClick();
        contextPage = contextMenuPage.clickDelete();
        contextPage.doClickReset();
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT33N_NativeMobileGuiElement_longClickOnInvalidElement() {

        TestAppHomePage testAppHomePage = PageFactory.getNew(TestAppHomePage.class);
        testAppHomePage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.LONG_CLICK);
    }
}
