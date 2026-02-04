package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import utilities.ConfigReader;  // ADD THIS IMPORT

public class LoginPage extends BasePage {
    
    @FindBy(id = "user-name")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(className = "login_logo")
    private WebElement loginLogo;
    
    public void navigateToLoginPage() {
        driver.get(ConfigReader.getUrl());  // FIXED: Remove 'utilities.' prefix
    }
    
    public void login(String username, String password) {
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }
    
    public void verifyLoginPage() {
        Assert.assertTrue(loginLogo.isDisplayed(), "Login page is not displayed");
        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");
    }
}