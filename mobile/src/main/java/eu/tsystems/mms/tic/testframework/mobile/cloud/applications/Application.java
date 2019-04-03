package eu.tsystems.mms.tic.testframework.mobile.cloud.applications;

import com.google.gson.annotations.SerializedName;
//TODO rework to core-interop
//import eu.tsystems.mms.tic.testframework.internal.DataContainer;
import eu.tsystems.mms.tic.testframework.mobile.cloud.Nameable;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Project;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Projects;

import java.util.Objects;

/**
 * Created by rnhb on 09.12.2016.
 */
public class Application implements Nameable
        /*TODO rework to core-interop , DataContainer*/ {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("version")
    public String version;

    @SerializedName("osType")
    public String osType;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("nonInstrumented")
    public String nonInstrumented;

    @SerializedName("packageName")
    public String packageName;

    @SerializedName("mainActivity")
    public String mainActivity;

    @SerializedName("bundleIdentifier")
    public String bundleIdentifier;

    @SerializedName("releaseVersion")
    public String releaseVersion;

    @SerializedName("applicationName")
    public String applicationName;

    @SerializedName("uniqueName")
    public String uniqueName;

    @SerializedName("cameraSupport")
    public String cameraSupport;

    @SerializedName("notes")
    public String notes;

    @SerializedName("instrumentByProfile")
    public String instrumentByProfile;

    @SerializedName("hasCustomKeystore")
    public String hasCustomKeystore;

    @SerializedName("isForSimulator")
    public String isForSimulator;

    @SerializedName("createdAtFormatted")
    public String createdAtFormatted;

    @SerializedName("productId")
    public String productId;

    public Projects projects = new Projects();

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Application " + name + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
