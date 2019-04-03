package eu.tsystems.mms.tic.testframework.mobile.test;

import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import eu.tsystems.mms.tic.testframework.mobile.adapter.ByTranslator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Checks the correct function of the <code>ByTranslator.translateForSeeTest()</code> method.
 */
public class ByTranslatorTest {

    @Test
    public void T01_xPath() {
        By xpath = By.xpath("xpath");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=xpath");
    }

    @Test
    public void T02_id() {
        By xpath = By.id("id");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=.//*[@id='id']");
    }

    @Test
    public void T03_className() {
        By xpath = By.className("class");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=.//*[contains(@class,'class')]");
    }

    @Test
    public void T04_linkText() {
        By xpath = By.linkText("linkText");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=.//*[@href='linkText']");
    }

    @Test
    public void T05_partialLinkText() {
        By xpath = By.partialLinkText("partialLinkText");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=.//*[contains(@href,'partialLinkText')]");
    }

    @Test
    public void T06_tagName() {
        By xpath = By.tagName("tagName");
        String translatedXPath = ByTranslator.translateForSeeTest(xpath);
        Assert.assertEquals(translatedXPath, "xpath=.//*[@nodeName='TAGNAME']");
    }

    @Test
    public void T07_byall_equal_Bys() {
        ByAll byall = new ByAll(By.xpath("xpath_1"), By.xpath("xpath_2"));
        String translatedXPath = ByTranslator.translateForSeeTest(byall);
        Assert.assertEquals(translatedXPath, "xpath=.//*[xpath_1 or xpath_2]");
    }

    @Test
    public void T08_byall_different_Bys() {
        ByAll byall = new ByAll(By.id("id"), By.className("class"));
        String translatedXPath = ByTranslator.translateForSeeTest(byall);
        Assert.assertEquals(translatedXPath, "xpath=.//*[@id='id' or contains(@class,'class')]");
    }

    @Test
    public void T09_byall_multiple_Bys() {
        ByAll byall = new ByAll(By.className("class"), By.xpath("xpath"), By.linkText("linkText"), By.xpath("xpath"));
        String translatedXPath = ByTranslator.translateForSeeTest(byall);
        Assert.assertEquals(translatedXPath, "xpath=.//*[contains(@class,'class') or xpath or @href='linkText' or xpath]");
    }

    @Test(expectedExceptions = FennecRuntimeException.class)
    public void T10_byall_nested_Byalls() {
        ByAll byall = new ByAll(new ByAll(By.className("class"), By.xpath("xpath")), By.linkText("linkText"));
        ByTranslator.translateForSeeTest(byall);
    }
}
