package utilities;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class Wait_Class {

    // ---------------- BASIC WAITS ----------------
    public static void waitForVisibleElement(WebDriver driver, WebElement element, int timeoutSec) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForClickable(WebDriver driver, WebElement element, int timeoutSec) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // ---------------- FLUENT WAIT ----------------
    public static WebElement fluentWait(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ---------------- DROPDOWN WAIT ----------------
    public static void waitForDropdownOptions(WebDriver driver, WebElement dropdown, int minOptions) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> {
                    Select select = new Select(dropdown);
                    return select.getOptions().size() > minOptions;
                });
    }

    // ---------------- SAFE SENDKEYS ----------------
    public static void sendKeysSafe(WebDriver driver, WebElement element, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.clear();
            element.sendKeys(value);
        } catch (InvalidElementStateException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", element, value);
        }
    }

    // ---------------- SAFE CLICK ----------------
    public static void clickElementSafely(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

	
}
