package runners;

import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;

public class JenkinsTestRunner {
    public static void main(String[] args) {
        System.out.println("Starting Jenkins Test Runner...");
        
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("testng.xml");
        testNG.setTestSuites(suites);
        
        testNG.run();
        
        System.out.println("Tests completed with status: " + (testNG.hasFailure() ? "FAILURE" : "SUCCESS"));
        System.exit(testNG.hasFailure() ? 1 : 0);
    }
}