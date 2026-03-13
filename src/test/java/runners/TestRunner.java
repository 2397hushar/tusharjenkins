package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import utilities.ConfigReader;
import utilities.ExtentReportManager;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",
    glue = {"stepDefinitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json",
       // "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
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
    }
    
    @AfterSuite
    public void tearDownSuite() {
        System.out.println("=========================================");
        System.out.println("FINALIZING EXTENT REPORTS");
        System.out.println("=========================================");
        
        // Flush Extent Report
        ExtentReportManager.endTest();
        System.out.println("✅ Extent Report saved at: " + ExtentReportManager.getCurrentReportPath());
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}