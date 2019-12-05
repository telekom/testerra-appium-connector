package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.cloud;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nkfa on 15.04.2016.
 */
public class NavigationPage extends Page {

    @Check
    private final GuiElement profileButton = new GuiElement(driver, By.className("dropdown-toggle"));

    @Check
    private final GuiElement deviceLabLink = new GuiElement(driver, By.linkText("Device Lab"));

    @Check
    private final GuiElement virtualizationLink = new GuiElement(driver, By.linkText("Virtualization"));

    @Check
    private final GuiElement administrationLink = new GuiElement(driver, By.linkText("Administration"));

    @Check
    private final GuiElement helpLink = new GuiElement(driver, By.linkText("Help"));

    public NavigationPage(WebDriver driver) {

        super(driver);
        checkPage();
    }

    public DeviceLabDevicesPage navigateToDeviceLabDevices() {

        deviceLabLink.click();
        final GuiElement deviceLabDevicesLink = new GuiElement(driver, By.linkText("Devices"));
        deviceLabDevicesLink.click();
        return new DeviceLabDevicesPage(driver);
    }

    public DeviceLabApplicationsPage navigateToDeviceLabApplications() {

        deviceLabLink.click();
        final GuiElement deviceLabApplicationsLink = new GuiElement(driver, By.linkText("Applications"));
        deviceLabApplicationsLink.click();
        return new DeviceLabApplicationsPage(driver);
    }

    public DeviceLabHostMachinesPage navigateToDeviceLabHostMachines() {

        deviceLabLink.click();
        final GuiElement deviceLabHostMachinesLink = new GuiElement(driver, By.linkText("Host Machines"));
        deviceLabHostMachinesLink.click();
        return new DeviceLabHostMachinesPage(driver);
    }

    public VirtualizationDevicesPage navigateToVirtualizationDevices() {

        virtualizationLink.click();
        final GuiElement virtualizationDevicesLink = new GuiElement(driver, By.linkText("Devices"));
        virtualizationDevicesLink.click();
        return new VirtualizationDevicesPage(driver);
    }

    public VirtualizationExternalDevicesPage navigateToVirtualizationExternalDevices() {

        virtualizationLink.click();
        final GuiElement virtualizationExternalDevicesLink = new GuiElement(driver, By.linkText("External Devices"));
        virtualizationExternalDevicesLink.click();
        return new VirtualizationExternalDevicesPage(driver);
    }

    public VirtualizationProfilesPage navigateToVirtualizationProfiles() {

        virtualizationLink.click();
        final GuiElement virtualizationProfilesLink = new GuiElement(driver, By.linkText("Profiles"));
        virtualizationProfilesLink.click();
        return new VirtualizationProfilesPage(driver);
    }

    public VirtualizationServersPage navigateToVirtualizationServers() {

        virtualizationLink.click();
        final GuiElement virtualizationServersLink = new GuiElement(driver, By.linkText("Servers"));
        virtualizationServersLink.click();
        return new VirtualizationServersPage(driver);
    }

    public AdministrationProfilePage navigateToAdministrationProfile() {

        administrationLink.click();
        final GuiElement administrationProfileLink = new GuiElement(driver, By.linkText("Profile"));
        administrationProfileLink.click();
        return new AdministrationProfilePage(driver);
    }

    public AdministrationUsersPage navigateToAdministrationUsers() {

        administrationLink.click();
        final GuiElement administrationUsersLink = new GuiElement(driver, By.linkText("Users"));
        administrationUsersLink.click();
        return new AdministrationUsersPage(driver);
    }

    public AdministrationLDAPPage navigateToAdministrationLDAP() {

        administrationLink.click();
        final GuiElement administrationLDAPLink = new GuiElement(driver, By.linkText("LDAP"));
        administrationLDAPLink.click();
        return new AdministrationLDAPPage(driver);
    }

    public AdministrationProjectsPage navigateToAdministrationProjects() {

        administrationLink.click();
        final GuiElement administrationProjectsLink = new GuiElement(driver, By.linkText("Projects"));
        administrationProjectsLink.click();
        return new AdministrationProjectsPage(driver);
    }

    public AdministrationDeviceCleanupPage navigateToAdministrationDeviceCleanup() {

        administrationLink.click();
        final GuiElement administrationDeviceCleanupLink = new GuiElement(driver, By.linkText("Device Cleanup"));
        administrationDeviceCleanupLink.click();
        return new AdministrationDeviceCleanupPage(driver);
    }

    public AdministrationSessionsPage navigateToAdministrationSessions() {

        administrationLink.click();
        final GuiElement administrationSessionsLink = new GuiElement(driver, By.linkText("Sessions"));
        administrationSessionsLink.click();
        return new AdministrationSessionsPage(driver);
    }

    public AdministrationEmailServerPage navigateToAdministrationEmailServer() {

        administrationLink.click();
        final GuiElement administrationEmailServerLink = new GuiElement(driver, By.linkText("Email Server"));
        administrationEmailServerLink.click();
        return new AdministrationEmailServerPage(driver);
    }

    public AdministrationProvisionProfilePage navigateToAdministrationProvisionProfile() {

        administrationLink.click();
        final GuiElement administrationProvisionProfileLink = new GuiElement(driver, By.linkText("Provision Profile"));
        administrationProvisionProfileLink.click();
        return new AdministrationProvisionProfilePage(driver);
    }

    public AdministrationDiagnosePage navigateToAdministrationDiagnose() {

        administrationLink.click();
        final GuiElement administrationDiagnoseLink = new GuiElement(driver, By.linkText("Diagnose"));
        administrationDiagnoseLink.click();
        return new AdministrationDiagnosePage(driver);
    }

    public AdministrationReportsPage navigateToAdministrationReports() {

        administrationLink.click();
        final GuiElement administrationReportsLink = new GuiElement(driver, By.linkText("Reports"));
        administrationReportsLink.click();
        return new AdministrationReportsPage(driver);
    }

    public AdministrationLicensePage navigateToAdministrationLicense() {

        administrationLink.click();
        final GuiElement administrationLicenseLink = new GuiElement(driver, By.linkText("License"));
        administrationLicenseLink.click();
        return new AdministrationLicensePage(driver);
    }

    public AdministrationEULAPage navigateToAdministrationEULA() {

        administrationLink.click();
        final GuiElement administrationEULALink = new GuiElement(driver, By.linkText("EULA"));
        administrationEULALink.click();
        return new AdministrationEULAPage(driver);
    }

    public HelpPage navigateToHelpPage() {

        helpLink.click();
        return new HelpPage(driver);
    }

    public LoginPage logout() {

        profileButton.click();
        final GuiElement logoutButton = new GuiElement(driver, By.linkText("Logout"));
        logoutButton.click();
        return new LoginPage(driver);
    }
}
