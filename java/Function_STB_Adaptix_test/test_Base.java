package Function_STB_Adaptix_test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ExtentManager;

public class test_Base {

	 protected WebDriver admindriver;
	    protected static ExtentReports extent;
	    protected static ExtentTest test;

	    public static final Logger Log = LogManager.getLogger(test_Base.class);

	    @BeforeClass(alwaysRun = true)
	    public void setUP(ITestContext context) {

	        Log.info("===== Browser Setup Started =====");

	        WebDriverManager.chromedriver().setup();
	        admindriver = new ChromeDriver();

	        admindriver.manage().window().maximize();
	        admindriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

	        admindriver.get(System.getProperty(
	                "app.url", "https://stb.adaptix.in/"));

	        // 🔥 MOST IMPORTANT
	        context.setAttribute("driver", admindriver);

	        extent = ExtentManager.getReport();
	    }

	    @BeforeMethod(alwaysRun = true)
	    public void startTest(Method method) {
	        test = extent.createTest(method.getName());
	    }

	    @AfterMethod(alwaysRun = true)
	    public void captureResult(ITestResult result) {

	        if (result.getStatus() == ITestResult.FAILURE) {
	            test.fail(result.getThrowable());
	        } else if (result.getStatus() == ITestResult.SUCCESS) {
	            test.pass("Test Passed");
	        } else {
	            test.skip("Test Skipped");
	        }
	    }

	    @AfterClass(alwaysRun = true)
	    public void tearDown() {

	        if (admindriver != null) {
	            admindriver.quit();
	            Log.info("Browser Closed");
	        }

	        if (extent != null) {
	            extent.flush();
	        }
	    }
	 // Screenshot helper (Listener bhi use karega)
	    public static String captureScreenshot(WebDriver driver, String testName) {

	        File src = ((TakesScreenshot) driver)
	                .getScreenshotAs(OutputType.FILE);

	        String path = "target/extent-report/screenshots/"
	                + testName + "_" + System.currentTimeMillis() + ".png";

	        try {
	            Files.createDirectories(
	                    new File("target/extent-report/screenshots").toPath());
	            Files.copy(src.toPath(), new File(path).toPath());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return path;
	    }
	}

