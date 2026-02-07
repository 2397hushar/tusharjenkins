package utilities;

import java.io.InputStream;
import java.util.Properties;
import java.util.Map;

public class ConfigReader {
    private static Properties properties;
    
    static {
        try {
            properties = new Properties();
            
            // First check if we're in Jenkins environment
            boolean isJenkins = System.getenv("JENKINS_HOME") != null || 
                               System.getenv("BUILD_NUMBER") != null;
            
            // Try to load appropriate config file
            String configFile = isJenkins ? "jenkins.properties" : "config.properties";
            System.out.println("🔧 Loading configuration from: " + configFile);
            
            InputStream input = ConfigReader.class.getClassLoader()
                    .getResourceAsStream(configFile);
            
            if (input != null) {
                properties.load(input);
                input.close();
                System.out.println("✅ " + configFile + " loaded successfully");
            } else {
                // If specific config not found, try the other one
                configFile = isJenkins ? "config.properties" : "jenkins.properties";
                input = ConfigReader.class.getClassLoader()
                        .getResourceAsStream(configFile);
                
                if (input != null) {
                    properties.load(input);
                    input.close();
                    System.out.println("✅ Fallback to " + configFile + " loaded successfully");
                } else {
                    System.out.println("⚠️ No config file found, using default values");
                    setDefaultProperties();
                }
            }
            
            // Override headless mode if specified via system property
            String headlessOverride = System.getProperty("headless");
            if (headlessOverride != null) {
                System.out.println("⚠️ Overriding headless mode to: " + headlessOverride);
                properties.setProperty("headless", headlessOverride);
            }
            
            // Also check for browser override
            String browserOverride = System.getProperty("browser");
            if (browserOverride != null && !browserOverride.trim().isEmpty()) {
                System.out.println("⚠️ Overriding browser to: " + browserOverride);
                properties.setProperty("browser", browserOverride);
            }
            
            // Initialize and validate JSON data
            if (!JsonDataProvider.hasUsers()) {
                System.out.println("⚠️ No users found in JSON, using manual fallback");
                JsonDataProvider.setUsersManually();
            }
            
            JsonDataProvider.printUserSummary();
            JsonDataProvider.validateUserData();
            
        } catch (Exception e) {
            System.out.println("❌ Error loading config, using defaults: " + e.getMessage());
            setDefaultProperties();
            // Set manual users as fallback
            JsonDataProvider.setUsersManually();
        }
    }
    
    private static void setDefaultProperties() {
        properties = new Properties();
        properties.setProperty("browser", "chrome");  // Changed from edge to chrome
        properties.setProperty("url", "https://preprod-hubbleorion.hubblehox.com/");
        properties.setProperty("username", "swatipatil22@yahoo.com");
        properties.setProperty("password", "Swat@123");
        properties.setProperty("implicit.wait", "20");
        properties.setProperty("screenshot.path", "target/screenshots/");
        properties.setProperty("extent.report.path", "target/extent-reports/");
        properties.setProperty("take.screenshot.on.failure", "true");
        properties.setProperty("take.screenshot.on.success", "false");
        properties.setProperty("report.name", "ERP Test Report");
        properties.setProperty("document.title", "ERP Test Results");
        properties.setProperty("theme", "standard");
        // Add headless property with default
        properties.setProperty("headless", "false");
    }
    
    // Basic property getters
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.out.println("⚠️ Property not found: " + key);
            return "";
        }
        return value.trim();
    }
    
    public static String getBrowser() {
        // Check for system property first (allows override)
        String browser = System.getProperty("browser");
        if (browser != null && !browser.trim().isEmpty()) {
            System.out.println("Using browser from system property: " + browser);
            return browser.toLowerCase();
        }
        return getProperty("browser");
    }
    
    public static String getUrl() {
        return getProperty("url");
    }
    
    public static String getUsername() {
        return getProperty("username");
    }
    
    public static String getPassword() {
        return getProperty("password");
    }
    
    // Add this new method to get headless property
    public static boolean isHeadlesss() {
        String headless = getProperty("headless");
        if (headless == null || headless.isEmpty()) {
            return false; // default to false if not specified
        }
        return Boolean.parseBoolean(headless);
    }
    
    // JSON User Management Methods
    public static String getUserUsername(int index) {
        return JsonDataProvider.getUsername(index);
    }
    
    public static String getUserPassword(int index) {
        return JsonDataProvider.getPassword(index);
    }
    
    public static String getUserDescription(int index) {
        return JsonDataProvider.getDescription(index);
    }
    
    public static boolean hasMultipleUsers() {
        return JsonDataProvider.hasUsers();
    }
    
    public static Map<String, String> getNextUserData() {
        return JsonDataProvider.getNextUser();
    }
    
    public static Map<String, String> getCurrentUserData() {
        return JsonDataProvider.getCurrentUser();
    }
    
    public static void resetUserIterator() {
        JsonDataProvider.resetIterator();
    }
    
    // Rest of existing methods...
    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait"));
        } catch (Exception e) {
            return 10;
        }
    }
    
    public static String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    public static String getExtentReportPath() {
        return getProperty("extent.report.path");
    }
    
    public static boolean takeScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("take.screenshot.on.failure"));
    }
    
    public static boolean takeScreenshotOnSuccess() {
        return Boolean.parseBoolean(getProperty("take.screenshot.on.success"));
    }
    
    public static String getReportName() {
        return getProperty("report.name");
    }
    
    public static String getDocumentTitle() {
        return getProperty("document.title");
    }
    
    public static String getTheme() {
        return getProperty("theme");
    }
}