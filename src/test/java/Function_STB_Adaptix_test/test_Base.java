package Function_STB_Adaptix_test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.github.bonigarcia.wdm.WebDriverManager;

public class test_Base {

    protected static WebDriver admindriver;
    public static final Logger Log = LogManager.getLogger(test_Base.class);

    @BeforeClass(alwaysRun = true)
    public void setUP(ITestContext context) {

        try {
            Log.info("===== Browser Setup Started =====");

            WebDriverManager.chromedriver().setup();
            admindriver = new ChromeDriver();

            admindriver.manage().window().maximize();
            admindriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

            String url = System.getProperty("app.url", "https://testing.adaptix.in/");
            admindriver.get(url);

            context.setAttribute("driver", admindriver);

            Log.info("ChromeDriver launched and navigated to: " + url);

        } catch (Exception e) {
            Log.error("Browser setup failed ", e);
            throw e; // IMPORTANT: TestNG ko failure batata hai
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (admindriver != null) {
            admindriver.quit();
            Log.info("ChromeDriver closed");
        }
    }

    public static String captureScreenshot(String testName) {
        File src = ((TakesScreenshot) admindriver).getScreenshotAs(OutputType.FILE);
        String path = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
        try {
            Files.createDirectories(new File("screenshots").toPath());
            Files.copy(src.toPath(), new File(path).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
