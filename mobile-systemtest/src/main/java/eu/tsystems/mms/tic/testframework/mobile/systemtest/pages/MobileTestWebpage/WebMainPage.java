package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.MobileTestWebpage;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.TestAppEnumerations;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.openqa.selenium.Point;

import java.awt.Color;

/**
 * Created by aero on 09.11.2017.
 */
public class WebMainPage extends MobilePage {

    private WebMobileGuiElement invalidGuiElement;
    @Check
    private WebMobileGuiElement heading;
    private WebMobileGuiElement logo;
    private WebMobileGuiElement learnMoreButton;
    private WebMobileGuiElement navBurger;
    private WebMobileGuiElement signInButton;


    public WebMainPage() {

        invalidGuiElement = new WebMobileGuiElement("xpath=//*[@id='sectionnn_0']");
        heading = new WebMobileGuiElement("xpath=//*[@id='section_0']");
        logo = new WebMobileGuiElement("xpath=//*[@nodeName='IMG']");
        learnMoreButton = new WebMobileGuiElement("//*[@id='learn_more']");
        navBurger = new WebMobileGuiElement("xpath=//*[@id='burger']");
        signInButton = new WebMobileGuiElement("xpath=//*[@id='sign_in']");
        checkPage();
    }

    /**
     * Performing an action on an invalid WebGuiElement
     */
    public String actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement action) {

        switch (action) {
            case GET_PROPERTY:
                return invalidGuiElement.getProperty("nodeName");
            case GET_TEXT:
                return invalidGuiElement.getText();
            case GET_LOCATION:
                Point location = invalidGuiElement.getLocation();
                int locationX = location.getX();
                int locationY = location.getY();
                return locationX + " - " + locationY;
            case GET_LOCATOR:
                return invalidGuiElement.getLocator().locator;
            case CLICK:
                invalidGuiElement.click();
                return null;
            case GET_NAME:
                return invalidGuiElement.getName();
            case GET_HINT:
                return invalidGuiElement.getHint();
            default:
                throw new IllegalArgumentException(action + "not handled");
        }

    }

    /**
     * Performing an action on heading or logo WebGuiElement
     */
    public String actionOnValidElement(TestAppEnumerations.methodsOnGuiElement action) {

        switch (action) {
            case GET_PROPERTY:
                return heading.getProperty("nodeNameee");
            case GET_TEXT:
                return logo.getText();
            case GET_HINT:
                return heading.getHint();
            default:
                throw new IllegalArgumentException();
        }

    }

    /**
     * Performing an action on the not visible signInButton
     * Don't perform another click yourself when using this method
     *
     * @return boolean, depending on the action on the element was successful or not
     */
    public boolean actionOnValidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement action) {

        clickNavBurger();

        boolean check = false;

        switch (action) {

            case IS_DISPLAYED:
                check = signInButton.isDisplayed();
                break;
            case IS_PRESENT:
                check = signInButton.isPresent();
                break;
            case WAIT_FOR_IS_DISPLAYED:
                check = signInButton.waitForIsDisplayed();
                break;
            case WAIT_FOR_IS_NOT_DISPLAYED:
                check = signInButton.waitForIsNotDisplayed();
                break;
            case WAIT_FOR_IS_PRESENT:
                check = signInButton.waitForIsPresent();
                break;
            case WAIT_FOR_IS_NOT_PRESENT:
                check = signInButton.waitForIsNotPresent();
                break;
            default:
                throw new IllegalArgumentException(action + "not handled");
        }

        return check;

    }


    /**
     * Performing an action on the not visible signInButton
     * Don't perform a navBurgerClick before using this method
     *
     * @return boolean, depending on the action on the element was successful or not
     */
    public boolean actionOnInvalidSignInButtonWithReturn(TestAppEnumerations.methodsOnGuiElement action) {

        boolean check = true;

        switch (action) {

            case IS_DISPLAYED:
                check = signInButton.isDisplayed();
                break;
            case IS_PRESENT:
                check = signInButton.isPresent();
                break;
            case WAIT_FOR_IS_DISPLAYED:
                check = signInButton.waitForIsDisplayed();
                break;
            case WAIT_FOR_IS_NOT_DISPLAYED:
                check = signInButton.waitForIsNotDisplayed();
                break;
            case WAIT_FOR_IS_PRESENT:
                check = signInButton.waitForIsPresent();
                break;
            case WAIT_FOR_IS_NOT_PRESENT:
                check = signInButton.waitForIsNotPresent();
                break;
            default:
                throw new IllegalArgumentException(action + "not handled");
        }

        return check;

    }

    /**
     * @return String which contains the value of the property nodeName of the heading-WebGuiElement
     */
    public String getNodeNamePropertyOfHeading() {

        return heading.getProperty("nodeName");
    }

    /**
     * @return String with the return value of the method getText of the heading-WebGuiElement
     */
    public String getTextOfHeading() {

        return heading.getText();
    }

    /**
     * @return MobileDriver of the heading-WebGuiElement
     */
    public MobileDriver getMobileDriverOfHeading() {

        return heading.getMobileDriver();
    }

    /**
     * @return int with the x-location of the heading-WebGuiElement
     */
    public int getLocationXvalueOfHeading() {

        return heading.getLocation().getX();
    }

    /**
     * @return int with the x-location of the heading-WebGuiElement
     */
    public int getLocationYvalueOfHeading() {

        return heading.getLocation().getY();
    }

    /**
     * @return String with the locator of the heading-WebGuiElement
     */
    public String getLocatorOfHeading() {

        return heading.getLocator().locator;
    }

    /**
     * @return String with the locator zone of the heading-WebGuiElement
     */
    public String getLocatorZoneOfHeading() {

        return heading.getLocator().zone;
    }

    /**
     * @return String with the return value of the method getName of the heading-WebGuiElement
     */
    public String getNameOfHeading() {

        return heading.getProperty("nodeName");
    }

    /**
     * @return Boolean check, which is false when the colorCheck wasn't successful of the learnMoreButton
     */
    public boolean colorCheckOfLearnMoreButton() {

        boolean check = true;
        Color colorOfPixel = learnMoreButton.getColorOfPixel(10, 10);
        logger.info("Color of pixel: {}", colorOfPixel.toString());
        if (!(colorOfPixel.getRed() > 45 && colorOfPixel.getRed() < 75)) {
            check = false;
        }

        if (!(colorOfPixel.getGreen() > 105 && colorOfPixel.getGreen() < 135)) {
            check = false;
        }

        if (!(colorOfPixel.getBlue() > 150 && colorOfPixel.getBlue() < 180)) {
            check = false;
        }

        return check;
    }

    /**
     * click navBuger to display te signButton
     */
    public void clickNavBurger() {

        navBurger.click();
    }

    /**
     * @return Boolean with the value of the isDisplayed() Method on the the signInButton
     */
    public boolean isSignInButtonIsDisplayed() {

        return signInButton.isDisplayed();
    }

}
