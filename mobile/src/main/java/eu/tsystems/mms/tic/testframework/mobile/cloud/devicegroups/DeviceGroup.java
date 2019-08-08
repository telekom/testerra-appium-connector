package eu.tsystems.mms.tic.testframework.mobile.cloud.devicegroups;

import eu.tsystems.mms.tic.testframework.internal.DataContainer;
import eu.tsystems.mms.tic.testframework.mobile.cloud.Nameable;

import java.util.Objects;

public class DeviceGroup implements Nameable, DataContainer {

    public String id;

    public String name;

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "DeviceGroup " + name + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceGroup that = (DeviceGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
