package runners;

import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;

public class JenkinsTestRunner {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("STARTING JENKINS TEST RUNNER");
        System.out.println("=========================================");
        
        // Set headless mode for Jenkins
        System.setProperty("headless", "true");
        System.setProperty("webdriver.chrome.silentOutput", "false");
        System.setProperty("webdriver.edge.silentOutput", "true");
        
        // Log environment info
        System.out.println("Environment Information:");
        System.out.println("  - Java Version: " + System.getProperty("java.version"));
        System.out.println("  - OS: " + System.getProperty("os.name"));
        System.out.println("  - User Dir: " + System.getProperty("user.dir"));
        System.out.println("  - Jenkins Build: " + System.getenv("BUILD_NUMBER"));
        System.out.println("  - Headless Mode: " + System.getProperty("headless"));
        System.out.println("=========================================");
        
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("testng.xml");
        testNG.setTestSuites(suites);
        
        // Add listeners for better reporting
        testNG.addListener(new org.testng.reporters.FailedReporter());
        testNG.addListener(new org.testng.reporters.SuiteHTMLReporter());
        
        try {
            testNG.run();
            
            int exitCode = testNG.hasFailure() ? 1 : 0;
            System.out.println("=========================================");
            System.out.println("TESTS COMPLETED");
            System.out.println("Status: " + (exitCode == 0 ? "SUCCESS" : "FAILURE"));
            System.out.println("Exit Code: " + exitCode);
            System.out.println("=========================================");
            
            // Generate Allure report
            try {
                System.out.println("Generating Allure report...");
                ProcessBuilder pb = new ProcessBuilder(
                    "cmd", "/c", "allure generate target/allure-results --clean -o target/allure-report"
                );
                Process process = pb.start();
                int allureExitCode = process.waitFor();
                
                if (allureExitCode == 0) {
                    System.out.println("✅ Allure report generated successfully");
                } else {
                    System.out.println("⚠️ Allure report generation failed");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error generating Allure report: " + e.getMessage());
            }
            
            System.exit(exitCode);
        } catch (Exception e) {
            System.out.println("❌ Error running tests: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}