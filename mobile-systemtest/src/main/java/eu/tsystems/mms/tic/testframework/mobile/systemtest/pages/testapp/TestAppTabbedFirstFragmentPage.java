package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 01.02.2017.
 */
public class TestAppTabbedFirstFragmentPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement firstFragSection = new NativeMobileGuiElement(By.id("first_frag_top_section"));

    /**
     * Default constructor.
     */
    public TestAppTabbedFirstFragmentPage() {

        super();
        checkPage();
    }

    /**
     * Go to the secondFragmentPage
     *
     * @return secondFragmentPage
     */
    public TestAppTabbedSecondFragmentPage goToSecondFrag() {

        firstFragSection.swipe(Direction.RIGHT,
                new Double(Integer.parseInt(firstFragSection.getProperty("width")) * 0.1).intValue(), 500);
        return new TestAppTabbedSecondFragmentPage();
    }

    /**
     * @return boolean with the result of the isDisplayed method after performing it on the firstFragSection
     */
    public boolean isFirstFragSectionIsDisplayed() {

        return firstFragSection.isDisplayed();
    }
}
