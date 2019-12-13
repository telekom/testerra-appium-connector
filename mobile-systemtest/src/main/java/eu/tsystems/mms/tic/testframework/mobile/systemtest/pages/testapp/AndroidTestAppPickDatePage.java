package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by nkfa on 31.01.2017.
 */
public class AndroidTestAppPickDatePage extends TestAppPickDatePage {

    /**
     * Default constructor.
     */
    public AndroidTestAppPickDatePage() {

        datePicker = new NativeMobileGuiElement(By.id("datePicker"));
        confirmButton = new NativeMobileGuiElement(By.id("button1"));
        checkPage();
    }
}
