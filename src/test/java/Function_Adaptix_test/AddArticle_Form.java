package Function_Adaptix_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import Function_STB_Adaptix_test.BaseTest;
import Listener_Class.TestListener;
import utilities.ScreenshotUtils;
import utilities.Wait_Class;

public class AddArticle_Form extends BaseTest {

    public static final Logger Log = LogManager.getLogger(AddArticle_Form.class);
    private SoftAssert softAssert;

    public AddArticle_Form(WebDriver driver) {
        super(driver);
        softAssert = new SoftAssert();
    }

    // ---------------- WebElements ----------------

    @FindBy(xpath="//span[@class='side-menu__label']/li[1]/ul/li[2]")
    WebElement workOrderBTN;

    @FindBy(xpath="//*[@id='sidebar-scroll']//li[2]//div/div[2]")
    WebElement plusBTN;

    @FindBy(xpath="//*[@id='root']//form//div[contains(@class,'control')]")
    WebElement customerDropdown;
    @FindBy(xpath="//div[@id='react-select-139-placeholder']")
    WebElement Article_No;

    @FindBy(xpath="//div[@id='react-select-15-placeholder']")
    WebElement Product_Type;

    @FindBy(xpath="//div[@id='react-select-16-placeholder']")
    WebElement Article_Type;

    @FindBy(id="d707c99e-c1d7-447a-8235-c2f73d175241")
    WebElement Panel_Type;

    @FindBy(id="b5d0d8ad-158e-4f5d-9bf8-da2e0b7ba059")
    WebElement Order_Type;

    @FindBy(id="5f8cec66-3006-40c1-9ca3-2b960445f423")
    WebElement FIBCGrade;

    @FindBy(id="a824936c-d926-4e80-8df9-b29a1a73e6da")
    WebElement FIBCType;

    @FindBy(id="0cbf5f51-4c4f-4a48-9446-4c854a99d396")
    WebElement SafetyFactor;

    @FindBy(id="16cb0189-b386-4ffb-9c5c-92eea2825383")
    WebElement SWL_KG;

    @FindBy(id="e2a74ab0-b9de-488a-a8ce-39f6a7677367")
    WebElement BagDimensions;

    @FindBy(id="9f678c10-d780-4397-a789-c3dd0fe624e8")
    WebElement BagWeight;

    @FindBy(xpath="//div[contains(@class,'peer-checked:bg-blue-600')]")
    WebElement FabricLamination;
 // ---------------- ag-grid ----------------
    @FindBy(xpath="(//div[@role='gridcell'])[2]") WebElement bodyQty;
    @FindBy(xpath="//div[@role='gridcell'][3]") WebElement color;
    @FindBy(xpath="//div[@role='gridcell'][4]") WebElement gsm;
    // ---------------- Generic Action ----------------

    private void performAction(WebElement element, Runnable action, String name) {

        try {

            Wait_Class.waitForVisibleElement(driver, element, 15);
            action.run();

            Log.info(name + " executed successfully");

            if(TestListener.testThread.get()!=null)
                TestListener.testThread.get().info(name + " executed");

        }
        catch(Exception e){

            Log.error("Error at "+name);

            if(TestListener.testThread.get()!=null){

                TestListener.testThread.get().fail(name+" failed");

                String path = ScreenshotUtils.takeScreenshot(driver,name);

                try{
                    TestListener.testThread.get().addScreenCaptureFromPath(path);
                }catch(Exception ex){}

            }

            throw e;
        }
    }

    // ---------------- Dropdown Utility ----------------

    private AddArticle_Form selectByIndex(WebElement dropdown,int index,String name){

        performAction(dropdown,()->{

            Wait_Class.waitForClickable(driver, dropdown, 15);

            Select select = new Select(dropdown);

            if(select.getOptions().size()>index)
                select.selectByIndex(index);
            else
                throw new RuntimeException(name+" index not available");

        },name);

        return this;
    }

    // ---------------- Page Actions ----------------

    public AddArticle_Form clickWorkOrder(){

        performAction(workOrderBTN,
                ()->Wait_Class.clickElementSafely(driver,workOrderBTN),
                "Work Order");

        return this;
    }

    public AddArticle_Form clickPlus(){

        performAction(plusBTN,
                ()->Wait_Class.clickElementSafely(driver,plusBTN),
                "Plus Button");

        return this;
    }

    public AddArticle_Form selectCustomer(String customer) {

        performAction(customerDropdown, () ->
                Wait_Class.selectReactDropdown(driver, customerDropdown, customer),
                "Customer Dropdown");

        return this;
       
    }

    // ---------------- Dropdown Calls ----------------

    
    public AddArticle_Form selectArticle(String article) {

        performAction(Article_No, () -> 
            Wait_Class.selectReactDropdown(driver, Article_No, article),
            "Article No");

        return this;
    }

    public AddArticle_Form selectProduct(String product) {

        performAction(Product_Type, () ->
                new Select(Product_Type).selectByVisibleText(product),
                "Product Type");

        return this;
    }

    public AddArticle_Form selectArticleType(int index){
        return selectByIndex(Article_Type,index,"Article Type");
    }

    public AddArticle_Form selectPanelType(int index){
        return selectByIndex(Panel_Type,index,"Panel Type");
    }

    public AddArticle_Form selectOrderType(int index){
        return selectByIndex(Order_Type,index,"Order Type");
    }

    public AddArticle_Form selectFIBCGrade(int index){
        return selectByIndex(FIBCGrade,index,"FIBC Grade");
    }

    public AddArticle_Form selectFIBCType(int index){
        return selectByIndex(FIBCType,index,"FIBC Type");
    }

    public AddArticle_Form selectSafetyFactor(int index){
        return selectByIndex(SafetyFactor,index,"Safety Factor");
    }

    // ---------------- Inputs ----------------

    public AddArticle_Form enterSWL(String value){

        performAction(SWL_KG,()->{
            SWL_KG.clear();
            SWL_KG.sendKeys(value);
        },"SWL");

        return this;
    }

    public AddArticle_Form enterBagDimension(String value){

        performAction(BagDimensions,()->{
            BagDimensions.clear();
            BagDimensions.sendKeys(value);
        },"Bag Dimension");

        return this;
    }

    public AddArticle_Form enterBagWeight(String value){

        performAction(BagWeight,()->{
            BagWeight.clear();
            BagWeight.sendKeys(value);
        },"Bag Weight");

        return this;
    }

    public AddArticle_Form clickFabricLamination(){

        performAction(FabricLamination,
                ()->Wait_Class.clickElementSafely(driver,FabricLamination),
                "Fabric Lamination");

        return this;
    }

    // ---------------- AG GRID ----------------

    private void enterGridValue(WebElement cell,String value,String field){

        Wait_Class.fluentWait(driver,cell);

        new Actions(driver).doubleClick(cell).perform();

        Wait_Class.sendKeysSafe(driver,cell,value);

        Log.info(field+" entered "+value);
    }

    public AddArticle_Form enterBodyQty(String value){
        enterGridValue(bodyQty,value,"BodyQty");
        return this;
    }

    public AddArticle_Form enterColor(String value){
        enterGridValue(color,value,"Color");
        return this;
    }

    public AddArticle_Form enterGSM(String value){
        enterGridValue(gsm,value,"GSM");
        return this;
    }

    // ---------------- Assert ----------------

    public void verifyAll(){
        softAssert.assertAll();
    }
}