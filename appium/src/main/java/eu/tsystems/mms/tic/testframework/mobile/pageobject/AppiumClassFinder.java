package eu.tsystems.mms.tic.testframework.mobile.pageobject;

import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileOsChecker;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.PageObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2023-03-13
 *
 * @author mgn
 */
public class AppiumClassFinder implements Loggable {

    private static AppiumClassFinder INSTANCE;

    /**
     * This call takes some time. It has an impact to the duration of the first page check (takes ca 2-3 seconds longer).
     */
    private final Reflections reflections = new Reflections(filter(configure()));

    private AppiumClassFinder() {
    }

    public static AppiumClassFinder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppiumClassFinder();
        }
        return INSTANCE;
    }

    public <T extends Page> Class<T> getBestMatchingClass(Class<T> baseClass, WebDriver webDriver) {
        MobileOsChecker osChecker = new MobileOsChecker();
        Platform platform = osChecker.getPlatform(webDriver);

        Class<? extends PageObject> matchedClass = AppiumClassFinder.Caches.getCache(baseClass, platform);

        if (matchedClass == null) {
            // scan and fill cache
            this.findSubPagesOf(baseClass);
            matchedClass = AppiumClassFinder.Caches.getCache(baseClass, platform);

            if (matchedClass == null) {
                throw new RuntimeException("Something went wrong scanning this class for sub types: " + baseClass.getName());
            }
        }

        return (Class<T>) matchedClass;
    }

    private <T extends PageObject> void findSubPagesOf(final Class<T> pageClass) {
        log().debug("Searching for subtypes of class {}", pageClass);
        Class<T> baseClass = null;
        if (!Modifier.isAbstract(pageClass.getModifiers())) {
            baseClass = pageClass;
            AppiumClassFinder.Caches.setCache(baseClass, this.getMatchingPlatform(baseClass.getSimpleName()), baseClass);
        }

        Set<? extends Class<T>> subClasses = reflections.getSubTypesOf((Class) baseClass);
        for (Class<T> subClass : subClasses) {
            String subClassName = subClass.getSimpleName();

            if (Modifier.isAbstract(subClass.getModifiers())) {
                log().debug("Not taking {} into consideration, because it is abstract", subClassName);
            } else {
                if (this.matchingPattern(subClassName, baseClass.getSimpleName())) {
                    AppiumClassFinder.Caches.setCache(baseClass, this.getMatchingPlatform(subClassName), subClass);
                }
            }
        }
    }

    public void clearCache() {
        AppiumClassFinder.Caches.IMPLEMENTATIONS_CACHE.clear();
    }

    private boolean matchingPattern(String className, String baseClassName) {
        Pattern pattern = Pattern.compile("(?i)^(ios|android)" + baseClassName);
        Matcher matcher = pattern.matcher(className);
        return matcher.find();
    }

    private Platform getMatchingPlatform(String className) {
        if (className.toLowerCase().contains(Platform.IOS.toString().toLowerCase())) {
            return Platform.IOS;
        }
        if (className.toLowerCase().contains(Platform.ANDROID.toString().toLowerCase())) {
            return Platform.ANDROID;
        }
        return Platform.ANY;
    }

    private ConfigurationBuilder configure() {
        return new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath());
    }

    /**
     * This method should prune resources we are not interested in, but not change the interesting results.
     */
    private ConfigurationBuilder filter(final ConfigurationBuilder configuration) {
        configuration.setScanners(new SubTypesScanner()); // drops TypeAnnotationScanner
        configuration.useParallelExecutor();
        return configuration;
    }

    /**
     * Store already found classes in a map like:
     * MainClass.class: [
     * Platform.ANY:       MainClass.class
     * Platform.IOS:       IOSMainClass.class
     * Platform.ANDROID:   AndroidMainClass.class
     * ]
     */
    private static class Caches {

        private static final Platform DEFAULT_PLATFORM = Platform.ANY;

        private static final Map<Class<? extends PageObject>, Map<Platform, Class<? extends PageObject>>> IMPLEMENTATIONS_CACHE = new ConcurrentHashMap<>();

        private static Class<? extends PageObject> getCache(Class<? extends PageObject> pageClass, Platform platform) {
            if (platform != Platform.ANDROID && platform != Platform.IOS) {
                platform = DEFAULT_PLATFORM;
            }

            synchronized (IMPLEMENTATIONS_CACHE) {
                if (!IMPLEMENTATIONS_CACHE.containsKey(pageClass)) {
                    return null;
                }

                Map<Platform, Class<? extends PageObject>> map = IMPLEMENTATIONS_CACHE.get(pageClass);
                Class<? extends PageObject> matchedClass = map.get(platform);
                if (matchedClass == null) {
                    matchedClass = map.get(DEFAULT_PLATFORM);
                }
                return matchedClass;
            }
        }

        /**
         * Returns the best matching class from map. If no platform specific map exists, the baseclass will return.
         */
        private static void setCache(Class<? extends PageObject> pageClass, Platform platform, Class<? extends PageObject> prioritizedClassInfos) {
            if (platform != Platform.ANDROID && platform != Platform.IOS) {
                platform = DEFAULT_PLATFORM;
            }

            synchronized (IMPLEMENTATIONS_CACHE) {
                if (!IMPLEMENTATIONS_CACHE.containsKey(pageClass)) {
                    IMPLEMENTATIONS_CACHE.put(pageClass, new HashMap<>());
                }
                Map<Platform, Class<? extends PageObject>> map = IMPLEMENTATIONS_CACHE.get(pageClass);

                map.put(platform, prioritizedClassInfos);
            }
        }
    }


}
