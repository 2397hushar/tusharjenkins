package mobile.android.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.commons.io.FileUtils;

import mobile.android.pages.drivers.AndroidDriverManager;
import utilities.ExtentReportManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AndroidBasePage {

    protected static AndroidDriver driver;
    protected WebDriverWait wait;

    // Progress Bar
    @AndroidFindBy(xpath = "//android.widget.ProgressBar")
    private WebElement progressBar;

    // Loading Text
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Loading...']")
    private WebElement loadingText;

    // Constructor
    public AndroidBasePage() {

        try {
            System.out.println("📱 Initializing AndroidBasePage...");

            // ✅ Get driver from AndroidDriverManager
            if (driver == null) {
                driver = AndroidDriverManager.getMobileDriver();
                System.out.println("✅ Driver obtained from AndroidDriverManager");
            }

            // ✅ If still null, throw exception
            if (driver == null) {
                throw new RuntimeException("Driver is null in AndroidBasePage constructor");
            }

            wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            PageFactory.initElements(
                    new AppiumFieldDecorator(driver, Duration.ofSeconds(1)),
                    this);

            // ✅ Handle Switch Access dialog immediately after initialization
            handleSwitchAccessDialog();

            // ✅ Disable accessibility popups for the session
            disableAccessibilityPopups();

            System.out.println("✅ AndroidBasePage initialized successfully");

        } catch (Exception e) {

            System.out.println("❌ Failed to initialize AndroidBasePage: "
                    + e.getMessage());

            e.printStackTrace();
            
            // Try to recover by reinitializing driver
            try {
                System.out.println("🔄 Attempting to recover driver...");
                AndroidDriverManager.resetDriver();
                driver = AndroidDriverManager.getMobileDriver();
                if (driver != null) {
                    wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                    PageFactory.initElements(
                            new AppiumFieldDecorator(driver, Duration.ofSeconds(1)),
                            this);
                    System.out.println("✅ Driver recovered successfully");
                }
            } catch (Exception ex) {
                System.out.println("❌ Driver recovery failed: " + ex.getMessage());
                throw new RuntimeException("Failed to initialize AndroidBasePage", e);
            }
        }
    }
    
    // ============================================
    // DRIVER VALIDATION METHODS
    // ============================================
    
    /**
     * Check if driver session is valid
     */
    private boolean isDriverSessionValid() {
        try {
            if (driver == null) {
                return false;
            }
            driver.getDeviceTime();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Driver session is invalid: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ensure driver is valid before any action
     */
    public void ensureDriverValid() {
        if (driver == null || !isDriverSessionValid()) {
            System.out.println("🔄 Driver invalid, reinitializing...");
            AndroidDriverManager.resetDriver();
            driver = AndroidDriverManager.getMobileDriver();
            if (driver != null) {
                wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(1)), this);
                System.out.println("✅ Driver reinitialized successfully");
            }
        }
    }

    // Add this method to AndroidBasePage.java
    public void takeScreenshotForStep(String stepName) {
        try {
            ensureDriverValid();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = stepName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            String screenshotPath = "target/mobile-screenshots/";
            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(directory, fileName);
            FileUtils.copyFile(srcFile, destFile);
            
            // Attach to Extent Report
            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            ExtentReportManager.addScreenshotBase64(stepName, base64Screenshot);
            
            System.out.println("📸 Screenshot for step '" + stepName + "' saved and attached to report");
        } catch (Exception e) {
            System.out.println("Failed to take screenshot for step: " + e.getMessage());
        }
    }
    
    // ============================================
    // SWITCH ACCESS DIALOG HANDLING METHODS
    // ============================================

    /**
     * Handle Switch Access Setup Guide dialog
     */
    public void handleSwitchAccessDialog() {
        try {
            // Check if Switch Access dialog is present
            if (isSwitchAccessDialogPresent()) {
                System.out.println("⚠️ Switch Access Setup Guide detected, handling...");
                
                // Try multiple strategies to close the dialog
                boolean closed = false;
                
                // Strategy 1: Click Next/Previous buttons to navigate through
                try {
                    WebElement nextButton = driver.findElement(org.openqa.selenium.By.xpath(
                        "//android.widget.Button[@text='Next']"));
                    if (nextButton.isDisplayed() && nextButton.isEnabled()) {
                        // Click Next multiple times to complete setup
                        for (int i = 0; i < 3; i++) {
                            if (isSwitchAccessDialogPresent()) {
                                nextButton.click();
                                explicitWait(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    // No Next button found
                }
                
                // Strategy 2: Click Previous button
                try {
                    WebElement previousButton = driver.findElement(org.openqa.selenium.By.xpath(
                        "//android.widget.Button[@text='Previous']"));
                    if (previousButton.isDisplayed() && previousButton.isEnabled()) {
                        previousButton.click();
                        explicitWait(1);
                    }
                } catch (Exception e) {
                    // No Previous button found
                }
                
                // Strategy 3: Press back button multiple times
                for (int i = 0; i < 5; i++) {
                    if (isSwitchAccessDialogPresent()) {
                        pressBackButton();
                        explicitWait(1);
                    } else {
                        break;
                    }
                }
                
                // Strategy 4: Tap outside the dialog (center of screen)
                if (isSwitchAccessDialogPresent()) {
                    tapCenterOfScreen();
                    explicitWait(1);
                }
                
                // Strategy 5: Use ADB to kill the dialog
                if (isSwitchAccessDialogPresent()) {
                    try {
                        driver.executeScript("mobile: shell", Map.of(
                            "command", "input keyevent KEYCODE_BACK"
                        ));
                        explicitWait(1);
                    } catch (Exception e) {
                        // ADB fallback failed
                    }
                }
                
                System.out.println("✅ Switch Access dialog handling completed");
            } else {
                System.out.println("✅ No Switch Access dialog detected");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error handling Switch Access dialog: " + e.getMessage());
        }
    }

    /**
     * Check if Switch Access Setup Guide dialog is present
     */
    public boolean isSwitchAccessDialogPresent() {
        try {
            // Check by title text
            List<WebElement> dialogTitle = driver.findElements(org.openqa.selenium.By.xpath(
                "//android.widget.TextView[@text='Switch Access Setup Guide']"));
            
            if (!dialogTitle.isEmpty()) {
                return true;
            }
            
            // Check by dialog elements
            List<WebElement> switchOptions = driver.findElements(org.openqa.selenium.By.xpath(
                "//android.widget.TextView[contains(@text, 'Switch')]"));
            
            return !switchOptions.isEmpty();
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Disable accessibility popups using ADB commands
     */
    public void disableAccessibilityPopups() {
        try {
            // Disable Switch Access service
            try {
                driver.executeScript("mobile: shell", Map.of(
                    "command", "settings put secure enabled_accessibility_services null"
                ));
                System.out.println("✅ Accessibility services disabled via ADB");
            } catch (Exception e) {
                System.out.println("⚠️ Could not disable accessibility services: " + e.getMessage());
            }
            
            // Disable accessibility completely
            try {
                driver.executeScript("mobile: shell", Map.of(
                    "command", "settings put secure accessibility_enabled 0"
                ));
                System.out.println("✅ Accessibility disabled via ADB");
            } catch (Exception e) {
                System.out.println("⚠️ Could not disable accessibility: " + e.getMessage());
            }
            
            // Force stop accessibility services
            try {
                driver.executeScript("mobile: shell", Map.of(
                    "command", "am force-stop com.android.server.accessibility"
                ));
                System.out.println("✅ Accessibility service force stopped");
            } catch (Exception e) {
                // Optional
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Failed to disable accessibility popups: " + e.getMessage());
        }
    }

    /**
     * Tap the center of the screen to dismiss dialogs
     */
    public void tapCenterOfScreen() {
        try {
            Dimension size = driver.manage().window().getSize();
            int centerX = size.width / 2;
            int centerY = size.height / 2;
            
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Collections.singletonList(tap));
            System.out.println("👆 Tapped center of screen");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to tap center of screen: " + e.getMessage());
        }
    }

    /**
     * Complete method to ensure dialog is closed before any test starts
     */
    public void ensureNoSystemDialogs() {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            if (isSwitchAccessDialogPresent()) {
                System.out.println("⚠️ System dialog detected on attempt " + (i + 1));
                handleSwitchAccessDialog();
                explicitWait(1);
            } else {
                break;
            }
        }
        disableAccessibilityPopups();
        System.out.println("✅ No system dialogs blocking the app");
    }

    // ============================================
    // EXISTING METHODS (KEEP AS IS)
    // ============================================

    // Wait for page load
    public void waitForPageLoad() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Explicit wait
    public void explicitWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Hide keyboard
//    public void hideKeyboard() {
//        try {
//            if (driver != null) {
//                driver.hideKeyboard();
//            }
//        } catch (Exception e) {
//            System.out.println("Keyboard already hidden");
//        }
//    }

    // Press back button
    public void pressBackButton() {
        try {
            ensureDriverValid();
            driver.pressKey(new KeyEvent(AndroidKey.BACK));
            System.out.println("🔙 Pressed back button");
        } catch (Exception e) {
            System.out.println("❌ Failed to press back button");
        }
    }

    // Press home button
    public void pressHomeButton() {
        try {
            ensureDriverValid();
            driver.pressKey(new KeyEvent(AndroidKey.HOME));
            System.out.println("🏠 Pressed home button");
        } catch (Exception e) {
            System.out.println("❌ Failed to press home button");
        }
    }

    // Press enter button
    public void pressEnterButton() {
        try {
            ensureDriverValid();
            driver.pressKey(new KeyEvent(AndroidKey.ENTER));
            System.out.println("⏎ Pressed enter button");
        } catch (Exception e) {
            System.out.println("❌ Failed to press enter button");
        }
    }

    // Scroll to exact text
    public void scrollToText(String text) {
        try {
            ensureDriverValid();
            driver.findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true))"
                                    + ".scrollIntoView(new UiSelector().text(\""
                                    + text
                                    + "\"))"));
            System.out.println("📜 Scrolled to text: " + text);
        } catch (Exception e) {
            System.out.println("Could not scroll to: " + text);
        }
    }

    // Scroll to contains text
    public void scrollToTextContains(String text) {
        try {
            ensureDriverValid();
            driver.findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true))"
                                    + ".scrollIntoView(new UiSelector().textContains(\""
                                    + text
                                    + "\"))"));
            System.out.println("📜 Scrolled to text containing: " + text);
        } catch (Exception e) {
            System.out.println("Could not scroll to text containing: " + text);
        }
    }

    // Scroll down
    public void scrollDown() {
        try {
            ensureDriverValid();
            driver.findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true))"
                                    + ".scrollForward()"));
            System.out.println("📜 Scrolled down");
        } catch (Exception e) {
            System.out.println("Scroll down failed");
        }
    }

    // Scroll up
    public void scrollUp() {
        try {
            ensureDriverValid();
            driver.findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true))"
                                    + ".scrollBackward()"));
            System.out.println("📜 Scrolled up");
        } catch (Exception e) {
            System.out.println("Scroll up failed");
        }
    }

    // Swipe Up
    public void swipeUp() {
        try {
            ensureDriverValid();
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8);
            int endY = (int) (size.height * 0.2);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
            System.out.println("👆 Swiped up");
        } catch (Exception e) {
            System.out.println("Swipe up failed: " + e.getMessage());
        }
    }

    // Swipe Down
    public void swipeDown() {
        try {
            ensureDriverValid();
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.2);
            int endY = (int) (size.height * 0.8);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
            System.out.println("👇 Swiped down");
        } catch (Exception e) {
            System.out.println("Swipe down failed: " + e.getMessage());
        }
    }

    // Wait for progress bar disappear
    public void waitForProgressBarToDisappear() {
        try {
            ensureDriverValid();
            wait.until(ExpectedConditions.invisibilityOf(progressBar));
            System.out.println("⏳ Progress bar disappeared");
        } catch (Exception e) {
            System.out.println("Progress bar already disappeared");
        }
    }

    // Wait for loading disappear
    public void waitForLoadingToDisappear() {
        try {
            ensureDriverValid();
            wait.until(ExpectedConditions.invisibilityOf(loadingText));
            System.out.println("⏳ Loading disappeared");
        } catch (Exception e) {
            System.out.println("Loading text already disappeared");
        }
    }

    // Screenshot
    public String takeScreenshot(String screenshotName) {
        try {
            ensureDriverValid();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            String screenshotPath = "target/mobile-screenshots/";
            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(directory, fileName);
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("📸 Screenshot saved: " + destFile.getAbsolutePath());
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Failed to take screenshot");
            return null;
        }
    }

    // Check element present
    public boolean isElementPresent(WebElement element) {
        try {
            ensureDriverValid();
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Clear and type
    public void clearAndSendKeys(WebElement element, String text) {
        try {
            ensureDriverValid();
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            System.out.println("✏️ Entered text: " + text);
        } catch (Exception e) {
            System.out.println("Failed to enter text");
        }
    }

    // Click element
    public void clickElement(WebElement element) {
        try {
            ensureDriverValid();
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            System.out.println("🖱️ Clicked element");
        } catch (Exception e) {
            System.out.println("Failed to click element");
        }
    }

    // Reset driver
    public void resetDriver() {
        try {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
            AndroidDriverManager.resetDriver();
            driver = AndroidDriverManager.getMobileDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(30)), this);
            System.out.println("✅ Driver reset successfully");
        } catch (Exception e) {
            System.out.println("❌ Failed to reset driver: " + e.getMessage());
        }
    }

    // Get text
    public String getText(WebElement element) {
        try {
            ensureDriverValid();
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getText();
        } catch (Exception e) {
            System.out.println("Failed to get text");
            return "";
        }
    }
}