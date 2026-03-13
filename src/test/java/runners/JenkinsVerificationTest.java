package runners;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JenkinsVerificationTest {
    
    @Test
    public void verifyJenkinsSetup() {
    	 System.out.println("=========================================");
         System.out.println("VERIFICATION TEST RUNNING");
         System.out.println("=========================================");
         System.out.println("Java Version: " + System.getProperty("java.version"));
         System.out.println("Headless Mode: " + System.getProperty("headless", "not set"));
         System.out.println("=========================================");
         
         // Simple assertion to verify test runs
         Assert.assertTrue(true, "This test should always pass");
    }
}