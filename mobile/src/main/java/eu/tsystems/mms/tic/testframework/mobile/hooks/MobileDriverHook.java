package eu.tsystems.mms.tic.testframework.mobile.hooks;

import eu.tsystems.mms.tic.testframework.execution.worker.finish.TakeScreenshotsWorker;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.report.FennecListener;

public class MobileDriverHook implements ModuleHook {
    @Override
    public void init() {
        MobileDriverManager.registerWebDriverFactory();
        FennecListener.registerAfterMethodWorker(TakeScreenshotsWorker.class);

    }

    @Override
    public void terminate() {

    }
}
