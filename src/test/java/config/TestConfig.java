package config;

import java.time.Duration;
import utilities.ConfigReader;

public class TestConfig {
    
    // Browser Configuration - Read from config
    public static final String BROWSER = ConfigReader.getBrowser();
    public static final boolean HEADLESS = ConfigReader.isHeadless();
    
    // Timeouts - Read from config
    public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(ConfigReader.getImplicitWait());
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
    
    // Application URLs - Read from config
    public static final String BASE_URL = ConfigReader.getUrl();
    public static final String LOGIN_URL = BASE_URL;
    
    // Screenshot Configuration - Read from config
    public static final String SCREENSHOT_PATH = ensureTrailingSeparator(ConfigReader.getScreenshotPath());
    
    // Extent Report Configuration - Read from config
    public static final String EXTENT_REPORT_PATH = ensureTrailingSeparator(ConfigReader.getExtentReportPath());
    public static final String REPORT_NAME = ConfigReader.getReportName();
    public static final String DOCUMENT_TITLE = ConfigReader.getDocumentTitle();
    
    // Screenshot settings
    public static final boolean TAKE_SCREENSHOT_ON_FAILURE = ConfigReader.takeScreenshotOnFailure();
    public static final boolean TAKE_SCREENSHOT_ON_SUCCESS = ConfigReader.takeScreenshotOnSuccess();
    
    private static String ensureTrailingSeparator(String path) {
        if (path == null || path.isEmpty()) {
            return "target/";
        }
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            return path + "/";
        }
        return path;
    }
    
    public static boolean isJenkinsEnvironment() {
        boolean isJenkins = System.getenv("JENKINS_HOME") != null || 
                           System.getenv("BUILD_NUMBER") != null ||
                           "jenkins".equals(System.getProperty("environment"));
        
        if (System.getProperty("force.headless") != null) {
            return Boolean.parseBoolean(System.getProperty("force.headless"));
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