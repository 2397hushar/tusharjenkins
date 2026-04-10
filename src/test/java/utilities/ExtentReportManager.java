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

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String currentReportPath;

    public static synchronized void initializeReport() {
        if (extent == null) {
            createInstance();
        }
    }

    public static synchronized ExtentReports createInstance() {
        try {
            System.out.println("=========================================");
            System.out.println("INITIALIZING EXTENT REPORT");
            System.out.println("=========================================");

            // ✅ Base directory
            String baseDir = "target/extent-reports/";

            // ✅ Create date folder (yyyy-MM-dd)
            String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String fullDirPath = baseDir + dateFolder + "/";

            File reportDir = new File(fullDirPath);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
                System.out.println("📁 Created directory: " + reportDir.getAbsolutePath());
            }

            // ✅ Screenshot folder inside date folder
            String screenshotDirPath = fullDirPath + "screenshots/";
            File screenshotDir = new File(screenshotDirPath);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                System.out.println("📸 Created screenshot directory: " + screenshotDir.getAbsolutePath());
            }

            // ✅ Timestamp for file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // ✅ Final report path
            String reportPath = fullDirPath + "ExtentReport_" + timeStamp + ".html";
            currentReportPath = reportPath;

            System.out.println("📊 Report Path: " + new File(reportPath).getAbsolutePath());

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(TestConfig.DOCUMENT_TITLE);
            sparkReporter.config().setReportName(TestConfig.REPORT_NAME);
            sparkReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            System.out.println("✅ Extent Report initialized");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return extent;
    }
    public static synchronized void createTest(String testName) {
        if (extent == null) {
            initializeReport();   // ✅ ensures report starts
        }
        test.set(extent.createTest(testName));
    }

    public static void logStep(String stepDescription, Status status) {
        try {
            ExtentTest currentTest = test.get();
            if (currentTest != null) {
                currentTest.log(status, stepDescription);
                System.out.println(getStatusSymbol(status) + " " + stepDescription);
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

    public static void addScreenshotBase64(String title, String base64Screenshot) {
        try {
            ExtentTest currentTest = test.get();
            if (currentTest != null && base64Screenshot != null && !base64Screenshot.isEmpty()) {
                currentTest.info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                System.out.println("📸 Screenshot attached: " + title);
            }
        } catch (Exception e) {
            System.out.println("❌ Error adding screenshot: " + e.getMessage());
        }
    }

    public static void addScreenshotBase64(String base64Screenshot) {
        addScreenshotBase64("Screenshot", base64Screenshot);
    }

    public static void passTest(String testDescription) {
        try {
            ExtentTest currentTest = test.get();
            if (currentTest != null) {
                currentTest.pass(MarkupHelper.createLabel(testDescription, ExtentColor.GREEN));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void failTest(String testDescription) {
        try {
            ExtentTest currentTest = test.get();
            if (currentTest != null) {
                currentTest.fail(MarkupHelper.createLabel(testDescription, ExtentColor.RED));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void endTest() {
        try {
            if (test.get() != null) {
                test.remove();
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public static String getCurrentReportPath() {
        return currentReportPath;
    }

    public static synchronized void flushReport() {
        try {
            System.out.println("=========================================");
            System.out.println("FLUSHING EXTENT REPORT");
            System.out.println("=========================================");
            
            if (extent != null) {
                extent.flush();
                System.out.println("✅ Extent.flush() completed");
                
                if (currentReportPath != null) {
                    File reportFile = new File(currentReportPath);
                    if (reportFile.exists()) {
                        System.out.println("✅ REPORT GENERATED SUCCESSFULLY!");
                        System.out.println("📍 Location: " + reportFile.getAbsolutePath());
                        System.out.println("📏 Size: " + reportFile.length() + " bytes");
                    } else {
                        System.out.println("❌ Report file not found!");
                        System.out.println("   Expected at: " + reportFile.getAbsolutePath());
                        
                        // Check if target directory exists
                        File targetDir = new File("target");
                        if (targetDir.exists()) {
                            System.out.println("   target directory exists");
                            File[] files = targetDir.listFiles();
                            System.out.println("   Files in target:");
                            for (File f : files) {
                                if (f.getName().contains("Extent") || f.getName().contains("Report")) {
                                    System.out.println("     - " + f.getName());
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("❌ Extent is null!");
            }
            System.out.println("=========================================");
        } catch (Exception e) {
            System.out.println("❌ Error flushing report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}