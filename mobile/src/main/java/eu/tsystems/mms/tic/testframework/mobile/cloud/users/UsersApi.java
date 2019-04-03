package eu.tsystems.mms.tic.testframework.mobile.cloud.users;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.tsystems.mms.tic.testframework.mobile.cloud.api.AbstractCloudApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.User;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.Users;

/**
 * Created by rnhb on 09.12.2016.
 */
public class UsersApi extends AbstractCloudApi {

    private final String prefix = "users/";

    public static Users parseFromJson(JsonObject jsonObject){
        Gson gson = new Gson();
        Users users = new Users();
        for (JsonElement jsonElement : jsonObject.get("data").getAsJsonArray()) {
            User user = gson.fromJson(jsonElement, User.class);
            users.add(user);
        }
        return users;
    }

    public Users getUsers() {
        JsonObject jsonObject = get(prefix);
        return parseFromJson(jsonObject);
    }

    /**
     * Add user to cloud
     * 
     * @param user User to add.
     */
    public Integer createUser(User user) {
        JsonObject jsonObject = post(prefix + "new", "username=" + user.userName,
                "firstName=" + user.firstName,
                "lastName=" + user.firstName, "email=" + user.email, "authenticationType=" + user.authenticationType);
        String status = jsonObject.get("status").getAsString();
        if ("SUCCESS".equals(status)) {
            return jsonObject.get("data").getAsJsonObject().get("id").getAsInt();
        }
        return null;
    }

    /**
     * Delete User from cloud.
     * 
     * @param userId id of User to delete.
     */
    public boolean deleteUser(Integer userId) {
        JsonObject jsonObject = post(prefix + userId + "/delete");
        String status = jsonObject.get("status").getAsString();
        return "SUCCESS".equals(status);
    }
}
