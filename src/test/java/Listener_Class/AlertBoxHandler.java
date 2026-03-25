package Listener_Class;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import utilities.Wait_Class;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertBoxHandler {

    private WebDriver driver;
    private static final Logger Log = LogManager.getLogger(AlertBoxHandler.class);

    public AlertBoxHandler(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ---------------- WebElements ----------------
    @FindBy(xpath = "//input[@placeholder='Type confirmation text here']")
    private WebElement confirmationInput;

    @FindBy(xpath = "//button[normalize-space()='Confirm & Save']")
    private WebElement confirmButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    // ---------------- Actions ----------------

    /**
     * Type text in confirmation input and click confirm
     */
    public void typeTextAndConfirm(String text) {
        try {
            Log.info("Typing confirmation text: " + text);
            Wait_Class.fluentWait(driver, confirmationInput).clear();
            confirmationInput.sendKeys(text);
            Wait_Class.clickElementSafely(driver, confirmButton);
            Log.info("Clicked on Confirm & Save");
        } catch (Exception e) {
            Log.error("Failed in typeTextAndConfirm: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Click Cancel button on the alert/modal
     */
    public void clickCancel() {
        try {
            Log.info("Clicking Cancel button");
            Wait_Class.clickElementSafely(driver, cancelButton);
        } catch (Exception e) {
            Log.error("Failed to click Cancel: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verify if toast message is visible and contains expected text
     */
    public boolean isToastMessageVisible(String expectedMessage) {
        try {
            WebElement toast = driver.findElement(By.xpath("//div[contains(@class,'Toastify')]"));
            boolean result = toast.getText().contains(expectedMessage);
            Log.info("Toast verification result: " + result);
            return result;
        } catch (NoSuchElementException e) {
            Log.warn("Toast message not found");
            return false;
        }
    }

    /**
     * Handle standard JS alert
     */
    public void handleJSAlert(boolean accept) {
        try {
            Alert alert = driver.switchTo().alert();
            Log.info("Alert Text: " + alert.getText());
            if (accept) alert.accept();
            else alert.dismiss();
        } catch (NoAlertPresentException e) {
            Log.warn("No JS Alert present to handle");
        }
    }
}
