package utilities;

import java.io.InputStream;
import java.util.Properties;
import java.util.Map;

public class ConfigReader {
    private static Properties properties;
    private static String testPlatform;
    
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
                System.out.println("⚠️ No config file found, using default values");
                setDefaultProperties();
            }
            
            // Override headless mode if specified via system property
            String headlessOverride = System.getProperty("headless");
            if (headlessOverride != null) {
                System.out.println("⚠️ Overriding headless mode to: " + headlessOverride);
                properties.setProperty("headless", headlessOverride);
            }
            
            // Check for browser override
            String browserOverride = System.getProperty("browser");
            if (browserOverride != null && !browserOverride.trim().isEmpty()) {
                System.out.println("⚠️ Overriding browser to: " + browserOverride);
                properties.setProperty("browser", browserOverride);
            }
            
            // Get test platform (web or mobile)
            testPlatform = System.getProperty("test.platform");
            if (testPlatform == null || testPlatform.isEmpty()) {
                testPlatform = properties.getProperty("test.platform", "web");
            }
            System.out.println("📱 Test Platform: " + testPlatform);
            
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
            JsonDataProvider.setUsersManually();
        }
    }
    
    private static void setDefaultProperties() {
        properties = new Properties();
        properties.setProperty("browser", "chrome");
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
        properties.setProperty("headless", "false");
        properties.setProperty("test.platform", "web");
        
        // Mobile defaults
        properties.setProperty("mobile.platform.name", "Android");
        properties.setProperty("mobile.automation.name", "UiAutomator2");
        properties.setProperty("mobile.device.name", "Xiaomi_2411DRN47I");
        properties.setProperty("mobile.udid", "a92a5ee7");
        properties.setProperty("mobile.platform.version", "15");
        properties.setProperty("mobile.appium.server.url", "http://127.0.0.1:4723");
        properties.setProperty("mobile.implicit.wait", "20");
        properties.setProperty("mobile.explicit.wait", "25");
        properties.setProperty("mobile.new.command.timeout", "300");
        properties.setProperty("mobile.no.reset", "true");
        properties.setProperty("mobile.full.reset", "false");
        properties.setProperty("mobile.auto.grant.permissions", "true");
        properties.setProperty("mobile.screenshot.path", "target/mobile-screenshots/");
        properties.setProperty("mobile.apk.path", "");
        properties.setProperty("mobile.app.package", "com.hubblehox.hubbleorion");
        properties.setProperty("mobile.app.activity", "com.hubblehox.hubbleorion.MainActivity");
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
    
    public static String getTestPlatform() {
        return testPlatform;
    }
    
    public static boolean isMobilePlatform() {
        return "mobile".equalsIgnoreCase(testPlatform);
    }
    
    public static boolean isWebPlatform() {
        return "web".equalsIgnoreCase(testPlatform);
    }
    
    public static String getBrowser() {
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
    
    public static boolean isHeadless() {
        String headless = getProperty("headless");
        if (headless == null || headless.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(headless);
    }
    
    // ============================================
    // MOBILE CONFIGURATION METHODS
    // ============================================
    public static String getMobilePlatformName() {
        return getProperty("mobile.platform.name");
    }
    
    public static String getMobileAutomationName() {
        return getProperty("mobile.automation.name");
    }
    
    public static String getMobileDeviceName() {
        String deviceOverride = System.getProperty("device.name");
        if (deviceOverride != null && !deviceOverride.isEmpty()) {
            return deviceOverride;
        }
        return getProperty("mobile.device.name");
    }
    
    public static String getMobileUdid() {
        String udidOverride = System.getProperty("udid");
        if (udidOverride != null && !udidOverride.isEmpty()) {
            return udidOverride;
        }
        return getProperty("mobile.udid");
    }
    
    public static String getMobilePlatformVersion() {
        return getProperty("mobile.platform.version");
    }
    
    public static String getMobileAppiumServerUrl() {
        return getProperty("mobile.appium.server.url");
    }
    
    public static String getMobileApkPath() {
        return getProperty("mobile.apk.path");
    }
    
    public static String getMobileAppPackage() {
        return getProperty("mobile.app.package");
    }
    
    public static String getMobileAppActivity() {
        return getProperty("mobile.app.activity");
    }
    
    public static String getMobileAppWaitActivity() {
        return getProperty("mobile.app.wait.activity");
    }
    
    public static int getMobileImplicitWait() {
        try {
            return Integer.parseInt(getProperty("mobile.implicit.wait"));
        } catch (Exception e) {
            return 20;
        }
    }
    
    public static int getMobileExplicitWait() {
        try {
            return Integer.parseInt(getProperty("mobile.explicit.wait"));
        } catch (Exception e) {
            return 25;
        }
    }
    
    public static boolean isMobileNoReset() {
        return Boolean.parseBoolean(getProperty("mobile.no.reset"));
    }
    
    public static boolean isMobileFullReset() {
        return Boolean.parseBoolean(getProperty("mobile.full.reset"));
    }
    
    public static boolean isMobileAutoGrantPermissions() {
        return Boolean.parseBoolean(getProperty("mobile.auto.grant.permissions"));
    }
    
    public static int getMobileNewCommandTimeout() {
        try {
            return Integer.parseInt(getProperty("mobile.new.command.timeout"));
        } catch (Exception e) {
            return 300;
        }
    }
    
    public static String getMobileScreenshotPath() {
        String path = getProperty("mobile.screenshot.path");
        if (path == null || path.isEmpty()) {
            return "target/mobile-screenshots/";
        }
        return ensureTrailingSeparator(path);
    }
    
    private static String ensureTrailingSeparator(String path) {
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            return path + "/";
        }
        return path;
    }
    
    // ============================================
    // JSON User Management Methods
    // ============================================
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
    
    // Existing methods
    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait"));
        } catch (Exception e) {
            return 20;
        }
    }
    
    public static String getScreenshotPath() {
        if (isMobilePlatform()) {
            return getMobileScreenshotPath();
        }
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