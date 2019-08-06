package eu.tsystems.mms.tic.testframework.mobile;

import eu.tsystems.mms.tic.testframework.exceptions.PageNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.exceptions.TimeoutException;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by rnhb on 03.02.2016.
 */
public class PageFactory {

    /**
     * @param mobileOperatingSystem
     * @deprecated Not necessary any more. In a getNew call, the mobileDriver will be retrieved and its active device is used to determine the operating system.
     */
    @Deprecated
    public static void setOperatingSystem(MobileOperatingSystem mobileOperatingSystem) {
        //Deprecated
    }

    public static <T extends MobilePage, S extends T> S getNew(Class<T> pageClass) {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        TestDevice activeDevice = mobileDriver.getActiveDevice();
        if (activeDevice == null) {
            throw new PageFactoryException("A page of class " + pageClass + " was requested from " + PageFactory.class.getSimpleName()
                    + ", but no device has been reserved and activated.");
        }
        String prefix = activeDevice.getOperatingSystem().getClassPrefix();

        String generalPageName = pageClass.getName();
        // try to find the appropriate class in the same package
        String parentPageName = pageClass.getSimpleName();
        String pageName = generalPageName.replace(parentPageName, prefix + parentPageName);

        Class<S> concretePageClass = null;
        Exception thrownException = null;
        try {
            //noinspection unchecked
            concretePageClass = (Class<S>) Class.forName(pageName);
        } catch (ClassNotFoundException | ClassCastException e) {
            thrownException = e;
        }

        if (concretePageClass == null && pageName.contains(".base.")) {
            pageName = pageName.replaceFirst("\\.base\\.", "." + prefix.toLowerCase() + ".");
            try {
                //noinspection unchecked
                concretePageClass = (Class<S>) Class.forName(pageName);
                thrownException = null;
            } catch (ClassNotFoundException | ClassCastException e) {
                thrownException = e;
            }
        }

        if (thrownException != null || concretePageClass == null) {
            throw new PageFactoryException("Did not find a class " + pageName + " for operating system " + prefix + "." +
                    " It is expected, that there is a class with the same name and the operating system as prefix in the same " +
                    "package, as the class that was given as parameter.", thrownException);
        }

        Constructor<?>[] constructors = concretePageClass.getConstructors();
        assert constructors.length > 0;

        Object pageInstance = null;
        try {
            pageInstance = constructors[0].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            thrownException = e;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause.getClass() == PageNotFoundException.class) {
                throw (PageNotFoundException) cause;
            } else if (cause.getClass() == AssertionError.class) {
                throw (AssertionError) cause;
            } else if (cause.getClass() == TimeoutException.class) {
                throw (TimeoutException) cause;
            } else {
                thrownException = e;
            }
        }

        if (thrownException != null) {
            throw new PageFactoryException("Failed to construct page " + concretePageClass.getName()
                    + ". Does it have a no-arg constructor (mandatory)?. If yes, check the underlying exceptions.", thrownException);
        }

        if (!pageInstance.getClass().isAssignableFrom(concretePageClass)) {
            throw new TesterraSystemException("Internal Error: Class of created page instance " + pageInstance.getClass() +
                    " should be assignable from concrete page class " + concretePageClass);
        }

        return concretePageClass.cast(pageInstance);
    }
}
