package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by nkfa on 31.01.2017.
 */
public class AndroidTestAppPickTimePage extends TestAppPickTimePage {

    /**
     * Default constructor.
     */
    public AndroidTestAppPickTimePage() {

        timePicker = new NativeMobileGuiElement(By.id("timePicker"));
        confirmButton = new NativeMobileGuiElement(By.id("button1"));
        checkPage();
    }
}
