package runners;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import utilities.ExtentReportManager;
import com.aventstack.extentreports.Status;

public class ReportVerificationTest {
    
    @BeforeSuite
    public void setup() {
        ExtentReportManager.initializeReport();
    }
    
    @Test
    public void testReportGeneration() {
        ExtentReportManager.createTest("Report Verification Test");
        ExtentReportManager.infoStep("Starting test");
        ExtentReportManager.passStep("This step passed");
        ExtentReportManager.infoStep("Test completed");
        ExtentReportManager.passTest("Test executed successfully");
        ExtentReportManager.endTest();
    }
    
    @AfterSuite
    public void teardown() {
        ExtentReportManager.flushReport();
    }
}