package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ExtentReportManager;
import utilities.BrowserUtils;

public class Hooks {

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        // Initialize Extent Report
        ExtentReportManager.initializeReport();
        
        // Create test in Extent Report
        String scenarioName = scenario.getName();
        ExtentReportManager.createTest(scenarioName);
        
        System.out.println("=========================================");
        System.out.println("🚀 Starting Scenario: " + scenarioName);
        System.out.println("=========================================");
        ExtentReportManager.infoStep("Test Started: " + scenarioName);
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        try {
            // Log step status
            if (scenario.isFailed()) {
                ExtentReportManager.failStep("Step failed at: " + scenario.getLine());
                
                // Take and attach screenshot
                String screenshot = BrowserUtils.takeScreenshotAsBase64("FAILED_" + scenario.getName());
                if (screenshot != null) {
                    ExtentReportManager.addScreenshotBase64("Failure Screenshot", screenshot);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in afterStep: " + e.getMessage());
        }
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            String scenarioName = scenario.getName();
            
            // Log final status
            if (scenario.isFailed()) {
                ExtentReportManager.failTest("Test Case Failed: " + scenarioName);
                System.out.println("❌ Scenario FAILED: " + scenarioName);
            } else {
                ExtentReportManager.passTest("Test Case Passed: " + scenarioName);
                System.out.println("✅ Scenario PASSED: " + scenarioName);
            }
            
            // Take final screenshot if failed
            if (scenario.isFailed()) {
                String finalScreenshot = BrowserUtils.takeScreenshotAsBase64("FINAL_" + scenario.getName());
                if (finalScreenshot != null) {
                    ExtentReportManager.addScreenshotBase64("Final State", finalScreenshot);
                }
            }
            
            System.out.println("=========================================");
            
        } catch (Exception e) {
            System.out.println("Error in tearDown: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close browser
            BrowserUtils.quitDriver();
        }
    }

    @After(order = 1)
    public void finalizeReport() {
        try {
            // Ensure report is flushed
            ExtentReportManager.endTest();
            System.out.println("📁 Report saved successfully");
        } catch (Exception e) {
            System.out.println("Error finalizing report: " + e.getMessage());
        }
    }
}