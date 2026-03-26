package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import utilities.ExtentReportManager;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",
    glue = {"stepDefinitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json"
        // REMOVED: "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        // We're using custom ExtentReportManager instead
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void setupSuite() {
        System.out.println("=========================================");
        System.out.println("EXTENT REPORTS INITIALIZATION");
        System.out.println("=========================================");
        
        // Initialize Extent Report
        ExtentReportManager.initializeReport();
        System.out.println("✅ Extent Report initialized successfully");
        System.out.println("📊 Report will be saved in: target/extent-reports/");
    }
    
    @AfterSuite
    public void tearDownSuite() {
        System.out.println("=========================================");
        System.out.println("FINALIZING EXTENT REPORTS");
        System.out.println("=========================================");
        
        // Flush Extent Report - this is critical
        ExtentReportManager.flushReport();
        
        String reportPath = ExtentReportManager.getCurrentReportPath();
        if (reportPath != null && !reportPath.isEmpty()) {
            System.out.println("✅ Extent Report saved at: " + reportPath);
            System.out.println("📊 To view the report, open this file in a browser");
        } else {
            System.out.println("⚠️ Report path is null - report may not have been generated");
        }
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}