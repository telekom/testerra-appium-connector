package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.PageFactory;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 01.02.2017.
 */
public class TestAppContextMenuPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement deleteButton = new NativeMobileGuiElement(By.textContains("Delete"));

    /**
     * Default constructor.
     */
    public TestAppContextMenuPage() {

        checkPage();
    }

    public TestAppContextPage clickDelete() {

        deleteButton.click();
        return PageFactory.getNew(TestAppContextPage.class);
    }

}
