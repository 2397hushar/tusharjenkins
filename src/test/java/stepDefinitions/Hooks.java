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

            // Take screenshot on failure
            try {
                String screenshot = BrowserUtils.takeScreenshotAsBase64("Failure_" + scenario.getName());
                if (screenshot != null && !screenshot.isEmpty()) {
                    ExtentReportManager.addScreenshotBase64(
                        "Failure Screenshot - " + scenario.getName(),
                        screenshot
                    );
                }
            } catch (Exception e) {
                ExtentReportManager.warningStep("Failed to capture screenshot: " + e.getMessage());
            }

            ExtentReportManager.failTest("Test Case Failed: " + scenario.getName());
        } else {
            ExtentReportManager.passStep("✅ Scenario passed: " + scenario.getName());
            ExtentReportManager.passTest("Test Case Passed: " + scenario.getName());
        }

        // End the test in Extent Report
        ExtentReportManager.endTest();

        // Close browser
        BrowserUtils.quitDriver();
    }

    // ✅ IMPORTANT FIX: Flush report after execution
    @After(order = 100)
    public void flushReport() {
        ExtentReportManager.flushReport();
    
    }
}