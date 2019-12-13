package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;

/**
 * Created by nkfa on 01.02.2017.
 */
public abstract class TestAppContextPage extends AbstractTestAppPage {

    protected NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text("Control"));
    @Check
    private final NativeMobileGuiElement resetButton = new NativeMobileGuiElement(By.id("context_reset_button"));
    private final NativeMobileGuiElement list = new NativeMobileGuiElement(By.id("context_listView"));
    private final NativeMobileGuiElement clickedItemsList = new NativeMobileGuiElement(By.id("context_menu_clicked_items"));

    /**
     * Default constructor.
     */
    public TestAppContextPage() {

        checkPage();
    }

    /**
     * perform longClick on a list entry
     *
     * @return TestAppContextMenuPage
     */
    public TestAppContextMenuPage doLongClick() {

        listEntry.longClick();
        return new TestAppContextMenuPage();
    }

    /**
     * Using swipeWhileNotFound function for finding the other fragmentPage
     *
     * @return firstFragmentPage
     */
    public void swipeWhileNotFoundListEntry(String entryText) {

        NativeMobileGuiElement lastListEntry = new NativeMobileGuiElement(By.text(entryText));
        lastListEntry.swipeWhileNotFound("DOWN",
                new Double(Integer.parseInt(list.getProperty("height")) * 0.6).intValue(), 500, 500,
                5, false);
    }

    /**
     * @return isDisplayed of a list entry
     */
    public boolean isListEntryIsDisplayed(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.isDisplayed();
    }

    /**
     * @return waitForIsDisplayed of a list entry
     */
    public boolean waitForIsListEntryIsDisplayed(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.waitForIsDisplayed();
    }

    /**
     * @return waitForIsNotDisplayed of a list entry
     */
    public boolean waitForIsListEntryIsNotDisplayed(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.waitForIsNotDisplayed();
    }

    /**
     * @return isPresent of a list entry
     */
    public boolean isListEntryIsPresent(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.isPresent();
    }

    /**
     * @return waitForIsPresent of a list entry
     */
    public boolean waitForIsListEntryIsPresent(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.waitForIsPresent();
    }

    /**
     * @return waitForIsNotPresent of a list entry
     */
    public boolean waitForIsListEntryIsNotPresent(String entryText) {

        NativeMobileGuiElement listEntry = new NativeMobileGuiElement(By.text(entryText));
        return listEntry.waitForIsNotPresent();
    }

    /**
     * Select and click a text from list
     *
     * @param text Text in list to select.
     */
    public void doListSelectAndClick(String text) {

        list.listSelect("text=" + text, 0, true);
    }

    /**
     * @return array of clicked items.
     */
    public String[] doGetClickedItems() {

        return clickedItemsList.getText().split(": ")[1].split(" ");
    }

    /**
     * Scroll to entry in list.
     *
     * @param entryText text of list entry to scroll to.
     */
    public void scrollToListEntry(String entryText) {

        list.scrollToListEntry("text=" + entryText, 0);
    }

    public void doClickReset() {

        resetButton.click();
    }
}
