# Testerra Mobile

Testerra Mobile is an extension for the Testerra Framework and uses the open source standard Appium to run web tests based on Testerra on mobile devices.

As the T-Systems Multimedia Solution provide an own grid for mobile device testing and an own device farm based on expritest cloud, Testerra Mobile provide some comfort features for the usage of seetest in combination with Appium as well.

Testerra Mobile will register with Testerra Hooking system and uses the event bus to react on Testerra events.


## Modules

* testerra-appium
* testerra-seetest

## Build


## Test


## Functions

### Screenshots
Screenshots on test case failure works out of the box, because Appium is implementing the necessary interfaces of Selenium to achieve this.  
So basically the screenshot-on-failure-mechanism of Testerra will trigger a mobile screenshot creation as well.

### Videos
Videos are a platform dependent feature. 
Testerra Mobile provides a simple connector to SeeTest based cloud platforms or device farms.  
Using this connector you will get videos on failure oder even on successful test cases if you enable them with basic Testerra properties.

### Device Queries
To filter the available devices on your chosen cloud platform or mobile device farm, you can use device queries.
Device queries can be stored in your `test.properties`, configured via `PropertyManager` or created as object instances of `AppiumDeviceQuery`.

In the background Appium, Selenium and Testerra will transform your device query into valid `DesiredCapabilities` to send valid request to your mobile device grid.



## Important Notes

### Different Behaviour possible
###


