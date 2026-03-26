package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
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
    private static boolean isInitialized = false;
    private static String currentTestName = "";

    // Initialize Extent Report at the start
    static {
        createInstance();
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static ExtentReports createInstance() {
        try {
            // Clean old reports first
            cleanupOldReports();
            
            // Generate daily folder path
            String dailyFolder = getDailyFolderPath();
            
            // Create daily report directory if it doesn't exist
            File reportDir = new File(dailyFolder);
            if (!reportDir.exists()) {
                boolean created = reportDir.mkdirs();
                if (created) {
                    System.out.println("✅ Created report directory: " + dailyFolder);
                }
            }

            // Generate report file name with timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = dailyFolder + "ERP_Report_" + timeStamp + ".html";
            currentReportPath = reportPath;

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            // Configure Spark reporter
            sparkReporter.config().setDocumentTitle(TestConfig.DOCUMENT_TITLE);
            sparkReporter.config().setReportName(TestConfig.REPORT_NAME);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
            sparkReporter.config().setCss(".badge-primary {background-color: #2196F3;}");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // System information
            extent.setSystemInfo("Organization", "Vibgyor Schools");
            extent.setSystemInfo("Project", "ERP Automation");
            extent.setSystemInfo("Environment", "Pre-Production");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", TestConfig.BROWSER.toUpperCase());
            extent.setSystemInfo("URL", TestConfig.BASE_URL);
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Execution Time", new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
            
            // Add Jenkins information if available
            if (System.getenv("BUILD_NUMBER") != null) {
                extent.setSystemInfo("Jenkins Build", System.getenv("BUILD_NUMBER"));
                extent.setSystemInfo("Jenkins Job", System.getenv("JOB_NAME"));
            }

            isInitialized = true;
            System.out.println("✅ Extent Report initialized: " + reportPath);
            
        } catch (Exception e) {
            System.out.println("❌ Error initializing Extent Report: " + e.getMessage());
            e.printStackTrace();
        }
        return extent;
    }

    // Method to clean old reports (keep only last 5 reports)
    private static void cleanupOldReports() {
        try {
            String basePath = TestConfig.EXTENT_REPORT_PATH;
            File baseDir = new File(basePath);
            
            if (baseDir.exists() && baseDir.isDirectory()) {
                // Get all date folders
                File[] dateFolders = baseDir.listFiles(File::isDirectory);
                
                if (dateFolders != null) {
                    for (File dateFolder : dateFolders) {
                        // Clean up old report files in each date folder
                        File[] reportFiles = dateFolder.listFiles((dir, name) -> 
                            name.startsWith("ERP_Report_") && name.endsWith(".html"));
                        
                        if (reportFiles != null && reportFiles.length > 5) {
                            Arrays.sort(reportFiles, Comparator.comparingLong(File::lastModified));
                            for (int i = 0; i < reportFiles.length - 5; i++) {
                                boolean deleted = reportFiles[i].delete();
                                if (deleted) {
                                    System.out.println("Deleted old report: " + reportFiles[i].getName());
                                }
                            }
                        }
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
            // Ensure extent is initialized
            getInstance();
            
            // Store current test name
            currentTestName = testName;
            
            ExtentTest extentTest = extent.createTest(testName);
            test.set(extentTest);
            step.set(extentTest);
            System.out.println("📋 Test created in Extent Report: " + testName);
            
        } catch (Exception e) {
            System.out.println("❌ Error creating test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void logStep(String stepDescription, Status status) {
        try {
            if (step.get() != null) {
                step.get().log(status, stepDescription);
                System.out.println(getStatusSymbol(status) + " " + stepDescription);
            } else {
                System.out.println("⚠️ ExtentTest is null, cannot log step: " + stepDescription);
            }
        } catch (Exception e) {
            System.out.println("❌ Error logging step: " + e.getMessage());
        }
    }
    
    private static String getStatusSymbol(Status status) {
        switch(status) {
            case PASS: return "✅";
            case FAIL: return "❌";
            case INFO: return "ℹ️";
            case WARNING: return "⚠️";
            default: return "📝";
        }
    }

    public static void passStep(String stepDescription) {
        logStep(stepDescription, Status.PASS);
    }

    public static void failStep(String stepDescription) {
        logStep(stepDescription, Status.FAIL);
    }

    public static void infoStep(String stepDescription) {
        logStep(stepDescription, Status.INFO);
    }

    public static void warningStep(String stepDescription) {
        logStep(stepDescription, Status.WARNING);
    }

    public static void addScreenshotBase64(String base64Screenshot) {
        addScreenshotBase64("Screenshot", base64Screenshot);
    }

    public static void addScreenshotBase64(String title, String base64Screenshot) {
        try {
            if (test.get() != null && base64Screenshot != null) {
                test.get().info(title, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                System.out.println("📸 Screenshot added with title: " + title);
            } else if (test.get() == null) {
                System.out.println("⚠️ Cannot add screenshot - test is null");
            }
        } catch (Exception e) {
            System.out.println("❌ Error adding Base64 screenshot with title: " + e.getMessage());
        }
    }

    public static void passTest(String testDescription) {
        try {
            if (test.get() != null) {
                test.get().pass(MarkupHelper.createLabel(testDescription, ExtentColor.GREEN));
                System.out.println("🎉 Test Passed: " + testDescription);
            } else {
                System.out.println("⚠️ Cannot mark test as passed - test is null");
            }
        } catch (Exception e) {
            System.out.println("❌ Error marking test as passed: " + e.getMessage());
        }
    }

    public static void failTest(String testDescription) {
        try {
            if (test.get() != null) {
                test.get().fail(MarkupHelper.createLabel(testDescription, ExtentColor.RED));
                System.out.println("💥 Test Failed: " + testDescription);
            } else {
                System.out.println("⚠️ Cannot mark test as failed - test is null");
            }
        } catch (Exception e) {
            System.out.println("❌ Error marking test as failed: " + e.getMessage());
        }
    }

    public static void endTest() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("📊 Extent Report saved to: " + currentReportPath);
                
                // Also print the absolute path for easy access
                File reportFile = new File(currentReportPath);
                System.out.println("📊 Report absolute path: " + reportFile.getAbsolutePath());
                
                // Verify if report was actually created
                if (reportFile.exists()) {
                    System.out.println("✅ Report file created successfully! Size: " + reportFile.length() + " bytes");
                } else {
                    System.out.println("❌ Report file was not created!");
                }
            } else {
                System.out.println("⚠️ Extent is null, cannot flush report");
            }
        } catch (Exception e) {
            System.out.println("❌ Error ending test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getCurrentReportPath() {
        return currentReportPath;
    }

    public static void initializeReport() {
        if (extent == null) {
            createInstance();
        } else {
            System.out.println("✅ Extent Report already initialized");
        }
    }
    
    public static boolean isReportInitialized() {
        return isInitialized;
    }
    
    public static void flushReport() {
        endTest();
    }
}