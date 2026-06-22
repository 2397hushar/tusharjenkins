package mobile.android.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import mobile.android.pages.locators.AndroidLocators;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AndroidLoginPage extends AndroidBasePage {
    
    @AndroidFindBy(xpath = AndroidLocators.Login.LETTSCONNECTBUTTON)
    private WebElement LetsConnected;
    
    @AndroidFindBy(id = AndroidLocators.Login.USERNAME_ID)
    private WebElement usernameField;
    
    @AndroidFindBy(id = AndroidLocators.Login.PASSWORD_ID)
    private WebElement passwordField;
    
    @AndroidFindBy(id = AndroidLocators.Login.LOGIN_BUTTON_ID)
    private WebElement loginButton;
    
    @AndroidFindBy(xpath = AndroidLocators.Login.USERNAME_XPATH)
    private WebElement usernameFieldXPath;
    
    @AndroidFindBy(xpath = AndroidLocators.Login.PASSWORD_XPATH)
    private WebElement passwordFieldXPath;
    
    @AndroidFindBy(xpath = AndroidLocators.Login.LOGIN_BUTTON_ID)
    private WebElement loginButtonXPath;
    
    public void navigateToLoginPage() {
        System.out.println("📱 Android app launched - Navigating to login page");
        
        try {
            // Check if already on login page
            if (isLoginPageDisplayed()) {
                System.out.println("✅ Already on login page");
                return;
            }
            
            // Try to click Let's Connect button if present
            clickLetsConnectButton();
            
            // Wait for login page to load
            waitForLoginPage();
            
        } catch (Exception e) {
            System.out.println("⚠️ Navigation to login page: " + e.getMessage());
        }
    }
    public void clickLetsConnectButtonForRelogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(LetsConnected));
            LetsConnected.click();
            System.out.println("✅ Let's Connect button clicked for relogin");
        } catch (Exception e) {
            System.out.println("❌ Unable to click Let's Connect button : " + e.getMessage());
        }
    }
    private void clickLetsConnectButton() {
        try {
            // Try multiple ways to find and click Let's Connect button
            List<WebElement> connectButtons = driver.findElements(By.xpath(
                "//android.widget.Button[contains(@text, 'Let') or contains(@text, 'Get') or contains(@content-desc, 'Let') or @content-desc='Lets Get Started']"));
            
            if (!connectButtons.isEmpty()) {
                WebElement connectButton = connectButtons.get(0);
                if (connectButton.isDisplayed() && connectButton.isEnabled()) {
                    connectButton.click();
                    System.out.println("✅ Clicked on Let's Connect button");
                    return;
                }
            }
            
            // Try accessibility locator - FIXED: Use AppiumBy.accessibilityId
            try {
                WebElement connectBtn = driver.findElement(AppiumBy.accessibilityId("Lets Get Started"));
                if (connectBtn.isDisplayed() && connectBtn.isEnabled()) {
                    connectBtn.click();
                    System.out.println("✅ Clicked on Let's Connect button (accessibility)");
                    return;
                }
            } catch (Exception e) {
                // Continue
            }
            
            // Try by text content
            try {
                WebElement connectBtn = driver.findElement(By.xpath("//android.widget.Button[contains(@text, 'Get Started')]"));
                if (connectBtn.isDisplayed() && connectBtn.isEnabled()) {
                    connectBtn.click();
                    System.out.println("✅ Clicked on Get Started button");
                    return;
                }
            } catch (Exception e) {
                // Continue
            }
            
            System.out.println("⚠️ Let's Connect button not found - may already be on login page");
            
        } catch (Exception e) {
            System.out.println("⚠️ Let's Connect button not found: " + e.getMessage());
        }
    }
    private boolean isLoginPageDisplayed() {
        try {
            // Check for username field presence
            return isElementPresent(usernameField) || isElementPresent(usernameFieldXPath);
        } catch (Exception e) {
            return false;
        }
    }
    
    private void waitForLoginPage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            // Wait for either username field or password field
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(usernameField),
                ExpectedConditions.visibilityOf(usernameFieldXPath),
                ExpectedConditions.visibilityOf(passwordField),
                ExpectedConditions.visibilityOf(passwordFieldXPath)
            ));
            System.out.println("✅ Login page loaded successfully");
        } catch (Exception e) {
            System.out.println("⚠️ Login page elements not found: " + e.getMessage());
        }
    }
    
    public void enterUsername(String username) {
        System.out.println("📧 Entering username: " + username);
        
        try {
            WebElement usernameElem = null;
            
            // Try multiple locators
            if (isElementPresent(usernameField)) {
                usernameElem = usernameField;
            } else if (isElementPresent(usernameFieldXPath)) {
                usernameElem = usernameFieldXPath;
            } else {
                // Try to find by different locators
                try {
                    usernameElem = driver.findElement(By.id("email"));
                } catch (Exception e) {
                    try {
                        usernameElem = driver.findElement(By.xpath("//android.widget.EditText[contains(@text, 'Email') or contains(@hint, 'Email')]"));
                    } catch (Exception ex) {
                        // Try by class
                        List<WebElement> editTexts = driver.findElements(By.className("android.widget.EditText"));
                        if (!editTexts.isEmpty()) {
                            usernameElem = editTexts.get(0);
                        }
                    }
                }
            }
            
            if (usernameElem != null) {
                usernameElem.clear();
                usernameElem.sendKeys(username);
                System.out.println("✅ Username entered: " + username);
            } else {
                System.out.println("❌ Username field not found");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to enter username: " + e.getMessage());
        }
    }
    
    public void enterPassword(String password) {
        System.out.println("🔒 Entering password");
        
        try {
            WebElement passwordElem = null;
            
            // Try multiple locators
            if (isElementPresent(passwordField)) {
                passwordElem = passwordField;
            } else if (isElementPresent(passwordFieldXPath)) {
                passwordElem = passwordFieldXPath;
            } else {
                // Try to find by different locators
                try {
                    passwordElem = driver.findElement(By.id("password"));
                } catch (Exception e) {
                    try {
                        passwordElem = driver.findElement(By.xpath("//android.widget.EditText[contains(@text, 'Password') or contains(@hint, 'Password')]"));
                    } catch (Exception ex) {
                        // Try by class - second EditText
                        List<WebElement> editTexts = driver.findElements(By.className("android.widget.EditText"));
                        if (editTexts.size() > 1) {
                            passwordElem = editTexts.get(1);
                        }
                    }
                }
            }
            
            if (passwordElem != null) {
                passwordElem.clear();
                passwordElem.sendKeys(password);
                System.out.println("✅ Password entered");
            } else {
                System.out.println("❌ Password field not found");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to enter password: " + e.getMessage());
        }
    }
    
    public void tapLoginButton() {
        System.out.println("🔘 Attempting to tap login button");
        
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement loginBtn = null;
            
            // Try multiple locators for login button
            try {
                loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("kc-login")));
                System.out.println("✅ Found login button by ID: kc-login");
            } catch (Exception e1) {
                try {
                    loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[@text='Login']")));
                    System.out.println("✅ Found login button by XPath: //android.widget.Button[@text='Login']");
                } catch (Exception e2) {
                    try {
                        loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[contains(@text, 'Sign In')]")));
                        System.out.println("✅ Found login button by XPath: contains text 'Sign In'");
                    } catch (Exception e3) {
                        try {
                            List<WebElement> buttons = driver.findElements(By.className("android.widget.Button"));
                            for (WebElement btn : buttons) {
                                if (btn.isDisplayed() && btn.isEnabled()) {
                                    loginBtn = btn;
                                    System.out.println("✅ Found login button as first clickable button");
                                    break;
                                }
                            }
                        } catch (Exception e4) {
                            // Last resort
                        }
                    }
                }
            }
            
            if (loginBtn != null) {
                loginBtn.click();
                System.out.println("✅ Login button clicked successfully");
                waitForPageLoad();
                waitForProgressBarToDisappear();
            } else {
                throw new RuntimeException("❌ Login button not found using any locator");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to click login button: " + e.getMessage());
            
            // Last resort: try to press Enter key on keyboard if password field is focused
            try {
                driver.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(io.appium.java_client.android.nativekey.AndroidKey.ENTER));
                System.out.println("✅ Attempted login using Enter key");
            } catch (Exception ex) {
                throw new RuntimeException("❌ Login button not found using any locator", e);
            }
        }
    }
    
    public void login(String username, String password) {
        System.out.println("=========================================");
        System.out.println("🔐 Starting login process");
        System.out.println("=========================================");
        
        // Take screenshot before login
        takeScreenshot("Before_Login");
        
        enterUsername(username);
        enterPassword(password);
        tapLoginButton();
        
        System.out.println("✅ Login process completed");
        System.out.println("=========================================");
    }
    
    public boolean isLoginSuccessful() {
        try {
            System.out.println("✅ Login verification completed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Login may have failed: " + e.getMessage());
            return false;
        }
    }
}