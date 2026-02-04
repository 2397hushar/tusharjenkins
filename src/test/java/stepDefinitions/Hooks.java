//package stepDefinitions;
//
//import io.cucumber.java.After;
//import io.cucumber.java.AfterStep;
//import io.cucumber.java.Before;
//import io.cucumber.java.Scenario;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import utilities.BrowserUtils;
//import utilities.ConfigReader;
//import utilities.ExtentReportManager;
//
//public class Hooks {
//    
//    @Before
//    public void setUp(Scenario scenario) {
//        System.out.println("Starting scenario: " + scenario.getName());
//        ExtentReportManager.createTest(scenario.getName());
//    }
//    
//    @AfterStep
//    public void afterStep(Scenario scenario) {
//        try {
//            // Take screenshot as Base64 first (primary method)
//            String base64Screenshot = BrowserUtils.takeScreenshotAsBase64(scenario.getName() + "_Step");
//            
//            if (base64Screenshot != null) {
//                // Add to Extent Report using Base64 (no file path issues)
//                ExtentReportManager.addScreenshotBase64(base64Screenshot);
//            }
//            
//            // Also save physical file for backup
//            BrowserUtils.takeScreenshot(scenario.getName() + "_Step");
//            
//            // Log step status
//            if (scenario.isFailed()) {
//                ExtentReportManager.failStep("Step failed");
//                // Attach to Cucumber report
//                byte[] screenshot = ((TakesScreenshot) BrowserUtils.getDriver()).getScreenshotAs(OutputType.BYTES);
//                scenario.attach(screenshot, "image/png", "Failure Screenshot");
//            } else {
//                ExtentReportManager.passStep("Step passed");
//            }
//            
//        } catch (Exception e) {
//            System.out.println("Error in afterStep: " + e.getMessage());
//            ExtentReportManager.infoStep("Screenshot capture failed: " + e.getMessage());
//        }
//    }
//    
//    @After
//    public void tearDown(Scenario scenario) {
//        try {
//            if (scenario.isFailed()) {
//                ExtentReportManager.failTest(scenario.getName() + " - FAILED");
//                // Take final failure screenshot
//                String base64Screenshot = BrowserUtils.takeScreenshotAsBase64("FINAL_FAILURE_" + scenario.getName());
//                if (base64Screenshot != null) {
//                    ExtentReportManager.addScreenshotBase64("Final Failure State", base64Screenshot);
//                }
//            } else {
//                ExtentReportManager.passTest(scenario.getName() + " - PASSED");
//            }
//            
//            ExtentReportManager.endTest();
//            
//        } catch (Exception e) {
//            System.out.println("Error in tearDown: " + e.getMessage());
//        } finally {
//            BrowserUtils.quitDriver();
//        }
//    }
//}

package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ExtentReportManager;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        // Initialize test in Extent Report
        String scenarioName = scenario.getName();
        ExtentReportManager.createTest(scenarioName);
        
        // If using step definitions that need the scenario name
        LoginStepsReportCard steps = new LoginStepsReportCard();
        steps.setupTest(scenarioName);
    }

    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario failed
        if (scenario.isFailed()) {
            String screenshot = utilities.BrowserUtils.takeScreenshotAsBase64("FAILED_" + scenario.getName());
            if (screenshot != null) {
                ExtentReportManager.addScreenshotBase64("Failure Screenshot", screenshot);
            }
            ExtentReportManager.failTest("Test Case Failed: " + scenario.getName());
        }
        
        // Ensure report is flushed
        ExtentReportManager.endTest();
    }
}