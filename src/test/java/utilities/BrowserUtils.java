package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.TestConfig;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.time.Duration;

public class BrowserUtils {
    private static WebDriver driver;
    
    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = TestConfig.BROWSER.toLowerCase();
            
            System.out.println("🚀 Initializing browser: " + browser + 
                             " (Headless: " + TestConfig.HEADLESS + ")");
            
            switch(browser) {
                case "chrome":
                    driver = setupChromeDriver();
                    break;
                case "edge":
                    driver = setupEdgeDriver();
                    break;
                case "firefox":
                    driver = setupFirefoxDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(TestConfig.IMPLICIT_WAIT);
            driver.manage().timeouts().pageLoadTimeout(TestConfig.PAGE_LOAD_TIMEOUT);
            
            System.out.println("✅ " + browser + " browser initialized successfully");
        }
        return driver;
    }
    
    private static WebDriver setupChromeDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            
            // Set headless mode based on TestConfig
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Chrome in HEADLESS mode");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-software-rasterizer");
            }
            
            // Common performance & security options
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-blink-features=AutomationControlled");
            
            // Remove automation flags
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            
            // Set preferences for better performance
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--ignore-certificate-errors");
            
            // WebDriverManager setup
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver(options);
            
        } catch (Exception e) {
            System.out.println("❌ Error setting up Chrome driver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Chrome driver", e);
        }
    }
    
    private static WebDriver setupEdgeDriver() {
        try {
            EdgeOptions options = new EdgeOptions();
            
            // Set headless mode based on TestConfig
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Edge in HEADLESS mode");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
            }
            
            // Common options
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            
            // WebDriverManager setup
            WebDriverManager.edgedriver().setup();
            return new EdgeDriver(options);
            
        } catch (Exception e) {
            System.out.println("❌ Error setting up Edge driver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Edge driver", e);
        }
    }
    
    private static WebDriver setupFirefoxDriver() {
        try {
            FirefoxOptions options = new FirefoxOptions();
            
            // Set headless mode based on TestConfig
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Firefox in HEADLESS mode");
                options.addArguments("--headless");
            }
            
            // Common options
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            
            // WebDriverManager setup
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver(options);
            
        } catch (Exception e) {
            System.out.println("❌ Error setting up Firefox driver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firefox driver", e);
        }
    }
    
    // ENHANCED SCREENSHOT METHOD WITH BETTER PATH HANDLING
    public static String takeScreenshot(String screenshotName) {
        try {
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            
            // Create daily screenshots directory
            String dailyScreenshotPath = getDailyScreenshotPath();
            File screenshotsDir = new File(dailyScreenshotPath);
            if (!screenshotsDir.exists()) {
                boolean created = screenshotsDir.mkdirs();
                if (created) {
                    System.out.println("Created screenshot directory: " + dailyScreenshotPath);
                }
            }
            
            // Take screenshot
            File screenshotFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(screenshotsDir, fileName);
            FileUtils.copyFile(screenshotFile, destinationFile);
            
            String absolutePath = destinationFile.getAbsolutePath();
            System.out.println("Screenshot saved: " + absolutePath);
            
            // Return the absolute path for Extent Reports
            return absolutePath;
            
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Method to get Base64 screenshot for direct embedding
    public static String getScreenshotAsBase64() {
        try {
            return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.out.println("Failed to get screenshot as Base64: " + e.getMessage());
            return null;
        }
    }
    
    // Method to take screenshot and return as Base64 for direct embedding in report
    public static String takeScreenshotAsBase64(String screenshotName) {
        try {
            String base64Screenshot = getScreenshotAsBase64();
            
            // Also save the physical file for backup
            takeScreenshot(screenshotName);
            
            return base64Screenshot;
        } catch (Exception e) {
            System.out.println("Failed to take Base64 screenshot: " + e.getMessage());
            return null;
        }
    }
    
    // Method to get daily screenshot folder path
    private static String getDailyScreenshotPath() {
        String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return TestConfig.SCREENSHOT_PATH + dateFolder + File.separator;
    }
    
    // Clean old screenshots (optional)
    private static void cleanOldScreenshots(String dailyScreenshotPath) {
        try {
            File folder = new File(dailyScreenshotPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] screenshotFiles = folder.listFiles((dir, name) -> name.endsWith(".png"));
                if (screenshotFiles != null && screenshotFiles.length > 10) {
                    Arrays.sort(screenshotFiles, Comparator.comparingLong(File::lastModified));
                    for (int i = 0; i < screenshotFiles.length - 10; i++) {
                        screenshotFiles[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error cleaning screenshots: " + e.getMessage());
        }
    }
    
    // Add this method to your BrowserUtils class
    public static void verifyCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        
        String currentUrl = getDriver().getCurrentUrl();
        if (!currentUrl.contains(expectedUrl)) {
            throw new AssertionError("Expected URL to contain: " + expectedUrl + 
                                   " but found: " + currentUrl);
        }
        System.out.println("Verified current URL: " + currentUrl);
    }
    
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Browser closed successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Error closing browser: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}