package config;
 
import java.time.Duration;
 
public class TestConfig {
    
    // Browser Configuration
    public static final String BROWSER = "edge";
    public static final boolean HEADLESS = false;
    public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    
    // Application URLs
    public static final String BASE_URL = "https://preprod-hubbleorion.hubblehox.com/";
    public static final String LOGIN_URL = BASE_URL;
    public static final String INVENTORY_URL = BASE_URL + "inventory.html";
    
    // Screenshot Configuration
    public static final String SCREENSHOT_PATH = System.getProperty("user.dir") + "/target/screenshots/";
    
    // Extent Report Configuration - Use relative paths
    public static final String EXTENT_REPORT_PATH = System.getProperty("user.dir") + "/target/extent-reports/";
    public static final String REPORT_NAME = "ERP Automation Report";
    public static final String DOCUMENT_TITLE = "ERP Test Results";
    
    public static final boolean TAKE_SCREENSHOT_ON_FAILURE = true;
    public static final boolean TAKE_SCREENSHOT_ON_SUCCESS = false;
    
    // Test Data
    public static class Users {
        public static final String STANDARD_USER = "ps1@vgos.org";
        public static final String LOCKED_OUT_USER = "locked_out_user";
        public static final String PROBLEM_USER = "problem_user";
        public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
        public static final String PASSWORD = "L8XcljPmjmGea322";
    }
    
    // Enhanced Jenkins detection
    public static boolean isJenkinsEnvironment() {
        boolean jenkinsHome = System.getenv("JENKINS_HOME") != null;
        boolean buildNumber = System.getenv("BUILD_NUMBER") != null;
        boolean jenkinsUrl = System.getenv("JENKINS_URL") != null;
        boolean jenkinsJobName = System.getenv("JOB_NAME") != null;
        boolean jenkinsNodeName = System.getenv("NODE_NAME") != null;
        boolean jenkinsMode = "jenkins".equals(System.getProperty("environment"));
        boolean headlessProp = "true".equalsIgnoreCase(System.getProperty("headless"));
        
        boolean isJenkins = jenkinsHome || buildNumber || jenkinsUrl || jenkinsJobName || 
                           jenkinsNodeName || jenkinsMode || headlessProp;
        
        if (isJenkins) {
            System.out.println("✅ Jenkins environment detected:");
            if (jenkinsHome) System.out.println("  - JENKINS_HOME: " + System.getenv("JENKINS_HOME"));
            if (buildNumber) System.out.println("  - BUILD_NUMBER: " + System.getenv("BUILD_NUMBER"));
            if (jenkinsJobName) System.out.println("  - JOB_NAME: " + System.getenv("JOB_NAME"));
            if (jenkinsNodeName) System.out.println("  - NODE_NAME: " + System.getenv("NODE_NAME"));
        }
        
        return isJenkins;
    }
    
    // Wait Configuration
    public static class Waits {
        public static final Duration SHORT_WAIT = Duration.ofSeconds(5);
        public static final Duration MEDIUM_WAIT = Duration.ofSeconds(10);
        public static final Duration LONG_WAIT = Duration.ofSeconds(20);
    }
}