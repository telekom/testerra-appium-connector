# Testerra Appium Connector

## Running the tests

Set grid url and your credentials into `src/test/resources/test.properties`

```properties
tt.mobile.grid.url=https://your-appium-server.tld/wd/hub
tt.mobile.grid.access.key=your_access_key
```

and then just run

```shell
gradle test
```

### Test suites

```shell
gradle test -P[suite-name]
```

* *smoke*: Smoke test suite
* *regression*: Regression test suite

### Test against another Testerra version
```shell
gradle test -DttVersion=1-SNAPSHOT
```
