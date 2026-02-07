package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",  // Direct path to your feature file
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

    @BeforeClass
    public static void setup() {
        // Check if running in Jenkins environment
        boolean isJenkins = System.getenv("JENKINS_HOME") != null || 
                           System.getenv("BUILD_NUMBER") != null;
        
        System.out.println("=========================================");
        System.out.println("ENVIRONMENT CONFIGURATION");
        System.out.println("=========================================");
        System.out.println("Running in Jenkins: " + isJenkins);
        System.out.println("Build Number: " + System.getenv("BUILD_NUMBER"));
        System.out.println("Workspace: " + System.getenv("WORKSPACE"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        
        // Set headless mode for Jenkins
        if (isJenkins) {
            System.setProperty("headless", "false");
            System.out.println("✅ Headless mode enabled for Jenkins");
        }
        
        System.out.println("=========================================");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        System.out.println("Initializing TestRunner...");
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Feature file path: src/test/java/resources/Reportcard.feature");
        
        // Log environment info
        System.out.println("\n=== ENVIRONMENT INFO ===");
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("User Name: " + System.getProperty("user.name"));
        System.out.println("Browser: " + System.getProperty("browser", "chrome"));
        System.out.println("Headless Mode: " + System.getProperty("headless", "false"));
        
        return super.scenarios();
    }

    @Test
    public void runCucumberTests() {
        System.out.println("Starting Cucumber tests from TestRunner...");
    }
}