package eu.tsystems.mms.tic.testframework.mobile.pageobject;

import eu.tsystems.mms.tic.testframework.enums.CheckRule;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.DefaultPageFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.PageFactory;
import org.openqa.selenium.WebDriver;

/**
 * Created on 2023-03-13
 *
 * @author mgn
 */
public class AppiumPageFactory extends DefaultPageFactory {

    @Override
    public <T extends Page> T createPageWithCheckRule(Class<T> pageClass, WebDriver webDriver, CheckRule checkRule) {
        return super.createPageWithCheckRule(AppiumClassFinder.getInstance().getBestMatchingClass(pageClass, webDriver), webDriver, checkRule);
    }

    @Override
    public PageFactory clearThreadLocalPagesPrefix() {
        AppiumClassFinder.getInstance().clearCache();
        return super.clearThreadLocalPagesPrefix();
    }

}
