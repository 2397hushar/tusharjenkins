package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import utilities.ExtentReportManager;

import java.io.File;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",
    glue = {"stepDefinitions"},  // Make sure this matches your step definitions package
    plugin = {
        "pretty",
        "html:target/cucumber-reports-multiple.html",
        "json:target/cucumber-multiple.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"  // Add this if using extent adapter
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public static void setup() {
        System.out.println("=========================================");
        System.out.println("TEST RUNNER INITIALIZATION");
        System.out.println("=========================================");
        
        // Clean up old inconsistent report folders
        cleanupOldInconsistentFolders();
        
        // Initialize Extent Report
        try {
            ExtentReportManager.initializeReport();
            System.out.println("✅ Extent Report Manager initialized");
        } catch (Exception e) {
            System.out.println("❌ Failed to initialize Extent Report: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Check if running in Jenkins environment
        boolean isJenkins = System.getenv("JENKINS_HOME") != null || 
                           System.getenv("BUILD_NUMBER") != null;
        
        System.out.println("Running in Jenkins: " + isJenkins);
        System.out.println("Build Number: " + System.getenv("BUILD_NUMBER"));
        System.out.println("Workspace: " + System.getenv("WORKSPACE"));
        
        // Set headless mode for Jenkins
        if (isJenkins) {
            System.setProperty("headless", "true");
            System.out.println("✅ Headless mode enabled for Jenkins");
        }
        
        // Verify driver paths
        utilities.BrowserUtils.checkDriverPaths();
        
        System.out.println("=========================================");
    }
    
    @AfterClass
    public static void teardown() {
        System.out.println("=========================================");
        System.out.println("TEST RUNNER COMPLETED");
        System.out.println("=========================================");
        
        // Ensure Extent Report is flushed
        ExtentReportManager.endTest();
        System.out.println("✅ Extent Report saved to: " + ExtentReportManager.getCurrentReportPath());
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        System.out.println("Initializing TestRunner...");
        System.out.println("Feature file path: src/test/java/resources/Reportcard.feature");
        
        // Log environment info
        System.out.println("\n=== ENVIRONMENT INFO ===");
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Browser: " + System.getProperty("browser", "chrome"));
        System.out.println("Headless Mode: " + System.getProperty("headless", "false"));
        
        return super.scenarios();
    }
    
    // NEW: Method to clean up old inconsistent folders
    private static void cleanupOldInconsistentFolders() {
        try {
            String basePath = System.getProperty("user.dir") + "/target/";
            File targetDir = new File(basePath);
            
            if (targetDir.exists() && targetDir.isDirectory()) {
                // Look for folders starting with "ExtentReport" but not "extent-reports"
                File[] oldFolders = targetDir.listFiles((dir, name) -> 
                    name.startsWith("ExtentReport") && !name.equals("extent-reports"));
                
                if (oldFolders != null && oldFolders.length > 0) {
                    System.out.println("Found " + oldFolders.length + " old inconsistent folder(s):");
                    for (File folder : oldFolders) {
                        System.out.println("  - " + folder.getName());
                    }
                    
                    // Delete each old folder
                    for (File folder : oldFolders) {
                        if (folder.isDirectory()) {
                            deleteDirectory(folder);
                            System.out.println("✅ Deleted old inconsistent folder: " + folder.getName());
                        }
                    }
                } else {
                    System.out.println("No old inconsistent folders found");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error cleaning old folders: " + e.getMessage());
        }
    }
    
    // Helper method to delete directory recursively
    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.out.println("⚠️ Could not delete file: " + file.getName());
                    }
                }
            }
        }
        boolean deleted = directory.delete();
        if (!deleted) {
            System.out.println("⚠️ Could not delete directory: " + directory.getName());
        }
    }
}