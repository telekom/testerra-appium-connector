package eu.tsystems.mms.tic.testframework.mobile.systemtest;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.execution.testng.NonFunctionalAssert;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.CloudApiClient;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.Application;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.Applications;
import eu.tsystems.mms.tic.testframework.mobile.cloud.applications.ApplicationsApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.DevicesApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.devices.MobileDevices;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Project;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.Projects;
import eu.tsystems.mms.tic.testframework.mobile.cloud.projects.ProjectsApi;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.User;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.Users;
import eu.tsystems.mms.tic.testframework.mobile.cloud.users.UsersApi;
import eu.tsystems.mms.tic.testframework.mobile.systemtest.data.Groups;
import eu.tsystems.mms.tic.testframework.report.model.steps.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * CloudAPITest
 *
 * @author sepr
 */
public class CloudAPITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudAPITest.class);

    /**
     *
     */
    @BeforeClass(alwaysRun = true)
    public void prepareClass() {

        String env = PropertyManager.getProperty("env", "local");
        LOGGER.info("Loading env: " + env);
        PropertyManager.loadProperties("env/" + env + ".properties");
    }

    /**
     * Checks if the Applications Api works as expected.
     */
    @Test(groups = {Groups.REGRESSION})
    public void T01_API_Applications() {

        setConnectionProperties(true);
        String prefix = "eu.tsystems.mms.tic.mdc.app.";
        String appName = prefix + "android/.HomeActivity";

        TestStep.begin("Get Application List");
        ApplicationsApi applicationsApi = new ApplicationsApi();
        Applications applications = applicationsApi.getApplications();
        List<Application> appList = StreamSupport
                .stream(applications.spliterator(), false)
                .collect(Collectors.toList());

        TestStep.begin("Check List");
        Optional<Application> mdcApp = appList.stream().filter(app -> app.name().equals(appName)).findFirst();
        Assert.assertTrue(mdcApp.isPresent(), "MDC App found in applications from api");
        Assert.assertEquals(applications.get(appName), mdcApp.get());
        Assert.assertTrue(applications.getByPartialName(prefix).size() >= 1, "At least one companion app (android/ios) should be found.");
        NonFunctionalAssert.assertEquals(applications.getByPartialName(prefix).size(), 2, "Both companion apps for android and ioS are expected. Looks like one is missing.");
    }

    /**
     * Checks if the Devices Api works as expected.
     */
    @Test(groups = {Groups.REGRESSION})
    public void T02_API_Devices() {

        setConnectionProperties(true);

        TestStep.begin("Get Device List");
        DevicesApi devicesApi = new DevicesApi();
        MobileDevices devices = devicesApi.getDevices();
        Long iPhoneCount = StreamSupport
                .stream(devices.spliterator(), false)
                .filter(d -> d.name().startsWith("Apple"))
                .count();
        Long sasmungCount = StreamSupport
                .stream(devices.spliterator(), false)
                .filter(d -> d.name().startsWith("Samsung"))
                .count();

        TestStep.begin("Check List");
        Assert.assertEquals(new Long(devices.getByPartialName("Apple").size()), iPhoneCount);
        Assert.assertEquals(new Long(devices.getByPartialName("Samsung").size()), sasmungCount);
    }

    // @Test(enabled = false)
    // public void testDeviceReservations() {
    // setConnectionProperties(false);
    // DevicesApi devicesApi = new DevicesApi();
    // MobileDevices devices = devicesApi.getDevices();
    //
    // MobileDevice device;
    // List<Reservation> reservations;
    // do {
    // device = devices.iterator().next();
    // reservations = devicesApi.getReservations(device, 60);
    // } while (!reservations.isEmpty());
    //
    // boolean reserved = devicesApi.reserveDevice(device, 20);
    // }

    /**
     * Checks if the User Api and Project Api work as expected.
     */
    @Test(groups = {Groups.REGRESSION})
    public void T03_API_Scenario() {

        setConnectionProperties(false);

        TestStep.begin("Create User");
        User user = new User();
        user.userName = "api systemtest";
        user.firstName = "api";
        user.lastName = "systemtest";
        user.email = "whoami@example.org";
        user.authenticationType = "User";
        UsersApi usersApi = new UsersApi();
        Integer userId = usersApi.createUser(user);
        try {

            TestStep.begin("Check User");
            Assert.assertNotNull(userId, "Created user must not be null");
            Users users = usersApi.getUsers();
            User userFromCloud = users.get(user.userName);
            Assert.assertEquals(userFromCloud.id, userId.toString());

            TestStep.begin("Create Project");
            ProjectsApi prjApi = new ProjectsApi();
            Integer prjId = prjApi.createProject("systemtest-api");
            try {

                TestStep.begin("Check Project");
                Assert.assertNotNull(prjId, "Created project must not be null");
                Projects projects = prjApi.getProjects();
                Project project = projects.get("systemtest-api");
                Assert.assertEquals(project.id, prjId.toString());
                Assert.assertTrue(prjApi.assignUser(project, userFromCloud));
            } finally {
                if (prjId != null) {
                    LOGGER.info("Delete Project");
                    prjApi.deleteProject(prjId);
                }
            }
        } finally {
            if (userId != null) {
                LOGGER.info("Delete User");
                usersApi.deleteUser(userId);
            }
        }
    }

    private void setConnectionProperties(boolean projectUser) {

        String suffix = projectUser ? ".perProject" : "";
        CloudApiClient.getConnectionProperties()
                .setPassword(PropertyManager.getProperty("cloudapi.password" + suffix, null));
        CloudApiClient.getConnectionProperties().setUser(PropertyManager.getProperty("cloudapi.user" + suffix, null));
    }
}
