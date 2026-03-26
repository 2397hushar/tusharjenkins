package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.BrowserUtils;
import utilities.ExtentReportManager;

public class Hooks {
    
    @Before(order = 1)
    public void setUp(Scenario scenario) {
        System.out.println("=========================================");
        System.out.println("STARTING SCENARIO: " + scenario.getName());
        System.out.println("=========================================");
        
        // Initialize Extent Report for this scenario
        ExtentReportManager.createTest(scenario.getName());
        ExtentReportManager.infoStep("🚀 Starting test execution for: " + scenario.getName());
        ExtentReportManager.infoStep("🔧 Scenario Tags: " + scenario.getSourceTagNames());
    }
    
    @After(order = 2)
    public void tearDown(Scenario scenario) {
        System.out.println("=========================================");
        System.out.println("COMPLETING SCENARIO: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("=========================================");
        
        // Log scenario status to Extent Report
        if (scenario.isFailed()) {
            ExtentReportManager.failStep("❌ Scenario failed: " + scenario.getName());
            ExtentReportManager.infoStep("Failure Reason: " + getFailureReason(scenario));
            
            // Take screenshot on failure
            try {
                String screenshot = BrowserUtils.takeScreenshotAsBase64("Failure_" + scenario.getName());
                if (screenshot != null && !screenshot.isEmpty()) {
                    ExtentReportManager.addScreenshotBase64("Failure Screenshot - " + scenario.getName(), screenshot);
                    ExtentReportManager.infoStep("📸 Failure screenshot captured");
                } else {
                    ExtentReportManager.warningStep("Could not capture screenshot - returned null or empty");
                }
            } catch (Exception e) {
                ExtentReportManager.warningStep("Failed to capture screenshot: " + e.getMessage());
                System.out.println("Failed to capture screenshot: " + e.getMessage());
            }
            
            ExtentReportManager.failTest("Test Case Failed: " + scenario.getName());
        } else {
            ExtentReportManager.passStep("✅ Scenario passed: " + scenario.getName());
            ExtentReportManager.passTest("Test Case Passed: " + scenario.getName());
        }
        
        // Close browser if still open
        BrowserUtils.quitDriver();
    }
    
    @After(order = 3)
    public void flushExtentReport() {
        // This ensures report is written at the end of all tests
        System.out.println("=========================================");
        System.out.println("FLUSHING EXTENT REPORT");
        System.out.println("=========================================");
        ExtentReportManager.flushReport();
        System.out.println("✅ Extent Report flush completed");
    }
    
    private String getFailureReason(Scenario scenario) {
        if (scenario.isFailed()) {
            Throwable error = getScenarioError(scenario);
            if (error != null) {
                return error.getMessage();
            }
            return "No error details available";
        }
        return "Not failed";
    }
    
    private Throwable getScenarioError(Scenario scenario) {
        try {
            // Try to get the error using reflection
            java.lang.reflect.Field field = scenario.getClass().getDeclaredField("error");
            field.setAccessible(true);
            return (Throwable) field.get(scenario);
        } catch (Exception e) {
            return null;
        }
    }
}