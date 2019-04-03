package eu.tsystems.mms.tic.testframework.mobile.cloud.projects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.AbstractCloudApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.UsersApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.DevicesApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevices;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.User;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.Users;

/**
 * Created by rnhb on 09.12.2016.
 */
public class ProjectsApi extends AbstractCloudApi {

    private final String prefix = "projects/";

    public Projects getProjects() {
        JsonObject jsonObject = get(prefix);
        return parseFromJson(jsonObject);
    }

    public static Projects parseFromJson(JsonObject jsonObject) {
        Projects projects = new Projects();
        Gson gson = new Gson();
        JsonElement data = jsonObject.get("data");
        for (JsonElement jsonElement : data.getAsJsonArray()) {
            Project project = gson.fromJson(jsonElement.toString(), Project.class);
            projects.add(project);
        }
        return projects;
    }

    /**
     * Create new project
     *
     * @param prjName name of project.
     */
    public Integer createProject(String prjName) {
        JsonObject jsonObject = post(prefix + "new", "name=" + prjName);
        String status = jsonObject.get("status").getAsString();
        if ("SUCCESS".equals(status)) {
            return jsonObject.get("data").getAsJsonObject().get("id").getAsInt();
        }
        return null;
    }

    /**
     * Delete project from cloud.
     *
     * @param prjId id of project to delete.
     */
    public boolean deleteProject(Integer prjId) {
        JsonObject jsonObject = post(prefix + prjId + "/delete");
        String status = jsonObject.get("status").getAsString();
        return "SUCCESS".equals(status);
    }

    /**
     * assign user to project
     *
     * @param project project to assign user to.
     * @param user    user to assign to project
     * @return true if api result success.
     */
    public boolean assignUser(Project project, User user) {
        JsonObject jsonObject = post(prefix + project.id + "/users/" + user.id + "/new");
        String status = jsonObject.get("status").getAsString();
        return "SUCCESS".equals(status);
    }

    public Users getUsers(Project project) {
        JsonObject jsonObject = get(prefix + project.id + "/users/");

        // bullshit - experitest's api is not consistent in case, so add this stupid workaround or jsonization fails
        String jsonAsString = jsonObject.toString().replaceAll("username", "userName");
        JsonObject usersAsjsonAgain = new JsonParser().parse(jsonAsString).getAsJsonObject();
        return UsersApi.parseFromJson(usersAsjsonAgain);
    }

    public MobileDevices getDevices(Project project) {
        JsonObject jsonObject = get(prefix + project.id + "/devices/");

        // bullshit - experitest's api is not consistent in case, so add this stupid workaround or jsonization fails
        return DevicesApi.parseFromJson(jsonObject);
    }

    public int getNumberOfReservations(Project project) {
        JsonObject jsonObject = get(prefix + project.id + "/max-reservations/");
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        if ("true".equalsIgnoreCase(data.get("maxReservationsMode").getAsString())) {
            return data.get("maxReservation").getAsInt();
        } else {
            return -1;
        }
    }

    public int getNumberOfConcurrentBrowser(Project project) {
        JsonObject jsonObject = get(prefix + project.id + "/max-concurrent-browser/");
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        return data.get("maxReservation").getAsInt();
    }
}
