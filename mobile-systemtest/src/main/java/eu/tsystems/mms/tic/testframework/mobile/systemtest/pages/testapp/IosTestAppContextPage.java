package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by nkfa on 01.02.2017.
 */
public class IosTestAppContextPage extends TestAppContextPage {

    /**
     * Default constructor.
     */
    public IosTestAppContextPage() {

        listEntry = new NativeMobileGuiElement(By.xPath("xpath=//*[@accessibilityLabel='Control']"));
        checkPage();
    }
}
