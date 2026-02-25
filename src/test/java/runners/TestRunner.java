package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import utilities.AllureReportManager;
import utilities.ExtentReportManager;

import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",
    glue = {"stepDefinitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports-multiple.html",
        "json:target/cucumber-multiple.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "utilities.AllureCucumberAdapter"
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public static void setupSuite() {
        System.out.println("=========================================");
        System.out.println("TEST SUITE INITIALIZATION");
        System.out.println("=========================================");
        
        // Clean old results
        cleanAllureResults();
        
        // Initialize reports
        try {
            ExtentReportManager.initializeReport();
            System.out.println("✅ Extent Report Manager initialized");
        } catch (Exception e) {
            System.out.println("❌ Failed to initialize Extent Report: " + e.getMessage());
        }
        
        // Create Allure environment properties
        try {
            AllureReportManager.createEnvironmentProperties();
            System.out.println("✅ Allure environment properties created");
        } catch (Exception e) {
            System.out.println("❌ Failed to create Allure properties: " + e.getMessage());
        }
        
        System.out.println("=========================================");
    }
    
    @AfterSuite
    public static void tearDownSuite() {
        System.out.println("=========================================");
        System.out.println("TEST SUITE COMPLETED");
        System.out.println("=========================================");
        
        // Display results summary
        displayResultsSummary();
    }
    
    private static void cleanAllureResults() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            if (Files.exists(resultsPath)) {
                Files.walk(resultsPath)
                    .sorted((p1, p2) -> p2.compareTo(p1)) // Reverse order for deletion
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
                System.out.println("✅ Cleaned old Allure results");
            }
            Files.createDirectories(resultsPath);
        } catch (Exception e) {
            System.out.println("⚠️ Error cleaning Allure results: " + e.getMessage());
        }
    }
    
    private static void displayResultsSummary() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            if (Files.exists(resultsPath)) {
                // Count total result files
                long count = Files.walk(resultsPath)
                    .filter(p -> p.toString().endsWith("-result.json"))
                    .count();
                System.out.println("📊 Allure result files generated: " + count);
                
                // Count by status (Java 8 compatible)
                long passed = 0;
                long failed = 0;
                
                // Use try-with-resources to ensure stream is closed
                try (java.util.stream.Stream<Path> stream = Files.walk(resultsPath)) {
                    List<Path> files = stream
                        .filter(p -> p.toString().endsWith("-result.json"))
                        .collect(Collectors.toList());
                    
                    for (Path file : files) {
                        String content = new String(Files.readAllBytes(file));
                        if (content.contains("\"status\":\"passed\"")) {
                            passed++;
                        } else if (content.contains("\"status\":\"failed\"") || 
                                   content.contains("\"status\":\"broken\"")) {
                            failed++;
                        }
                    }
                }
                
                System.out.println("   ✅ Passed: " + passed);
                System.out.println("   ❌ Failed: " + failed);
                System.out.println("   📁 Location: target/allure-results");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error displaying summary: " + e.getMessage());
        }
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}