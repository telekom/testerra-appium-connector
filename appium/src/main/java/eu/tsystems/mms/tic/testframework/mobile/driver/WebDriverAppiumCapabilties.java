package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.appium.AppiumCapabilityHelper;
import eu.tsystems.mms.tic.testframework.utils.CertUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.AbstractWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WebDriverAppiumCapabilties implements Consumer<WebDriverRequest>, AppiumCapabilityHelper {

    /**
     * Testerra sets 'acceptInsecureCerts' in case of using CertUtils or property 'tt.cert.trusted.hosts'
     * For mobile browsers this plain cap is not allowed (especially Safari) and it has to be used with 'appium' prefix.
     */
    @Override
    public void accept(WebDriverRequest webDriverRequest) {
        if (webDriverRequest instanceof AbstractWebDriverRequest) {
            CertUtils certUtils = CertUtils.getInstance();
            if (certUtils.isTrustAllHosts() || certUtils.getTrustedHosts().length > 0) {
                // To modify the cap list we convert it to a HashMap.
                Capabilities capabilities = webDriverRequest.getCapabilities();
                Map<String, Object> map = new HashMap<>(capabilities.asMap());
                final String aicCap = "acceptInsecureCerts";
                map.remove(aicCap);
                MutableCapabilities mutableCapabilities = new MutableCapabilities(map);
                mutableCapabilities.setCapability(getAppiumCap(aicCap), true);
                ((AbstractWebDriverRequest) webDriverRequest).setCapabilities(mutableCapabilities);
            }
        }
    }
}
