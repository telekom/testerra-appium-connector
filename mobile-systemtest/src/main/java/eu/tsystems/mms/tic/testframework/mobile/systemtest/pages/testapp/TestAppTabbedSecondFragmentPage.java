package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 01.02.2017.
 */
public class TestAppTabbedSecondFragmentPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement secondFragSection = new NativeMobileGuiElement(By.id("second_frag_top_section"));

    /**
     * Default constructor.
     */
    public TestAppTabbedSecondFragmentPage() {

        super();
        checkPage();
    }

    /**
     * Go to the firstFragmentPage
     *
     * @return firstFragmentPage
     */
    public TestAppTabbedFirstFragmentPage goToFirstFrag() {

        secondFragSection.swipe(Direction.LEFT,
                new Double(Integer.parseInt(secondFragSection.getProperty("width")) * 0.1).intValue(), 500);
        return new TestAppTabbedFirstFragmentPage();
    }

    /**
     * @return boolean with the result of the isDisplayed method after performing it on the secondFragSection
     */
    public boolean isSecondFragSectionIsDisplayed() {

        return secondFragSection.isDisplayed();
    }
}
