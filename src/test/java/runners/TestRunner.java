package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import utilities.ConfigReader;
import utilities.ExtentReportManager;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CucumberOptions(
    features = "src/test/java/resources/Reportcard.feature",
    glue = {"stepDefinitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports-multiple.html",
        "json:target/cucumber-multiple.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    tags = "@Regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void setupSuite() {
        System.out.println("=========================================");
        System.out.println("ALLURE REPORT INITIALIZATION");
        System.out.println("=========================================");
        
        cleanupAllureResults();
        createAllureEnvironmentProperties();
        createAllureExecutorJson();
        createAllureCategoriesJson();
    }
    
    private void cleanupAllureResults() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            if (Files.exists(resultsPath)) {
                Files.walk(resultsPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
                System.out.println("✅ Cleaned old Allure results");
            }
            Files.createDirectories(resultsPath);
        } catch (Exception e) {
            System.out.println("⚠️ Cleanup error: " + e.getMessage());
        }
    }
    
    private void createAllureEnvironmentProperties() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            Files.createDirectories(resultsPath);
            
            Properties props = new Properties();
            
            // Add all environment variables for the report
            props.setProperty("Browser", ConfigReader.getBrowser().toUpperCase());
            props.setProperty("Browser.Version", getBrowserVersion());
            props.setProperty("URL", ConfigReader.getUrl());
            props.setProperty("OS", System.getProperty("os.name"));
            props.setProperty("OS.Version", System.getProperty("os.version"));
            props.setProperty("Java.Version", System.getProperty("java.version"));
            props.setProperty("Java.Vendor", System.getProperty("java.vendor"));
            props.setProperty("User", System.getProperty("user.name"));
            props.setProperty("Headless", String.valueOf(ConfigReader.isHeadlesss()));
            props.setProperty("Test.Environment", "Pre-Production");
            
            // Add timestamp
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            props.setProperty("Execution.Time", dtf.format(now));
            props.setProperty("Execution.Date", now.toLocalDate().toString());
            
            // Add Jenkins info if available
            if (System.getenv("BUILD_NUMBER") != null) {
                props.setProperty("Jenkins.Build", System.getenv("BUILD_NUMBER"));
                props.setProperty("Jenkins.Job", System.getenv("JOB_NAME"));
                props.setProperty("Jenkins.URL", System.getenv("JENKINS_URL"));
            }
            
            // Add suite info
            props.setProperty("Test.Suite", "ERP Test Suite");
            props.setProperty("Test.Type", "Regression");
            
            try (FileOutputStream out = new FileOutputStream(
                    resultsPath.resolve("environment.properties").toFile())) {
                props.store(out, "Allure Environment Variables");
            }
            
            // Also create a more readable format
            createEnvironmentTxt(resultsPath);
            
            System.out.println("✅ Created environment.properties for Allure");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }
    
    private void createEnvironmentTxt(Path resultsPath) throws IOException {
        StringBuilder env = new StringBuilder();
        env.append("Environment Information\n");
        env.append("======================\n\n");
        env.append("Browser: ").append(ConfigReader.getBrowser().toUpperCase()).append("\n");
        env.append("OS: ").append(System.getProperty("os.name")).append("\n");
        env.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        env.append("URL: ").append(ConfigReader.getUrl()).append("\n");
        env.append("Headless: ").append(ConfigReader.isHeadlesss()).append("\n");
        
        Files.write(resultsPath.resolve("environment.txt"), env.toString().getBytes());
    }
    
    private void createAllureExecutorJson() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            Files.createDirectories(resultsPath);
            
            String executorJson = String.format("""
                {
                  "name": "Jenkins",
                  "type": "jenkins",
                  "url": "http://localhost:8080/job/ERP-Tests/",
                  "buildOrder": %s,
                  "buildName": "ERP Automation #%s",
                  "buildUrl": "http://localhost:8080/job/ERP-Tests/%s/",
                  "reportName": "Allure Report",
                  "reportUrl": "http://localhost:8080/job/ERP-Tests/%s/allure/"
                }""",
                getBuildNumber(),
                getBuildNumber(),
                getBuildNumber(),
                getBuildNumber()
            );
            
            Files.write(resultsPath.resolve("executor.json"), executorJson.getBytes());
            System.out.println("✅ Created executor.json for Allure");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error creating executor.json: " + e.getMessage());
        }
    }
    
    private void createAllureCategoriesJson() {
        try {
            Path resultsPath = Paths.get("target/allure-results");
            Files.createDirectories(resultsPath);
            
            String categoriesJson = """
                [
                  {
                    "name": "Passed Tests",
                    "matchedStatuses": ["passed"]
                  },
                  {
                    "name": "Failed Tests",
                    "matchedStatuses": ["failed"]
                  },
                  {
                    "name": "Broken Tests",
                    "matchedStatuses": ["broken"]
                  },
                  {
                    "name": "Skipped Tests",
                    "matchedStatuses": ["skipped"]
                  },
                  {
                    "name": "Test Defects",
                    "matchedStatuses": ["failed"],
                    "messageRegex": ".*AssertionError.*"
                  },
                  {
                    "name": "Environment Issues",
                    "matchedStatuses": ["broken"],
                    "messageRegex": ".*WebDriverException.*|.*TimeoutException.*"
                  }
                ]""";
            
            Files.write(resultsPath.resolve("categories.json"), categoriesJson.getBytes());
            System.out.println("✅ Created categories.json for Allure");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error creating categories.json: " + e.getMessage());
        }
    }
    
    @AfterSuite
    public void tearDownSuite() {
        System.out.println("=========================================");
        System.out.println("GENERATING ALLURE REPORT");
        System.out.println("=========================================");
        
        generateAllureReport();
    }
    
    private void generateAllureReport() {
        try {
            System.out.println("📊 Generating Allure report...");
            
            // Check if allure-results has files
            Path resultsPath = Paths.get("target/allure-results");
            if (Files.exists(resultsPath)) {
                long fileCount = Files.walk(resultsPath)
                    .filter(Files::isRegularFile)
                    .count();
                System.out.println("📁 Found " + fileCount + " result files");
            }
            
            // Generate report
            ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", 
                "allure generate target/allure-results --clean -o target/allure-report"
            );
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("✅ Allure report generated successfully!");
                System.out.println("📁 Report location: target/allure-report/index.html");
                
                // Print report summary
                printReportSummary();
                
                // Open report (only if not in Jenkins)
                if (System.getenv("JENKINS_HOME") == null) {
                    openAllureReport();
                }
            } else {
                System.out.println("❌ Report generation failed");
                // Try maven fallback
                generateMavenAllureReport();
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void generateMavenAllureReport() {
        try {
            System.out.println("📦 Trying Maven Allure plugin...");
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "mvn allure:report");
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("✅ Allure report generated via Maven!");
                System.out.println("📁 Report location: target/site/allure-maven-plugin/index.html");
            }
        } catch (Exception e) {
            System.out.println("❌ Maven Allure failed: " + e.getMessage());
        }
    }
    
    private void printReportSummary() {
        System.out.println("\n📊 REPORT SUMMARY");
        System.out.println("========================================");
        System.out.println("• SUITES: Test suites executed");
        System.out.println("• TREND: Execution history");
        System.out.println("• CATEGORIES: Test categories");
        System.out.println("• ENVIRONMENT: Test environment details");
        System.out.println("• FEATURES BY STORIES: Feature breakdown");
        System.out.println("• EXECUTORS: Build information");
        System.out.println("========================================\n");
    }
    
    private void openAllureReport() {
        try {
            File reportFile = new File("target/allure-report/index.html");
            if (reportFile.exists()) {
                Runtime.getRuntime().exec("cmd /c start " + reportFile.getAbsolutePath());
                System.out.println("🌐 Report opened in browser");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not open report: " + e.getMessage());
        }
    }
    
    private String getBuildNumber() {
        String buildNumber = System.getenv("BUILD_NUMBER");
        return buildNumber != null ? buildNumber : "1";
    }
    
    private String getBrowserVersion() {
        return "120.0"; // You can implement actual browser version detection
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}