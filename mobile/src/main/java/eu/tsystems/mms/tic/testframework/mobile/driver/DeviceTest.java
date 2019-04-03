package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;

/**
 * Created by rnhb on 16.06.2017.
 */
public abstract class DeviceTest {

    public static final String DEFAULT_NAME = "device test";

    String name;

    public DeviceTest() {
        this(DEFAULT_NAME);
    }

    public DeviceTest(String name) {
        this.name = name;
    }

    public abstract boolean doDeviceTest(MobileDriver mobileDriver, TestDevice testDevice) throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof DeviceTest) {
            return this.name.equals(((DeviceTest) other).name);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}
