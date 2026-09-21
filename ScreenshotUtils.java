package com.saucedemo.utils;

import com.saucedemo.config.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    /**
     * Captures a screenshot and saves it under the configured screenshot directory.
     * Returns the absolute path of the saved file, or null if capture failed.
     */
    public static String capture(WebDriver driver, String testName) {
        try {
            Path dir = Paths.get(ConfigReader.screenshotDir());
            Files.createDirectories(dir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            Path target = dir.resolve(fileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), target);
            return target.toAbsolutePath().toString();
        } catch (IOException | ClassCastException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
