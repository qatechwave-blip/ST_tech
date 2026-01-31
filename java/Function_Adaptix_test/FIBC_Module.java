package Function_Adaptix_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Function_STB_Adaptix_test.BaseTest;
import utilities.ElementUtils;
import utilities.Wait_Class;

public class FIBC_Module extends BaseTest{

	 public static final Logger Log = LogManager.getLogger(Login_form.class);
	    private ElementUtils element;

	    public FIBC_Module (WebDriver driver) {
	        super(driver);
	        element = new ElementUtils(driver);
	    }
	    
	    @FindBy(xpath="//div[@class=\"grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6\"]/div[3]/div/div")
	    WebElement moduleBTN;
	    
	 // -------- Click on FIBC Module --------
	    public void fibcModule() {
	        Wait_Class.waitForVisibleElement(driver, moduleBTN, 0);
	        Wait_Class.clickElementSafely(driver, moduleBTN);
	        Log.info("Clicked on FIBC Module");
	    }
	
}
