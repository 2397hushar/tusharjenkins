package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.TestConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.Comparator;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> step = new ThreadLocal<>();
    private static String currentReportPath;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static ExtentReports createInstance() {
        try {
            // Generate daily folder path
            String dailyFolder = getDailyFolderPath();
            
            // Create daily report directory if it doesn't exist
            File reportDir = new File(dailyFolder);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            // Clean old reports before creating new one
            cleanOldReports(dailyFolder);

            // Generate report file name with timestamp
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String reportPath = dailyFolder + "ExtentReport_" + timeStamp + ".html";
            currentReportPath = reportPath;

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            // Configure Spark reporter
            sparkReporter.config().setDocumentTitle(TestConfig.DOCUMENT_TITLE);
            sparkReporter.config().setReportName(TestConfig.REPORT_NAME);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // System information
            extent.setSystemInfo("Organization", "SauceDemo");
            extent.setSystemInfo("Project", "Automation Framework");
            extent.setSystemInfo("Environment", "Test");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", TestConfig.BROWSER.toUpperCase());
            extent.setSystemInfo("URL", TestConfig.BASE_URL);
            extent.setSystemInfo("User", System.getProperty("user.name"));

            System.out.println("Extent Report initialized: " + reportPath);
            
        } catch (Exception e) {
            System.out.println("Error initializing Extent Report: " + e.getMessage());
            e.printStackTrace();
        }
        return extent;
    }

    // Method to clean old reports
    private static void cleanOldReports(String dailyFolder) {
        try {
            File folder = new File(dailyFolder);
            if (folder.exists() && folder.isDirectory()) {
                File[] reportFiles = folder.listFiles((dir, name) -> 
                    name.startsWith("ExtentReport_") && name.endsWith(".html"));
                if (reportFiles != null && reportFiles.length > 3) {
                    Arrays.sort(reportFiles, Comparator.comparingLong(File::lastModified));
                    for (int i = 0; i < reportFiles.length - 3; i++) {
                        reportFiles[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error cleaning old reports: " + e.getMessage());
        }
    }

    private static String getDailyFolderPath() {
        String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return TestConfig.EXTENT_REPORT_PATH + dateFolder + File.separator;
    }

    public static void createTest(String testName) {
        try {
            ExtentTest extentTest = getInstance().createTest(testName);
            test.set(extentTest);
            step.set(extentTest);
            infoStep("Test Started: " + testName);
        } catch (Exception e) {
            System.out.println("Error creating test: " + e.getMessage());
        }
    }

    public static void passStep(String stepDescription) {
        try {
            if (step.get() != null) {
                step.get().log(Status.PASS, stepDescription);
            }
        } catch (Exception e) {
            System.out.println("Error logging passed step: " + e.getMessage());
        }
    }

    public static void failStep(String stepDescription) {
        try {
            if (step.get() != null) {
                step.get().log(Status.FAIL, stepDescription);
            }
        } catch (Exception e) {
            System.out.println("Error logging failed step: " + e.getMessage());
        }
    }

    public static void infoStep(String stepDescription) {
        try {
            if (step.get() != null) {
                step.get().log(Status.INFO, stepDescription);
            }
        } catch (Exception e) {
            System.out.println("Error logging info step: " + e.getMessage());
        }
    }

    // METHOD 1: Add screenshot using Base64 (RECOMMENDED - No file path issues)
    public static void addScreenshotBase64(String base64Screenshot) {
        try {
            if (test.get() != null && base64Screenshot != null) {
                test.get().info(MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                System.out.println("Base64 screenshot added to report");
            }
        } catch (Exception e) {
            System.out.println("Error adding Base64 screenshot: " + e.getMessage());
        }
    }

    // METHOD 2: Add screenshot with title using Base64
    public static void addScreenshotBase64(String title, String base64Screenshot) {
        try {
            if (test.get() != null && base64Screenshot != null) {
                test.get().info(title, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                System.out.println("Base64 screenshot with title added: " + title);
            }
        } catch (Exception e) {
            System.out.println("Error adding Base64 screenshot with title: " + e.getMessage());
        }
    }

    // METHOD 3: Add screenshot using file path (Fallback method)
    public static void addScreenshot(String screenshotPath) {
        try {
            if (test.get() != null && screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    // Use relative path from project root
                    String relativePath = getRelativePath(screenshotPath);
                    test.get().info(MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
                    System.out.println("Screenshot added via file path: " + relativePath);
                } else {
                    System.out.println("Screenshot file not found: " + screenshotPath);
                    // Fallback to Base64 if file not found
                    String base64Screenshot = BrowserUtils.getScreenshotAsBase64();
                    if (base64Screenshot != null) {
                        addScreenshotBase64(base64Screenshot);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error adding screenshot via file path: " + e.getMessage());
            // Fallback to Base64
            try {
                String base64Screenshot = BrowserUtils.getScreenshotAsBase64();
                if (base64Screenshot != null) {
                    addScreenshotBase64(base64Screenshot);
                }
            } catch (Exception ex) {
                System.out.println("Fallback screenshot also failed: " + ex.getMessage());
            }
        }
    }

    // Helper method to get relative path
    private static String getRelativePath(String absolutePath) {
        try {
            if (absolutePath == null) return "";
            File currentDir = new File(System.getProperty("user.dir"));
            File file = new File(absolutePath);
            
            if (file.getAbsolutePath().startsWith(currentDir.getAbsolutePath())) {
                return file.getAbsolutePath().substring(currentDir.getAbsolutePath().length() + 1)
                          .replace("\\", "/");
            }
            return absolutePath;
        } catch (Exception e) {
            return absolutePath;
        }
    }

    public static void passTest(String testDescription) {
        try {
            if (test.get() != null) {
                test.get().pass(MarkupHelper.createLabel(testDescription, ExtentColor.GREEN));
            }
        } catch (Exception e) {
            System.out.println("Error marking test as passed: " + e.getMessage());
        }
    }

    public static void failTest(String testDescription) {
        try {
            if (test.get() != null) {
                test.get().fail(MarkupHelper.createLabel(testDescription, ExtentColor.RED));
            }
        } catch (Exception e) {
            System.out.println("Error marking test as failed: " + e.getMessage());
        }
    }

    public static void endTest() {
        try {
            if (extent != null) {
                extent.flush();
            }
        } catch (Exception e) {
            System.out.println("Error ending test: " + e.getMessage());
        }
    }

    public static String getCurrentReportPath() {
        return currentReportPath;
    }
}

