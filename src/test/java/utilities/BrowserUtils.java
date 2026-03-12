package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import config.TestConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.time.Duration;

public class BrowserUtils {
    private static WebDriver driver;
    
    // Common driver paths - UPDATE THESE PATHS ACCORDING TO YOUR SYSTEM
    private static final String CHROME_DRIVER_PATH = "C:/drivers/chromedriver.exe";
    private static final String EDGE_DRIVER_PATH = "C:\\Users\\tushar.sangale\\Downloads\\UpdatedFrameworkChanges-master (1)\\UpdatedFrameworkChanges-master\\workselenium\\driver\\msedgedriver.exe";
    
    public static WebDriver getDriver() {
        if (driver == null) {
            // Read browser directly from ConfigReader
            String browser = ConfigReader.getBrowser().toLowerCase();
            
            System.out.println("=========================================");
            System.out.println("BROWSER INITIALIZATION");
            System.out.println("=========================================");
            System.out.println("Configured Browser: " + browser);
            System.out.println("Headless Mode: " + TestConfig.HEADLESS);
            System.out.println("=========================================");
            
            try {
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
                        System.out.println("⚠️ Unknown browser: " + browser + ". Defaulting to Chrome");
                        driver = setupChromeDriver();
                }
                
            } catch (Exception e) {
                System.out.println("❌ Failed to initialize " + browser + ": " + e.getMessage());
                
                // Try fallback to other browser if configured
                String fallbackBrowser = System.getProperty("fallback.browser", "chrome");
                System.out.println("⚠️ Attempting fallback to: " + fallbackBrowser);
                
                try {
                    if (fallbackBrowser.equalsIgnoreCase("chrome")) {
                        driver = setupChromeDriver();
                        System.out.println("✅ Successfully initialized Chrome as fallback");
                    } else if (fallbackBrowser.equalsIgnoreCase("edge")) {
                        driver = setupEdgeDriver();
                        System.out.println("✅ Successfully initialized Edge as fallback");
                    }
                } catch (Exception ex) {
                    System.out.println("❌ Fallback browser also failed: " + ex.getMessage());
                    System.out.println("⚠️ Please check your driver paths in BrowserUtils.java");
                    System.out.println("💡 Current Chrome driver path: " + CHROME_DRIVER_PATH);
                    System.out.println("💡 Current Edge driver path: " + EDGE_DRIVER_PATH);
                    throw new RuntimeException("Browser initialization failed - no browsers available", ex);
                }
            }
            
            if (driver != null) {
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                System.out.println("✅ Browser initialized successfully: " + browser);
                
                // Log driver path info
                if (browser.equals("chrome")) {
                    System.out.println("   Chrome Driver: " + System.getProperty("webdriver.chrome.driver", "Using WebDriverManager"));
                } else if (browser.equals("edge")) {
                    System.out.println("   Edge Driver: " + System.getProperty("webdriver.edge.driver", "Using WebDriverManager"));
                }
            }
        }
        return driver;
    }
    
    private static WebDriver setupChromeDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Chrome in HEADLESS mode");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
            }
            
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            
            // Try manual driver path first
            boolean manualDriverSuccess = tryManualChromeDriver(options);
            if (manualDriverSuccess) {
                return new ChromeDriver(options);
            }
            
            // Fallback to WebDriverManager
            System.out.println("⚠️ Manual Chrome driver failed, trying WebDriverManager...");
            try {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(options);
            } catch (Exception e) {
                System.out.println("❌ Chrome driver auto-download failed: " + e.getMessage());
                System.out.println("⚠️ Make sure you have Chrome installed");
                System.out.println("💡 Download Chrome driver manually from: https://chromedriver.chromium.org/downloads");
                System.out.println("💡 Place it at: " + CHROME_DRIVER_PATH);
                throw e;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Chrome driver setup failed: " + e.getMessage());
            throw e;
        }
    }
    
    private static boolean tryManualChromeDriver(ChromeOptions options) {
        try {
            File driverFile = new File(CHROME_DRIVER_PATH);
            if (driverFile.exists()) {
                System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
                System.out.println("✅ Using manually configured Chrome driver at: " + CHROME_DRIVER_PATH);
                return true;
            } else {
                System.out.println("⚠️ Manual Chrome driver not found at: " + CHROME_DRIVER_PATH);
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error checking manual Chrome driver: " + e.getMessage());
            return false;
        }
    }
    
    private static WebDriver setupEdgeDriver() {
        try {
            EdgeOptions options = new EdgeOptions();
            
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Edge in HEADLESS mode");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            }
            
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            
            // Try manual driver path first
            boolean manualDriverSuccess = tryManualEdgeDriver(options);
            if (manualDriverSuccess) {
                return new EdgeDriver(options);
            }
            
            // Fallback to WebDriverManager
            System.out.println("⚠️ Manual Edge driver failed, trying WebDriverManager...");
            try {
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(options);
            } catch (Exception e) {
                System.out.println("❌ Edge driver auto-download failed: " + e.getMessage());
                System.out.println("⚠️ Make sure you have internet access or manually download Edge driver");
                System.out.println("💡 Download Edge driver manually from: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/");
                System.out.println("💡 Place it at: " + EDGE_DRIVER_PATH);
                throw e;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Edge driver setup failed: " + e.getMessage());
            throw e;
        }
    }
    
    private static boolean tryManualEdgeDriver(EdgeOptions options) {
        try {
            File driverFile = new File(EDGE_DRIVER_PATH);
            if (driverFile.exists()) {
                System.setProperty("webdriver.edge.driver", EDGE_DRIVER_PATH);
                System.out.println("✅ Using manually configured Edge driver at: " + EDGE_DRIVER_PATH);
                return true;
            } else {
                System.out.println("⚠️ Manual Edge driver not found at: " + EDGE_DRIVER_PATH);
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error checking manual Edge driver: " + e.getMessage());
            return false;
        }
    }
    
    private static WebDriver setupFirefoxDriver() {
        try {
            FirefoxOptions options = new FirefoxOptions();
            
            if (TestConfig.HEADLESS) {
                System.out.println("🚀 Running Firefox in HEADLESS mode");
                options.addArguments("--headless");
            }
            
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            
            // Try manual Firefox driver path
            String firefoxDriverPath = "C:/drivers/geckodriver.exe";
            File driverFile = new File(firefoxDriverPath);
            if (driverFile.exists()) {
                System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
                System.out.println("✅ Using manually configured Firefox driver at: " + firefoxDriverPath);
            } else {
                System.out.println("⚠️ Manual Firefox driver not found, trying WebDriverManager...");
                WebDriverManager.firefoxdriver().setup();
            }
            
            return new FirefoxDriver(options);
            
        } catch (Exception e) {
            System.out.println("❌ Firefox driver setup failed: " + e.getMessage());
            throw e;
        }
    }
    
    // Method to set custom Chrome driver path
    public static void setChromeDriverPath(String path) {
        System.setProperty("webdriver.chrome.driver", path);
        System.out.println("✅ Chrome driver path set to: " + path);
    }
    
    // Method to set custom Edge driver path
    public static void setEdgeDriverPath(String path) {
        System.setProperty("webdriver.edge.driver", path);
        System.out.println("✅ Edge driver path set to: " + path);
    }
    
    // Screenshot methods
    public static String takeScreenshot(String screenshotName) {
        try {
            if (driver == null) {
                System.out.println("⚠️ Cannot take screenshot - driver is null");
                return null;
            }
            
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            
            String dailyScreenshotPath = getDailyScreenshotPath();
            File screenshotsDir = new File(dailyScreenshotPath);
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }
            
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(screenshotsDir, fileName);
            FileUtils.copyFile(screenshotFile, destinationFile);
            
            System.out.println("📸 Screenshot saved: " + destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
            
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public static String takeScreenshotAsBase64(String screenshotName) {
        try {
            if (driver == null) {
                System.out.println("⚠️ Cannot take Base64 screenshot - driver is null");
                return null;
            }
            
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            takeScreenshot(screenshotName); // Also save physical copy
            return base64;
        } catch (Exception e) {
            System.out.println("Failed to take Base64 screenshot: " + e.getMessage());
            return null;
        }
    }
    
    private static String getDailyScreenshotPath() {
        String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return ConfigReader.getScreenshotPath() + dateFolder + File.separator;
    }
    
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Browser closed");
            } catch (Exception e) {
                System.out.println("⚠️ Error closing browser: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
    
    // Utility method to check if drivers exist
    public static void checkDriverPaths() {
        System.out.println("=========================================");
        System.out.println("DRIVER PATH VERIFICATION");
        System.out.println("=========================================");
        
        File chromeDriver = new File(CHROME_DRIVER_PATH);
        System.out.println("Chrome Driver (" + CHROME_DRIVER_PATH + "): " + 
            (chromeDriver.exists() ? "✅ Found" : "❌ Not Found"));
        
        File edgeDriver = new File(EDGE_DRIVER_PATH);
        System.out.println("Edge Driver (" + EDGE_DRIVER_PATH + "): " + 
            (edgeDriver.exists() ? "✅ Found" : "❌ Not Found"));
        
        System.out.println("=========================================");
    }
}