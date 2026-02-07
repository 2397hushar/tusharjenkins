package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(
    features = "src/test/resources/features/Reportcard.feature",
    glue = {"stepDefinitions", "ERP_Page"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports-multiple.html",
        "json:target/cucumber-multiple.json"
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        System.out.println("Initializing TestRunner...");
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        return super.scenarios();
    }

    @Test
    public void runCucumberTests() {
        System.out.println("Starting Cucumber tests from TestRunner...");
    }
}