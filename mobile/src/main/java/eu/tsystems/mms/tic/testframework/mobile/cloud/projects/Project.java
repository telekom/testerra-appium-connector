package eu.tsystems.mms.tic.testframework.mobile.cloud.projects;

import com.google.gson.annotations.SerializedName;
//TODO rework to core-interop
//import eu.tsystems.mms.tic.testframework.internal.DataContainer;
import eu.tsystems.mms.tic.testframework.mobile.cloud.Nameable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by rnhb on 09.12.2016.
 */
public class Project implements Nameable
        /*TODO rework to core-interop , DataContainer*/ {

    @SerializedName("created")
    public String created;

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public String id;

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Project " + name + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
