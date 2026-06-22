package utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrowserUtils {
    
    /**
     * Get driver (automatically selects web or mobile based on config)
     */
    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }
    
    /**
     * Take screenshot (works for both web and mobile)
     */
    public static String takeScreenshot(String screenshotName) {
        try {
            WebDriver driver = getDriver();
            if (driver == null) {
                System.out.println("⚠️ Cannot take screenshot - driver is null");
                return null;
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            
            String screenshotPath = ConfigReader.getScreenshotPath();
            File screenshotsDir = new File(screenshotPath);
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
    
    /**
     * Take screenshot as Base64 (for Extent Reports)
     */
    public static String takeScreenshotAsBase64(String screenshotName) {
        try {
            WebDriver driver = getDriver();
            if (driver == null) {
                System.out.println("⚠️ Cannot take Base64 screenshot - driver is null");
                return null;
            }
            
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            takeScreenshot(screenshotName);
            return base64;
        } catch (Exception e) {
            System.out.println("Failed to take Base64 screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Quit driver (works for both web and mobile)
     */
    public static void quitDriver() {
        DriverManager.quitDriver();
    }
    
    /**
     * Check if running on mobile platform
     */
    public static boolean isMobilePlatform() {
        return ConfigReader.isMobilePlatform();
    }
    
    /**
     * Check if running on web platform
     */
    public static boolean isWebPlatform() {
        return ConfigReader.isWebPlatform();
    }
}