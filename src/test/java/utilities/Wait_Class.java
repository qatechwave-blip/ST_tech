package utilities;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class Wait_Class {

    // ---------------- Visible Element ----------------
    public static void waitForVisibleElement(WebDriver driver, WebElement element, int timeoutSec) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ---------------- Clickable Element ----------------
    public static void waitForClickable(WebDriver driver, WebElement element, int timeoutSec) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ---------------- Fluent Wait ----------------
    public static WebElement fluentWait(WebDriver driver, WebElement element) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ---------------- Dropdown Option Wait ----------------
    public static void waitForDropdownOptions(WebDriver driver, WebElement dropdown, int minOptions) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d -> {

            Select select = new Select(dropdown);
            return select.getOptions().size() > minOptions;

        });
    }

    // ---------------- Safe SendKeys ----------------
    public static void sendKeysSafe(WebDriver driver, WebElement element, String value) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));

        try {

            element.clear();
            element.sendKeys(value);

        }
        catch (Exception e) {

            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("arguments[0].value='';", element);
            js.executeScript("arguments[0].value=arguments[1];", element, value);

        }
    }

    // ---------------- Safe Click ----------------
    public static void clickElementSafely(WebDriver driver, WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.elementToBeClickable(element));

        try {

            element.click();

        }
        catch (Exception e) {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);

        }
    }

    // ---------------- Scroll To Element ----------------
    public static void scrollToElement(WebDriver driver, WebElement element) {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

 // ---------------- React Dropdown ----------------
    public static void selectReactDropdown(WebDriver driver, WebElement dropdown, String value) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // open dropdown
        wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();

        // search input
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[contains(@id,'react-select') or @role='combobox']")));

        input.clear();
        input.sendKeys(value);

        // option locator (more flexible)
        By optionLocator = By.xpath("//*[contains(@class,'option') and contains(text(),'" + value + "')]"
                + "| //div[@role='option']//span[contains(text(),'" + value + "')]"
                + "| //div[contains(text(),'" + value + "')]");

        WebElement option = wait.until(
                ExpectedConditions.visibilityOfElementLocated(optionLocator));

        scrollToElement(driver, option);

        try {

            wait.until(ExpectedConditions.elementToBeClickable(option)).click();

        } 
        catch (Exception e) {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", option);

        }
    }


}