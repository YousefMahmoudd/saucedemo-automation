package com.saucedemo.driver;
import com.saucedemo.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            initDriver(ConfigReader.browser());
        }
        return DRIVER_THREAD_LOCAL.get();
    }

    public static void initDriver(String browserName) {
        WebDriver driver = createDriver(browserName);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.implicitWaitSeconds()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.pageLoadTimeoutSeconds()));
        driver.manage().window().maximize();
        DRIVER_THREAD_LOCAL.set(driver);
    }

    private static WebDriver createDriver(String browserName) {
        boolean headless = ConfigReader.headless();
        switch (browserName.trim().toLowerCase()) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                return new FirefoxDriver(options);
            }
            case "edge": {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return new EdgeDriver(options);
            }
            case "chrome":
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return new ChromeDriver(options);
            }
        }
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
