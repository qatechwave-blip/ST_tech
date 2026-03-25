package Listener_Class;

import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.Test;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.*;

public class TestListener implements ITestListener {

    // Thread-safe ExtentTest
    public static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    // Thread-safe WebDriver
    public static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private static final boolean AUTO_JIRA =
            Boolean.parseBoolean(System.getProperty("auto.jira", "false"));

    private static final boolean AUTO_EMAIL =
            Boolean.parseBoolean(System.getProperty("auto.email", "true"));

    // ---------------- TEST START ----------------
    @Override
    public void onTestStart(ITestResult result) {

        String testName =
                result.getTestClass().getName()
                        + " :: "
                        + result.getMethod().getMethodName();

        ExtentTest test = ExtentManager.getReport().createTest(testName);
        testThread.set(test);

        // Capture WebDriver
        Object driverObj = result.getTestContext().getAttribute("driver");

        if (driverObj instanceof WebDriver) {
            driverThread.set((WebDriver) driverObj);
        }

        // Assign groups as category
        Test annotation =
                result.getMethod()
                        .getConstructorOrMethod()
                        .getMethod()
                        .getAnnotation(Test.class);

        if (annotation != null && annotation.groups().length > 0) {
            test.assignCategory(annotation.groups());
        }

        logStep("Test Started");
    }

    // ---------------- TEST PASS ----------------
    @Override
    public void onTestSuccess(ITestResult result) {

        logStep("Test Passed");

        if (testThread.get() != null) {
            testThread.get().pass("Test Passed Successfully");
        }
    }

    // ---------------- TEST FAIL ----------------
    @Override
    public void onTestFailure(ITestResult result) {

        logStep("Test Failed");

        if (testThread.get() != null) {
            testThread.get().fail(result.getThrowable());
        }

        WebDriver driver = driverThread.get();
        String screenshotPath = null;

        try {
            // ✅ Capture Screenshot
            if (driver != null) {

                screenshotPath =
                        ScreenshotUtils.takeScreenshot(
                                driver,
                                result.getMethod().getMethodName()
                        );

                if (screenshotPath != null && testThread.get() != null) {

                    testThread.get().fail(
                            "Screenshot on Failure",
                            MediaEntityBuilder
                                    .createScreenCaptureFromPath(screenshotPath)
                                    .build()
                    );
                }
            }

            // ✅ Auto Jira Bug
            if (AUTO_JIRA) {

                String summary =
                        "Automation Failure: "
                                + result.getMethod().getMethodName();

                String description =
                        result.getThrowable().toString();

                String jiraKey =
                        JiraUtils.createBug(summary, description, "Bug");

                if (jiraKey != null && screenshotPath != null) {

                    JiraUtils.attachScreenshot(jiraKey, screenshotPath);
                    logStep("Screenshot attached to Jira: " + jiraKey);
                }
            }

        } catch (Exception e) {

            if (testThread.get() != null) {
                testThread.get().warning("Listener error: " + e.getMessage());
            }
        }
    }

    // ---------------- TEST SKIPPED ----------------
    @Override
    public void onTestSkipped(ITestResult result) {

        logStep("Test Skipped");

        if (testThread.get() != null) {
            testThread.get().skip("Test Skipped");
        }
    }

    // ---------------- SUITE FINISH ----------------
    @Override
    public void onFinish(ITestContext context) {

        // ✅ Flush Report (MOST IMPORTANT)
        ExtentManager.getReport().flush();
        logStep("Extent Report Flushed");

        // ✅ Print path (debug for Jenkins)
        System.out.println("Report generated at: "
                + System.getProperty("user.dir")
                + "/target/extent-report/index.html");

        // ✅ Email Report
        if (AUTO_EMAIL) {

            try {
                EmailUtils_Class.sendExtentReportByEmail();
                logStep("Extent Report emailed successfully");

            } catch (Exception e) {
                logStep("Email sending failed: " + e.getMessage());
            }
        }

        // Cleanup
        testThread.remove();
        driverThread.remove();
    }

    // ---------------- LOG HELPER ----------------
    private void logStep(String msg) {

        if (testThread.get() != null) {
            testThread.get().info(msg);
        }

        System.out.println("[STEP] " + msg);
    }
}