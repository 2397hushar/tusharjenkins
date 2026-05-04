package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import utilities.ExtentReportManager;

@CucumberOptions(
    features = "src/test/resources/feature",
    glue = {"stepDefinitions", "utilities"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json"
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void setupSuite() {
        System.out.println("=========================================");
        System.out.println("@BeforeSuite - Initializing Extent Report");
        System.out.println("=========================================");
        ExtentReportManager.initializeReport();
    }
    
    @AfterSuite
    public void tearDownSuite() {
        System.out.println("=========================================");
        System.out.println("@AfterSuite - Flushing Extent Report");
        System.out.println("=========================================");
        ExtentReportManager.flushReport();
        
        String reportPath = ExtentReportManager.getCurrentReportPath();
        if (reportPath != null) {
            System.out.println("📊 Extent Report: " + reportPath);
        }
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
