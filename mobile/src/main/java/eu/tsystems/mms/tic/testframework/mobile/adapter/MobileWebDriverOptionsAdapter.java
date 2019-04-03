package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rnhb on 10.02.2016.
 */
public class MobileWebDriverOptionsAdapter implements WebDriver.Options {

    private final MobileDriver mobileDriver;
    private final MobileWebDriverWindowAdapter windowAdapter;

    public MobileWebDriverOptionsAdapter(MobileDriver mobileDriver) {

        this.mobileDriver = mobileDriver;
        windowAdapter = new MobileWebDriverWindowAdapter(mobileDriver);
    }

    @Override
    public void addCookie(Cookie cookie) {
        mobileDriver.seeTestClient().hybridRunJavascript("", 0,
                "document.cookie = \"" + cookie.getName() + "=" + cookie.getValue() + ";\";");
    }

    @Override
    public void deleteCookieNamed(String s) {
        mobileDriver.seeTestClient()
                .hybridRunJavascript("", 0, "document.cookie = \"" + s + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;\"");
    }

    @Override
    public void deleteCookie(Cookie cookie) {
        mobileDriver.seeTestClient().hybridRunJavascript("", 0,
                "document.cookie = \"" + cookie.getName() + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;\"");
    }

    @Override
    public void deleteAllCookies() {
        mobileDriver.seeTestClient().hybridClearCache(true, false);
    }

    @Override
    public Set<Cookie> getCookies() {
        String result = mobileDriver.seeTestClient().hybridRunJavascript("", 0, "var result = document.cookie;");
        Set<Cookie> cookies = new HashSet<>();

        for (String cookiePair : result.split(";")) {
            String[] keyValue = cookiePair.split("=");
            cookies.add(new Cookie(keyValue[0], keyValue[1]));
        }

        return cookies;
    }

    @Override
    public Cookie getCookieNamed(String s) {
        String result = mobileDriver.seeTestClient().hybridRunJavascript("", 0, "var result = document.cookie;");
        Cookie cookie = null;

        for (String cookiePair : result.split(";")) {
            String[] keyValue = cookiePair.split("=");
            if(keyValue[0].trim().equals(s)) {
                cookie = new Cookie(keyValue[0], keyValue[1]);
                break;
            }
        }
        return cookie;
    }

    @Override
    public WebDriver.Timeouts timeouts() {
        throw new NotSupportedException();
    }

    @Override
    public WebDriver.ImeHandler ime() {
        throw new NotSupportedException();
    }

    @Override
    public WebDriver.Window window() {
        return windowAdapter;
    }

    @Override
    public Logs logs() {
        throw new NotSupportedException();
    }
}
