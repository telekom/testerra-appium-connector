# Appium Connector

Appium connector is an extension for the Testerra Framework and uses the open source standard Appium to run web tests based on
Testerra on mobile devices.

It will register with Testerra Hooking system and uses the event bus to react on Testerra events.

---- 

## Releases

* Latest Release: `1.0-RC-4`

## Requirements

* Testerra in Version `1.0-RC-26`

## Usage

Include the following dependency in your project.

Gradle:

````groovy
implementation 'eu.tsystems.mms.tic.testerra:appium:1.0-RC-4'
````

Maven:

````xml

<dependency>
    <groupId>eu.tsystems.mms.tic.testerra</groupId>
    <artifactId>appium</artifactId>
    <version>1.0-RC-4</version>
</dependency>
````

## Use appium features

The Appium connector will register `mobile_chrome` and `mobile_safari` as available browser configurations for your `{browser}`
value. Further the connector will provide the `AppiumDriverManager` that you can use to unlock appium related features on the
implementation of `WebDriver` interface.

*Appium test class*

````java
public class ExampleTest extends TesterraTest {

    @Test
    public void testT01_My_first_test() {

        final AppiumDriverManager appiumDriverManager = new AppiumDriverManager();

        final WebDriver driver = WebDriverManager.getWebDriver();
        final AppiumDriver<MobileElement> appiumDriver = appiumDriverManager.fromWebDriver(driver);

        appiumDriver.rotate(ScreenOrientation.LANDSCAPE);
        driver.get(PropertyManager.get("tt.baseurl"));
    }
}
````

## Device filtering

If you have to run your tests on specific mobile devices available on your mobile device farm, you can use
the `tt.mobile.device.query.android` and `tt.mobile.device.query.ios` to filter for your devices by different properties. To reserve
a device and run tests on it you can simply use `WebDriverManager.getWebDriver()` call. If one or more device(s) matches your
filter, the driver will be instantiated, otherwise the call will fail.

The following query attributes are available for the device query strings.

- @category
- @os
- @version
- @manufacture
- @model
- @name

Please keep in mind, that you have to specify the device query for each operating system with the properties mentioned above. The
default values will provide you a device with given operating system and of `@category PHONE`.

### Screenshots

Screenshots on test case failure works out of the box, because Appium is implementing the necessary interfaces of Selenium to
achieve this.

### Videos

Because videos are a platform dependent feature, Appium connector does not provide any platform-related video recording features.

## Properties

|Property|default|Description|
|---|---|---|
|tt.mobile.grid.url|NONE|Grid URL of Appium / Selenium Grid ending on "wd/hub"|
|tt.mobile.grid.access.key|NONE|Access key of your user and project|
|tt.mobile.device.query.ios|"@os='ios' and @category='PHONE'"|Access key of your user  and project|
|tt.mobile.device.query.android|"@os='android' and @category='PHONE'"|Access key of your user  and project|

---

## Publication

### ... to a Maven repo

```sh
gradle publishToMavenLocal
```

or pass then properties via. CLI

```sh
gradle publish -DdeployUrl=<repo-url> -DdeployUsername=<repo-user> -DdeployPassword=<repo-password>
```

Set a custom version

```shell script
gradle publish -DmoduleVersion=<version>
```

### ... to Bintray

Upload and publish this module to Bintray:

````sh
gradle bintrayUpload -DmoduleVersion=<version> -DBINTRAY_USER=<bintray-user> -DBINTRAY_API_KEY=<bintray-api-key>
```` 

