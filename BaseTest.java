package com.saucedemo.tests;

import com.saucedemo.config.ConfigReader;
import com.saucedemo.driver.DriverFactory;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Every test extends this. Each test method gets its own fresh WebDriver
 * instance (ThreadLocal-backed), so tests are independently executable,
 * order-independent, and safe to run in parallel.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    private static final String STANDARD_USER = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("") String browserParam) {
        String browser = (browserParam == null || browserParam.isBlank()) ? ConfigReader.browser() : browserParam;
        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /** Convenience helper: opens the app and logs in with the standard (happy-path) user. */
    protected InventoryPage loginAsStandardUser() {
        return new LoginPage(driver).open(ConfigReader.baseUrl()).login(STANDARD_USER, PASSWORD);
    }

    protected LoginPage openLoginPage() {
        return new LoginPage(driver).open(ConfigReader.baseUrl());
    }
}
