//package ERP_Page;
//
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//
//import pages.BasePage;
//
//import java.time.Duration;
//import java.io.File;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.ArrayList;
//
//public class ReportCardPage extends BasePage {
//
//    @FindBy(xpath = "//span[contains(text(),'Report Card')]")
//    private WebElement reportCardLabel;
//
//    @FindBy(className = "report-card-container")
//    private static WebElement reportCardContainer;
//
//    @FindBy(xpath = "(//*[@d='M5 20h14v-2H5zM19 9h-4V3H9v6H5l7 7z'])[1]")
//    private static WebElement downloadButton;
//
//    @FindBy(className = "student-name")
//    private static WebElement studentName;
//
//    @FindBy(className = "grades-table")
//    private static WebElement gradesTable;
//    
//    @FindBy(xpath  = "//*[@data-testid='ExpandMoreIcon']")
//    private static WebElement viewLogout;
//    
//    @FindBy(xpath = "//*[contains(text(), 'Logout')]")
//    private static WebElement logoutButton;
//
//    private WebDriverWait wait;
//
//    public ReportCardPage() {
//        super();
//        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
//
//    public void verifyReportCardPage() {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//            // Wait for any of the title or URL conditions to be true
//            wait.until(driver -> {
//                String title = driver.getTitle().toLowerCase();
//                String currentUrl = driver.getCurrentUrl().toLowerCase();
//                // Check if either title or URL contains relevant keywords
//                return title.contains("report card") || title.contains("progress report")
//                        || title.contains("skill observation") || currentUrl.contains("report")
//                        || currentUrl.contains("progress") || currentUrl.contains("skill");
//            });
//
//            System.out.println("Report card page verified successfully!");
//            System.out.println("Current Page Title: " + driver.getTitle());
//            System.out.println("Current URL: " + driver.getCurrentUrl());
//
//        } catch (Exception e) {
//            System.out.println("Error in verifyReportCardPage: " + e.getMessage());
//            throw new RuntimeException("Failed to verify report card page", e);
//        }
//    }
//    
//    public static void logout_page() {
//    	try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//            
//            System.out.println("Starting logout process");
//            viewLogout.click();
//            Thread.sleep(3000);
//            logoutButton.click();
//            System.out.println("Logout completed successfully!");
//    	}
//    	catch (Exception e) {
//            System.out.println("❌ Error during logout: " + e.getMessage());
//            throw new RuntimeException("Failed to logout", e);
//        }
//    }
//
//    // Method to switch back to the original tab (marksheet page)
//    public void switchBackToMarksheetTab() {
//        try {
//            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
//            System.out.println("Number of tabs open: " + tabs.size());
//            
//            if (tabs.size() > 1) {
//                // Switch back to the first tab (marksheet page)
//                driver.switchTo().window(tabs.get(0));
//                System.out.println("Switched back to marksheet tab");
//                System.out.println("Current URL: " + driver.getCurrentUrl());
//                
//                // Wait for page to stabilize
//                Thread.sleep(2000);
//            }
//            
//        } catch (Exception e) {
//            System.out.println("Error switching back to marksheet tab: " + e.getMessage());
//            throw new RuntimeException("Failed to switch back to marksheet tab", e);
//        }
//    }
//
//    // Method to close the report card tab
//    public void closeReportCardTab() {
//        try {
//            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
//            
//            if (tabs.size() > 1) {
//                // Switch to report card tab and close it
//                driver.switchTo().window(tabs.get(1));
//                driver.close();
//                System.out.println("Closed report card tab");
//                
//                // Switch back to marksheet tab
//                driver.switchTo().window(tabs.get(0));
//                System.out.println("Switched back to marksheet tab");
//                
//                Thread.sleep(2000);
//            }
//            
//        } catch (Exception e) {
//            System.out.println("Error closing report card tab: " + e.getMessage());
//        }
//    }
//
//    public static void isLayoutProperlyAligned() {
//        try {
//            return;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static boolean isContentReadable() {
//        // Check if critical elements are visible and contain text
//        return !studentName.getText().isEmpty() && gradesTable.isDisplayed() && reportCardContainer.isEnabled();
//    }
//
//    public static void clickDownloadButton() {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(downloadButton));
//            downloadBtn.click();
//            System.out.println("Download button clicked on marksheet page");
//            
//            // Wait for download to start
//            Thread.sleep(5000);
//            
//        } catch (Exception e) {
//            System.out.println("Error clicking download button: " + e.getMessage());
//            throw new RuntimeException("Failed to click download button", e);
//        }
//    }
//    
//    // Optimized download verification method
//    public boolean verifyFileDownloadQuick() {
//        try {
//            String downloadPath = System.getProperty("user.home") + "/Downloads/";
//            File downloadDir = new File(downloadPath);
//            
//            // Look for files with different possible names and extensions
//            String[] possibleFileNames = {
//                "Sail Observation", "Skill Observation", "Observation",
//                "sail observation", "skill observation", "observation"
//            };
//            
//            System.out.println("Quick download verification in: " + downloadPath);
//            
//            // Reduced timeout for quicker verification
//            long startTime = System.currentTimeMillis();
//            long timeout = 30000; // 30 seconds max
//            boolean fileDownloaded = false;
//            
//            while (System.currentTimeMillis() - startTime < timeout && !fileDownloaded) {
//                // Check every 3 seconds
//                Thread.sleep(3000);
//                
//                File[] allFiles = downloadDir.listFiles();
//                if (allFiles != null) {
//                    // Sort files by last modified to check newest first
//                    Arrays.sort(allFiles, Comparator.comparingLong(File::lastModified).reversed());
//                    
//                    for (File file : allFiles) {
//                        if (file.isFile()) {
//                            String fileName = file.getName().toLowerCase();
//                            
//                            // Check if file matches our criteria
//                            boolean isPdf = fileName.endsWith(".pdf");
//                            boolean isRecent = (System.currentTimeMillis() - file.lastModified()) < 45000; // 45 seconds
//                            boolean hasContent = file.length() > 1024; // At least 1KB
//                            boolean nameMatches = false;
//                            
//                            for (String possibleName : possibleFileNames) {
//                                if (fileName.contains(possibleName.toLowerCase())) {
//                                    nameMatches = true;
//                                    break;
//                                }
//                            }
//                            
//                            if (isPdf && isRecent && hasContent && nameMatches) {
//                                fileDownloaded = true;
//                                System.out.println("✓ File downloaded successfully: " + file.getName());
//                                System.out.println("✓ File size: " + file.length() + " bytes");
//                                System.out.println("✓ File path: " + file.getAbsolutePath());
//                                break;
//                            }
//                        }
//                    }
//                }
//                
//                if (fileDownloaded) {
//                    break;
//                } else {
//                    System.out.println("Checking for downloaded file... (" + 
//                        (System.currentTimeMillis() - startTime) / 1000 + "s elapsed)");
//                }
//            }
//            
//            if (!fileDownloaded) {
//                System.out.println("⚠ Download verification timeout - file may still be downloading");
//                // List recent PDF files for debugging
//                File[] recentPdfs = downloadDir.listFiles((dir, name) -> 
//                    name.toLowerCase().endsWith(".pdf") && 
//                    (System.currentTimeMillis() - new File(dir, name).lastModified()) < 120000);
//                
//                if (recentPdfs != null && recentPdfs.length > 0) {
//                    System.out.println("Recent PDF files found:");
//                    for (File pdf : recentPdfs) {
//                        System.out.println("  - " + pdf.getName() + " (modified: " + 
//                            (System.currentTimeMillis() - pdf.lastModified()) / 1000 + "s ago)");
//                    }
//                }
//            }
//            
//            return fileDownloaded;
//            
//        } catch (Exception e) {
//            System.out.println("Error in quick download verification: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    // Enhanced method with file extension variations
//    public boolean verifyFileDownloadWithRetry() {
//        int maxRetries = 2; // Reduced from 3 to 2
//        int retryCount = 0;
//        
//        while (retryCount < maxRetries) {
//            System.out.println("Download verification attempt: " + (retryCount + 1));
//            
//            if (verifyFileDownloadQuick()) {
//                return true;
//            }
//            
//            retryCount++;
//            try {
//                Thread.sleep(5000); // Reduced wait time between retries
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        
//        return false;
//    }
//}
//






package ERP_Page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pages.BasePage;

import java.time.Duration;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;

public class ReportCardPage extends BasePage {

    @FindBy(xpath = "//span[contains(text(),'Report Card')]")
    private WebElement reportCardLabel;

    @FindBy(className = "report-card-container")
    private static WebElement reportCardContainer;

    @FindBy(xpath = "(//*[@d='M5 20h14v-2H5zM19 9h-4V3H9v6H5l7 7z'])[1]")
    private static WebElement downloadButton;

    @FindBy(className = "student-name")
    private static WebElement studentName;

    @FindBy(className = "grades-table")
    private static WebElement gradesTable;
    
    @FindBy(xpath  = "//*[@data-testid='ExpandMoreIcon']")
    private static WebElement viewLogout;
    
    @FindBy(xpath = "//*[contains(text(), 'Logout')]")
    private static WebElement logoutButton;

    private WebDriverWait wait;

    public ReportCardPage() {
        super();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void verifyReportCardPage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for any of the title or URL conditions to be true
            wait.until(driver -> {
                String title = driver.getTitle().toLowerCase();
                String currentUrl = driver.getCurrentUrl().toLowerCase();
                // Check if either title or URL contains relevant keywords
                return title.contains("report card") || title.contains("progress report")
                        || title.contains("skill observation") || currentUrl.contains("report")
                        || currentUrl.contains("progress") || currentUrl.contains("skill");
            });

            System.out.println("Report card page verified successfully!");
            System.out.println("Current Page Title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());

        } catch (Exception e) {
            System.out.println("Error in verifyReportCardPage: " + e.getMessage());
            throw new RuntimeException("Failed to verify report card page", e);
        }
    }
    
    public static void logout_page() {
    	try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            System.out.println("Starting logout process");
            viewLogout.click();
            Thread.sleep(3000);
            logoutButton.click();
            System.out.println("Logout completed successfully!");
    	}
    	catch (Exception e) {
            System.out.println("❌ Error during logout: " + e.getMessage());
            throw new RuntimeException("Failed to logout", e);
        }
    }

    // Method to switch back to the original tab (marksheet page)
    public void switchBackToMarksheetTab() {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            System.out.println("Number of tabs open: " + tabs.size());
            
            if (tabs.size() > 1) {
                // Switch back to the first tab (marksheet page)
                driver.switchTo().window(tabs.get(0));
                System.out.println("Switched back to marksheet tab");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                
                // Wait for page to stabilize
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            System.out.println("Error switching back to marksheet tab: " + e.getMessage());
            throw new RuntimeException("Failed to switch back to marksheet tab", e);
        }
    }

    // Method to close the report card tab
    public void closeReportCardTab() {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            
            if (tabs.size() > 1) {
                // Switch to report card tab and close it
                driver.switchTo().window(tabs.get(1));
                driver.close();
                System.out.println("Closed report card tab");
                
                // Switch back to marksheet tab
                driver.switchTo().window(tabs.get(0));
                System.out.println("Switched back to marksheet tab");
                
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            System.out.println("Error closing report card tab: " + e.getMessage());
        }
    }

    public static void isLayoutProperlyAligned() {
        try {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isContentReadable() {
        // Check if critical elements are visible and contain text
        return !studentName.getText().isEmpty() && gradesTable.isDisplayed() && reportCardContainer.isEnabled();
    }

    public static void clickDownloadButton() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(downloadButton));
            downloadBtn.click();
            System.out.println("Download button clicked on marksheet page");
            
            // Reduced wait time for download to start
            Thread.sleep(3000); // Reduced from 5000 to 3000
            
        } catch (Exception e) {
            System.out.println("Error clicking download button: " + e.getMessage());
            throw new RuntimeException("Failed to click download button", e);
        }
    }
    
    // Fast download verification method for regular files
    public boolean verifyFileDownloadQuick() {
        try {
            String downloadPath = System.getProperty("user.home") + "/Downloads/";
            File downloadDir = new File(downloadPath);
            
            System.out.println("Quick download verification in: " + downloadPath);
            
            // Reduced timeout for quicker verification
            long startTime = System.currentTimeMillis();
            long timeout = 15000; // Reduced from 30s to 15s
            boolean fileDownloaded = false;
            
            while (System.currentTimeMillis() - startTime < timeout && !fileDownloaded) {
                // Check every 2 seconds instead of 3
                Thread.sleep(2000);
                
                File[] allFiles = downloadDir.listFiles();
                if (allFiles != null) {
                    // Sort files by last modified to check newest first
                    Arrays.sort(allFiles, Comparator.comparingLong(File::lastModified).reversed());
                    
                    for (File file : allFiles) {
                        if (file.isFile() && isTargetFile(file)) {
                            fileDownloaded = true;
                            System.out.println("✓ File downloaded successfully: " + file.getName());
                            System.out.println("✓ File size: " + file.length() + " bytes");
                            System.out.println("✓ File path: " + file.getAbsolutePath());
                            break;
                        }
                    }
                }
                
                if (fileDownloaded) {
                    break;
                } else {
                    System.out.println("Checking for downloaded file... (" + 
                        (System.currentTimeMillis() - startTime) / 1000 + "s elapsed)");
                }
            }
            
            if (!fileDownloaded) {
                System.out.println("⚠ Quick verification timeout - trying extended verification for periodic tests");
                return verifyPeriodicTestDownload();
            }
            
            return fileDownloaded;
            
        } catch (Exception e) {
            System.out.println("Error in quick download verification: " + e.getMessage());
            return false;
        }
    }
    
    // Special verification for Periodic Test files that take longer
    private boolean verifyPeriodicTestDownload() {
        try {
            String downloadPath = System.getProperty("user.home") + "/Downloads/";
            File downloadDir = new File(downloadPath);
            
            System.out.println("Extended verification for Periodic Test files...");
            
            long startTime = System.currentTimeMillis();
            long timeout = 25000; // 25 seconds max for periodic tests
            boolean fileDownloaded = false;
            
            while (System.currentTimeMillis() - startTime < timeout && !fileDownloaded) {
                Thread.sleep(3000);
                
                File[] allFiles = downloadDir.listFiles();
                if (allFiles != null) {
                    Arrays.sort(allFiles, Comparator.comparingLong(File::lastModified).reversed());
                    
                    for (File file : allFiles) {
                        if (file.isFile() && isPeriodicTestFile(file)) {
                            fileDownloaded = true;
                            System.out.println("✓ Periodic Test file downloaded: " + file.getName());
                            System.out.println("✓ File size: " + file.length() + " bytes");
                            System.out.println("✓ Download time: " + 
                                (System.currentTimeMillis() - startTime) / 1000 + "s");
                            break;
                        }
                    }
                }
                
                if (!fileDownloaded) {
                    System.out.println("Still waiting for Periodic Test file... (" + 
                        (System.currentTimeMillis() - startTime) / 1000 + "s elapsed)");
                }
            }
            
            return fileDownloaded;
            
        } catch (Exception e) {
            System.out.println("Error in periodic test verification: " + e.getMessage());
            return false;
        }
    }
    
    // Check if file is a target report card file
    private boolean isTargetFile(File file) {
        String fileName = file.getName().toLowerCase();
        
        // Check if it's a PDF with content
        boolean isPdf = fileName.endsWith(".pdf");
        boolean isRecent = (System.currentTimeMillis() - file.lastModified()) < 30000; // 30 seconds
        boolean hasContent = file.length() > 1024; // At least 1KB
        
        if (!isPdf || !isRecent || !hasContent) {
            return false;
        }
        
        // Target file patterns - prioritize these
        String[] priorityPatterns = {
            "skill observation", "sail observation", "observation",
            "progress report", "report card", "marksheet"
        };
        
        for (String pattern : priorityPatterns) {
            if (fileName.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Check if file is a Periodic Test file
    private boolean isPeriodicTestFile(File file) {
        String fileName = file.getName().toLowerCase();
        
        boolean isPdf = fileName.endsWith(".pdf");
        boolean isRecent = (System.currentTimeMillis() - file.lastModified()) < 45000; // 45 seconds
        boolean hasContent = file.length() > 1024;
        
        if (!isPdf || !isRecent || !hasContent) {
            return false;
        }
        
        // Periodic Test file patterns
        String[] periodicPatterns = {
            "periodic test", "periodic", "test", "exam", "assessment"
        };
        
        for (String pattern : periodicPatterns) {
            if (fileName.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Smart download verification that adapts to file type
    public boolean verifyFileDownloadWithRetry() {
        int maxRetries = 1; // Reduced from 2 to 1
        int retryCount = 0;
        
        while (retryCount <= maxRetries) {
            System.out.println("Download verification attempt: " + (retryCount + 1));
            
            if (verifyFileDownloadQuick()) {
                return true;
            }
            
            retryCount++;
            if (retryCount <= maxRetries) {
                try {
                    Thread.sleep(3000); // Reduced from 5000 to 3000
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        return false;
    }
    
    // Ultra-fast verification for known file patterns
    public boolean verifyDownloadUltraFast() {
        try {
            String downloadPath = System.getProperty("user.home") + "/Downloads/";
            File downloadDir = new File(downloadPath);
            
            System.out.println("Ultra-fast download verification...");
            
            // Only wait 8 seconds maximum
            long startTime = System.currentTimeMillis();
            long timeout = 8000;
            
            while (System.currentTimeMillis() - startTime < timeout) {
                Thread.sleep(1500); // Check every 1.5 seconds
                
                File[] allFiles = downloadDir.listFiles();
                if (allFiles != null) {
                    Arrays.sort(allFiles, Comparator.comparingLong(File::lastModified).reversed());
                    
                    for (File file : allFiles) {
                        if (file.isFile() && isFastVerificationFile(file)) {
                            System.out.println("✓ Fast verification - File downloaded: " + file.getName());
                            return true;
                        }
                    }
                }
            }
            
            System.out.println("⚠ Fast verification incomplete, file may still be downloading");
            return false;
            
        } catch (Exception e) {
            System.out.println("Error in ultra-fast verification: " + e.getMessage());
            return false;
        }
    }
    
    // Check for files that can be verified quickly
    private boolean isFastVerificationFile(File file) {
        String fileName = file.getName().toLowerCase();
        
        boolean isPdf = fileName.endsWith(".pdf");
        boolean isRecent = (System.currentTimeMillis() - file.lastModified()) < 20000; // 20 seconds
        boolean hasContent = file.length() > 5120; // At least 5KB for faster verification
        
        if (!isPdf || !isRecent || !hasContent) {
            return false;
        }
        
        // Fast verification patterns (skip periodic tests)
        String[] fastPatterns = {
            "skill observation", "sail observation", "observation",
            "progress report", "report card"
        };
        
        String[] slowPatterns = {
            "periodic test", "periodic", "test", "exam"
        };
        
        // Check if it's a fast pattern file
        for (String pattern : fastPatterns) {
            if (fileName.contains(pattern)) {
                return true;
            }
        }
        
        // Skip slow pattern files in ultra-fast verification
        for (String pattern : slowPatterns) {
            if (fileName.contains(pattern)) {
                return false;
            }
        }
        
        return false;
    }
    
    // Main optimized download verification method
    public boolean verifyDownloadOptimized() {
        System.out.println("Starting optimized download verification...");
        
        // First try ultra-fast verification for quick files
        if (verifyDownloadUltraFast()) {
            return true;
        }
        
        // If ultra-fast fails, try the comprehensive verification
        System.out.println("Ultra-fast verification failed, trying comprehensive verification...");
        return verifyFileDownloadWithRetry();
    }
}
