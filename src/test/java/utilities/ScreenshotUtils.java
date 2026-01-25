package utilities;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    // Normal screenshot
    public static String takeScreenshot(WebDriver driver, String methodName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String path = System.getProperty("user.dir") + "/Screenshots/" + methodName + "_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Screenshot with element highlight
    public static String takeScreenshotWithHighlight(WebDriver driver, WebElement element, String methodName) {
        String path = takeScreenshot(driver, methodName);
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid red'", element);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            path = System.getProperty("user.dir") + "/Screenshots/" + methodName + "_highlight_" + timestamp + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(path));

            js.executeScript("arguments[0].style.border=''", element); // remove highlight
        } catch (Exception e) {
            System.out.println("Highlight screenshot failed: " + e.getMessage());
        }
        return path;
    }
}
