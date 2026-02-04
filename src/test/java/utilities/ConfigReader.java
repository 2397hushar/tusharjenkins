//package utilities;
//
//import java.io.InputStream;
//import java.util.Properties;
//
//public class ConfigReader {
//    private static Properties properties;
//    
//    static {
//        try {
//            properties = new Properties();
//            InputStream input = ConfigReader.class.getClassLoader()
//                    .getResourceAsStream("config.properties");
//            
//            if (input != null) {
//                properties.load(input);
//                input.close();
//                System.out.println("Config properties loaded successfully");
//            } else {
//                System.out.println("Config file not found, using default values");
//                setDefaultProperties();
//            }
//        } catch (Exception e) {
//            System.out.println("Error loading config, using defaults: " + e.getMessage());
//            setDefaultProperties();
//        }
//    }
//    
//    private static void setDefaultProperties() {
//        properties = new Properties();
//        properties.setProperty("browser", "edge");
//        properties.setProperty("url", "https://www.saucedemo.com/");
//        properties.setProperty("username", "standard_user");
//        properties.setProperty("password", "secret_sauce");
//        properties.setProperty("implicit.wait", "10");
//        properties.setProperty("edge.driver.path", "C:\\Users\\Tushar Here\\selenium\\workselenium\\driver\\msedgedriver.exe");
//        properties.setProperty("screenshot.path", "target/screenshots/");
//        properties.setProperty("extent.report.path", "target/extent-reports/");
//        properties.setProperty("take.screenshot.on.failure", "true");
//        properties.setProperty("take.screenshot.on.success", "false");
//        properties.setProperty("report.name", "SauceDemo Test Report");
//        properties.setProperty("document.title", "SauceDemo Test Results");
//        properties.setProperty("theme", "standard");
//    }
//    
//    public static String getProperty(String key) {
//        return properties.getProperty(key);
//    }
//    
//    public static String getBrowser() {
//        return getProperty("browser");
//    }
//    
//    public static String getUrl() {
//        return getProperty("url");
//    }
//    
//    public static String getUsername() {
//        return getProperty("username");
//    }
//    
//    public static String getPassword() {
//        return getProperty("password");
//    }
//    
//    public static int getImplicitWait() {
//        try {
//            return Integer.parseInt(getProperty("implicit.wait"));
//        } catch (Exception e) {
//            return 10;
//        }
//    }
//    
//    public static String getEdgeDriverPath() {
//        return getProperty("edge.driver.path");
//    }
//    
//    // SCREENSHOT CONFIGURATION METHODS
//    public static String getScreenshotPath() {
//        return getProperty("screenshot.path");
//    }
//    
//    public static String getExtentReportPath() {
//        return getProperty("extent.report.path");
//    }
//    
//    public static boolean takeScreenshotOnFailure() {
//        return Boolean.parseBoolean(getProperty("take.screenshot.on.failure"));
//    }
//    
//    public static boolean takeScreenshotOnSuccess() {
//        return Boolean.parseBoolean(getProperty("take.screenshot.on.success"));
//    }
//    
//    // EXTENT REPORT CONFIGURATION METHODS
//    public static String getReportName() {
//        return getProperty("report.name");
//    }
//    
//    public static String getDocumentTitle() {
//        return getProperty("document.title");
//    }
//    
//    public static String getTheme() {
//        return getProperty("theme");
//    }
// // Add these methods to your existing ConfigReader class
//    public static String getAdminUsername() {
//        return getProperty("admin.username");
//    }
//
//    public static String getAdminPassword() {
//        return getProperty("admin.password");
//    }
//
//    public static String getApplicationUrl() {
//        return getProperty("application.url");
//    }
// // Add these methods to your existing ConfigReader class
//    public static String getSimonsEmail() {
//        return getProperty("simons.email");
//    }
//
//    public static String getSimonsPassword() {
//        return getProperty("simons.password");
//    }
//
//    public static String getSimonsUrl() {
//        return getProperty("simons.url");
//    }
//}





//********************************** upadted

//package utilities;
//
//import java.io.InputStream;
//import java.util.Properties;
//import java.util.Map;
//
//public class ConfigReader {
//    private static Properties properties;
//    
//    static {
//        try {
//            properties = new Properties();
//            InputStream input = ConfigReader.class.getClassLoader()
//                    .getResourceAsStream("config.properties");
//            
//            if (input != null) {
//                properties.load(input);
//                input.close();
//                System.out.println("✅ Config properties loaded successfully");
//            } else {
//                System.out.println("⚠️ Config file not found, using default values");
//                setDefaultProperties();
//            }
//            
//            // Initialize and validate JSON data
//            JsonDataProvider.printUserSummary();
//            JsonDataProvider.validateUserData();
//            
//        } catch (Exception e) {
//            System.out.println("❌ Error loading config, using defaults: " + e.getMessage());
//            setDefaultProperties();
//        }
//    }
//    
//    private static void setDefaultProperties() {
//        properties = new Properties();
//        properties.setProperty("browser", "edge");
//        properties.setProperty("url", "https://preprod-hubbleorion.hubblehox.com/");
//        properties.setProperty("username", "swatipatil22@yahoo.com");
//        properties.setProperty("password", "Swat@123");
//        properties.setProperty("implicit.wait", "20");
//        properties.setProperty("screenshot.path", "target/screenshots/");
//        properties.setProperty("extent.report.path", "target/extent-reports/");
//        properties.setProperty("take.screenshot.on.failure", "true");
//        properties.setProperty("take.screenshot.on.success", "false");
//        properties.setProperty("report.name", "ERP Test Report");
//        properties.setProperty("document.title", "ERP Test Results");
//        properties.setProperty("theme", "standard");
//    }
//    
//    // Basic property getters
//    public static String getProperty(String key) {
//        return properties.getProperty(key);
//    }
//    
//    public static String getBrowser() {
//        return getProperty("browser");
//    }
//    
//    public static String getUrl() {
//        return getProperty("url");
//    }
//    
//    public static String getUsername() {
//        return getProperty("username");
//    }
//    
//    public static String getPassword() {
//        return getProperty("password");
//    }
//    
//    // JSON User Management - Delegated to JsonDataProvider
//    public static boolean hasMultipleUsers() {
//        return JsonDataProvider.hasUsers();
//    }
//    
//    public static Map<String, String> getNextUserData() {
//        return JsonDataProvider.getNextUser();
//    }
//    
//    public static Map<String, String> getCurrentUserData() {
//        return JsonDataProvider.getCurrentUser();
//    }
//    
//    public static void resetUserIterator() {
//        JsonDataProvider.resetIterator();
//    }
//    
//    // Rest of existing methods remain the same...
//    public static int getImplicitWait() {
//        try {
//            return Integer.parseInt(getProperty("implicit.wait"));
//        } catch (Exception e) {
//            return 10;
//        }
//    }
//    
//    public static String getScreenshotPath() {
//        return getProperty("screenshot.path");
//    }
//    
//    public static String getExtentReportPath() {
//        return getProperty("extent.report.path");
//    }
//    
//    public static boolean takeScreenshotOnFailure() {
//        return Boolean.parseBoolean(getProperty("take.screenshot.on.failure"));
//    }
//    
//    public static boolean takeScreenshotOnSuccess() {
//        return Boolean.parseBoolean(getProperty("take.screenshot.on.success"));
//    }
//    
//    public static String getReportName() {
//        return getProperty("report.name");
//    }
//    
//    public static String getDocumentTitle() {
//        return getProperty("document.title");
//    }
//    
//    public static String getTheme() {
//        return getProperty("theme");
//    }
//}


package utilities;

import java.io.InputStream;
import java.util.Properties;
import java.util.Map;

public class ConfigReader {
    private static Properties properties;
    
    static {
        try {
            properties = new Properties();
            InputStream input = ConfigReader.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            
            if (input != null) {
                properties.load(input);
                input.close();
                System.out.println("✅ Config properties loaded successfully");
            } else {
                System.out.println("⚠️ Config file not found, using default values");
                setDefaultProperties();
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
        properties.setProperty("browser", "edge");
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
    }
    
    // Basic property getters
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getBrowser() {
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
    
    // JSON User Management Methods - These were missing
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