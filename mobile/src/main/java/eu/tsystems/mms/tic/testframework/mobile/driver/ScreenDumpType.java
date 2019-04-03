package eu.tsystems.mms.tic.testframework.mobile.driver;

/**
 * Created by rnhb on 21.12.2015.
 *
 * @link https://docs.experitest.com/display/public/SA/GetVisualDump
 */
public enum ScreenDumpType {
    NATIVE_INSTRUMENTED("Native"),
    WEB("Web"),
    NON_INSTRUMENTED("non-instrumented");

    private final String value;

    ScreenDumpType(String value) {

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
