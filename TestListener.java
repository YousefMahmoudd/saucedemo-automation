package com.saucedemo.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.saucedemo.driver.DriverFactory;
import com.saucedemo.utils.ExtentManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener wired in testng.xml. Handles:
 *  - Extent report lifecycle (start/pass/fail/skip per test)
 *  - Automatic screenshot capture on failure
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentManager.startTest(result.getMethod().getMethodName(), description == null ? "" : description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String screenshotPath = null;
        try {
            screenshotPath = ScreenshotUtils.capture(DriverFactory.getDriver(), testName);
        } catch (Exception e) {
            System.err.println("Could not capture screenshot for " + testName + ": " + e.getMessage());
        }

        ExtentManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable());
        if (screenshotPath != null) {
            try {
                ExtentManager.getTest().fail("Screenshot on failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                ExtentManager.getTest().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onStart(ITestContext context) {
        // no-op: ExtentManager is initialized lazily on first startTest()
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }
}
