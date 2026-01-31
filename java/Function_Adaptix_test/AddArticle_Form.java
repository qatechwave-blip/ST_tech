package Function_Adaptix_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import Function_STB_Adaptix_test.BaseTest;
import Listener_Class.TestListener;
import utilities.ElementUtils;
import utilities.ScreenshotUtils;
import utilities.Wait_Class;

import java.time.Duration;
import java.util.List;

public class AddArticle_Form extends BaseTest {
    public static final Logger Log = LogManager.getLogger(AddArticle_Form.class);
    private ElementUtils element;
    private SoftAssert softAssert;

    public AddArticle_Form(WebDriver driver) {
        super(driver);
        element = new ElementUtils(driver);
        softAssert = new SoftAssert();
    }

    // ---------------- WebElements ----------------
    @FindBy(xpath = "//span[@class=\"side-menu__label\"]/li[1]/ul/li[2]")
    WebElement workOrderBTN;
    @FindBy(xpath = "//*[@id=\"sidebar-scroll\"]/div[1]/div[2]/div/div/div/nav/ul/li[2]/span/li[1]/ul/li[2]/ul/li/div/div[2]")
    WebElement plusBTN;
    @FindBy(xpath = "//select[@id='805beaec-0ed4-4a44-9376-b9646b95d1c5']") 
    WebElement CustomerName;
    @FindBy(xpath = "//select[@id=\"ff955001-9119-44da-9b4a-b6f406f28f2c\"]") WebElement Article_No;
    @FindBy(xpath = "//select[@id=\"c62e107f-03e1-478e-8522-16b590abd72d\"]") WebElement Product_Type;
    @FindBy(xpath = "//select[@id=\"4effa11d-8498-457d-9448-f503031e4b03\"]") WebElement Article_Type;
    @FindBy(xpath = "//select[@id=\"d707c99e-c1d7-447a-8235-c2f73d175241\"]") WebElement Panel_Type;
    @FindBy(xpath = "//select[@id=\"b5d0d8ad-158e-4f5d-9bf8-da2e0b7ba059\"]") WebElement Order_Type;
    @FindBy(xpath = "//select[@id='5f8cec66-3006-40c1-9ca3-2b960445f423']") WebElement FIBCGrade;
    @FindBy(xpath = "//select[@id='a824936c-d926-4e80-8df9-b29a1a73e6da']") WebElement FIBCType;
    @FindBy(xpath = "//select[@id='0cbf5f51-4c4f-4a48-9446-4c854a99d396']") WebElement SafetyFactor;
    @FindBy(xpath = "//input[@id='16cb0189-b386-4ffb-9c5c-92eea2825383']") WebElement SWL_KG;
    @FindBy(xpath = "//input[@id='e2a74ab0-b9de-488a-a8ce-39f6a7677367']") WebElement BagDimensions;
    @FindBy(xpath = "//input[@id='9f678c10-d780-4397-a789-c3dd0fe624e8']") WebElement BagWeight;
    @FindBy(xpath = "//div[@class='relative h-6 transition-colors duration-200 ease-in-out bg-gray-300 rounded-full w-11 peer-checked:bg-blue-600']") WebElement FabricLamination;
    @FindBy(xpath = "//select[@id='eb519dff-db9a-49b7-88ec-4eb30fba53e3']") WebElement PrintingColor;
    @FindBy(xpath = "//select[@id='6efb8d63-7103-4499-84fb-27c8307930d8']") WebElement PrintingSide;

    // Components - ag-grid cells
    @FindBy(xpath="(//div[@role='gridcell'])[2]")
    WebElement bodyQty;
    @FindBy(xpath="//div[@class='ag-body ag-layout-auto-height']/div/div[2]/div/div[1]/div[3]")
    WebElement color;
    @FindBy(xpath="//div[@class='ag-body ag-layout-auto-height']/div/div[2]/div/div[1]/div[4]") 
    WebElement gsm;

    // ---------------- Generic Action ----------------
    private void performActionWithHighlight(WebElement element, Runnable action, String elementName) {
        try {
            Wait_Class.waitForVisibleElement(driver, element, 15);
            action.run();
            Log.info(elementName + " performed successfully");
            if (TestListener.testThread.get() != null) {
                TestListener.testThread.get().info(elementName + " performed successfully");
            }
        } catch (Exception e) {
            Log.error("Failed at " + elementName + ": " + e.getMessage());
            if (TestListener.testThread.get() != null) {
                TestListener.testThread.get().fail("Failed at " + elementName + ": " + e.getMessage());
                try {
                    String path = ScreenshotUtils.takeScreenshot(driver, elementName);
                    TestListener.testThread.get().fail("Screenshot attached",
                            com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                } catch (Exception ex) {
                    Log.error("Failed to capture screenshot: " + ex.getMessage());
                }
            }
            throw e;
        }
    }

    private void enterAgGridValue(WebElement cell, String value, String fieldName) {
        Wait_Class.fluentWait(driver, cell);
        new Actions(driver).doubleClick(cell).perform();
        Wait_Class.sendKeysSafe(driver, cell, value);
        Log.info(fieldName + " entered: " + value);
        if (TestListener.testThread.get() != null) {
            TestListener.testThread.get().info(fieldName + " entered: " + value);
        }
    }

    // ---------------- Form Actions ----------------
    public AddArticle_Form workBTN() {
        workOrderBTN.click();
        return this;
    }

    public AddArticle_Form PlusIcon() {
        performActionWithHighlight(plusBTN, () -> {
            softAssert.assertTrue(plusBTN.isDisplayed(), "Plus button is not visible");
            plusBTN.click();
        }, "PlusIcon");
        return this;
        
    }
 // ---------------- Select Methods (Safe) ----------------
    public AddArticle_Form selectCustomer(int index) {
        performActionWithHighlight(CustomerName, () -> {
            Wait_Class.waitForDropdownOptions(driver, CustomerName, index);
            Select select = new Select(CustomerName);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "CustomerName");
        return this;
    }

    public AddArticle_Form selectArticleByIndex(int index) {
        performActionWithHighlight(Article_No, () -> {
            Wait_Class.waitForDropdownOptions(driver, Article_No, index);
            Select select = new Select(Article_No);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "Article_No");
        return this;
    }

    public AddArticle_Form selectProductByIndex(int index) {
        performActionWithHighlight(Product_Type, () -> {
            Wait_Class.waitForDropdownOptions(driver, Product_Type, index);
            Select select = new Select(Product_Type);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "Product_Type");
        return this;
    }

    public AddArticle_Form selectArticleTypeByIndex(int index) {
        performActionWithHighlight(Article_Type, () -> {
            Wait_Class.waitForDropdownOptions(driver, Article_Type, index);
            Select select = new Select(Article_Type);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "Article_Type");
        return this;
    }

    public AddArticle_Form selectPanelTypeByIndex(int index) {
        performActionWithHighlight(Panel_Type, () -> {
            Wait_Class.waitForDropdownOptions(driver, Panel_Type, index);
            Select select = new Select(Panel_Type);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "Panel_Type");
        return this;
    }

    public AddArticle_Form selectOrderType(int index) {
        performActionWithHighlight(Order_Type, () -> {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(Order_Type));
            new Select(Order_Type).selectByIndex(index);
        }, "Order_Type");
        return this;
    }

    public AddArticle_Form selectFIBCGradeByIndex(int index) {
        performActionWithHighlight(FIBCGrade, () -> {
            Wait_Class.waitForDropdownOptions(driver, FIBCGrade, index);
            Select select = new Select(FIBCGrade);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "FIBCGrade");
        return this;
    }

    public AddArticle_Form selectFIBCTypeByIndex(int index) {
        performActionWithHighlight(FIBCType, () -> {
            Wait_Class.waitForDropdownOptions(driver, FIBCType, index);
            Select select = new Select(FIBCType);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "FIBCType");
        return this;
    }

    public AddArticle_Form selectSafetyFactorByIndex(int index) {
        performActionWithHighlight(SafetyFactor, () -> {
            Wait_Class.waitForDropdownOptions(driver, SafetyFactor, index);
            Select select = new Select(SafetyFactor);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "SafetyFactor");
        return this;
    }

    // ---------------- Input Methods ----------------
    public AddArticle_Form enterSWL_KG(String swl) {
        performActionWithHighlight(SWL_KG, () -> { SWL_KG.clear(); SWL_KG.sendKeys(swl); }, "SWL_KG");
        return this;
    }

    public AddArticle_Form enterBagDimension(String bagDimension) {
        performActionWithHighlight(BagDimensions, () -> { BagDimensions.clear(); BagDimensions.sendKeys(bagDimension); }, "BagDimensions");
        return this;
    }

    public AddArticle_Form enterBagWeight(String bagWeight) {
        performActionWithHighlight(BagWeight, () -> { BagWeight.clear(); BagWeight.sendKeys(bagWeight); }, "BagWeight");
        return this;
    }

    public AddArticle_Form clickFabricLamination() {
        performActionWithHighlight(FabricLamination, FabricLamination::click, "FabricLamination");
        return this;
    }

    public AddArticle_Form selectPrintingColorByIndex(int index) {
        performActionWithHighlight(PrintingColor, () -> {
            Wait_Class.waitForDropdownOptions(driver, PrintingColor, index);
            Select select = new Select(PrintingColor);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "PrintingColor");
        return this;
    }

    public AddArticle_Form selectPrintingSideByIndex(int index) {
        performActionWithHighlight(PrintingSide, () -> {
            Wait_Class.waitForDropdownOptions(driver, PrintingSide, index);
            Select select = new Select(PrintingSide);
            if(select.getOptions().size() > index) select.selectByIndex(index);
        }, "PrintingSide");
        return this;
    }

    // ---------------- AgGrid Methods ----------------
    public AddArticle_Form enterBodyQty(String bodyqty) {
        enterAgGridValue(bodyQty, bodyqty, "BodyQty");
        return this;
    }

    public AddArticle_Form enterColor(String colour) {
        enterAgGridValue(color, colour, "Color");
        return this;
    }

    public AddArticle_Form enterGSM(String number) {
        enterAgGridValue(gsm, number, "GSM");
        return this;
    }

    // ---------------- Final Assert ----------------
    public void verifyAll() {
        softAssert.assertAll();
    }
}
