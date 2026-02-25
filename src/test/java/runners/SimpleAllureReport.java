package runners;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SimpleAllureReport {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("CREATING SIMPLE ALLURE REPORT");
        System.out.println("=========================================");
        
        String resultsPath = "target/allure-results";
        String reportPath = "target/allure-report";
        
        // Check if results exist
        File resultsDir = new File(resultsPath);
        if (!resultsDir.exists()) {
            System.out.println("❌ No allure-results folder found!");
            System.out.println("Please run your tests first.");
            return;
        }
        
        File[] resultFiles = resultsDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (resultFiles == null || resultFiles.length == 0) {
            System.out.println("❌ No JSON result files found in " + resultsPath);
            return;
        }
        
        System.out.println("✅ Found " + resultFiles.length + " result files");
        
        // Create a simple HTML report manually
        try {
            createSimpleHtmlReport(resultsPath, reportPath);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    private static void createSimpleHtmlReport(String resultsPath, String reportPath) throws Exception {
        Path reportDir = Paths.get(reportPath);
        Files.createDirectories(reportDir);
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <title>Allure Report</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial; margin: 20px; }\n");
        html.append("        .pass { color: green; }\n");
        html.append("        .fail { color: red; }\n");
        html.append("        .container { max-width: 1200px; margin: auto; }\n");
        html.append("        .header { background: #f0f0f0; padding: 10px; }\n");
        html.append("        .test { border: 1px solid #ddd; margin: 5px 0; padding: 10px; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class='container'>\n");
        html.append("        <div class='header'>\n");
        html.append("            <h1>Test Execution Report</h1>\n");
        
        // Add environment info
        File envFile = new File(resultsPath + "/environment.properties");
        if (envFile.exists()) {
            html.append("            <h3>Environment:</h3>\n");
            Files.lines(envFile.toPath()).forEach(line -> {
                html.append("            <p>").append(line).append("</p>\n");
            });
        }
        
        html.append("        </div>\n");
        
        // List test files
        html.append("        <h2>Test Results</h2>\n");
        try (Stream<Path> paths = Files.walk(Paths.get(resultsPath))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".json"))
                 .forEach(p -> {
                     html.append("        <div class='test'>\n");
                     html.append("            <h3>").append(p.getFileName()).append("</h3>\n");
                     try {
                         String content = Files.readString(p);
                         if (content.contains("\"status\":\"passed\"")) {
                             html.append("            <p class='pass'>✅ PASSED</p>\n");
                         } else if (content.contains("\"status\":\"failed\"")) {
                             html.append("            <p class='fail'>❌ FAILED</p>\n");
                         }
                     } catch (Exception e) {
                         html.append("            <p>Error reading file</p>\n");
                     }
                     html.append("        </div>\n");
                 });
        }
        
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        Files.writeString(Paths.get(reportPath + "/index.html"), html.toString());
        System.out.println("✅ Simple HTML report created at: " + reportPath + "/index.html");
        
        // Open in browser
        try {
            Runtime.getRuntime().exec("cmd /c start " + reportPath + "/index.html");
        } catch (Exception e) {
            System.out.println("Please open manually: " + reportPath + "/index.html");
        }
    }
}