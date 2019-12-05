/*
 * Created on 27.01.2017
 *
 * Copyright(c) 2011 - 2017 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp.appium;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

/**
 * Page class for Homepage of app.
 *
 * @author sepr
 */
public abstract class TestAppHomePage extends AbstractTestAppPage {

    /**
     * @param driver
     */
    public TestAppHomePage(WebDriver driver) {

        super(driver);
    }

    public static final String INVALIDLOCATOR = "xpath=//*[@id='imageButton_textEdit23']";
    @Check
    private final GuiElement inputButton = new GuiElement(driver, By.id("imageButton_textEdit"));
    protected GuiElement middleSection;

    // /**
    // * Switch to special page for appropiate tests.
    // *
    // * @param targetPage Page to go to.
    // *
    // * @return new page object of targetPage
    // */
    // @SuppressWarnings("unchecked")
    // public <T extends AbstractTestAppPage> T goToActivity(Class<T> targetPage) {
    // MobileDriver driver = MobileDriverManager.getMobileDriver();
    // if (targetPage.isAssignableFrom(TestAppInputPage.class)) {
    // inputButton.click();
    // driver.closeKeyboard();
    // return PageFactory.getNew(targetPage);
    // }
    // if (targetPage.isAssignableFrom(TestAppDragnDropPage.class)) {
    // new NativeMobileGuiElement(By.id("imageButton_dragndrop")).click();
    // return PageFactory.getNew(targetPage);
    // }
    // if (targetPage.isAssignableFrom(TestAppDatePickerPage.class)) {
    // new NativeMobileGuiElement(By.id("imageButton_datepicker")).click();
    // return (T) new TestAppDatePickerPage();
    // }
    // if (targetPage.isAssignableFrom(TestAppProgressPage.class)) {
    // new NativeMobileGuiElement(By.id("imageButton_progress")).click();
    // return PageFactory.getNew(targetPage);
    // }
    // if (targetPage.isAssignableFrom(TestAppContextPage.class)) {
    // new NativeMobileGuiElement(By.id("imageButton_context")).click();
    // return PageFactory.getNew(targetPage);
    // }
    // if (targetPage.isAssignableFrom(TestAppTabbedFirstFragmentPage.class)) {
    // new NativeMobileGuiElement(By.id("imageButton_tabbed")).click();
    // return (T) new TestAppTabbedFirstFragmentPage();
    // }
    // if (targetPage.isAssignableFrom(TestAppCameraPage.class)) {
    // new NativeMobileGuiElement(By.id("action_camera")).click();
    // return (T) new TestAppCameraPage();
    // } else {
    // throw new XetaRuntimeException("Could not launch requested activity.");
    // }
    // }

    /**
     * Return location of input button.
     *
     * @return {@link Point}
     */
    public Point doGetInputButtonLocation() {

        return inputButton.getLocation();
    }

    // /**
    // * Get magenta background color.
    // *
    // * @return {@link Color}
    // */
    // public Color doGetMainColor() {
    // return middleSection.getColorOfPixel(10, 10);
    // }
    //
    // /**
    // * @return Property map of {@link NativeMobileGuiElement} of main area
    // */
    // public Map<String, String> doGetMainProperties() {
    // return middleSection.getProperties();
    // }

    /**
     * @return Value of the the name property of the inputButton
     */
    public String getNamePropertyOfInputButton() {

        return inputButton.getName();
    }

    // /**
    // * Performing an action on the inputButton
    // */
    // public String actionOnValidElement(TestAppEnumerations.methodsOnGuiElement action) {
    //
    // switch (action) {
    //
    // case WAIT_FOR_ELEMENT_IS_FOUND:
    // return Boolean.toString(inputButton.waitForElementIsFound());
    // case WAIT_FOR_ELEMENT_IS_NOT_FOUND:
    // return Boolean.toString(inputButton.waitForElementIsNotFound());
    // case WAIT_FOR_ELEMENT:
    // return Boolean.toString(inputButton.waitForElement(1000));
    // case SET_TEXT:
    // inputButton.setText("tester");
    // return null;
    // case SEND_TEXT:
    // inputButton.sendText("tester");
    // return null;
    // case SET_NAME:
    // inputButton.setName("tester");
    // return null;
    // case GET_HINT:
    // return inputButton.getHint();
    // default:
    // throw new IllegalArgumentException(action + " not implemented.");
    // }
    // }

    // /**
    // * Performing an action on an invalid inputButton
    // */
    // public String actionOnInvalidElement(TestAppEnumerations.methodsOnGuiElement action) {
    //
    // NativeMobileGuiElement invalidInputButton = new NativeMobileGuiElement(INVALIDLOCATOR);
    //
    // switch (action) {
    // case LONG_CLICK:
    // invalidInputButton.longClick();
    // return null;
    // case CLICK:
    // invalidInputButton.click();
    // return null;
    // case SWIPE:
    // invalidInputButton.swipe(Direction.RIGHT, 0, 500);
    // return null;
    // case DRAG:
    // invalidInputButton.drag(300, 300);
    // return null;
    // case GET_LOCATOR:
    // return invalidInputButton.getLocator().locator;
    // case GET_LOCATION:
    // Point location = invalidInputButton.getLocation();
    // int locationX = location.getX();
    // int locationY = location.getY();
    // return locationX + " - " + locationY;
    // case WAIT_FOR_ELEMENT_IS_FOUND:
    // return Boolean.toString(invalidInputButton.waitForElementIsFound());
    // case WAIT_FOR_ELEMENT_IS_NOT_FOUND:
    // return Boolean.toString(invalidInputButton.waitForElementIsNotFound());
    // case WAIT_FOR_ELEMENT:
    // return Boolean.toString(invalidInputButton.waitForElement(1000));
    // case SET_TEXT:
    // invalidInputButton.setText("tester");
    // return null;
    // case SEND_TEXT:
    // invalidInputButton.sendText("tester");
    // return null;
    // case SET_NAME:
    // invalidInputButton.setName("tester");
    // return null;
    // case GET_HINT:
    // return invalidInputButton.getHint();
    // case GET_PROPERTY:
    // return invalidInputButton.getProperty("text");
    // default:
    // throw new IllegalArgumentException(action + " not implemented.");
    // }
    //
    // }

}
