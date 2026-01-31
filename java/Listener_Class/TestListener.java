package Listener_Class;

import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.Test;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.*;

public class TestListener implements ITestListener {

    // Thread-safe ExtentTest per test
    public static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    public static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private static final boolean AUTO_JIRA =
            Boolean.parseBoolean(System.getProperty("auto.jira", "false"));

    private static final boolean AUTO_EMAIL =
            Boolean.parseBoolean(System.getProperty("auto.email", "false"));

    //  TEST START
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName()
                + " :: " + result.getMethod().getMethodName();

        // Get ExtentReport instance
        ExtentTest test = ExtentManager.getReport().createTest(testName);
        testThread.set(test);

        //  Store WebDriver if available
        Object driverObj = result.getTestContext().getAttribute("driver");
        if (driverObj instanceof WebDriver) {
            driverThread.set((WebDriver) driverObj);
        }

        //  Assign categories from TestNG groups
        Test annotation = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(Test.class);

        if (annotation != null && annotation.groups().length > 0) {
            test.assignCategory(annotation.groups());
        }

        logStep("Test Started");
    }

    // PASS
    @Override
    public void onTestSuccess(ITestResult result) {
        logStep("Test Passed");
        testThread.get().pass("Test Passed Successfully");
    }

    //  FAIL
    @Override
    public void onTestFailure(ITestResult result) {
        logStep("Test Failed");
        testThread.get().fail(result.getThrowable());

        WebDriver driver = driverThread.get();
        String screenshotPath = null;

        try {
            // Screenshot capture
            if (driver != null) {
                screenshotPath = ScreenshotUtils.takeScreenshot(
                        driver, result.getMethod().getMethodName());

                if (screenshotPath != null) {
                    testThread.get().fail(
                            "Failure Screenshot",
                            MediaEntityBuilder
                                    .createScreenCaptureFromPath(screenshotPath)
                                    .build());
                }
            }

            // Jira bug creation
            if (AUTO_JIRA) {
                String summary = "Automation Failure : " + result.getMethod().getMethodName();
                String description = result.getThrowable().toString();

                String jiraKey = JiraUtils.createBug(summary, description, "Bug");

                if (jiraKey != null && screenshotPath != null) {
                    JiraUtils.attachScreenshot(jiraKey, screenshotPath);
                    logStep("Screenshot attached to Jira: " + jiraKey);
                }
            }

        } catch (Exception e) {
            testThread.get().warning("Listener error: " + e.getMessage());
        }
    }

    //  SKIPPED
    @Override
    public void onTestSkipped(ITestResult result) {
        logStep("Test Skipped");
        testThread.get().skip("Test Skipped");
    }

    // SUITE FINISH
    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getReport().flush();
        logStep("Extent Report Flushed");

        // Auto email if failure
        if (AUTO_EMAIL && context.getFailedTests().size() > 0) {
            try {
                EmailUtils_Class.sendExtentReportByEmail();
                logStep("Extent Report emailed");
            } catch (Exception e) {
                logStep("Email failed: " + e.getMessage());
            }
        }

        // Cleanup ThreadLocal
        testThread.remove();
        driverThread.remove();
    }

    //  Logging helper
    private void logStep(String msg) {
        if (testThread.get() != null) {
            testThread.get().info(msg);
        }
        System.out.println("[STEP] " + msg);
    }
}
