package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import config.TestConfig;
import mobile.android.pages.drivers.AndroidDriverManager;

import java.io.File;
import java.time.Duration;

public class DriverManager {

    private static WebDriver webDriver;
    private static boolean isMobileMode = false;

    private static final String CHROME_DRIVER_PATH =
            "C:/drivers/chromedriver.exe";

    private static final String EDGE_DRIVER_PATH =
            "C:\\Users\\tushar.sangale\\Downloads\\UpdatedFrameworkChanges-master (1)\\UpdatedFrameworkChanges-master\\workselenium\\driver\\msedgedriver.exe";

    // ==========================================================
    // GET DRIVER - SUPPORTS BOTH WEB AND MOBILE
    // ==========================================================

    public static WebDriver getDriver() {
        // Check if mobile platform is configured
        if (ConfigReader.isMobilePlatform()) {
            isMobileMode = true;
            return AndroidDriverManager.getMobileDriver();
        }
        isMobileMode = false;
        return getWebDriver();
    }

    // ==========================================================
    // WEB DRIVER
    // ==========================================================

    public static WebDriver getWebDriver() {

        if (webDriver == null) {

            String browser = ConfigReader.getBrowser().toLowerCase();

            System.out.println("=========================================");
            System.out.println("WEB DRIVER INITIALIZATION");
            System.out.println("=========================================");
            System.out.println("Browser: " + browser);
            System.out.println("=========================================");

            try {

                switch (browser) {

                    case "chrome":
                        webDriver = setupChromeDriver();
                        break;

                    case "edge":
                        webDriver = setupEdgeDriver();
                        break;

                    case "firefox":
                        webDriver = setupFirefoxDriver();
                        break;

                    default:
                        webDriver = setupChromeDriver();
                }

                webDriver.manage().window().maximize();
                webDriver.manage().timeouts()
                        .implicitlyWait(Duration.ofSeconds(
                                ConfigReader.getImplicitWait()));

                System.out.println("✅ Web Driver initialized");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Web driver failed", e);
            }
        }

        return webDriver;
    }

    // ==========================================================
    // QUIT DRIVER - SUPPORTS BOTH WEB AND MOBILE
    // ==========================================================

    public static void quitDriver() {
        if (isMobileMode || ConfigReader.isMobilePlatform()) {
            // Quit mobile driver
            AndroidDriverManager.quitDriver();
            isMobileMode = false;
            System.out.println("✅ Mobile driver closed");
        } else {
            // Quit web driver
            quitWebDriver();
        }
    }

    private static void quitWebDriver() {
        try {
            if (webDriver != null) {
                webDriver.quit();
                webDriver = null;
                System.out.println("✅ Web driver closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isMobileMode() {
        return isMobileMode || ConfigReader.isMobilePlatform();
    }

    // ==========================================================
    // WEB SETUP
    // ==========================================================

    private static WebDriver setupChromeDriver() {

        ChromeOptions options = new ChromeOptions();

        // Headless mode for Jenkins
        if (ConfigReader.isHeadless() || TestConfig.isJenkinsEnvironment()) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            System.out.println("🔧 Running Chrome in headless mode");
        }

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        File file = new File(CHROME_DRIVER_PATH);

        if (file.exists()) {
            System.setProperty(
                    "webdriver.chrome.driver",
                    CHROME_DRIVER_PATH);
        } else {
            WebDriverManager.chromedriver()
                    .setup();
        }

        return new ChromeDriver(options);
    }

    private static WebDriver setupEdgeDriver() {

        EdgeOptions options = new EdgeOptions();

        if (ConfigReader.isHeadless() || TestConfig.isJenkinsEnvironment()) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            System.out.println("🔧 Running Edge in headless mode");
        }

        File file = new File(EDGE_DRIVER_PATH);

        if (file.exists()) {
            System.setProperty(
                    "webdriver.edge.driver",
                    EDGE_DRIVER_PATH);
        } else {
            WebDriverManager.edgedriver()
                    .setup();
        }

        return new EdgeDriver(options);
    }

    private static WebDriver setupFirefoxDriver() {

        FirefoxOptions options = new FirefoxOptions();

        if (ConfigReader.isHeadless() || TestConfig.isJenkinsEnvironment()) {
            options.addArguments("--headless");
            System.out.println("🔧 Running Firefox in headless mode");
        }

        WebDriverManager
                .firefoxdriver()
                .setup();

        return new FirefoxDriver(options);
    }
}