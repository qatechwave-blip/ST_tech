package Function_Adaptix_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import Function_STB_Adaptix_test.BaseTest;
import utilities.ElementUtils;
import utilities.Wait_Class;

public class Login_form extends BaseTest {
    public static final Logger Log = LogManager.getLogger(Login_form.class);

    private ElementUtils element;
    private SoftAssert softAssert;

    public Login_form(WebDriver driver) {
        super(driver);
        element = new ElementUtils(driver);
        softAssert = new SoftAssert();
    }

    @FindBy(name = "username") WebElement txt_userid;
    @FindBy(name = "password") WebElement txt_password;
    @FindBy(xpath = "(//button[normalize-space()='Sign in'])[1]") 
    WebElement btn_login;
   
    @FindBy(xpath = "//div[contains(@class,'dashboard')]")
    WebElement dashboardElement;

    public void setUserName(String email) {
        Wait_Class.waitForVisibleElement(driver, txt_userid, 0);
        element.sendKeys(txt_userid, email);
        Log.info("Username entered: " + email);
    }

    public void setUserPass(String pass) {
        Wait_Class.waitForVisibleElement(driver, txt_password, 0);
        element.sendKeys(txt_password, pass);
        Log.info("Password entered");
    }

    public void signINBTN() {
       Wait_Class.waitForClickable(driver, btn_login, 10);
        btn_login.click();
        Log.info("Clicked on Sign In button");
    }

    public void verifyLoginSuccess() {
        try {
            Wait_Class.waitForVisibleElement(driver, dashboardElement, 0);
            softAssert.assertTrue(dashboardElement.isDisplayed(), "Dashboard is not visible");
            Log.info("Login successful, dashboard visible");
        } catch (Exception e) {
            softAssert.fail("Dashboard not visible after login");
        }
    }

    public void verifyAll() {
        softAssert.assertAll();
    }
}
