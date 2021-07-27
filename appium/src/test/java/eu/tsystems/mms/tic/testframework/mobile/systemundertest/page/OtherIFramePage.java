package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class OtherIFramePage extends Page {

    public final GuiElement iframe = new GuiElement(getWebDriver(), By.xpath("//iframe[@src='rot.html']"));

    public final GuiElement iframeElem = new GuiElement(getWebDriver(), By.xpath("//body"), iframe);


    public OtherIFramePage(WebDriver webDriver) {
        super(webDriver);
    }

}
