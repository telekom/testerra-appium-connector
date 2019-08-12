package eu.tsystems.mms.tic.testframework.mobile.playground;

import com.experitest.client.MobileListener;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.Cloud;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.Applications;
import eu.tsystems.mms.tic.testframework.mobile.device.DeviceStore;
import eu.tsystems.mms.tic.testframework.mobile.device.MobileOperatingSystem;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.driver.DeviceReservationPolicy;
import eu.tsystems.mms.tic.testframework.mobile.driver.Direction;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriverManager;
import eu.tsystems.mms.tic.testframework.mobile.driver.ScreenDumpType;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.MobilePage;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ImageMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.MobileLocator;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.ScreenDump;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.WebMobileGuiElement;
import eu.tsystems.mms.tic.testframework.mobile.worker.MobileScreenshotGrabber;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.utils.TestUtils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by rnhb on 17.11.2015.
 */
@Listeners(TesterraListener.class)
public class Tests {

    private final String visualDum = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<node onScreen=\"false\" top=\"false\">\n" +
            "    <node busy=\"false\" class=\"com.android.internal.policy.impl.PhoneWindow$DecorView\" enabled=\"true\" height=\"1920\" hidden=\"false\" knownSuperClass=\"android.widget.FrameLayout\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"0\">\n" +
            "        <node busy=\"false\" class=\"com.android.internal.widget.ActionBarOverlayLayout\" enabled=\"true\" height=\"1920\" hidden=\"false\" id=\"decor_content_parent\" knownSuperClass=\"android.view.ViewGroup\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"0\">\n" +
            "            <node busy=\"false\" class=\"android.widget.FrameLayout\" enabled=\"true\" height=\"1701\" hidden=\"false\" id=\"content\" knownSuperClass=\"android.widget.FrameLayout\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"219\">\n" +
            "                <node backColor=\"0xFFFFFF\" busy=\"false\" class=\"android.widget.ScrollView\" enabled=\"true\" height=\"1701\" hidden=\"false\" knownSuperClass=\"android.widget.ScrollView\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"219\">\n" +
            "                    <node busy=\"false\" class=\"android.widget.LinearLayout\" enabled=\"true\" height=\"6470\" hidden=\"false\" knownSuperClass=\"android.widget.LinearLayout\" onScreen=\"true\" top=\"false\" width=\"990\" x=\"45\" y=\"264\">\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"596\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\"\n" +
            "                            text=\"Die DHL Paket GmbH (nachfolgend „DHL“) freut sich über Ihre Nutzung unserer DHL Paket App und Ihr Interesse an unserem Unternehmen und unseren Produkten und Dienstleistungen. Der Schutz Ihrer personenbezogenen Daten bei der Verarbeitung während des gesamten Geschäftsprozesses ist für uns ein wichtiges Anliegen und wir möchten, dass Sie sich beim Gebrauch unserer App stets sicher fühlen.&#xa;&#xa;Im Folgenden erläutern wir, welche Informationen wir bei DHL während Ihrer Benutzung unserer DHL Paket App erfassen und wie diese genutzt werden.\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"264\">Die DHL Paket GmbH (nachfolgend „DHL“) freut sich über Ihre Nutzung unserer DHL Paket App und Ihr Interesse an unserem Unternehmen und unseren Produkten und Dienstleistungen. Der Schutz Ihrer personenbezogenen Daten bei der Verarbeitung während des gesamten Geschäftsprozesses ist für uns ein wichtiges Anliegen und wir möchten, dass Sie sich beim Gebrauch unserer App stets sicher fühlen.\n" +
            "\n" +
            "Im Folgenden erläutern wir, welche Informationen wir bei DHL während Ihrer Benutzung unserer DHL Paket App erfassen und wie diese genutzt werden.</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\" text=\"Personenbezogene Daten\" textColor=\"0x323232\" top=\"true\" width=\"594\" x=\"45\" y=\"944\">Personenbezogene Daten</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"498\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\"\n" +
            "                            text=\"Personenbezogene Daten sind Einzelangaben über persönliche oder sachliche Verhältnisse einer bestimmten oder bestimmbaren natürlichen Person. Darunter fallen Informationen wie z.B. Ihr richtiger Name, Ihre Anschrift, Ihre Telefonnummer und Ihr Geburtsdatum. Informationen, die nicht direkt mit Ihrer wirklichen Identität in Verbindung gebracht werden - wie z.B. favorisierte Webseiten oder Anzahl der Nutzer einer Seite - sind keine personenbezogenen Daten.\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"1027\">Personenbezogene Daten sind Einzelangaben über persönliche oder sachliche Verhältnisse einer bestimmten oder bestimmbaren natürlichen Person. Darunter fallen Informationen wie z.B. Ihr richtiger Name, Ihre Anschrift, Ihre Telefonnummer und Ihr Geburtsdatum. Informationen, die nicht direkt mit Ihrer wirklichen Identität in Verbindung gebracht werden - wie z.B. favorisierte Webseiten oder Anzahl der Nutzer einer Seite - sind keine personenbezogenen Daten.</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"127\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\" text=\"Erhebung und Verarbeitung von personenbezogenen Daten\" textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"1570\">Erhebung und Verarbeitung von personenbezogenen Daten</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"1184\" hidden=\"false\" id=\"privacy_headline2_text\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\"\n" +
            "                            text=\"Wenn Sie unsere App besuchen, speichern die eingesetzten Server standardmäßig zum Zweck der Systemsicherheit und für statistische Auswertungen das Datum, den Typ des benutzten Endgerätes, Typ und Version des genutzten Betriebssystems und den Typ und die Version des genutzten Browsers. Wenn Sie sich in unserer App einloggen speichert die DHL Paket App verschlüsselt ihre Login-Daten (Nutzername und Passwort), damit Sie sich dauerhaft anmelden können. Diese personenbezogenen Daten verwenden wir ausschließlich zum Zweck der technischen Administration. Darüber hinausgehende personenbezogene Angaben wie Ihr Name, Ihre Anschrift, Telefonnummer oder E-Mail-Adresse werden nicht erfasst. Somit erhält DHL nur die Information, dass z.B. ein unbekanntes iPhone 6 mit installiertem iOS 8 in der DHL Paket App auf die Funktion „Portorechner“ geklickt hat. Der Umgang mit Ihren Daten bei der Registrierung des Paket?de Accounts und während der Nutzung des selbigen wird in den Datenschutzhinweisen für die Nutzung von Paket?de unter folgendem Link dargelegt: Datenschutzhinweis auf www.paket.de\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"1712\">Wenn Sie unsere App besuchen, speichern die eingesetzten Server standardmäßig zum Zweck der Systemsicherheit und für statistische Auswertungen das Datum, den Typ des benutzten Endgerätes, Typ und Version des genutzten Betriebssystems und den Typ und die Version des genutzten Browsers. Wenn Sie sich in unserer App einloggen speichert die DHL Paket App verschlüsselt ihre Login-Daten (Nutzername und Passwort), damit Sie sich dauerhaft anmelden können. Diese personenbezogenen Daten verwenden wir ausschließlich zum Zweck der technischen Administration. Darüber hinausgehende personenbezogene Angaben wie Ihr Name, Ihre Anschrift, Telefonnummer oder E-Mail-Adresse werden nicht erfasst. Somit erhält DHL nur die Information, dass z.B. ein unbekanntes iPhone 6 mit installiertem iOS 8 in der DHL Paket App auf die Funktion „Portorechner“ geklickt hat. Der Umgang mit Ihren Daten bei der Registrierung des Paket?de Accounts und während der Nutzung des selbigen wird in den Datenschutzhinweisen für die Nutzung von Paket?de unter folgendem Link dargelegt: Datenschutzhinweis auf www.paket.de</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Datenschutzhinweis zu Crashlytics\" textColor=\"0x323232\" top=\"true\" width=\"803\" x=\"45\" y=\"2941\">Datenschutzhinweis zu Crashlytics</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"498\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\"\n" +
            "                            text=\"Innerhalb der DHL Paket App wird das Tool Crashlytics verwendet. Nach einem Programmabsturz werden Reports zum Absturz mit genauen Angaben zu Codestellen und Geräteinformationen erstellt und versendet, die die Wartung vereinfachen und die daraus resultierende Stabilität der DHL Paket App verbessern soll. Es werden keine personenbezogenen Daten übermittelt. Die Datenschutzerklärung von Crashlytics finden Sie unter: http://try.crashlytics.com/terms\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"3024\">Innerhalb der DHL Paket App wird das Tool Crashlytics verwendet. Nach einem Programmabsturz werden Reports zum Absturz mit genauen Angaben zu Codestellen und Geräteinformationen erstellt und versendet, die die Wartung vereinfachen und die daraus resultierende Stabilität der DHL Paket App verbessern soll. Es werden keine personenbezogenen Daten übermittelt. Die Datenschutzerklärung von Crashlytics finden Sie unter: http://try.crashlytics.com/terms</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Datenschutzhinweis zu Flurry\" textColor=\"0x323232\" top=\"true\" width=\"681\" x=\"45\" y=\"3567\">Datenschutzhinweis zu Flurry</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"596\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\"\n" +
            "                            text=\"Um unsere App ständig zu verbessern sowie Fehler frühzeitig zu erkennen verwenden wir Flurry Analytics. Darüber hinaus hilft uns das Tool, User-Verhalten in der App nachzuvollziehen indem es Nutzungsberichte anonymisiert anfertigt. Wir übermitteln keine personenbezogenen Daten an Flurry, zudem sind alle Daten anonymisiert. Die Möglichkeit sich von den Services von Flurry abzumelden (Opt-out) finden Sie unter:  https://dev.flurry.com/secure/optOut.do,  die Datenschutzerklärung von Flurry finden Sie unter: http://www.flurry.com/resources/privacy.html\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"3650\">Um unsere App ständig zu verbessern sowie Fehler frühzeitig zu erkennen verwenden wir Flurry Analytics. Darüber hinaus hilft uns das Tool, User-Verhalten in der App nachzuvollziehen indem es Nutzungsberichte anonymisiert anfertigt. Wir übermitteln keine personenbezogenen Daten an Flurry, zudem sind alle Daten anonymisiert. Die Möglichkeit sich von den Services von Flurry abzumelden (Opt-out) finden Sie unter:  https://dev.flurry.com/secure/optOut.do,  die Datenschutzerklärung von Flurry finden Sie unter: http://www.flurry.com/resources/privacy.html</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Einsatz von Cookies\" textColor=\"0x323232\" top=\"true\" width=\"464\" x=\"45\" y=\"4291\">Einsatz von Cookies</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"743\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\"\n" +
            "                            text=\"Um die Wirkung unserer Kampagnen zu beurteilen und festzustellen, ob Kunden uns infolge einer Kampagne kontaktiert haben wird ein Cookie angelegt, wenn Sie von einem DHL-Werbebanner auf eine Webseite von DHL gelangen. Der Cookie speichert keinerlei personenbezogene Kundendaten, lediglich die ID der Kampagne. Der Cookie erlischt bereits nach 24 Stunden. Sie können Ihren Browser auf ihrem mobilen Endgerät so einstellen, dass Sie über das Setzen von Cookies informiert werden und einzeln über deren Annahme entscheiden oder die Annahme von Cookies für bestimmte Fälle oder generell ausschließen. Bei der Nichtannahme von Cookies kann die Funktionalität unseres mobilen Angebots eingeschränkt sein.\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"4374\">Um die Wirkung unserer Kampagnen zu beurteilen und festzustellen, ob Kunden uns infolge einer Kampagne kontaktiert haben wird ein Cookie angelegt, wenn Sie von einem DHL-Werbebanner auf eine Webseite von DHL gelangen. Der Cookie speichert keinerlei personenbezogene Kundendaten, lediglich die ID der Kampagne. Der Cookie erlischt bereits nach 24 Stunden. Sie können Ihren Browser auf ihrem mobilen Endgerät so einstellen, dass Sie über das Setzen von Cookies informiert werden und einzeln über deren Annahme entscheiden oder die Annahme von Cookies für bestimmte Fälle oder generell ausschließen. Bei der Nichtannahme von Cookies kann die Funktionalität unseres mobilen Angebots eingeschränkt sein.</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Konzerndatenschutzrichtlinie\" textColor=\"0x323232\" top=\"true\" width=\"680\" x=\"45\" y=\"5162\">Konzerndatenschutzrichtlinie</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"498\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\"\n" +
            "                            text=\"Die Konzerndatenschutzrichtlinie (DPDHL Data Privacy Policy) regelt die konzernweit gültigen Standards der Datenverarbeitung mit einem besonderen Fokus auf so genannte Drittlandtransfers, d. h. Übermittlungen persönlicher Daten in Länder außerhalb der EU, die kein anerkanntes angemessenes Datenschutzniveau haben. Wenn Sie mehr über die Konzerndatenschutzrichtlinie erfahren möchten, nutzen Sie bitte diesen Link:\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"5245\">Die Konzerndatenschutzrichtlinie (DPDHL Data Privacy Policy) regelt die konzernweit gültigen Standards der Datenverarbeitung mit einem besonderen Fokus auf so genannte Drittlandtransfers, d. h. Übermittlungen persönlicher Daten in Länder außerhalb der EU, die kein anerkanntes angemessenes Datenschutzniveau haben. Wenn Sie mehr über die Konzerndatenschutzrichtlinie erfahren möchten, nutzen Sie bitte diesen Link:</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"106\" hidden=\"false\" id=\"privacy_url\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Download Konzerndatenschutzrichtlinie (Zusammenfassung) (PDF, 362 KB)\" textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"5743\">Download Konzerndatenschutzrichtlinie (Zusammenfassung) (PDF, 362 KB)</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"68\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\" text=\"Kontakt\" textColor=\"0x323232\" top=\"true\" width=\"177\" x=\"45\" y=\"5894\">Kontakt</node>\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"547\" hidden=\"false\" knownSuperClass=\"android.widget.TextView\" onScreen=\"false\"\n" +
            "                            text=\"Wenn Sie Fragen hinsichtlich der Verarbeitung Ihrer persönlichen Daten haben, können Sie sich an die Datenschutzbeauftragte der DHL wenden, die mit ihrem Team auch im Falle von Auskunftsersuchen, Anregungen oder Beschwerden zur Verfügung steht.&#xa;&#xa;Datenschutzbeauftragte der DHL&#xa;&#xa;Gabriela Krader, LL.M&#xa;Deutsche Post AG&#xa;53250 Bonn\"\n" +
            "                            textColor=\"0x323232\" top=\"true\" width=\"990\" x=\"45\" y=\"5977\">Wenn Sie Fragen hinsichtlich der Verarbeitung Ihrer persönlichen Daten haben, können Sie sich an die Datenschutzbeauftragte der DHL wenden, die mit ihrem Team auch im Falle von Auskunftsersuchen, Anregungen oder Beschwerden zur Verfügung steht.\n" +
            "\n" +
            "Datenschutzbeauftragte der DHL\n" +
            "\n" +
            "Gabriela Krader, LL.M\n" +
            "Deutsche Post AG\n" +
            "53250 Bonn</node>\n" +
            "                    </node>\n" +
            "                </node>\n" +
            "            </node>\n" +
            "            <node busy=\"false\" class=\"com.android.internal.widget.ActionBarContainer\" enabled=\"true\" height=\"144\" hidden=\"false\" id=\"action_bar_container\" knownSuperClass=\"android.widget.FrameLayout\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"75\">\n" +
            "                <node busy=\"false\" class=\"com.android.internal.widget.ActionBarView\" enabled=\"true\" height=\"144\" hidden=\"false\" id=\"action_bar\" knownSuperClass=\"android.view.ViewGroup\" onScreen=\"true\" top=\"false\" width=\"1080\" x=\"0\" y=\"75\">\n" +
            "                    <node backColor=\"0x000000\" busy=\"false\" class=\"android.widget.LinearLayout\" contentDescription=\"DHL Paket MOPS GIT. Nach Hause navigieren\" enabled=\"true\" height=\"144\" hidden=\"false\" knownSuperClass=\"android.widget.LinearLayout\" onScreen=\"true\" top=\"false\" width=\"72\" x=\"25\" y=\"75\">\n" +
            "                        <node busy=\"false\" class=\"com.android.internal.widget.ActionBarView$HomeView\" enabled=\"true\" height=\"144\" hidden=\"false\" knownSuperClass=\"android.widget.FrameLayout\" onScreen=\"true\" top=\"false\" width=\"72\" x=\"25\" y=\"75\">\n" +
            "                            <node busy=\"true\" class=\"android.widget.ImageView\" enabled=\"true\" height=\"0\" hidden=\"true\" id=\"up\" knownSuperClass=\"android.widget.ImageView\" onScreen=\"false\" top=\"false\" width=\"0\" x=\"25\" y=\"75\"/>\n" +
            "                            <node busy=\"false\" class=\"android.widget.ImageView\" enabled=\"true\" height=\"82\" hidden=\"false\" id=\"home\" knownSuperClass=\"android.widget.ImageView\" onScreen=\"true\" top=\"true\" width=\"48\" x=\"37\" y=\"106\"/>\n" +
            "                        </node>\n" +
            "                    </node>\n" +
            "                    <node busy=\"false\" class=\"android.widget.LinearLayout\" enabled=\"true\" height=\"73\" hidden=\"false\" id=\"linearLayoutABSTitle\" knownSuperClass=\"android.widget.LinearLayout\" onScreen=\"true\" top=\"false\" width=\"350\" x=\"365\" y=\"110\">\n" +
            "                        <node busy=\"false\" class=\"android.widget.TextView\" enabled=\"true\" height=\"73\" hidden=\"false\" id=\"textViewTitle\" knownSuperClass=\"android.widget.TextView\" onScreen=\"true\" text=\"Datenschutz\" textColor=\"0x000000\" top=\"true\" width=\"350\" x=\"365\" y=\"110\">Datenschutz</node>\n" +
            "                    </node>\n" +
            "                    <node busy=\"true\" class=\"android.widget.ActionMenuView\" enabled=\"true\" height=\"144\" hidden=\"true\" knownSuperClass=\"android.widget.LinearLayout\" onScreen=\"false\" top=\"false\" width=\"0\" x=\"1080\" y=\"75\"/>\n" +
            "                </node>\n" +
            "                <node busy=\"true\" class=\"com.android.internal.widget.ActionBarContextView\" enabled=\"true\" height=\"0\" hidden=\"true\" id=\"action_context_bar\" knownSuperClass=\"android.view.ViewGroup\" onScreen=\"false\" top=\"false\" width=\"0\" x=\"0\" y=\"75\"/>\n" +
            "            </node>\n" +
            "            <node busy=\"true\" class=\"com.android.internal.widget.ActionBarContainer\" enabled=\"true\" height=\"0\" hidden=\"true\" id=\"split_action_bar\" knownSuperClass=\"android.widget.FrameLayout\" onScreen=\"false\" top=\"false\" width=\"0\" x=\"0\" y=\"0\"/>\n" +
            "        </node>\n" +
            "    </node>\n" +
            "</node>";

    @Test
    public void testAAA() {
        System.setProperty(MobileProperties.MOBILE_DEVICE_RESERVATION_POLICY, DeviceReservationPolicy.UNRESERVED_OR_OWN_DEVICES.toString());
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        TestDevice testDevice = TestDevice.builder().name("Samsung Galaxy S6 (Nr 2)").operatingSystem(MobileOperatingSystem.ANDROID).build();
        mobileDriver.reserveDevice(testDevice);
        mobileDriver.switchToDevice(testDevice);

        WebMobileGuiElement webMobileGuiElement = new WebMobileGuiElement("xpath=//*[@text='Anmelden' and @nodeName='BUTTON']");
        webMobileGuiElement.centerVertically(0.5f);
    }

    @Test
    public void testT00() throws Exception {
        ScreenDump screenDump = new ScreenDump("<?xml version=\"1.0\" encoding=\"UTF-8\"?><node onScreen=\"false\" top=\"false\" visible=\"false\">\n" +
                "   <UIWindow alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UILayoutContainerView alpha=\"1\" class=\"UILayoutContainerView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "         <UITransitionView alpha=\"1\" class=\"UITransitionView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "            <UIViewControllerWrapperView alpha=\"1\" class=\"UIViewControllerWrapperView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "               <UILayoutContainerView alpha=\"1\" class=\"UILayoutContainerView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "                  <UINavigationTransitionView alpha=\"1\" class=\"UINavigationTransitionView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "                     <UIViewControllerWrapperView alpha=\"1\" class=\"UIViewControllerWrapperView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "                        <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "                           <UITableView alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableView\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UITableView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "                              <UITableViewWrapperView alpha=\"1\" class=\"UITableViewWrapperView\" enabled=\"true\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIScrollView\" onScreen=\"false\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-4330\">\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell\" enabled=\"false\" height=\"804\" hidden=\"false\" knownSuperClass=\"UITableViewCell\" onScreen=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"750\" x=\"0\" y=\"432\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"804\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"750\" x=\"0\" y=\"432\">\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView\" enabled=\"false\" height=\"804\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"432\">\n" +
                "                                          <UIView accessibilityIdentifier=\"ProductSlider\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"804\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"432\">\n" +
                "                                             <UICollectionView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UICollectionView\" enabled=\"false\" height=\"670\" hidden=\"false\" knownSuperClass=\"UIScrollView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"566\">\n" +
                "                                                <FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"360\" y=\"566\">\n" +
                "                                                   <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"360\" y=\"566\">\n" +
                "                                                      <UIImageView accessibilityIdentifier=\"ProductImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"428\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"300\" x=\"360\" y=\"566\"/>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductName\" accessibilityLabel=\"Pullover\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"Pullover\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"360\" y=\"1014\">Pullover</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.Copy2Label accessibilityIdentifier=\"ProductBrand\" accessibilityLabel=\"s.Oliver\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.Copy2Label\" enabled=\"false\" height=\"42\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"s.Oliver\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"360\" y=\"1054\">s.Oliver</FashionApp_3_1_0_1_UNIVERSAL.Copy2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductPrice\" accessibilityLabel=\"79,99 €\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"79,99 €\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"360\" y=\"1128\">79,99 €</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                   </UIView>\n" +
                "                                                </FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell>\n" +
                "                                                <FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"680\" y=\"566\">\n" +
                "                                                   <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"680\" y=\"566\">\n" +
                "                                                      <UIImageView accessibilityIdentifier=\"ProductImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"428\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"300\" x=\"680\" y=\"566\"/>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductName\" accessibilityLabel=\"Langarmshirt\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"Langarmshirt\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"680\" y=\"1014\">Langarmshirt</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.Copy2Label accessibilityIdentifier=\"ProductBrand\" accessibilityLabel=\"s.Oliver\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.Copy2Label\" enabled=\"false\" height=\"42\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"s.Oliver\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"680\" y=\"1054\">s.Oliver</FashionApp_3_1_0_1_UNIVERSAL.Copy2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductPrice\" accessibilityLabel=\"19,99 €\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"19,99 €\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"680\" y=\"1128\">19,99 €</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                   </UIView>\n" +
                "                                                </FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell>\n" +
                "                                                <FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"40\" y=\"566\">\n" +
                "                                                   <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"670\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"300\" x=\"40\" y=\"566\">\n" +
                "                                                      <UIImageView accessibilityIdentifier=\"ProductImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"428\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"300\" x=\"40\" y=\"566\"/>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductName\" accessibilityLabel=\"T-Shirt\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"T-Shirt\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"40\" y=\"1014\">T-Shirt</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.Copy2Label accessibilityIdentifier=\"ProductBrand\" accessibilityLabel=\"s.Oliver\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.Copy2Label\" enabled=\"false\" height=\"42\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"s.Oliver\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"40\" y=\"1054\">s.Oliver</FashionApp_3_1_0_1_UNIVERSAL.Copy2Label>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductPrice\" accessibilityLabel=\"12,99 €\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"12,99 €\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"300\" x=\"40\" y=\"1128\">12,99 €</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                   </UIView>\n" +
                "                                                </FashionApp_3_1_0_1_UNIVERSAL.ProductSliderCell>\n" +
                "                                             </UICollectionView>\n" +
                "                                             <FashionApp_3_1_0_1_UNIVERSAL.SL3Label accessibilityIdentifier=\"Headline\" accessibilityLabel=\"DIE STYLES DER NEUEN SAISON\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL3Label\" enabled=\"false\" height=\"34\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"DIE STYLES DER NEUEN SAISON\" textColor=\"0x000000\" top=\"true\" visible=\"true\" width=\"670\" x=\"40\" y=\"502\">DIE STYLES DER NEUEN SAISON</FashionApp_3_1_0_1_UNIVERSAL.SL3Label>\n" +
                "                                             <UIActivityIndicatorView accessibilityLabel=\"Angehalten\" alpha=\"1\" class=\"UIActivityIndicatorView\" enabled=\"true\" height=\"74\" hidden=\"true\" knownSuperClass=\"UIActivityIndicatorView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"74\" x=\"338\" y=\"796\">\n" +
                "                                                <UIImageView alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"74\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"74\" x=\"338\" y=\"796\"/>\n" +
                "                                             </UIActivityIndicatorView>\n" +
                "                                          </UIView>\n" +
                "                                       </FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" tag=\"0\" top=\"false\" visible=\"false\" width=\"610\" x=\"30\" y=\"974\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell accessibilityIdentifier=\"TeaserTile\" alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell\" enabled=\"false\" height=\"750\" hidden=\"false\" knownSuperClass=\"UITableViewCell\" onScreen=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"-318\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"-318\">\n" +
                "                                       <UIImageView accessibilityIdentifier=\"TeaserImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"750\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"750\" x=\"0\" y=\"-318\"/>\n" +
                "                                       <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"186\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"718\" x=\"16\" y=\"150\">\n" +
                "                                          <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"Subline\" accessibilityLabel=\"Strickjacken, Pullover &amp; mehr\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" tag=\"0\" text=\"Strickjacken, Pullover &amp; mehr\" textColor=\"0xFFFFFF\" top=\"true\" visible=\"true\" width=\"428\" x=\"160\" y=\"150\">Strickjacken, Pullover &amp; mehr</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                          <FashionApp_3_1_0_1_UNIVERSAL.HL1TextView accessibilityIdentifier=\"Headline\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.HL1TextView\" enabled=\"false\" height=\"128\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"true\" tag=\"0\" text=\"HERBSTSTRICK\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"true\" width=\"638\" x=\"56\" y=\"208\">HERBSTSTRICK<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"128\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"638\" x=\"56\" y=\"208\"/>\n" +
                "                                          </FashionApp_3_1_0_1_UNIVERSAL.HL1TextView>\n" +
                "                                       </UIView>\n" +
                "                                       <UITableViewLabel alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableViewLabel\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"0\" y=\"-318\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" tag=\"0\" top=\"false\" visible=\"false\" width=\"604\" x=\"30\" y=\"354\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileInfoCell accessibilityIdentifier=\"InfoTile\" alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileInfoCell\" enabled=\"false\" height=\"364\" hidden=\"true\" knownSuperClass=\"UITableViewCell\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-682\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"364\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-682\">\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"eardhae\" accessibilityLabel=\"Kostenlose Lieferung in die Filiale\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Kostenlose Lieferung in die Filiale\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"638\" x=\"56\" y=\"-532\">Kostenlose Lieferung in die Filiale</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.Copy1TextView accessibilityIdentifier=\"rt6djt\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.Copy1TextView\" enabled=\"false\" height=\"116\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Lassen Sie sich Ihre Bestellung in einen&#10;von über 200 Wunschstores liefern.\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"670\" x=\"40\" y=\"-474\">Lassen Sie sich Ihre Bestellung in einen\n" +
                "von über 200 Wunschstores liefern.<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"116\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"670\" x=\"40\" y=\"-474\"/>\n" +
                "                                       </FashionApp_3_1_0_1_UNIVERSAL.Copy1TextView>\n" +
                "                                       <UIImageView accessibilityIdentifier=\"zsjwtrjz\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"90\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"670\" x=\"40\" y=\"-642\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"610\" x=\"30\" y=\"-356\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileInfoCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell accessibilityIdentifier=\"TeaserTile\" alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell\" enabled=\"false\" height=\"750\" hidden=\"true\" knownSuperClass=\"UITableViewCell\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-1432\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-1432\">\n" +
                "                                       <UIImageView accessibilityIdentifier=\"TeaserImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"750\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-1432\"/>\n" +
                "                                       <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"186\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"718\" x=\"16\" y=\"-964\">\n" +
                "                                          <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"Subline\" accessibilityLabel=\"Jetzt entdecken!\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Jetzt entdecken!\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"236\" x=\"256\" y=\"-964\">Jetzt entdecken!</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                          <FashionApp_3_1_0_1_UNIVERSAL.HL1TextView accessibilityIdentifier=\"Headline\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.HL1TextView\" enabled=\"false\" height=\"128\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"NEW BUSINESS RULES!\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"638\" x=\"56\" y=\"-906\">NEW BUSINESS RULES!<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"128\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"638\" x=\"56\" y=\"-906\"/>\n" +
                "                                          </FashionApp_3_1_0_1_UNIVERSAL.HL1TextView>\n" +
                "                                       </UIView>\n" +
                "                                       <UITableViewLabel alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableViewLabel\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"0\" y=\"-1432\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"604\" x=\"30\" y=\"-760\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileTeaserCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileSwipeTeaserCell accessibilityIdentifier=\"SwipeTeaserTile\" alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileSwipeTeaserCell\" enabled=\"false\" height=\"978\" hidden=\"true\" knownSuperClass=\"UITableViewCell\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UITableViewCellContentView\" enabled=\"false\" height=\"978\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                       <UIView accessibilityIdentifier=\"PagerView\" alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                          <_UIPageViewControllerContentView alpha=\"1\" class=\"_UIPageViewControllerContentView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                             <_UIQueuingScrollView alpha=\"1\" class=\"_UIQueuingScrollView\" enabled=\"true\" height=\"750\" hidden=\"false\" knownSuperClass=\"UIScrollView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                                <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"-750\" y=\"-2410\"/>\n" +
                "                                                <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                                   <UIView accessibilityIdentifier=\"SwipeTeaserPageView\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\">\n" +
                "                                                      <UIImageView accessibilityIdentifier=\"TeaserImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"750\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-2410\"/>\n" +
                "                                                      <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"186\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-1926\">\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"Subline\" accessibilityLabel=\"The perfect fit!\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"The perfect fit!\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"210\" x=\"270\" y=\"-1926\">The perfect fit!</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.HL1TextView accessibilityIdentifier=\"Headline\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.HL1TextView\" enabled=\"false\" height=\"128\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"NEUE DENIMSTYLES\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"460\" x=\"144\" y=\"-1868\">NEUE DENIMSTYLES<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"128\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"460\" x=\"144\" y=\"-1868\"/>\n" +
                "                                                         </FashionApp_3_1_0_1_UNIVERSAL.HL1TextView>\n" +
                "                                                      </UIView>\n" +
                "                                                      <FashionApp_3_1_0_1_UNIVERSAL.HeartToggle accessibilityIdentifier=\"HeartToggleButton\" alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.HeartToggle\" enabled=\"true\" height=\"100\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"100\" x=\"640\" y=\"-2380\">\n" +
                "                                                         <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"100\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"100\" x=\"640\" y=\"-2380\">\n" +
                "                                                            <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"72\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"72\" x=\"654\" y=\"-2366\"/>\n" +
                "                                                            <UIImageView accessibilityIdentifier=\"icon_heart_red_m\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"0\" x=\"690\" y=\"-2330\"/>\n" +
                "                                                            <UIButton accessibilityLabel=\"HeartToggleBtn\" alpha=\"1\" class=\"UIButton\" enabled=\"true\" height=\"100\" hidden=\"false\" knownSuperClass=\"UIButton\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"100\" x=\"640\" y=\"-2380\"/>\n" +
                "                                                         </UIView>\n" +
                "                                                      </FashionApp_3_1_0_1_UNIVERSAL.HeartToggle>\n" +
                "                                                      <UIButton accessibilityIdentifier=\"DescriptionButton\" accessibilityLabel=\"icon open layer m\" alpha=\"1\" class=\"UIButton\" enabled=\"true\" height=\"100\" hidden=\"false\" knownSuperClass=\"UIButton\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"100\" x=\"640\" y=\"-2280\">\n" +
                "                                                         <UIImageView accessibilityIdentifier=\"icon_open_layer_m\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"62\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"60\" x=\"660\" y=\"-2262\"/>\n" +
                "                                                      </UIButton>\n" +
                "                                                      <UIView accessibilityIdentifier=\"DescriptionView\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"204\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"0\" x=\"660\" y=\"-2260\">\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.Copy2TextView accessibilityIdentifier=\"BrandLabel\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"FashionApp_3_1_0_1_UNIVERSAL.Copy2TextView\" enabled=\"false\" height=\"68\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"s.Oliver\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"640\" y=\"-2202\">s.Oliver<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"68\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"0\" x=\"640\" y=\"-2202\"/>\n" +
                "                                                         </FashionApp_3_1_0_1_UNIVERSAL.Copy2TextView>\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductNameLabel\" accessibilityLabel=\"Tubx Straight: Denim mit Gürtel\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Tubx Straight: Denim mit Gürtel\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"680\" y=\"-2230\">Tubx Straight: Denim mit Gürtel</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityLabel=\"Label\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"true\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Label\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"650\" y=\"-2124\">Label</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                         <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"ProductPriceLabel\" accessibilityLabel=\"59,99 €\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"59,99 €\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"670\" y=\"-2124\">59,99 €</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                                      </UIView>\n" +
                "                                                   </UIView>\n" +
                "                                                </UIView>\n" +
                "                                                <UIView alpha=\"1\" class=\"UIView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"750\" y=\"-2410\"/>\n" +
                "                                             </_UIQueuingScrollView>\n" +
                "                                          </_UIPageViewControllerContentView>\n" +
                "                                       </UIView>\n" +
                "                                       <UIPageControl accessibilityIdentifier=\"PageControl\" alpha=\"1\" class=\"UIPageControl\" enabled=\"true\" height=\"84\" hidden=\"false\" knownSuperClass=\"UIPageControl\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"46\" x=\"352\" y=\"-1730\">\n" +
                "                                          <UIView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"14\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"14\" x=\"352\" y=\"-1700\"/>\n" +
                "                                          <UIView alpha=\"1\" backgroundColor=\"0xFEFFFE\" class=\"UIView\" enabled=\"false\" height=\"14\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"14\" x=\"384\" y=\"-1700\"/>\n" +
                "                                       </UIPageControl>\n" +
                "                                       <UIView accessibilityIdentifier=\"HeartVSXContainer\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"40\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-1512\">\n" +
                "                                          <UIImageView accessibilityIdentifier=\"VSImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"40\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"36\" x=\"356\" y=\"-1512\"/>\n" +
                "                                          <UIImageView accessibilityIdentifier=\"CrossImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"40\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"40\" x=\"302\" y=\"-1512\"/>\n" +
                "                                          <UIImageView accessibilityIdentifier=\"HeartImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"40\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"40\" x=\"406\" y=\"-1512\"/>\n" +
                "                                       </UIView>\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"CaptionLabel\" accessibilityLabel=\"Find your Denimstyle!\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"38\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Find your Denimstyle!\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"670\" x=\"40\" y=\"-1620\">Find your Denimstyle!</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                       <UITableViewLabel alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableViewLabel\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"0\" y=\"-2410\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"610\" x=\"30\" y=\"-1470\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileSwipeTeaserCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell\" enabled=\"false\" height=\"770\" hidden=\"true\" knownSuperClass=\"UITableViewCell\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-3580\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"770\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-3580\">\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView\" enabled=\"false\" height=\"770\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-3580\">\n" +
                "                                          <UIView accessibilityIdentifier=\"ProductSlider\" alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UIView\" enabled=\"false\" height=\"770\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-3580\">\n" +
                "                                             <UICollectionView alpha=\"1\" backgroundColor=\"0xFFFFFF\" class=\"UICollectionView\" enabled=\"false\" height=\"670\" hidden=\"false\" knownSuperClass=\"UIScrollView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-3480\"/>\n" +
                "                                             <FashionApp_3_1_0_1_UNIVERSAL.SL3Label accessibilityIdentifier=\"Headline\" accessibilityLabel=\" \" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL3Label\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\" \" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"670\" x=\"40\" y=\"-3510\"> </FashionApp_3_1_0_1_UNIVERSAL.SL3Label>\n" +
                "                                             <UIActivityIndicatorView accessibilityLabel=\"In Arbeit\" alpha=\"1\" class=\"UIActivityIndicatorView\" enabled=\"true\" height=\"74\" hidden=\"false\" knownSuperClass=\"UIActivityIndicatorView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"74\" x=\"338\" y=\"-3232\">\n" +
                "                                                <UIImageView alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"74\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"74\" x=\"338\" y=\"-3232\"/>\n" +
                "                                             </UIActivityIndicatorView>\n" +
                "                                          </UIView>\n" +
                "                                       </FashionApp_3_1_0_1_UNIVERSAL.ProductSliderView>\n" +
                "                                       <UITableViewLabel alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableViewLabel\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"0\" y=\"-3580\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"610\" x=\"30\" y=\"-3036\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileProductSliderCell>\n" +
                "                                 <FashionApp_3_1_0_1_UNIVERSAL.StartPageTileWelcomeTeaserCell accessibilityIdentifier=\"WelcomeTile\" alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"FashionApp_3_1_0_1_UNIVERSAL.StartPageTileWelcomeTeaserCell\" enabled=\"false\" height=\"750\" hidden=\"true\" knownSuperClass=\"UITableViewCell\" onScreen=\"false\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-4330\">\n" +
                "                                    <UITableViewCellContentView alpha=\"1\" class=\"UITableViewCellContentView\" enabled=\"true\" height=\"750\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-4330\">\n" +
                "                                       <UIImageView accessibilityIdentifier=\"TeaserImage\" alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"750\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"-4330\"/>\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.HL1TextView accessibilityIdentifier=\"Headline\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.HL1TextView\" enabled=\"false\" height=\"224\" hidden=\"false\" knownSuperClass=\"UITextView\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"HALLO&#10;HERR TEST\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"258\" x=\"246\" y=\"-4066\">HALLO\n" +
                "HERR TEST<_UITextContainerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"_UITextContainerView\" enabled=\"false\" height=\"224\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"258\" x=\"246\" y=\"-4066\"/>\n" +
                "                                       </FashionApp_3_1_0_1_UNIVERSAL.HL1TextView>\n" +
                "                                       <FashionApp_3_1_0_1_UNIVERSAL.SL2Label accessibilityIdentifier=\"Subline\" accessibilityLabel=\"Hemden mit neuen Details - mit Stehkragen oder klassisch!\" alpha=\"1\" backgroundColor=\"0x000000\" class=\"FashionApp_3_1_0_1_UNIVERSAL.SL2Label\" enabled=\"false\" height=\"70\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" text=\"Hemden mit neuen Details -&#10;mit Stehkragen oder klassisch!\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"false\" width=\"650\" x=\"50\" y=\"-3730\">Hemden mit neuen Details -\n" +
                "mit Stehkragen oder klassisch!</FashionApp_3_1_0_1_UNIVERSAL.SL2Label>\n" +
                "                                       <UITableViewLabel alpha=\"1\" backgroundColor=\"0xF6F6F6\" class=\"UITableViewLabel\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" textColor=\"0x000000\" top=\"false\" visible=\"false\" width=\"0\" x=\"0\" y=\"-4330\"/>\n" +
                "                                    </UITableViewCellContentView>\n" +
                "                                    <_UITableViewCellSeparatorView alpha=\"1\" class=\"_UITableViewCellSeparatorView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"false\" width=\"676\" x=\"30\" y=\"-3584\"/>\n" +
                "                                 </FashionApp_3_1_0_1_UNIVERSAL.StartPageTileWelcomeTeaserCell>\n" +
                "                              </UITableViewWrapperView>\n" +
                "                              <UIImageView alpha=\"0\" class=\"UIImageView\" enabled=\"false\" height=\"4\" hidden=\"true\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"738\" x=\"6\" y=\"1224\"/>\n" +
                "                              <UIImageView alpha=\"0\" class=\"UIImageView\" enabled=\"false\" height=\"278\" hidden=\"true\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"4\" x=\"738\" y=\"950\"/>\n" +
                "                           </UITableView>\n" +
                "                           <UIActivityIndicatorView accessibilityLabel=\"Angehalten\" alpha=\"1\" class=\"UIActivityIndicatorView\" enabled=\"true\" height=\"74\" hidden=\"true\" knownSuperClass=\"UIActivityIndicatorView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"74\" x=\"338\" y=\"630\">\n" +
                "                              <UIImageView alpha=\"1\" class=\"UIImageView\" enabled=\"false\" height=\"74\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" parentHidden=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"74\" x=\"338\" y=\"630\"/>\n" +
                "                           </UIActivityIndicatorView>\n" +
                "                        </UIView>\n" +
                "                     </UIViewControllerWrapperView>\n" +
                "                  </UINavigationTransitionView>\n" +
                "               </UILayoutContainerView>\n" +
                "            </UIViewControllerWrapperView>\n" +
                "         </UITransitionView>\n" +
                "         <UITabBar alpha=\"1\" class=\"UITabBar\" enabled=\"true\" height=\"98\" hidden=\"false\" knownSuperClass=\"UITabBar\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"1236\">\n" +
                "            <_UIBarBackground alpha=\"1\" class=\"_UIBarBackground\" enabled=\"false\" height=\"98\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"1236\">\n" +
                "               <UIImageView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIImageView\" enabled=\"false\" height=\"0\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"false\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"1234\"/>\n" +
                "               <UIVisualEffectView alpha=\"1\" class=\"UIVisualEffectView\" enabled=\"true\" height=\"98\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"1236\">\n" +
                "                  <_UIVisualEffectBackdropView alpha=\"1\" class=\"_UIVisualEffectBackdropView\" enabled=\"false\" height=\"98\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"1236\"/>\n" +
                "                  <_UIVisualEffectFilterView alpha=\"1\" backgroundColor=\"0xF7F7F7\" class=\"_UIVisualEffectFilterView\" enabled=\"false\" height=\"98\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"1236\"/>\n" +
                "               </UIVisualEffectView>\n" +
                "            </_UIBarBackground>\n" +
                "            <UITabBarButton alpha=\"1\" class=\"UITabBarButton\" enabled=\"true\" height=\"96\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"142\" x=\"4\" y=\"1238\">\n" +
                "               <UITabBarSwappableImageView accessibilityIdentifier=\"icon_nav_home_inactive_m\" alpha=\"1\" class=\"UITabBarSwappableImageView\" enabled=\"false\" height=\"48\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"60\" x=\"44\" y=\"1262\"/>\n" +
                "            </UITabBarButton>\n" +
                "            <UITabBarButton alpha=\"1\" class=\"UITabBarButton\" enabled=\"true\" height=\"96\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"142\" x=\"154\" y=\"1238\">\n" +
                "               <UITabBarSwappableImageView accessibilityIdentifier=\"icon_nav_search2_inactive_m\" alpha=\"1\" class=\"UITabBarSwappableImageView\" enabled=\"false\" height=\"50\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"56\" x=\"196\" y=\"1260\"/>\n" +
                "            </UITabBarButton>\n" +
                "            <UITabBarButton alpha=\"1\" class=\"UITabBarButton\" enabled=\"true\" height=\"96\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"142\" x=\"304\" y=\"1238\">\n" +
                "               <UITabBarSwappableImageView accessibilityIdentifier=\"icon_nav_wishlist_inactive_m\" alpha=\"1\" class=\"UITabBarSwappableImageView\" enabled=\"false\" height=\"52\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"50\" x=\"350\" y=\"1260\"/>\n" +
                "            </UITabBarButton>\n" +
                "            <UITabBarButton alpha=\"1\" class=\"UITabBarButton\" enabled=\"true\" height=\"96\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"142\" x=\"454\" y=\"1238\">\n" +
                "               <UITabBarSwappableImageView accessibilityIdentifier=\"icon_nav_onlineshop_inactive_m\" alpha=\"1\" class=\"UITabBarSwappableImageView\" enabled=\"false\" height=\"48\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"48\" x=\"500\" y=\"1262\"/>\n" +
                "            </UITabBarButton>\n" +
                "            <UITabBarButton alpha=\"1\" class=\"UITabBarButton\" enabled=\"true\" height=\"96\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"142\" x=\"604\" y=\"1238\">\n" +
                "               <UITabBarSwappableImageView accessibilityIdentifier=\"icon_nav_more_inactive_m\" alpha=\"1\" class=\"UITabBarSwappableImageView\" enabled=\"false\" height=\"52\" hidden=\"false\" knownSuperClass=\"UIImageView\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"68\" x=\"640\" y=\"1260\"/>\n" +
                "            </UITabBarButton>\n" +
                "            <FashionApp_3_1_0_1_UNIVERSAL.BadgeView alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.BadgeView\" enabled=\"false\" height=\"40\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"60\" x=\"374\" y=\"1284\">\n" +
                "               <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"36\" hidden=\"true\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"60\" x=\"374\" y=\"1284\">\n" +
                "                  <UILabel accessibilityLabel=\"28\" alpha=\"1\" backgroundColor=\"0xB7002A\" class=\"UILabel\" enabled=\"false\" height=\"36\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" parentHidden=\"true\" tag=\"0\" text=\"28\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"true\" width=\"36\" x=\"374\" y=\"1290\">28</UILabel>\n" +
                "               </UIView>\n" +
                "            </FashionApp_3_1_0_1_UNIVERSAL.BadgeView>\n" +
                "            <FashionApp_3_1_0_1_UNIVERSAL.BadgeView alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.BadgeView\" enabled=\"false\" height=\"40\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"60\" x=\"530\" y=\"1284\">\n" +
                "               <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"36\" hidden=\"true\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"60\" x=\"530\" y=\"1284\">\n" +
                "                  <UILabel accessibilityLabel=\"28\" alpha=\"1\" backgroundColor=\"0xB7002A\" class=\"UILabel\" enabled=\"false\" height=\"36\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" parentHidden=\"true\" tag=\"0\" text=\"28\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"true\" width=\"36\" x=\"530\" y=\"1290\">28</UILabel>\n" +
                "               </UIView>\n" +
                "            </FashionApp_3_1_0_1_UNIVERSAL.BadgeView>\n" +
                "            <FashionApp_3_1_0_1_UNIVERSAL.BadgeView alpha=\"1\" class=\"FashionApp_3_1_0_1_UNIVERSAL.BadgeView\" enabled=\"false\" height=\"40\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"true\" visible=\"true\" width=\"60\" x=\"688\" y=\"1284\">\n" +
                "               <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"36\" hidden=\"true\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"60\" x=\"688\" y=\"1284\">\n" +
                "                  <UILabel accessibilityLabel=\"28\" alpha=\"1\" backgroundColor=\"0xB7002A\" class=\"UILabel\" enabled=\"false\" height=\"36\" hidden=\"false\" knownSuperClass=\"UILabel\" onScreen=\"true\" parentHidden=\"true\" tag=\"0\" text=\"28\" textColor=\"0xFFFFFF\" top=\"false\" visible=\"true\" width=\"36\" x=\"688\" y=\"1290\">28</UILabel>\n" +
                "               </UIView>\n" +
                "            </FashionApp_3_1_0_1_UNIVERSAL.BadgeView>\n" +
                "         </UITabBar>\n" +
                "      </UILayoutContainerView>\n" +
                "   </UIWindow>\n" +
                "   <UIWindow alpha=\"1\" class=\"UIWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\"/>\n" +
                "   </UIWindow>\n" +
                "   <UITextEffectsWindow alpha=\"1\" class=\"UITextEffectsWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIInputSetContainerView alpha=\"1\" class=\"UIInputSetContainerView\" enabled=\"true\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "         <UIInputSetHostView alpha=\"1\" class=\"UIInputSetHostView\" enabled=\"true\" height=\"0\" hidden=\"false\" onScreen=\"false\" tag=\"0\" top=\"false\" visible=\"false\" width=\"750\" x=\"0\" y=\"1334\"/>\n" +
                "      </UIInputSetContainerView>\n" +
                "   </UITextEffectsWindow>\n" +
                "   <UIWindow alpha=\"1\" class=\"UIWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\"/>\n" +
                "   </UIWindow>\n" +
                "   <UIWindow alpha=\"1\" class=\"UIWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\"/>\n" +
                "   </UIWindow>\n" +
                "   <UIWindow alpha=\"1\" class=\"UIWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIView\" enabled=\"false\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\"/>\n" +
                "   </UIWindow>\n" +
                "   <UITextEffectsWindow alpha=\"1\" class=\"UITextEffectsWindow\" enabled=\"false\" height=\"1334\" hidden=\"false\" knownSuperClass=\"UIWindow\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\">\n" +
                "      <UIApplicationRotationFollowingControllerView alpha=\"1\" backgroundColor=\"0x000000\" class=\"UIApplicationRotationFollowingControllerView\" enabled=\"false\" height=\"1334\" hidden=\"false\" onScreen=\"true\" tag=\"0\" top=\"false\" visible=\"true\" width=\"750\" x=\"0\" y=\"0\"/>\n" +
                "   </UITextEffectsWindow>\n" +
                "</node>\n" +
                "\n");

        String n = screenDump.getContent(new MobileLocator("n", "//*[@knownSuperClass='UITableViewCell']", 1));
        System.out.println(n);

    }

    @Test
    public void testT88() throws Exception {
        TestDevice build = TestDevice.builder("Apple iPhone 7 (Nr 3)", MobileOperatingSystem.IOS).build();
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDevice(build);

        mobileDriver.seeTestClient().addMobileListener("NATIVE", "xpath=//*[@accessibilityLabel='Einstellungen']", new MobileListener() {
            @Override
            public boolean recover(String s, String s1) {
                System.out.println("Mobile Listener triggered: " + s + "  -  " + s1);
                return true;
            }
        });

        mobileDriver.swipe(Direction.RIGHT, 0.2f, 500);
        int milliSeconds = 5000;
        TestUtils.sleep(milliSeconds);
        mobileDriver.swipe(Direction.LEFT, 0.2f, 500);
        TestUtils.sleep(milliSeconds);
        mobileDriver.swipe(Direction.RIGHT, 0.2f, 500);
        TestUtils.sleep(milliSeconds);
        mobileDriver.swipe(Direction.LEFT, 0.2f, 500);
        TestUtils.sleep(milliSeconds);
    }

    /**
     * @throws Exception
     */
    @Test
    public void T01_Screenshot_Quality() throws Exception {
        System.setProperty(MobileProperties.MOBILE_SERVER_HOST, "https://mobiledevicecloud.t-systems-mms.eu");
        System.setProperty(MobileProperties.MOBILE_GRID_USER, "xeta_systemtest"); //
        System.setProperty(MobileProperties.MOBILE_GRID_PASSWORD, "Mas4test#"); // pAL7bKB
        System.setProperty(MobileProperties.MOBILE_GRID_PROJECT, "Testing");
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "os.type=android");
        //        System.setProperty(MobileProperties.MOBILE_REPORT_SAVE_VIDEO, "true");

        System.setProperty(TesterraProperties.PROXY_SETTINGS_LOAD, "false");

        final MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.reserveDeviceByFilter();

        List<Screenshot> screenshots = new MobileScreenshotGrabber().takeScreenshots();
    }

    @Test
    public void test1234() throws Exception {
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        String adb = "Apple iPhone 7 (Nr 2)";
        TestDevice build = TestDevice.builder().operatingSystem(MobileOperatingSystem.IOS).name(adb).build();

        mobileDriver.reserveDevice(build);
        mobileDriver.resetWlanOfActiveDevice();

    }

    @Test
    public void testCachedMobileGuiElement() throws Exception {
        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project6");
        TestDevice pf28_xperia_z = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Xperia_Z3").build();
        MobileDriverManager.deviceStore().addDevice(pf28_xperia_z);
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.switchToDevice(pf28_xperia_z);
        MobilePage mobilePage = new MobilePage();
    }


    @Test
    public void testName() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document parse = documentBuilder.parse(new InputSource(new StringReader(visualDum)));

        XPath xPath = XPathFactory.newInstance().newXPath();
        Object evaluate = xPath.compile("//*[@id='up']").evaluate(parse, XPathConstants.NODE);
        System.out.println(evaluate);
    }

    @Test
    public void testVisualDump() throws Exception {
        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project6");
        TestDevice pf28_xperia_z = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Xperia_Z3").build();
        MobileDriverManager.deviceStore().addDevice(pf28_xperia_z);
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.switchToDevice(pf28_xperia_z);
        NativeMobileGuiElement nativeMobileGuiElement = new NativeMobileGuiElement("xpath=//*[@id='loginButton']");
        String visualDump = mobileDriver.seeTestClient().getVisualDump(ScreenDumpType.NATIVE_INSTRUMENTED.toString());
    }

    @Test
    public void testDeviceInfo() throws Exception {
        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project9");
        System.setProperty(MobileProperties.MOBILE_OPEN_REFLECTION_SCREEN, "true");

        TestDevice pf28_xperia_z = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Xperia_Z3").build();
        MobileDriverManager.deviceStore().addDevice(pf28_xperia_z);
        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();
        mobileDriver.switchToDevice(pf28_xperia_z);

        ImageMobileGuiElement imageMobileGuiElement = new ImageMobileGuiElement("Login", "default");
        imageMobileGuiElement.isDisplayed();
    }

    @Test
    public void testApi() {
        Applications applications = Cloud.applications().getApplications();
    }

    @Test
    public void testName1() throws Exception {
        DeviceStore deviceStore = new DeviceStore();
        deviceStore.addDevice(TestDevice.builder().name("iPhone").operatingSystem(MobileOperatingSystem.IOS).build());
        System.setProperty(MobileProperties.MOBILE_DEVICE_FILTER, "name=ipsahone");

    }

    public void testParse(String s) throws Exception {
      /*  String s ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><devices>\n" +
                "<device added=\"true\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33:1081\" location=\"341\" manufacture=\"samsung\" model=\"sm-g900f\" name=\"PF28_Galaxy_S5\" os=\"android\" remote=\"true\" serialnumber=\"a9d2150e\" used=\"true\" version=\"5.0\" versionnumber=\"5.0\"/>\n" +
                "<device added=\"true\" agent=\"-1\" category=\"PHONE\" host=\"195.13.42.33:1081\" location=\"341\" manufacture=\"samsung\" model=\"sm-g920f\" name=\"PF28_Galaxy_S6\" os=\"android\" remote=\"true\" serialnumber=\"0715f7a3ef970836\" used=\"true\" version=\"5.1.1\" versionnumber=\"5.1\"/>\n" +
                "<device added=\"true\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33:1081\" location=\"341\" manufacture=\"sony\" model=\"c6603\" name=\"PF28_Xperia_Z\" os=\"android\" remote=\"true\" serialnumber=\"cb5a1x30y4\" used=\"true\" version=\"4.4.4\" versionnumber=\"4.4\"/>\n" +
                "<device added=\"true\" agent=\"-1\" host=\"195.13.42.33\" location=\"341\" manufacture=\"apple\" model=\"iphone 4s\" name=\"PF28_iPhone_4S_B\" os=\"ios\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"1b23fae50658ce9e4bc2fa256bcefd4b5f719dfc\" status=\"unreserved online\" used=\"true\" version=\"8.1.2\" versionnumber=\"8.1\"/>\n" +
                "<device added=\"true\" agent=\"-1\" host=\"195.13.42.33\" location=\"341\" manufacture=\"apple\" model=\"iphone 6\" name=\"PF28_iPhone_6\" os=\"ios\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"3ba3ea273fc464f3d0155fd581c88be738227c7f\" status=\"unreserved online\" used=\"true\" version=\"9.0.2\" versionnumber=\"9.0\"/>\n" +
                "<device added=\"false\" agent=\"-1\" host=\"195.13.42.33\" location=\"341\" manufacture=\"apple\" model=\"iphone 5\" name=\"PF28_iPhone_5_B\" os=\"ios\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"ed51b182f5da3634a3b557005b12a69ce0d953ac\" status=\"reserved online\" used=\"true\" version=\"9.1\" versionnumber=\"9.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"htc\" model=\"htc one mini\" name=\"PF28_One_mini\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"ht38ywa05788\" status=\"unreserved online\" used=\"true\" version=\"4.4.2\" versionnumber=\"4.4\"/>\n" +
                "<device added=\"true\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"htc\" model=\"htc one s\" name=\"PF28_One_S\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"fa2c8w400213\" status=\"unreserved online\" used=\"true\" version=\"4.1.1\" versionnumber=\"4.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"htc\" model=\"htc desire s\" name=\"PF28_Desire_S\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"ht15ltj06287\" status=\"unreserved online\" used=\"true\" version=\"2.3.5\" versionnumber=\"2.3\"/>\n" +
                "<device added=\"false\" agent=\"-1\" host=\"195.13.42.33\" location=\"341\" manufacture=\"apple\" model=\"iphone 4s\" name=\"PF28_iPhone_4S_A\" os=\"ios\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"adca9f1ce15ae8a183058bc629a857b07ae5811a\" status=\"unreserved online\" used=\"true\" version=\"8.1.3\" versionnumber=\"8.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" host=\"195.13.42.33\" location=\"341\" manufacture=\"apple\" model=\"iphone 4\" name=\"PF28_iPhone_4\" os=\"ios\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"26f8ac73bb610650dd90547838146640106ddee2\" status=\"unreserved online\" used=\"true\" version=\"6.0\" versionnumber=\"6.0\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"samsung\" model=\"gt-i8190n\" name=\"PF28_Galaxy_S3mini\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"479005ccc24c3002\" status=\"unreserved online\" used=\"true\" version=\"4.1.2\" versionnumber=\"4.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"htc\" model=\"htc desire x\" name=\"PF28_Desire_X\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"ht33dly00488\" status=\"unreserved online\" used=\"true\" version=\"4.1.1\" versionnumber=\"4.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"sony\" model=\"d6603\" name=\"PF28_Xperia_Z3\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"cb5a27ws36\" status=\"unreserved online\" used=\"true\" version=\"5.0.2\" versionnumber=\"5.0\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"huawei\" model=\"ale-l21\" name=\"PF28_P_8lite\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"w3d7n15822015243\" status=\"unreserved online\" used=\"true\" version=\"5.0\" versionnumber=\"5.0\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"sony ericsson\" model=\"lt26i\" name=\"PF28_Xperia_S\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"bx903dwhq6\" status=\"unreserved online\" used=\"true\" version=\"4.1.2\" versionnumber=\"4.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"UNKNOWN\" host=\"195.13.42.33\" location=\"341\" manufacture=\"lge\" model=\"lg-h815\" name=\"PF28_G_4\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"lgh815f27eb307\" status=\"unreserved online\" used=\"true\" version=\"5.1\" versionnumber=\"5.1\"/>\n" +
                "<device added=\"false\" agent=\"-1\" category=\"TABLET\" host=\"195.13.42.33\" location=\"341\" manufacture=\"htc\" model=\"nexus 9\" name=\"PF28_Nexus_9\" os=\"android\" remote=\"true\" reservedToYou=\"false\" serialnumber=\"ht4cpwv01062\" status=\"unreserved online\" used=\"true\" version=\"5.0.1\" versionnumber=\"5.0\"/>\n" +
                "</devices>\n";
                */

        /*
        Document jsoupDocument = XMLUtils.jsoup().createJsoupDocument(s);
        for (Element device : jsoupDocument.getElementsByTag("device")) {
            String name = device.attr("name");
            String reservedToYou = device.attr("reservedToYou");
            String status = device.attr("status");
            String version = device.attr("version");
            System.out.println(name + " " + reservedToYou + " " + status);
        }
        */
    }

    @Test
    public void test1() throws Exception {
        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project6");
        System.setProperty(MobileProperties.MOBILE_OPEN_REFLECTION_SCREEN, "true");

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

        TestDevice pf28_xperia_z = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_One_X").build();
        MobileDriverManager.deviceStore().addDevice(pf28_xperia_z);
        mobileDriver.reserveDevice(pf28_xperia_z);
        mobileDriver.switchToDevice(pf28_xperia_z);

    }

    @Test
    public void testT01_Sendungsverfolgung_SucheValideNummer() throws Exception {
        System.setProperty(MobileProperties.MOBILE_PROJECT_DIR, "C:\\Users\\rnhb\\workspace\\project6");
        System.setProperty(MobileProperties.MOBILE_OPEN_REFLECTION_SCREEN, "true");

        MobileDriver mobileDriver = MobileDriverManager.getMobileDriver();

        TestDevice pf28_xperia_z = TestDevice.builder().operatingSystem(MobileOperatingSystem.ANDROID).name("PF28_Xperia_Z").build();
        MobileDriverManager.deviceStore().addDevice(pf28_xperia_z);
        mobileDriver.reserveDevice(pf28_xperia_z);
        mobileDriver.switchToDevice(pf28_xperia_z);

        Path path = Paths.get("src/test/resources/apps/DPAG-2014-09-08_ref.apk");
        mobileDriver.uninstall("de.dhl.paket.ref.debug");
        mobileDriver.installApplication(path.toAbsolutePath().toString());

        mobileDriver.launchApplication("de.dhl.paket.ref.debug/de.dhl.packet.MainActivity");

        NativeMobileGuiElement skipTutorialButton = new NativeMobileGuiElement("xpath=//*[@id='textViewTutorialText']");
        skipTutorialButton.click();

        NativeMobileGuiElement searchButton = new NativeMobileGuiElement("xpath=//*[@id='item_search']");
        searchButton.click();

        NativeMobileGuiElement nativeMobileGuiElement = new NativeMobileGuiElement("xpath=//*[contains(@hint,'Sendungsnummer')]");
        nativeMobileGuiElement.sendText("1234567890");

        mobileDriver.pressEnterOnKeyboard();

        System.out.println("");
        //  mobileDriver.releaseDevice(pf28_xperia_z);
    }
}
