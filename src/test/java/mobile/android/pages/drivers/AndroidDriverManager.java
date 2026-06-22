package mobile.android.pages.drivers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.net.URL;
import java.time.Duration;

import utilities.ConfigReader;

public class AndroidDriverManager {
    
    private static ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();
    private static AppiumDriverLocalService appiumService;
    private static boolean isAppiumServerStarted = false;
    
    public static AndroidDriver getMobileDriver() {
        if (driverThreadLocal.get() == null || !isDriverValid()) {
            AndroidDriver driver = createAndroidDriver();
            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }
    
    private static synchronized AndroidDriver createAndroidDriver() {
        try {
            System.out.println("=========================================");
            System.out.println("CREATING ANDROID DRIVER");
            System.out.println("=========================================");
            
            // Start Appium server if not already running
            startAppiumServer();
            
            // Set up capabilities
            UiAutomator2Options options = new UiAutomator2Options();
            
            // Get mobile configuration from ConfigReader
            options.setPlatformName(ConfigReader.getMobilePlatformName());
            options.setAutomationName(ConfigReader.getMobileAutomationName());
            options.setDeviceName(ConfigReader.getMobileDeviceName());
            options.setUdid(ConfigReader.getMobileUdid());
            options.setPlatformVersion(ConfigReader.getMobilePlatformVersion());
            
            // Set app if APK path is provided
            String apkPath = ConfigReader.getMobileApkPath();
            if (apkPath != null && !apkPath.isEmpty()) {
                options.setApp(apkPath);
                System.out.println("📱 APK Path: " + apkPath);
            }
            
            // Set app package and activity if provided
            String appPackage = ConfigReader.getMobileAppPackage();
            String appActivity = ConfigReader.getMobileAppActivity();
            if (appPackage != null && !appPackage.isEmpty() && appActivity != null && !appActivity.isEmpty()) {
                options.setAppPackage(appPackage);
                options.setAppActivity(appActivity);
                System.out.println("📱 App Package: " + appPackage);
                System.out.println("📱 App Activity: " + appActivity);
            }
            
            // Set other capabilities
            options.setNoReset(ConfigReader.isMobileNoReset());
            options.setFullReset(ConfigReader.isMobileFullReset());
            options.setAutoGrantPermissions(ConfigReader.isMobileAutoGrantPermissions());
            options.setNewCommandTimeout(Duration.ofSeconds(ConfigReader.getMobileNewCommandTimeout()));
            
            // Additional capabilities for better stability
            options.setCapability("appWaitDuration", 60000);
            options.setCapability("androidInstallTimeout", 60000);
            options.setCapability("skipDeviceInitialization", false);
            options.setCapability("shouldTerminateApp", true);
            options.setCapability("unicodeKeyboard", true);
            options.setCapability("resetKeyboard", true);
            
            // Appium server URL
            String appiumServerUrl = ConfigReader.getMobileAppiumServerUrl();
            URL serverUrl = new URL(appiumServerUrl);
            
            System.out.println("📱 Platform: " + ConfigReader.getMobilePlatformName());
            System.out.println("📱 Device: " + ConfigReader.getMobileDeviceName());
            System.out.println("📱 UDID: " + ConfigReader.getMobileUdid());
            System.out.println("📱 Platform Version: " + ConfigReader.getMobilePlatformVersion());
            System.out.println("📱 Appium Server: " + appiumServerUrl);
            System.out.println("📱 No Reset: " + ConfigReader.isMobileNoReset());
            System.out.println("📱 Auto Grant Permissions: " + ConfigReader.isMobileAutoGrantPermissions());
            System.out.println("=========================================");
            
            // Create the driver
            AndroidDriver driver = new AndroidDriver(serverUrl, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getMobileImplicitWait()));
            
            System.out.println("✅ Android driver created successfully");
            System.out.println("✅ Session ID: " + driver.getSessionId());
            System.out.println("=========================================");
            
            return driver;
            
        } catch (Exception e) {
            System.out.println("❌ Failed to create Android driver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create Android driver", e);
        }
    }
    
    private static synchronized void startAppiumServer() {
        try {
            if (appiumService == null || !appiumService.isRunning()) {
                System.out.println("🚀 Starting Appium server...");
                
                AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .withTimeout(Duration.ofSeconds(300));
                
                // Add logs for debugging
                builder.withLogFile(new java.io.File("target/appium-logs.txt"));
                
                appiumService = AppiumDriverLocalService.buildService(builder);
                appiumService.start();
                
                isAppiumServerStarted = true;
                System.out.println("✅ Appium server started successfully on port 4723");
                System.out.println("✅ Appium server logs: target/appium-logs.txt");
            } else {
                System.out.println("✅ Appium server already running");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Appium server error: " + e.getMessage());
            System.out.println("⚠️ Assuming Appium server is already running on port 4723");
            // Continue - maybe server is already running
        }
    }
    
    private static boolean isDriverValid() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver == null) {
            return false;
        }
        try {
            driver.getDeviceTime();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Driver session is invalid: " + e.getMessage());
            return false;
        }
    }
    
    public static void setMobileDriver(AndroidDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    public static boolean isDriverValid(AndroidDriver driver) {
        if (driver == null) {
            return false;
        }
        try {
            driver.getDeviceTime();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ Driver session is invalid: " + e.getMessage());
            return false;
        }
    }
    
    public static void quitDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Android driver quit successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Error quitting driver: " + e.getMessage());
            }
            driverThreadLocal.remove();
        }
        
        // Stop Appium server if it was started by this class
        if (appiumService != null && appiumService.isRunning() && isAppiumServerStarted) {
            try {
                appiumService.stop();
                isAppiumServerStarted = false;
                System.out.println("✅ Appium server stopped");
            } catch (Exception e) {
                System.out.println("⚠️ Error stopping Appium server: " + e.getMessage());
            }
        }
    }
    
    public static void resetDriver() {
        quitDriver();
        // New driver will be created on next getMobileDriver() call
        System.out.println("🔄 Driver reset completed");
    }
}