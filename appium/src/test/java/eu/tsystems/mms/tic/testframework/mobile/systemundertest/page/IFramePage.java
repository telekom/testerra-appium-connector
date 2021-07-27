package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IFramePage extends AbstractInternetPage {

    @Check
    public final GuiElement iframe = new GuiElement(getWebDriver(), By.id("mce_0_ifr"));

    public final GuiElement textArea = new GuiElement(getWebDriver(), By.id("mce_0"), iframe);

    public IFramePage(WebDriver driver) {
        super(driver);
    }

}
