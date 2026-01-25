package Function_STB_Adaptix_test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BaseTest {
    protected WebDriver driver;

    public BaseTest(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize all @FindBy elements
    }
}
