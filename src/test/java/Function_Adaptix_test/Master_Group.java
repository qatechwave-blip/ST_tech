package Function_Adaptix_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Function_STB_Adaptix_test.BaseTest;
import utilities.ElementUtils;
import utilities.Wait_Class;

public class Master_Group extends BaseTest {
    public static final Logger Log = LogManager.getLogger(Master_Group.class);
    private ElementUtils element;

    public Master_Group(WebDriver driver) {
        super(driver);
        element = new ElementUtils(driver);
    }

    @FindBy(xpath="//span[@class='side-menu__label']/li[1]/a") WebElement master;

    public void masterBTN() {
        Wait_Class.waitForVisibleElement(driver, master, 0);
        Wait_Class.clickElementSafely(driver, master);
        Log.info("Clicked on Master Group menu");
    }
}
