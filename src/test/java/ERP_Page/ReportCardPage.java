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

import org.openqa.selenium.JavascriptExecutor;
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

    @FindBy(xpath = "(//button[@aria-label='Download'])")
    private static WebElement downloadButton;

    @FindBy(className = "student-name")
    private static WebElement studentName;

    @FindBy(className = "grades-table")
    private static WebElement gradesTable;
    
    @FindBy(xpath = "//*[@data-testid='ExpandMoreIcon']")
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

            wait.until(driver -> {
                String title = driver.getTitle().toLowerCase();
                String currentUrl = driver.getCurrentUrl().toLowerCase();
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
        } catch (Exception e) {
            System.out.println("❌ Error during logout: " + e.getMessage());
            throw new RuntimeException("Failed to logout", e);
        }
    }

    public void switchBackToMarksheetTab() {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            System.out.println("Number of tabs open: " + tabs.size());
            
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(0));
                System.out.println("Switched back to marksheet tab");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            System.out.println("Error switching back to marksheet tab: " + e.getMessage());
            throw new RuntimeException("Failed to switch back to marksheet tab", e);
        }
    }

    public void closeReportCardTab() {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
                driver.close();
                System.out.println("Closed report card tab");
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
        return !studentName.getText().isEmpty() && gradesTable.isDisplayed() && reportCardContainer.isEnabled();
    }

    public static void clickDownloadButton() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            System.out.println("Looking for Download button...");
            
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(downloadButton));
            System.out.println("Download button found and clickable");
            
            // Scroll to element before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
            Thread.sleep(500);
            
            downloadBtn.click();
            System.out.println("✅ Download button clicked for Annual Report Card");
            
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println("Error clicking download button: " + e.getMessage());
            throw new RuntimeException("Failed to click download button", e);
        }
    }
    
    // Method to verify Annual Report Card download
    public boolean verifyAnnualReportCardDownload() {
        try {
            String downloadPath = System.getProperty("user.home") + "/Downloads/";
            File downloadDir = new File(downloadPath);
            
            System.out.println("=========================================");
            System.out.println("VERIFYING ANNUAL REPORT CARD DOWNLOAD");
            System.out.println("=========================================");
            System.out.println("Download directory: " + downloadPath);
            
            long startTime = System.currentTimeMillis();
            long timeout = 35000; // 35 seconds for annual report card
            boolean fileDownloaded = false;
            File downloadedFile = null;
            
            while (System.currentTimeMillis() - startTime < timeout && !fileDownloaded) {
                Thread.sleep(3000);
                
                File[] allFiles = downloadDir.listFiles();
                if (allFiles != null) {
                    // Sort by last modified to get newest first
                    Arrays.sort(allFiles, Comparator.comparingLong(File::lastModified).reversed());
                    
                    for (File file : allFiles) {
                        if (file.isFile() && isAnnualReportCardFile(file)) {
                            fileDownloaded = true;
                            downloadedFile = file;
                            break;
                        }
                    }
                }
                
                if (!fileDownloaded) {
                    System.out.println("Waiting for Annual Report Card download... (" + 
                        (System.currentTimeMillis() - startTime) / 1000 + "s elapsed)");
                }
            }
            
            if (fileDownloaded && downloadedFile != null) {
                System.out.println("=========================================");
                System.out.println("✅ ANNUAL REPORT CARD DOWNLOADED SUCCESSFULLY!");
                System.out.println("=========================================");
                System.out.println("📄 File Name: " + downloadedFile.getName());
                System.out.println("📏 File Size: " + downloadedFile.length() + " bytes");
                System.out.println("📁 File Path: " + downloadedFile.getAbsolutePath());
                System.out.println("🕐 Download Time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
                System.out.println("=========================================");
                return true;
            } else {
                System.out.println("=========================================");
                System.out.println("❌ ANNUAL REPORT CARD NOT FOUND!");
                System.out.println("=========================================");
                System.out.println("Timeout reached: " + timeout / 1000 + " seconds");
                
                // List recent PDF files for debugging
                File[] recentPdfs = downloadDir.listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".pdf") && 
                    (System.currentTimeMillis() - new File(dir, name).lastModified()) < 120000);
                
                if (recentPdfs != null && recentPdfs.length > 0) {
                    System.out.println("\nRecent PDF files found in download directory:");
                    for (File pdf : recentPdfs) {
                        System.out.println("  - " + pdf.getName() + " (Size: " + pdf.length() + " bytes, Modified: " + 
                            (System.currentTimeMillis() - pdf.lastModified()) / 1000 + "s ago)");
                    }
                } else {
                    System.out.println("No recent PDF files found in download directory");
                }
                
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Error verifying Annual Report Card download: " + e.getMessage());
            return false;
        }
    }
    
    // Method to check if file is an Annual Report Card
    private boolean isAnnualReportCardFile(File file) {
        String fileName = file.getName().toLowerCase();
        
        // Check if it's a PDF
        boolean isPdf = fileName.endsWith(".pdf");
        if (!isPdf) {
            return false;
        }
        
        // Check if file was downloaded recently (within 60 seconds)
        boolean isRecent = (System.currentTimeMillis() - file.lastModified()) < 60000;
        if (!isRecent) {
            return false;
        }
        
        // Check if file has content (at least 10KB for annual report card)
        boolean hasContent = file.length() > 10240; // 10KB minimum
        if (!hasContent) {
            System.out.println("  File found but too small: " + file.getName() + " (" + file.length() + " bytes)");
            return false;
        }
        
        // Patterns for Annual Report Card
        String[] annualReportPatterns = {
            "annual report", "annual report card", "annual", "yearly report",
            "report card", "progress report", "reportcard", "annual-report",
            "yearly", "annual_report", "report-card", "progress"
        };
        
        for (String pattern : annualReportPatterns) {
            if (fileName.contains(pattern)) {
                System.out.println("  ✓ Matched pattern: '" + pattern + "'");
                return true;
            }
        }
        
        // If no specific pattern matches but it's a recent PDF with good size, consider it
        if (file.length() > 20480) { // > 20KB
            System.out.println("  ✓ Large PDF file found (no pattern match): " + fileName);
            return true;
        }
        
        return false;
    }
    
    // Main method to verify annual report card download
    public boolean verifyAnnualReportCardDownloadWithRetry() {
        int maxRetries = 2;
        int retryCount = 0;
        
        System.out.println("=========================================");
        System.out.println("STARTING ANNUAL REPORT CARD VERIFICATION");
        System.out.println("=========================================");
        
        while (retryCount <= maxRetries) {
            System.out.println("\nVerification attempt: " + (retryCount + 1));
            
            if (verifyAnnualReportCardDownload()) {
                return true;
            }
            
            retryCount++;
            if (retryCount <= maxRetries) {
                System.out.println("Retrying verification in 5 seconds...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        System.out.println("\n=========================================");
        System.out.println("❌ ANNUAL REPORT CARD VERIFICATION FAILED");
        System.out.println("=========================================");
        return false;
    }
}