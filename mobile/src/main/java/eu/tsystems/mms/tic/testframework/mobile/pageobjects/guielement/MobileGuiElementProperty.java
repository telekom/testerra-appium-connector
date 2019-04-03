package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

/**
 * Created by rnhb on 22.12.2015.
 */
public enum MobileGuiElementProperty {
    HIDDEN("hidden"), ON_SCREEN("onScreen"), PARENT_HIDDEN("parentHidden"), TEXT("text"), TOP("top");

    private final String propertyName;

    MobileGuiElementProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public static MobileGuiElementProperty getValueFor(String propertyName) {
        for (MobileGuiElementProperty mobileGuiElementProperty : values()) {
            if (mobileGuiElementProperty.propertyName.equals(propertyName)) {
                return mobileGuiElementProperty;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return propertyName;
    }
}
