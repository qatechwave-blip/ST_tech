package utilities;

import java.time.Duration;
import java.util.function.Supplier;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AgGridUtils {

    public static void enterAgGridValue(
            WebDriver driver,
            String columnId,
            int rowIndex,
            String value
    ) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        By cellLocator = By.xpath(
                "//div[@row-index='" + rowIndex + "']" +
                "//div[@col-id='" + columnId + "']"
        );

        retryOnStale(() -> {

            WebElement cell = wait.until(
                    ExpectedConditions.elementToBeClickable(cellLocator)
            );
            cell.click();

            WebElement input = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[contains(@class,'ag-input')]")
                    )
            );

            input.sendKeys(Keys.CONTROL + "a");
            input.sendKeys(Keys.DELETE);
            input.sendKeys(value);
            input.sendKeys(Keys.ENTER);

            return true;
        }, 3);
    }

    // 🔁 reusable stale retry
    public static void retryOnStale(Supplier<Boolean> action, int retryCount) {
        int attempts = 0;
        while (attempts < retryCount) {
            try {
                action.get();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == retryCount) {
                    throw e;
                }
            }
        }
    }
}
