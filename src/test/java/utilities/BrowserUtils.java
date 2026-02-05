package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.TestConfig;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

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
            String browser = ConfigReader.getBrowser().toLowerCase();
            
            switch (browser) {
                case "edge":
                    setupEdgeDriver();
                    break;
                case "chrome":
                default:
                    setupChromeDriver();
                    break;
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(
            	    Duration.ofSeconds(20)); // Increased to 20 seconds
        }
        return driver;
    }
    
    private static void setupChromeDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            
            // Incognito mode
            options.addArguments("--incognito");
            
            // Performance & Security
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            // Window settings
            options.addArguments("--start-maximized");
            options.addArguments("--disable-infobars");
            
            // Remove automation flags
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
            System.out.println("✅ Chrome browser launched in INCOGNITO mode with enhanced settings");
            
        } catch (Exception e) {
            // Fallback with basic options
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Tushar Here\\selenium\\workselenium\\driver\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
            System.out.println("✅ Chrome browser launched via local driver in INCOGNITO mode");
        }
    }
    private static void setupEdgeDriver() {
        try {
            io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            System.out.println("Edge browser launched via WebDriverManager");
        } catch (Exception e) {
            System.out.println("WebDriverManager failed, trying local Edge driver...");
            System.setProperty("webdriver.edge.driver", "C:\\Users\\tushar.sangale\\Downloads\\UpdatedFrameworkChanges-master (1)\\UpdatedFrameworkChanges-master\\workselenium\\driver\\msedgedriver.exe");
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(options);
            System.out.println("Edge browser launched via local driver");
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
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
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
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
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
            driver.quit();
            driver = null;
            System.out.println("Browser closed");
        }
    }
}