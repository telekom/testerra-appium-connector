package eu.tsystems.mms.tic.testframework.mobile.adapter;

import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class ByTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByTranslator.class);

    private ByTranslator() {
    }

    /**
     * Translate the By locator to be readable by SeeTest. Though possible to construct, nested Byall's will throw a FennecRuntimeException.
     *
     * @param by the locator to be translated.
     * @return a string representation specially prepared for SeeTest.
     */
    public static String translateForSeeTest(By by) {
        return translateForSeeTest(by.toString(), true);
    }

    /**
     * Translate the By locator string expression to be readable be SeeTest.
     *
     * @param byExpression the locator string representation to be translated.
     * @param wrap         wraps the result in xpath=.//*[%s] or xpath=%s, depending on the given locator.
     * @return a string representation specially prepared for SeeTest.
     */
    private static String translateForSeeTest(String byExpression, boolean wrap) {
        String result;
        if (byExpression.contains("By.all")) {
            if (byExpression.split("By\\.all").length > 2) {
                throw new FennecRuntimeException(String.format("Nested ByAlls like \"%s\" are not supported.", byExpression));
            }
            String[] byExpressions = byExpression.substring(byExpression.indexOf("By.all({") + 8, byExpression.lastIndexOf("})")).split(",By\\.");
            StringBuilder sbr = new StringBuilder("");
            for (String by : byExpressions) {
                StringBuilder sb = new StringBuilder(by);
                sb.insert(0, "By.");
                sbr.append(" or ").append(translateForSeeTest(sb.toString(), false));
            }
            sbr.replace(0, 4, "");
            result = sbr.toString();
        } else {
            String value = byExpression.substring(byExpression.indexOf(' ') + 1);
            if (byExpression.contains("By.xpath") && wrap) {
                return String.format("xpath=%s", value);
            } else if (byExpression.contains("By.xpath") && !wrap) {
                return value;
            } else if (byExpression.contains("By.id:")) {
                result = String.format("@id='%s'", value);
            } else if (byExpression.contains("By.className:")) {
                result = String.format("contains(@class,'%s')", value);
            } else if (byExpression.contains("By.linkText:")) {
                result = String.format("@href='%s'", value);
            } else if (byExpression.contains("By.partialLinkText:")) {
                result = String.format("contains(@href,'%s')", value);
            } else if (byExpression.contains("By.tagName:")) {
                result = String.format("@nodeName='%s'", value.toUpperCase());
            } else {
                throw new FennecRuntimeException(String.format("The By Expression \"%s\" is not translatable.", byExpression));
            }
        }
        if (wrap) {
            return String.format("xpath=.//*[%s]", result);
        } else {
            return result;
        }
    }
}
