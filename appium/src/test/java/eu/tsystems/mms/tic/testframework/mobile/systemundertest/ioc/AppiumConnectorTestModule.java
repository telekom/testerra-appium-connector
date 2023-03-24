package eu.tsystems.mms.tic.testframework.mobile.systemundertest.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import eu.tsystems.mms.tic.testframework.mobile.pageobject.AppiumPageFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.PageFactory;

/**
 * Created on 2023-03-13
 *
 * @author mgn
 */
public class AppiumConnectorTestModule extends AbstractModule {

    protected void configure() {
        bind(PageFactory.class).to(AppiumPageFactory.class).in(Scopes.SINGLETON);
    }

}
