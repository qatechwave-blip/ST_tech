package Listener_Class;

import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.Test;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.*;

public class TestListener implements ITestListener {

    public static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private static final boolean AUTO_JIRA = true;
    private static final boolean AUTO_EMAIL = true;

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest test = ExtentManager.getReport()
                .createTest(result.getMethod().getMethodName());
        testThread.set(test);

        Object driverObj = result.getTestContext().getAttribute("driver");
        if (driverObj instanceof WebDriver) {
            driverThread.set((WebDriver) driverObj);
        }

        logStep("Test Started: " + result.getMethod().getMethodName());

        Test annotation =
                result.getMethod().getConstructorOrMethod()
                      .getMethod().getAnnotation(Test.class);

        if (annotation != null && annotation.groups().length > 0) {
            test.assignCategory(annotation.groups());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logStep("Test Passed: " + result.getMethod().getMethodName());
        testThread.get().pass("✅ Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        logStep("❌ Test Failed: " + result.getMethod().getMethodName());
        testThread.get().fail(result.getThrowable());

        WebDriver driver = driverThread.get();
        String screenshotPath = null;

        try {
            // 📸 Screenshot
            if (driver != null) {
                screenshotPath =
                        ScreenshotUtils.takeScreenshot(
                                driver,
                                result.getMethod().getMethodName()
                        );

                testThread.get().fail(
                        "Failure Screenshot",
                        MediaEntityBuilder
                                .createScreenCaptureFromPath(screenshotPath)
                                .build()
                );
            }

            // 🐞 Jira Bug Creation
            if (AUTO_JIRA) {

                String summary =
                        "Automation Failure: " +
                        result.getMethod().getMethodName();

                String description =
                        "Test fail ho gaya.\n\n" +
                        "Test Name: " + result.getMethod().getMethodName() + "\n" +
                        "Error: " + result.getThrowable() + "\n\n" +
                        "Screenshot Extent Report me attached hai.";

                String issueType = "Bug";

                String jiraKey =
                        JiraUtils.createBug(summary, description, issueType);

                if (jiraKey != null) {
                    logStep("✅ Jira Bug created: " + jiraKey);

                    // 📎 Attach screenshot to Jira
                    if (screenshotPath != null) {
                        JiraUtils.attachScreenshot(jiraKey, screenshotPath);
                        logStep("📎 Screenshot attached to Jira");
                    }
                } else {
                    logStep("❌ Jira Bug creation failed");
                }
            }

        } catch (Exception e) {
            logStep("❌ Listener Exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logStep("⚠️ Test Skipped: " + result.getMethod().getMethodName());
        testThread.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {

        logStep("🏁 Test Suite Finished");
        ExtentManager.getReport().flush();

        if (AUTO_EMAIL && context.getFailedTests().size() > 0) {
            try {
                EmailUtils_Class.sendExtentReportByEmail();
                logStep("📧 Extent Report emailed successfully");
            } catch (Exception e) {
                logStep("❌ Email sending failed: " + e.getMessage());
            }
        }
    }

    public static void logStep(String step) {
        if (testThread.get() != null) {
            testThread.get().info(step);
        }
        System.out.println("[STEP] " + step);
    }
}
