package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utilities.BrowserUtils;

public class BasePage {
    protected static WebDriver driver;
    
    public BasePage() {
        this.driver = BrowserUtils.getDriver();
        PageFactory.initElements(driver, this);
    }
}