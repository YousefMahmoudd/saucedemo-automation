package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from config.properties and allows overrides via
 * JVM system properties (-Dbrowser=firefox) or environment variables,
 * so the application URL / browser never need to be hard-coded.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    /**
     * Resolution order: JVM system property -> environment variable -> config.properties -> default.
     */
    public static String get(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }
        String envVar = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public static String baseUrl() {
        return get("base.url", "https://www.saucedemo.com/");
    }

    public static String browser() {
        return get("browser", "chrome");
    }

    public static boolean headless() {
        return getBoolean("headless", false);
    }

    public static int implicitWaitSeconds() {
        return getInt("implicit.wait.seconds", 5);
    }

    public static int explicitWaitSeconds() {
        return getInt("explicit.wait.seconds", 10);
    }

    public static int pageLoadTimeoutSeconds() {
        return getInt("page.load.timeout.seconds", 30);
    }

    public static String screenshotDir() {
        return get("screenshot.dir", "test-output/screenshots");
    }

    public static String reportDir() {
        return get("report.dir", "test-output/extent-report");
    }
}
