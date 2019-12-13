package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by nkfa on 31.01.2017.
 */
public class AndroidTestAppProgressPage extends TestAppProgressPage {

    /**
     * Default constructor.
     */
    public AndroidTestAppProgressPage() {

        progressBar = new NativeMobileGuiElement(By.id("progress_bar"));
        seekBar = new NativeMobileGuiElement(By.id("progress_seek_bar"));
        resultText = new NativeMobileGuiElement(By.id("progress_text"));
        resetButton = new NativeMobileGuiElement(By.id("progress_reset_button"));
        checkPage();
    }
}
