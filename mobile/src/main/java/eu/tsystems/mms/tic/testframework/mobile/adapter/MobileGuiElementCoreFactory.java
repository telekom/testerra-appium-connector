package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.creation.GuiElementCoreFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by rnhb on 12.02.2016.
 */
public class MobileGuiElementCoreFactory implements GuiElementCoreFactory {


    @Override
    public GuiElementCore create(By by, WebDriver webDriver, GuiElementData guiElementData) {

        final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver(webDriver);
        final String translatedBy = ByTranslator.translateForSeeTest(by);
        final WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement(mobileDriver, translatedBy);

        return new MobileGuiElementCoreAdapter(webMobileGuiElement);
    }

}
