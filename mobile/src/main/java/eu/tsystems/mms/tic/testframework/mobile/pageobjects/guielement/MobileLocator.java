package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

/**
 * Created by rnhb on 22.12.2015.
 */
public class MobileLocator {
    public final String zone;
    public final String locator;
    public final int index;

    public MobileLocator(String zone, String locator, int index) {
        this.zone = zone;
        this.locator = locator;
        this.index = index;
    }

    public String getLocatorWithoutXpathPrefix() {
        return locator.replace("xpath=", "");
    }

    @Override
    public String toString() {
        String indexInfo = "";
        if (index > 0) {
            indexInfo = "[" + index + "] ";
        }
        return locator + " " + indexInfo + "in " + zone;
    }


}
