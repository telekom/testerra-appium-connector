package eu.tsystems.mms.tic.testframework.appium.windows;

import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.PreparedLocator;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import org.openqa.selenium.WebDriver;

public class CalculatorApp extends Page {

    private final PreparedLocator automationLocator = LOCATE.prepare("//*[@AutomationId=\"%s\"]");

    private final UiElement num1Btn = find(automationLocator.with("num1Button"));
    private final UiElement num3Btn = find(automationLocator.with("num3Button"));
    private final UiElement num7Btn = find(automationLocator.with("num7Button"));
    private final UiElement results = find(automationLocator.with("CalculatorResults"));

    public CalculatorApp(WebDriver webDriver) {
        super(webDriver);
    }

    public void typeSomething() {
        num1Btn.click();
        num3Btn.click();
        num3Btn.click();
        num7Btn.click();
    }

    public TestableUiElement getResults() {
        return results;
    }
}
