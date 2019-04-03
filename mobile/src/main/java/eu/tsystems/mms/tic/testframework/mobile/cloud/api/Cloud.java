package eu.tsystems.mms.tic.testframework.mobile.cloud.api;

import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.ApplicationsApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devicegroups.DeviceGroupsApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.DevicesApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.ProjectsApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.UsersApi;

/**
 * Created by rnhb on 09.12.2016.
 */
public class Cloud {

    private static final ApplicationsApi APPLICATIONS_API = new ApplicationsApi();
    private static final ProjectsApi PROJECTS_API = new ProjectsApi();
    private static final UsersApi USERS_API = new UsersApi();
    private static final DevicesApi DEVICES_API = new DevicesApi();
    private static final DeviceGroupsApi DEVICE_GROUPS_API = new DeviceGroupsApi();

    public static ApplicationsApi applications() {
        return APPLICATIONS_API;
    }

    public static ProjectsApi projects() {
        return PROJECTS_API;
    }

    public static UsersApi users() {
        return USERS_API;
    }

    public static DevicesApi devices() {
        return DEVICES_API;
    }

    public static DeviceGroupsApi deviceGroups() {
        return DEVICE_GROUPS_API;
    }
}
