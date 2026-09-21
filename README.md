# SauceDemo Selenium Automation Framework

A Maven-based Selenium WebDriver + Java + TestNG automation framework that
covers login/authentication, product inventory & sorting, shopping cart,
end-to-end checkout, logout, and cart/session behavior for
[saucedemo.com](https://www.saucedemo.com/).

## Tech stack

| Concern | Tool |
|---|---|
| Language / build | Java 11, Maven |
| Browser automation | Selenium WebDriver 4 |
| Driver management | WebDriverManager (auto-downloads matching browser drivers) |
| Test runner | TestNG (parallel execution, data providers) |
| Reporting | ExtentReports (HTML report per run) |
| Data-driven testing | OpenCSV (`src/test/resources/testdata/*.csv`) |
| CI | GitHub Actions (`.github/workflows/ci.yml`) |

## Project structure

```
src/main/java/com/saucedemo/
  config/ConfigReader.java        # reads config.properties, system props, env vars
  driver/DriverFactory.java       # thread-safe WebDriver factory (Chrome/Firefox/Edge, headless)
  pages/                          # Page Object Model
    BasePage.java
    LoginPage.java
    InventoryPage.java
    ProductDetailsPage.java
    CartPage.java
    CheckoutStepOnePage.java
    CheckoutStepTwoPage.java
    CheckoutCompletePage.java
  utils/
    ScreenshotUtils.java          # screenshot capture on failure
    ExtentManager.java            # shared Extent report instance
  listeners/TestListener.java     # TestNG listener: report + screenshots

src/test/java/com/saucedemo/
  tests/                          # one class per functional area, all extend BaseTest
    BaseTest.java
    LoginTest.java
    InventoryTest.java
    CartTest.java
    CheckoutTest.java
    LogoutTest.java
    CartSessionTest.java
  dataproviders/CsvDataProviders.java

src/test/resources/
  config.properties               # base URL, browser, timeouts (all overridable)
  testdata/users.csv               # all 6 accepted users + negative cases
  testdata/checkout_data.csv       # checkout field validation data

testng.xml                        # suite definition, parallel="tests" thread-count="4"
.github/workflows/ci.yml          # GitHub Actions CI pipeline
```

## Capabilities implemented (8 of the requested "at least 4")

1. **Data-driven testing** — `users.csv` / `checkout_data.csv` via OpenCSV + TestNG `@DataProvider`.
2. **Cross-browser execution** — Chrome, Firefox, Edge via `DriverFactory` + WebDriverManager.
3. **Configurable application/environment settings** — `config.properties`, overridable with `-Dbrowser=`, `-Dheadless=`, `-Dbase.url=`, or env vars.
4. **Automatic screenshots on test failure** — `ScreenshotUtils` + `TestListener.onTestFailure`.
5. **Test reporting** — ExtentReports HTML report generated per run under `test-output/extent-report/`.
6. **Parallel test execution** — `testng.xml` (`parallel="tests"`, 4 threads), thread-safe driver via `ThreadLocal`.
7. **CI/CD execution readiness** — GitHub Actions workflow runs the suite headless on every push/PR across Chrome & Firefox, and uploads the report/screenshots as artifacts.
8. **Reusable driver/factory & test-base architecture** — `DriverFactory` + `BaseTest` give every test an independent, self-cleaning browser session.

## Running the tests

Prerequisites: JDK 11+, Maven 3.8+, Google Chrome and/or Firefox installed locally.

```bash
# default run (Chrome, from config.properties)
mvn clean test

# choose a browser
mvn clean test -Dbrowser=firefox

# headless (used in CI)
mvn clean test -Dbrowser=chrome -Dheadless=true

# point at a different environment
mvn clean test -Dbase.url=https://www.saucedemo.com/
```

Each test method starts and quits its own WebDriver, so tests are
independently executable and can run in any order or in parallel.

## Reports & failure diagnostics

- **HTML report:** `test-output/extent-report/report_<timestamp>.html` — open in a browser, shows pass/fail per test with system info (browser, base URL).
- **Failure screenshots:** `test-output/screenshots/<TestName>_<timestamp>.png`, automatically captured and embedded in the Extent report.
- **Raw Surefire results:** `target/surefire-reports/`.

## Configuration

`src/test/resources/config.properties`:

```properties
base.url=https://www.saucedemo.com/
browser=chrome
headless=false
implicit.wait.seconds=5
explicit.wait.seconds=10
page.load.timeout.seconds=30
screenshot.dir=test-output/screenshots
report.dir=test-output/extent-report
```

Any key can be overridden without touching this file: `-D<key>=<value>` on
the Maven command line takes precedence, then the matching environment
variable (e.g. `BASE_URL`), then this file.

## Test coverage summary (automation)

| Area | Tests |
|---|---|
| Login | valid login, locked-out user, invalid credentials, empty username, empty password, both empty, full data-driven sweep of all 6 accepted users + negatives |
| Inventory & sorting | products display name+price, sort A-Z, Z-A, price low-high, high-low, product details page + back navigation |
| Cart | add 2 / remove 1 / verify remaining + badge, empty cart badge, remove only item empties cart |
| Checkout | full happy-path with subtotal/tax/total verification and order confirmation, data-driven missing-field negative cases |
| Logout | returns to login page |
| Cart/session | cart persists across Inventory↔Cart navigation, cart/session behavior across a fresh login after logout |

## Pushing this project to GitHub

This project is not yet pushed anywhere — create a repo on GitHub, then:

```bash
cd saucedemo-automation
git init
git add .
git commit -m "Initial commit: SauceDemo Selenium automation framework"
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo>.git
git push -u origin main
```
