package utilities;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    public static String takeScreenshot(WebDriver driver, String methodName) {

        try {
            String timestamp =
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String dirPath =
                    System.getProperty("user.dir") + "/screenshots/";

            new File(dirPath).mkdirs(); //  auto create folder

            String path =
                    dirPath + methodName + "_" + timestamp + ".png";

            File src =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            FileUtils.copyFile(src, new File(path));

            return path;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
