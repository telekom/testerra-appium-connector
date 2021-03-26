# Testerra Appium Connector

<p align="center">
    <a href="https://mvnrepository.com/artifact/io.testerra/appium" title="MavenCentral"><img src="https://img.shields.io/maven-central/v/io.testerra/appium?label=Maven%20Central"></a>
    <a href="/../../commits/" title="Last Commit"><img src="https://img.shields.io/github/last-commit/telekom/testerra-appium-connector?style=flat"></a>
    <a href="/../../issues" title="Open Issues"><img src="https://img.shields.io/github/issues/telekom/testerra-appium-connector?style=flat"></a>
    <a href="./LICENSE" title="License"><img src="https://img.shields.io/badge/License-Apache%202.0-green.svg?style=flat"></a>
</p>

<p align="center">
  <a href="#setup">Setup</a> •
  <a href="#documentation">Documentation</a> •
  <a href="#support-and-feedback">Support</a> •
  <a href="#how-to-contribute">Contribute</a> •
  <a href="#contributors">Contributors</a> •
  <a href="#licensing">Licensing</a>
</p>

## About this module

This module provides additional features for [Testerra Framework](https://github.com/telekom/testerra) for automated tests.

Appium connector uses the open source standard Appium to run web tests based on Testerra on mobile devices.

It will register with Testerra Hooking system and uses the event bus to react on Testerra events.

## Setup

### Requirements

![Maven Central](https://img.shields.io/maven-central/v/io.testerra/core/1.0-RC-32?label=Testerra)

### Usage

Include the following dependency in your project.

Gradle:

````groovy
implementation 'io.testerra:appium:1.0'
````

Maven:

````xml

<dependency>
    <groupId>io.testerra</groupId>
    <artifactId>appium</artifactId>
    <version>1.0</version>
</dependency>
````

## Documentation

### Use appium features

The Appium connector will register `mobile_chrome` and `mobile_safari` as available browser configurations for your `{browser}`
value. Further the connector will provide the `AppiumDriverManager` that you can use to unlock appium related features on the
implementation of `WebDriver` interface.

*Appium test class*

```java
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
```
        
### Device filtering

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

#### Screenshots

Screenshots on test case failure works out of the box, because Appium is implementing the necessary interfaces of Selenium to
achieve this.

#### Videos

Because videos are a platform dependent feature, Appium connector does not provide any platform-related video recording features.

### Properties

|Property|default|Description|
|---|---|---|
|tt.mobile.grid.url|NONE|Grid URL of Appium / Selenium Grid ending on "wd/hub"|
|tt.mobile.grid.access.key|NONE|Access key of your user and project|
|tt.mobile.device.query.ios|"@os='ios' and @category='PHONE'"|Access key of your user  and project|
|tt.mobile.device.query.android|"@os='android' and @category='PHONE'"|Access key of your user  and project|

---

## Publication

This module is deployed and published to Maven Central. All JAR files are signed via Gradle signing plugin.

The following properties have to be set via command line or ``~/.gradle/gradle.properties``

| Property                      | Description                                         |
| ----------------------------- | --------------------------------------------------- |
| `moduleVersion`               | Version of deployed module, default is `1-SNAPSHOT` |
| `deployUrl`                   | Maven repository URL                                |
| `deployUsername`              | Maven repository username                           |
| `deployPassword`              | Maven repository password                           |
| `signing.keyId`               | GPG private key ID (short form)                     |
| `signing.password`            | GPG private key password                            |
| `signing.secretKeyRingFile`   | Path to GPG private key                             |

If all properties are set, call the following to build, deploy and release this module:
````shell
gradle publish closeAndReleaseRepository
````

## Code of Conduct

This project has adopted the [Contributor Covenant](https://www.contributor-covenant.org/) in version 2.0 as our code of conduct. Please see the details in our [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md). All contributors must abide by the code of conduct.

## Working Language

We decided to apply _English_ as the primary project language.  

Consequently, all content will be made available primarily in English. We also ask all interested people to use English as language to create issues, in their code (comments, documentation etc.) and when you send requests to us. The application itself and all end-user faing content will be made available in other languages as needed.


## Support and Feedback
The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **Issues**   | <a href="/../../issues/new/choose" title="Issues"><img src="https://img.shields.io/github/issues/telekom/testerra-appium-connector?style=flat"></a> |
| **Other Requests**    | <a href="mailto:testerra@t-systems-mms.com" title="Email us"><img src="https://img.shields.io/badge/email-CWA%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a>   |

## How to Contribute

Contribution and feedback is encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](./CONTRIBUTING.md). By participating in this project, you agree to abide by its [Code of Conduct](./CODE_OF_CONDUCT.md) at all times.

## Contributors

At the same time our commitment to open source means that we are enabling -in fact encouraging- all interested parties to contribute and become part of its developer community.

## Licensing

Copyright (c) 2021 Deutsche Telekom AG.

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
