package eu.tsystems.mms.tic.testframework.mobile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.experitest.client.Client;

import eu.tsystems.mms.tic.testframework.mobile.driver.LocatorType;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * ClientListener
 * 
 * @author rani
 */
public abstract class ClientListener {

    static final Logger logger = LoggerFactory.getLogger(ClientListener.class);

    /**
     * Google Automatic Translator Hook
     */
    public static void addGoogleLanguagePopupRemoverListener() {
        Client client = MobileDriverManager.getMobileDriver().seeTestClient();
        client.addMobileListener(
                LocatorType.NATIVE.toString(), "//*[@id='infobar_close_button']",
                (s, s1) -> {
                    logger.error("Google Translator seems to be activated!");
                    client.click(LocatorType.NATIVE.toString(), "id=infobar_close_button", 0, 1);
                    return true;
                });
        logger.info("Google Chrome automatic translator listener added.");
    }

    /**
     * Google Chrome Terms and Conditions Hook
     */
    public static void addGoogleChromeTermsAndConditionsListener() {
        Client client = MobileDriverManager.getMobileDriver().seeTestClient();
        client.addMobileListener(
                LocatorType.NATIVE.toString(), "//*[@id='terms_accept']",
                (s, s1) -> {
                    logger.warn("Google Chrome seems to have changed its terms and conditions!");
                    client.click(LocatorType.NATIVE.toString(), "id=terms_accept", 0, 1);
                    NativeMobileGuiElement element = new NativeMobileGuiElement("//*[@id='negative_button']");
                    if (element.isDisplayed()) {
                        element.click();
                    }
                    return true;
                });
        logger.info("Google Chrome terms and conditions listener added.");
    }

    /*
     * Native Permission Hook does not work for Chrome Browser. Might be because the client listens to events fired in
     * chrome not outside of chrome. /** Chrome Permission Hook \/ public static void
     * addGoogleChromePermissionsListener() { Client client = MobileDriverManager.getMobileDriver().seeTestClient();
     * client.addMobileListener( LocatorType.NATIVE.toString(), "xpath=//*[@id='permission_message']", (s, s1) -> {
     * logger.warn("Chrome needs permissions. Allow these."); client.click(LocatorType.NATIVE.toString(),
     * "id=permission_allow_button", 0, 1); return true; } ); logger.info("Google Chrome permissions listener added.");
     * }
     */

    /**
     * This might not work for automation because the elements viewing are not in webview
     */
    public static void addGoogleChromePromoDialogListener() {
        Client client = MobileDriverManager.getMobileDriver().seeTestClient();
        client.addMobileListener(
                LocatorType.NATIVE.toString(), "//*[@id='promo_dialog_layout']",
                (s, s1) -> {
                    logger.warn("Chrome want something to advertise. Do not comply.");
                    client.click(LocatorType.NATIVE.toString(), "id=button_secondary", 0, 1);
                    return true;
                });
        logger.info("Google Chrome promo dialog listener added.");
    }
}