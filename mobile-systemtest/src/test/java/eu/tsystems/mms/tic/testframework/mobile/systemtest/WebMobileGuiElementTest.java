package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import com.experitest.client.InternalException;
import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.MobileTestWebpage.WebMainPage;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppEnumerations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Created by rnhb on 23.11.2016.
 */
public class WebMobileGuiElementTest extends AbstractTest {

    WebMainPage mainPage;

    @BeforeMethod(alwaysRun = true)
    public void launchWebApp(Method m) {

        MobileDriverManager.getMobileDriver().launchApplication(MOBILE_TEST_PAGE);
        mainPage = PageFactory.getNew(WebMainPage.class);
    }

    @Test(groups = Groups.SMOKE)
    public void testT57_WebMobileGuiElement_getProperty_nodeName() {

        Assert.assertEquals(mainPage.getNodeNamePropertyOfHeading(), "H1", "NameProperty should be correct");
    }

    @Test(expectedExceptions = ElementNotFoundException.class)
    public void testT57N1_WebMobileGuiElement_getPropertyOfInvalidElement() {

        mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_PROPERTY);
    }

    @Test
    public void testT57N2_WebMobileGuiElement_getInvalidPropertyOfElement() {

        Assert.assertNull(mainPage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.GET_PROPERTY));
    }

    @Test
    public void testT58_WebMobileGuiElement_getText() {

        Assert.assertEquals(mainPage.getTextOfHeading(), "Mobile Test Page", "Text should be correct");
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT58N1_WebMobileGuiElement_getTextOfInvalidElement() {

        mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_TEXT);
    }

    @Test(expectedExceptions = InternalException.class)
    public void testT58N2_WebMobileGuiElement_getTextOfNoneTextElement() {

        mainPage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.GET_TEXT);
    }

    @Test
    public void testT59_WebMobileGuiElement_getColorOfPixel() {

        Assert.assertTrue(mainPage.colorCheckOfLearnMoreButton(), "Colors should be displayed in a different way");
    }

    @Test
    public void testT60_WebMobileGuiElement_getMobileDriver() {

        Assert.assertEquals(mainPage.getMobileDriverOfHeading(), MobileDriverManager.getMobileDriver(), "MobileDriver should be correct");
    }

    @Test
    public void testT61_WebMobileGuiElement_isDisplayed() {

        Assert.assertTrue(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.IS_DISPLAYED), "Element should be displayed");
    }

    @Test
    public void testT61N_WebMobileGuiElement_isDisplayed() {

        Assert.assertTrue(!(mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.IS_DISPLAYED)), "Element should not be displayed");
    }

    @Test
    public void testT62_WebMobileGuiElement_getLocation() {

        Assert.assertTrue(mainPage.getLocationXvalueOfHeading() < mainPage.getLocationYvalueOfHeading(), "Location should be correct");
    }

    @Test(expectedExceptions = TesterraRuntimeException.class)
    public void testT62N_WebMobileGuiElement_getLocationOfInvalidElement() {

        mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_LOCATION);
    }

    @Test
    public void testT63_WebMobileGuiElement_getLocator() {

        Assert.assertEquals(mainPage.getLocatorOfHeading(), "xpath=//*[@id='section_0']", "Locator should be correct");
        Assert.assertEquals(mainPage.getLocatorZoneOfHeading(), "WEB", "Locator zone should be correct");
    }

    @Test
    public void testT63N_WebMobileGuiElement_getLocatorOfInvalidElement() {

        Assert.assertFalse(mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_LOCATOR).isEmpty(),
                "locator should always be available, also if element is invalid.");
    }

    @Test(groups = Groups.SMOKE)
    public void testT64_WebMobileGuiElement_click() {

        Assert.assertTrue(!(mainPage.isSignInButtonIsDisplayed()), "SignInButton should not be displayed");
        mainPage.clickNavBurger();
        Assert.assertTrue(mainPage.isSignInButtonIsDisplayed(), "SignInButtin should be displayed0");
    }

    @Test(expectedExceptions = InternalException.class)
    public void test64N_WebMobileGuiElement_clickOnInvalidElement() {

        mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.CLICK);
    }

    @Test
    public void test65_WebMobileGuiElement_getName() {

        Assert.assertEquals(mainPage.getNameOfHeading(), "H1", "Name should be correct");
    }

    @Test
    public void test65N_WebMobileGuiElement_getNameOfInvalidElement() {

        Assert.assertEquals(mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_NAME), "invalidGuiElement");
    }

    @Test(groups = Groups.SMOKE)
    public void test66_WebMobileGuiElement_waitForIsDisplayed() {

        Assert.assertTrue(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_DISPLAYED), "Element should be displayed");
    }

    @Test
    public void test66N_WebMobileGuiElement_waitForIsDisplayed() {

        Assert.assertTrue(!(mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_DISPLAYED)), "Element should not be displayed");
    }

    @Test
    public void test67_WebMobileGuiElement_waitForIsNotDisplayed() {

        Assert.assertTrue(mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_NOT_DISPLAYED), "Element should not be displayed");
    }

    @Test
    public void test67N_WebMobileGuiElement_waitForIsNotDisplayed() {

        Assert.assertTrue(!(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_NOT_PRESENT)), "Element should be displayed");
    }

    @Test
    public void test68_WebMobileGuiElement_waitForIsPresent() {

        Assert.assertTrue(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_PRESENT), "Element should be present");
    }

    @Test
    public void test68N_WebMobileGuiElement_waitForIsPresent() {

        Assert.assertTrue(!(mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_PRESENT)), "Element should not be present");
    }

    @Test(groups = Groups.SMOKE)
    public void test69_WebMobileGuiElement_waitForIsNotPresent() {

        Assert.assertTrue(mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_NOT_PRESENT), "Element should not be present");
    }

    @Test
    public void test69N_WebMobileGuiElement_waitForIsNotPresent() {

        Assert.assertTrue(!(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.WAIT_FOR_IS_NOT_PRESENT)), "Element should be present");
    }

    @Test
    public void test70_WebMobileGuiElement_isPresent() {

        Assert.assertTrue(mainPage.actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.IS_PRESENT), "Element should be present");
    }

    @Test
    public void test70N_WebMobileGuiElement_isPresent() {

        Assert.assertTrue(!mainPage.actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement.IS_PRESENT), "Element should not be present");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test71N1_WebMobileGuiElement_getHintOfInvalidElement() {

        mainPage.actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement.GET_HINT);
    }

    @Test
    public void test71N2_WebMobileGuiElement_getHintOfElementWithoutHint() {

        Assert.assertEquals(mainPage.actionOnValidElement(TestAppEnumerations.methodsOnGuiElement.GET_HINT), "");
    }
}