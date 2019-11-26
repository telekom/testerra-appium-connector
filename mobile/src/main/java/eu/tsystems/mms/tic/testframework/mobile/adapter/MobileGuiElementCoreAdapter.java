package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnhb on 09.02.2016.
 */
public class MobileGuiElementCoreAdapter extends MobileWebElementAdapter implements GuiElementCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileGuiElementCoreAdapter.class);

    public MobileGuiElementCoreAdapter(WebMobileGuiElement mobileGuiElement) {
        super(mobileGuiElement);
    }

    @Override
    public WebElement getWebElement() {
        return new MobileWebElementAdapter(webMobileGuiElement);
    }

    @Override
    public By getBy() {
        throw new NotSupportedException("getBy() is not supported for MobileGuiElement.");
    }

    @Override
    public void scrollToElement() {
        scrollToElement(0);
    }

    @Override
    public void scrollToElement(int yOffset) {
        final Point location = webMobileGuiElement.getLocation();
        final int x = location.getX();
        final int y = location.getY() - yOffset;
        LOGGER.trace("Scrolling into view: " + x + ", " + y);
        webMobileGuiElement.getMobileDriver().seeTestClient().hybridRunJavascript("", 0, "scroll(" + x + ", " + y + ")");

        // not sufficient - we might need to scroll upwards
        // webMobileGuiElement.swipeWhileNotFound(Direction.DOWN.toString(), 100, 1000, 0, 5, false);
    }

    @Override
    public void select() {
        webMobileGuiElement.click();
    }

    @Override
    public void deselect() {
        webMobileGuiElement.click();
    }

    @Override
    public void type(String text) {
        webMobileGuiElement.sendText(text);
    }

    @Override
    public void clickJS() {
        webMobileGuiElement.click();
    }

    @Override
    public void clickAbsolute() {
        webMobileGuiElement.click();
    }

    @Override
    public void mouseOverAbsolute2Axis() {
        LOGGER.warn("Mouse over not possible on mobile system.");
    }

    @Override
    public GuiElement getSubElement(By byLocator, String description) {

        return getSubElement(byLocator).setName(description);
    }

    @Override
    public GuiElement getSubElement(Locate locate) {

        final String translatedBy = ByTranslator.translateForSeeTest(locate.getBy());
        final String parentLocator = this.webMobileGuiElement.getLocator().getLocatorWithoutXpathPrefix();

        String subLocator = new MobileLocator(LocatorType.WEB.toString(), translatedBy, 0).getLocatorWithoutXpathPrefix();
        if (subLocator.startsWith(".")) {
            subLocator = subLocator.substring(1);
        }

        return new GuiElement(WebDriverManager.getWebDriver(), By.xpath(parentLocator + subLocator));
    }

    @Override
    public GuiElement getSubElement(By by) {
        return getSubElement(Locate.by(by));
    }

    @Override
    public WebElement findElement(By byLocator) {
        throw new NotSupportedException("Should be implemented later");
    }

    @Override
    public Rectangle getRect() {
        throw new NotSupportedException("getRect() not supported by " + MobileGuiElementCoreAdapter.class.getSimpleName());
    }

    @Override
    public void mouseOver() {
        LOGGER.warn("Mouse over not possible on mobile system.");
    }

    @Override
    public void mouseOverJS() {
        LOGGER.warn("Mouse over not possible on mobile system.");
    }

    @Override
    public Select getSelectElement() {
        throw new NotSupportedException("Should be implemented later");
    }

    @Override
    public List<String> getTextsFromChildren() {
        MobileLocator locator = webMobileGuiElement.getLocator();
        String xPath = locator.locator + "/*";
        MobileLocator childrenLocator = new MobileLocator("irrelevant", xPath, 0);

        MobileDriver mobileDriver = webMobileGuiElement.getMobileDriver();
        ScreenDump nativeScreenDump = mobileDriver.getScreenDump(ScreenDump.Type.NATIVE_INSTRUMENTED);

        List<String> childrenTexts = new ArrayList<String>();

        NodeList elements = nativeScreenDump.findElements(childrenLocator);
        for (int i = 0; i < elements.getLength(); i++) {
            Node item = elements.item(i);
            NamedNodeMap attributes = item.getAttributes();
            String nodeValue = item.getNodeValue();
            Set<Node> values = new HashSet<Node>();
            values.add(attributes.getNamedItem("text"));
            values.add(attributes.getNamedItem("value"));
            values.add(attributes.getNamedItem("hint"));
            values.add(attributes.getNamedItem("placeholder"));
            for (Node value : values) {
                if (value != null && StringUtils.isNotEmpty(value.getNodeValue())) {
                    childrenTexts.add(value.getNodeValue());
                }
            }
            childrenTexts.add(nodeValue);
        }

        ScreenDump webScreenDump = mobileDriver.getScreenDump(ScreenDump.Type.WEB);
        elements = webScreenDump.findElements(childrenLocator);

        for (int i = 0; i < elements.getLength(); i++) {
            Node item = elements.item(i);
            String nodeValue = item.getNodeValue();
            childrenTexts.add(nodeValue);
        }

        return childrenTexts;
    }

    @Override
    public void doubleClick() {
        throw new NotSupportedException("Should be implemented later");
    }

    @Override
    public void highlight() {
        LOGGER.warn("highlight not implemented on mobile system.");
    }

    @Override
    public void swipe(int offsetX, int offSetY) {
        if (offsetX != 0 && offSetY != 0) {
            LOGGER.warn("Swipe was called for WebMobileGuiElement with a diagonal offset. This is not supported. Will only swipe the x offset!");
        }
        Direction direction = Direction.getByOffset(offsetX, offSetY);
        webMobileGuiElement.swipe(direction, offsetX, offSetY);
    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {
        throw new NotSupportedException("Should be implemented later");
    }

    @Override
    public int getNumberOfFoundElements() {
        return webMobileGuiElement.getNumberOfElements();
    }

    @Override
    public void rightClick() {
        LOGGER.warn("rightClick not implemented on mobile system.");
    }

    @Override
    public void rightClickJS() {
        LOGGER.warn("rightClickJS not implemented on mobile system.");
    }

    @Override
    public void doubleClickJS() {
        LOGGER.warn("doubleClickJS not implemented on mobile system.");
    }

    @Override
    public File takeScreenshot() {
        return webMobileGuiElement.takeScreenshot();
    }

    @Override
    public boolean isPresent() {
        return webMobileGuiElement.isPresent();
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {
        throw new NotSupportedException("Should be implemented later");
    }

    @Override
    public boolean isVisible(boolean complete) {

        if (!isDisplayed()) return false;

        final Dimension size = this.getSize();
        final Point location = this.getLocation();

        // todo erku here
        final int screenWidth = MobileDriverManager.getMobileDriver().getActiveDevice().getPixelsX();
        final int screenHeight = MobileDriverManager.getMobileDriver().getActiveDevice().getPixelsY();

        //        final int posY = location.getY();
        //        final int height = size.getHeight();
        //        final int elementEndY = posY + height;
        //        final boolean isAboveTopBorder = elementEndY < 0 || posY <= 0;
        //        final boolean isBelowBottomBorder = elementEndY > borderBottom;

        java.awt.Rectangle viewportRect = new java.awt.Rectangle(0, 0, screenWidth, screenHeight);
        java.awt.Rectangle elementRect = new java.awt.Rectangle(location.x, location.y, size.width, size.height);
        return ((complete && viewportRect.contains(elementRect)) || viewportRect.intersects(elementRect));
    }

    @Override
    public boolean isDisplayedFromWebElement() {
        return webMobileGuiElement.isDisplayed();
    }

    @Override
    public boolean isSelectable() {
        Map<String, String> properties = webMobileGuiElement.getProperties();
        return properties.containsKey("selected");
    }

    @Override
    public String toString() {
        return webMobileGuiElement.toString();
    }
}
