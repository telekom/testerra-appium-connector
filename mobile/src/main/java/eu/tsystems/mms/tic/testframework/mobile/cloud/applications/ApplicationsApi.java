package eu.tsystems.mms.tic.testframework.mobile.cloud.applications;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.AbstractCloudApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.CloudApiClient;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.Application;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.Applications;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Project;

/**
 * Created by rnhb on 09.12.2016.
 */
public class ApplicationsApi extends AbstractCloudApi {

    private final String prefix = "applications/";

    public Applications getApplications() {
        String applicationsString = CloudApiClient.getInstance().get(prefix);
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        Applications applications = new Applications();
        for (JsonElement applicationJsonElement : jsonParser.parse(applicationsString).getAsJsonArray()) {
            Application application = gson.fromJson(applicationJsonElement, Application.class);
            for (JsonElement projectsJsonElement : applicationJsonElement.getAsJsonObject().get("projectsInfo")
                    .getAsJsonArray()) {
                Project project = gson.fromJson(projectsJsonElement, Project.class);
                application.projects.add(project);
            }
            applications.add(application);
        }
        return applications;
    }
}
