# Testerra Mobile

Testerra Mobile is an extension for the Testerra Framework and uses the open source standard Appium to run web tests based on Testerra on mobile devices.

Testerra Mobile will register with Testerra Hooking system and uses the event bus to react on Testerra events.

## Modules

* testerra-appium

## Build


## Test
Run following command 
```gradle
gradle clean regressionTest
```


## Functions

### Screenshots
Screenshots on test case failure works out of the box, because Appium is implementing the necessary interfaces of Selenium to achieve this.  
So basically the screenshot-on-failure-mechanism of Testerra will trigger a mobile screenshot creation as well.

### Videos
Beacuase videos are a platform dependent feature, Testerra mobile and it's appium connector does not provide any platform-related video recording features.

### Device Queries
To filter the available devices on your chosen cloud platform or mobile device farm, you can use device queries.
Device queries can be stored in your `test.properties`, configured via `PropertyManager` or created as object instances of `AppiumDeviceQuery`.

In the background Appium, Selenium and Testerra will transform your device query into valid `DesiredCapabilities` to send valid request to your mobile device grid.



## Important Notes

### Different Behaviour possible
###


