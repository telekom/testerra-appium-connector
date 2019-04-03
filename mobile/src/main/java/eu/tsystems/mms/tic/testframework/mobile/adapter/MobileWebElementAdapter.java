package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.exceptions.ElementNotFoundException;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rnhb on 09.02.2016.
 */
public class MobileWebElementAdapter implements WebElement {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileWebElementAdapter.class);
    final WebMobileGuiElement webMobileGuiElement;

    public MobileWebElementAdapter(WebMobileGuiElement webMobileGuiElement) {
        this.webMobileGuiElement = webMobileGuiElement;
    }

    @Override
    public final void click() {
        webMobileGuiElement.click();
    }

    @Override
    public final void submit() {
        MobileDriver mobileDriver = webMobileGuiElement.getMobileDriver();
        mobileDriver.pressEnterOnKeyboard();
    }

    @Override
    public final void sendKeys(CharSequence... charSequences) {
        webMobileGuiElement.sendText(Arrays.toString(charSequences));
    }

    @Override
    public final void clear() {
        webMobileGuiElement.setText("");
    }

    @Override
    public final String getTagName() {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        if (properties == null) {
            throw new ElementNotFoundException(webMobileGuiElement.toString());
        }
        return properties.get("nodeName");
    }

    @Override
    public final String getAttribute(String s) {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        if (properties == null) {
            throw new ElementNotFoundException(webMobileGuiElement.toString());
        }
        return properties.get(s);
    }

    @Override
    public final boolean isSelected() {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        if (properties == null) {
            throw new ElementNotFoundException(webMobileGuiElement.toString());
        }
        return properties.containsKey("selected") && "true".equals(properties.get("selected"));
    }

    @Override
    public final boolean isEnabled() {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        if (properties == null) {
            throw new ElementNotFoundException(webMobileGuiElement.toString());
        }
        return properties.containsKey("enabled") && "true".equals(properties.get("enabled"));
    }

    @Override
    public final String getText() {
        return webMobileGuiElement.getText();
    }

    @Override
    public final List<WebElement> findElements(By by) {
        throw new NotSupportedException("findElements() not supported on MobileWebElementAdapter.");
    }

    @Override
    public WebElement findElement(By by) {
        throw new NotSupportedException("findElement() not supported on MobileWebElementAdapter.");
    }

    @Override
    public boolean isDisplayed() {
        return webMobileGuiElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return webMobileGuiElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        if (properties == null) {
            throw new ElementNotFoundException(webMobileGuiElement.toString());
        }
        try {
            Integer width = Integer.valueOf(properties.get("width"));
            Integer height = Integer.valueOf(properties.get("height"));
            return new Dimension(width, height);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public Rectangle getRect() {
        throw new NotSupportedException("getRect() not supported on MobileWebElementAdapter.");
    }

    @Override
    public String getCssValue(String cssProperty) {
        // works only for xpath
        String locator = webMobileGuiElement.getLocator().getLocatorWithoutXpathPrefix();
        String script = String
                .format("var element = document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue,"
                                + "style = window.getComputedStyle(element)," +
                                "result = style.getPropertyValue('%s');",
                        locator, cssProperty);
        String jsResult = webMobileGuiElement.getMobileDriver().seeTestClient().hybridRunJavascript(
                "", 0,
                script);
        // Webdriver always returns rgba so adapt
        if (jsResult.startsWith("rgb(")) {
            jsResult = "rgba(" + jsResult.substring(4, jsResult.lastIndexOf(')')) + ", 1)";
        }
        return jsResult;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) {
        return null;
    }
}
