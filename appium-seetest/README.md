# Testerra SeeTest Connector

## Running the tests

Set grid url and your credentials into `src/test/resources/test.properties` based on `src/test/resources/test_templ.properties`

```properties
tt.mobile.grid.url=https://your-appium-server.tld/wd/hub
tt.mobile.grid.access.key=your_access_key

tt.screencaster.active=true
tt.appium.seetest.video.onsuccess=true
```

and then just run

```shell
gradle test
```
