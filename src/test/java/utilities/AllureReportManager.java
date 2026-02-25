package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

public class AllureReportManager {
    
    private static final String ALLURE_RESULTS_PATH = "target/allure-results/";
    private static ThreadLocal<String> currentStepUuid = new ThreadLocal<>();
    
    static {
        createAllureResultsDirectory();
        createEnvironmentProperties();
        createCategoriesFile();
    }
    
    private static void createAllureResultsDirectory() {
        try {
            Path path = Paths.get(ALLURE_RESULTS_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✅ Created Allure results directory: " + ALLURE_RESULTS_PATH);
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to create Allure results directory: " + e.getMessage());
        }
    }
    
    public static void createEnvironmentProperties() {
        try {
            Properties props = new Properties();
            props.setProperty("Browser", ConfigReader.getBrowser());
            props.setProperty("Browser.Version", getBrowserVersion());
            props.setProperty("URL", ConfigReader.getUrl());
            props.setProperty("OS", System.getProperty("os.name"));
            props.setProperty("OS.Version", System.getProperty("os.version"));
            props.setProperty("Java.Version", System.getProperty("java.version"));
            props.setProperty("Headless", String.valueOf(ConfigReader.isHeadlesss()));
            
            File file = new File(ALLURE_RESULTS_PATH + "environment.properties");
            try (FileOutputStream out = new FileOutputStream(file)) {
                props.store(out, "Allure Environment Properties");
            }
            System.out.println("✅ Created environment.properties for Allure");
        } catch (Exception e) {
            System.out.println("⚠️ Could not create environment.properties: " + e.getMessage());
        }
    }
    
    private static void createCategoriesFile() {
        try {
            String categories = "[\n" +
                "  {\n" +
                "    \"name\": \"Passed Tests\",\n" +
                "    \"matchedStatuses\": [\"passed\"]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Failed Tests\",\n" +
                "    \"matchedStatuses\": [\"failed\"]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Broken Tests\",\n" +
                "    \"matchedStatuses\": [\"broken\"]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Skipped Tests\",\n" +
                "    \"matchedStatuses\": [\"skipped\"]\n" +
                "  }\n" +
                "]";
            
            Files.writeString(Paths.get(ALLURE_RESULTS_PATH + "categories.json"), categories);
            System.out.println("✅ Created categories.json for Allure");
        } catch (Exception e) {
            System.out.println("⚠️ Could not create categories.json: " + e.getMessage());
        }
    }
    
    // ADD THESE MISSING METHODS:
    
    public static void logInfo(String message) {
        try {
            startStep("ℹ️ INFO: " + message);
            endStep(Status.PASSED);
        } catch (Exception e) {
            System.out.println("❌ Error logging info: " + e.getMessage());
        }
    }
    
    public static void logPass(String message) {
        try {
            startStep("✅ PASS: " + message);
            endStep(Status.PASSED);
        } catch (Exception e) {
            System.out.println("❌ Error logging pass: " + e.getMessage());
        }
    }
    
    public static void logFail(String message) {
        try {
            startStep("❌ FAIL: " + message);
            endStep(Status.FAILED);
        } catch (Exception e) {
            System.out.println("❌ Error logging fail: " + e.getMessage());
        }
    }
    
    private static void startStep(String stepName) {
        try {
            String uuid = UUID.randomUUID().toString();
            currentStepUuid.set(uuid);
            
            StepResult stepResult = new StepResult()
                    .setName(stepName)
                    .setStart(System.currentTimeMillis());
            
            io.qameta.allure.Allure.getLifecycle().startStep(uuid, stepResult);
            
        } catch (Exception e) {
            System.out.println("❌ Error starting Allure step: " + e.getMessage());
        }
    }
    
    private static void endStep(Status status) {
        try {
            String uuid = currentStepUuid.get();
            if (uuid != null) {
                io.qameta.allure.Allure.getLifecycle().updateStep(uuid, step -> 
                    step.setStatus(status).setStop(System.currentTimeMillis()));
                io.qameta.allure.Allure.getLifecycle().stopStep(uuid);
                currentStepUuid.remove();
            }
        } catch (Exception e) {
            System.out.println("❌ Error ending Allure step: " + e.getMessage());
        }
    }
    
    public static void addScreenshot(WebDriver driver, String name) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
                System.out.println("📸 Allure screenshot added: " + name);
            }
        } catch (Exception e) {
            System.out.println("❌ Error adding Allure screenshot: " + e.getMessage());
        }
    }
    
    public static void addAttachment(String name, String content, String type) {
        try {
            Allure.addAttachment(name, type, content);
            System.out.println("📎 Allure attachment added: " + name);
        } catch (Exception e) {
            System.out.println("❌ Error adding Allure attachment: " + e.getMessage());
        }
    }
    
    private static String getBrowserVersion() {
        try {
            WebDriver driver = BrowserUtils.getDriver();
            if (driver != null) {
                if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
                    return String.valueOf(((org.openqa.selenium.chrome.ChromeDriver) driver)
                        .getCapabilities().getCapability("browserVersion"));
                } else if (driver instanceof org.openqa.selenium.edge.EdgeDriver) {
                    return String.valueOf(((org.openqa.selenium.edge.EdgeDriver) driver)
                        .getCapabilities().getCapability("browserVersion"));
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return "Unknown";
    }
}