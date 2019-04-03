package eu.tsystems.mms.tic.testframework.mobile.cloud.users;

import com.google.gson.annotations.SerializedName;
//TODO rework to core-interop
//import eu.tsystems.mms.tic.testframework.internal.DataContainer;
import eu.tsystems.mms.tic.testframework.mobile.cloud.Nameable;

import java.util.Objects;

/**
 * Created by rnhb on 12.12.2016.
 */
public class User implements Nameable
       /*TODO rework to core-interop , DataContainer*/ {

    @SerializedName("id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("userName")
    public String userName;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("authenticationType")
    public String authenticationType;

    @SerializedName("created")
    public String created;

    @Override
    public String name() {
        return userName;
    }

    @Override
    public String toString() {
        return "User " + userName + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
