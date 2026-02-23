package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.BrowserUtils;
import utilities.ExtentReportManager;

public class Hooks {
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("=========================================");
        System.out.println("STARTING SCENARIO: " + scenario.getName());
        System.out.println("=========================================");
        
        // Initialize Extent Report for this scenario
        ExtentReportManager.createTest(scenario.getName());
        ExtentReportManager.infoStep("Starting test execution for: " + scenario.getName());
    }
    
    @After
    public void tearDown(Scenario scenario) {
        System.out.println("=========================================");
        System.out.println("COMPLETING SCENARIO: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("=========================================");
        
        // Log scenario status to Extent Report
        if (scenario.isFailed()) {
            ExtentReportManager.failStep("Scenario failed: " + scenario.getName());
            
            // Take screenshot on failure
            try {
                String screenshot = BrowserUtils.takeScreenshotAsBase64("Failure_" + scenario.getName());
                if (screenshot != null) {
                    ExtentReportManager.addScreenshotBase64("Failure Screenshot", screenshot);
                }
            } catch (Exception e) {
                System.out.println("Failed to capture screenshot: " + e.getMessage());
            }
            
            ExtentReportManager.failTest("Test Case Failed: " + scenario.getName());
        } else {
            ExtentReportManager.passStep("Scenario passed: " + scenario.getName());
            ExtentReportManager.passTest("Test Case Passed: " + scenario.getName());
        }
        
        // Close browser if still open
        BrowserUtils.quitDriver();
    }
    
    @After
    public void flushExtentReport() {
        // This ensures report is written at the end of all tests
        ExtentReportManager.endTest();
    }
}