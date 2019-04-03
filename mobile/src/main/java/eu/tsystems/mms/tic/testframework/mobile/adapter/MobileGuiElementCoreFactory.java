package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.creation.GuiElementCoreFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by rnhb on 12.02.2016.
 */
public class MobileGuiElementCoreFactory implements GuiElementCoreFactory {


    //FIXME rework/fix create
    @Override
    public GuiElementCore create(By by, WebDriver webDriver) {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver(webDriver);
        String translatedBy = ByTranslator.translateForSeeTest(by);
        WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(mobileDriver, translatedBy);
        MobileGuiElementCoreAdapter mobileGuiElementCoreAdapter = new MobileGuiElementCoreAdapter(webMobileGuiElement);
        return mobileGuiElementCoreAdapter;
    }

}
