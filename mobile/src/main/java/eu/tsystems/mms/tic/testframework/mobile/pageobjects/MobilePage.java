package eu.tsystems.mms.tic.testframework.mobile.pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.google.common.base.Stopwatch;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.mobilepageobject.MobileGuiElementCheckFieldAction;
import eu.tsystems.mms.tic.testframework.mobile.mobilepageobject.SetContainingPageFieldAction;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.pageobjects.AbstractPage;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldWithActionConfig;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.SetNameFieldAction;

/**
 * Created by rnhb on 29.01.2015.
 *
 * @author rnhb
 */
public class MobilePage extends AbstractPage {

    protected MobileDriver driver;

    private ScreenDump nativeScreenDump;

    private static final int MAX_CACHE_TIME_IN_SECONDS = 10;

    private Stopwatch timeSinceCache;

    public MobilePage() {
        driver = MobileDriverManager.getMobileDriver();
    }

    @Override
    protected final void handleDemoMode(WebDriver webDriver) {
    }

    @Override
    protected List<FieldAction> getFieldActions(List<FieldWithActionConfig> fields, AbstractPage declaringPage) {
        List<FieldAction> fieldActions = new ArrayList<FieldAction>();
        for (FieldWithActionConfig field : fields) {
            SetNameFieldAction setNameFieldAction = new SetNameFieldAction(field.field, declaringPage);
            SetContainingPageFieldAction setContainingPageFieldAction = new SetContainingPageFieldAction(field.field,
                    declaringPage);
            MobileGuiElementCheckFieldAction mobileGuiElementCheckFieldAction = new MobileGuiElementCheckFieldAction(
                    field, declaringPage);

            fieldActions.add(setNameFieldAction);
            fieldActions.add(setContainingPageFieldAction);
            fieldActions.add(mobileGuiElementCheckFieldAction);
        }
        return fieldActions;
    }

    @Override
    public void waitForPageToLoad() {
    }

    @Override
    protected void checkPagePreparation() {

    }

    private void cacheScreenDump(ScreenDump.Type screenDumpType) {
        nativeScreenDump = new ScreenDump(driver.seeTestClient().getVisualDump(screenDumpType.toString()));
        timeSinceCache = Stopwatch.createStarted();
    }

    public void clearCachedMobileGuiElements() {
        nativeScreenDump = null;
    }

    public ScreenDump getScreenDump(ScreenDump.Type screenDumpType) {
        if (nativeScreenDump == null) {
            cacheScreenDump(screenDumpType);
        } else {
            long elapsed = timeSinceCache.elapsed(TimeUnit.SECONDS);
            if (elapsed > MAX_CACHE_TIME_IN_SECONDS) {
                cacheScreenDump(screenDumpType);
            }
        }
        return nativeScreenDump;
    }
}
