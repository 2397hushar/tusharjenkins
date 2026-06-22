package web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.BasePage;
import utilities.ConfigReader;
import utilities.BrowserUtils;

import java.time.Duration;

public class SimonsLoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement loginButton;
    
    // Mobile locators (will be used when on mobile platform)
    @FindBy(xpath = "//android.widget.EditText[@resource-id='email']")
    private WebElement mobileUsernameField;
    
    @FindBy(xpath = "//android.widget.EditText[@resource-id='password']")
    private WebElement mobilePasswordField;
    
    @FindBy(xpath = "//android.widget.Button[@text='Login']")
    private WebElement mobileLoginButton;

    public void navigateToSimonsLoginPage() {
        if (BrowserUtils.isMobilePlatform()) {
            // For mobile, app is already launched via capabilities
            System.out.println("📱 Mobile app already launched");
            waitForPageLoad();
        } else {
            String url = ConfigReader.getUrl();
            driver.get(url);
            System.out.println("🌐 Navigated to: " + url);
        }
    }

    public void loginToSimons(String username, String password) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        if (BrowserUtils.isMobilePlatform()) {
            // Mobile login logic
            try {
                wait.until(ExpectedConditions.visibilityOf(mobileUsernameField));
                mobileUsernameField.sendKeys(username);
                mobilePasswordField.sendKeys(password);
                mobileLoginButton.click();
                System.out.println("📱 Mobile login completed");
            } catch (Exception e) {
                // Fallback to web locators if mobile-specific not found
                System.out.println("⚠️ Mobile locators not found, trying web locators...");
                wait.until(ExpectedConditions.visibilityOf(usernameField));
                usernameField.sendKeys(username);
                passwordField.sendKeys(password);
                loginButton.click();
            }
        } else {
            // Web login logic
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.sendKeys(username);
            
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.sendKeys(password);
            
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
            System.out.println("🌐 Web login completed for: " + username);
        }
        
        Thread.sleep(3000);
    }

    public void clickForgotPassword() {
        // TODO: Implement when needed
    }
}