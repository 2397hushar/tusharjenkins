package runners;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class AllureReportViewer {
    
    private static List<TestResult> testResults = new ArrayList<>();
    
    static class TestResult {
        String id;
        String name;
        String status;
        String fullName;
        long start;
        long stop;
        List<String> steps = new ArrayList<>();
        List<String> attachments = new ArrayList<>();
        
        long getDuration() {
            return stop - start;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("ALLURE REPORT VIEWER");
        System.out.println("=========================================");
        
        String resultsPath = "target/allure-results";
        
        // Parse all result files
        parseAllureResults(resultsPath);
        
        // Generate HTML report
        generateHtmlReport(resultsPath);
        
        System.out.println("✅ Report generated at: target/allure-report/index.html");
        
        // Open in browser
        try {
            Runtime.getRuntime().exec("cmd /c start target/allure-report/index.html");
        } catch (Exception e) {
            System.out.println("Please open manually: target/allure-report/index.html");
        }
    }
    
    private static void parseAllureResults(String resultsPath) {
        System.out.println("\n📊 Parsing Allure results...");
        
        try (Stream<Path> paths = Files.walk(Paths.get(resultsPath))) {
            paths.filter(p -> p.toString().endsWith("-result.json"))
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         TestResult result = new TestResult();
                         result.id = path.getFileName().toString();
                         
                         // Extract basic info
                         result.name = extractJsonValue(content, "name");
                         result.status = extractJsonValue(content, "status");
                         result.fullName = extractJsonValue(content, "fullName");
                         
                         // Extract timestamps
                         String startStr = extractJsonValue(content, "start");
                         String stopStr = extractJsonValue(content, "stop");
                         
                         try {
                             result.start = Long.parseLong(startStr);
                             result.stop = Long.parseLong(stopStr);
                         } catch (Exception e) {
                             result.start = 0;
                             result.stop = 0;
                         }
                         
                         // Look for attachments
                         String baseName = path.getFileName().toString().replace("-result.json", "");
                         try (Stream<Path> attachments = Files.walk(Paths.get(resultsPath))) {
                             attachments.filter(a -> a.toString().contains(baseName))
                                       .filter(a -> a.toString().endsWith("-attachment.txt") || 
                                                   a.toString().endsWith("-attachment.png"))
                                       .forEach(a -> result.attachments.add(a.getFileName().toString()));
                         }
                         
                         testResults.add(result);
                         System.out.println("  ✅ Found test: " + result.name + " - " + result.status);
                         
                     } catch (Exception e) {
                         System.out.println("  ❌ Error parsing " + path.getFileName() + ": " + e.getMessage());
                     }
                 });
            
        } catch (Exception e) {
            System.out.println("❌ Error parsing results: " + e.getMessage());
        }
        
        System.out.println("\n📊 Summary:");
        System.out.println("  Total tests: " + testResults.size());
        System.out.println("  Passed: " + testResults.stream().filter(t -> "passed".equals(t.status)).count());
        System.out.println("  Failed: " + testResults.stream().filter(t -> "failed".equals(t.status)).count());
        System.out.println("  Broken: " + testResults.stream().filter(t -> "broken".equals(t.status)).count());
    }
    
    private static String extractJsonValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\":\"";
            int start = json.indexOf(pattern);
            if (start > 0) {
                start += pattern.length();
                int end = json.indexOf("\"", start);
                if (end > start) {
                    return json.substring(start, end);
                }
            }
            
            // Try without quotes
            pattern = "\"" + key + "\":";
            start = json.indexOf(pattern);
            if (start > 0) {
                start += pattern.length();
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                if (end > start) {
                    return json.substring(start, end).trim();
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return "N/A";
    }
    
    private static void generateHtmlReport(String resultsPath) {
        try {
            Path reportDir = Paths.get("target/allure-report");
            Files.createDirectories(reportDir);
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang='en'>\n");
            html.append("<head>\n");
            html.append("    <meta charset='UTF-8'>\n");
            html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
            html.append("    <title>Test Execution Report</title>\n");
            html.append("    <style>\n");
            html.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
            html.append("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f0f2f5; }\n");
            html.append("        .navbar { background: #1a73e8; color: white; padding: 1rem; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
            html.append("        .navbar h1 { margin: 0; font-size: 1.5rem; }\n");
            html.append("        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }\n");
            html.append("        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.5rem; margin-bottom: 2rem; }\n");
            html.append("        .stat-card { background: white; padding: 1.5rem; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
            html.append("        .stat-card h3 { color: #5f6368; font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.5rem; }\n");
            html.append("        .stat-card .value { font-size: 2.5rem; font-weight: 600; }\n");
            html.append("        .stat-card .label { color: #5f6368; font-size: 0.9rem; margin-top: 0.5rem; }\n");
            html.append("        .test-list { background: white; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden; }\n");
            html.append("        .test-header { display: grid; grid-template-columns: 100px 1fr 150px 150px; padding: 1rem; background: #f8f9fa; border-bottom: 2px solid #e0e0e0; font-weight: 600; color: #5f6368; }\n");
            html.append("        .test-row { display: grid; grid-template-columns: 100px 1fr 150px 150px; padding: 1rem; border-bottom: 1px solid #e0e0e0; cursor: pointer; transition: background 0.2s; }\n");
            html.append("        .test-row:hover { background: #f5f5f5; }\n");
            html.append("        .status-badge { padding: 0.25rem 0.75rem; border-radius: 50px; font-size: 0.85rem; font-weight: 500; display: inline-block; text-align: center; }\n");
            html.append("        .status-passed { background: #e6f4ea; color: #137333; }\n");
            html.append("        .status-failed { background: #fce8e6; color: #c5221f; }\n");
            html.append("        .status-broken { background: #fef7e0; color: #b45f06; }\n");
            html.append("        .status-skipped { background: #e8eaed; color: #5f6368; }\n");
            html.append("        .test-details { display: none; padding: 2rem; background: #f8f9fa; border-top: 1px solid #e0e0e0; }\n");
            html.append("        .test-details.active { display: block; }\n");
            html.append("        .details-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem; margin-bottom: 1.5rem; }\n");
            html.append("        .detail-item { background: white; padding: 1rem; border-radius: 8px; }\n");
            html.append("        .detail-label { color: #5f6368; font-size: 0.85rem; margin-bottom: 0.25rem; }\n");
            html.append("        .detail-value { font-weight: 500; }\n");
            html.append("        .attachment-list { list-style: none; }\n");
            html.append("        .attachment-item { padding: 0.5rem; background: white; margin: 0.5rem 0; border-radius: 4px; display: flex; align-items: center; }\n");
            html.append("        .attachment-icon { margin-right: 0.5rem; font-size: 1.2rem; }\n");
            html.append("        .attachment-link { color: #1a73e8; text-decoration: none; }\n");
            html.append("        .attachment-link:hover { text-decoration: underline; }\n");
            html.append("        .environment-panel { background: white; padding: 1.5rem; border-radius: 10px; margin-bottom: 2rem; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
            html.append("        .env-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 1rem; margin-top: 1rem; }\n");
            html.append("        .env-item { padding: 0.75rem; background: #f8f9fa; border-radius: 6px; }\n");
            html.append("        .env-key { color: #5f6368; font-size: 0.85rem; }\n");
            html.append("        .env-value { font-weight: 500; margin-top: 0.25rem; }\n");
            html.append("        .timestamp { color: #5f6368; font-size: 0.9rem; text-align: right; margin-top: 1rem; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            
            // Navbar
            html.append("    <div class='navbar'>\n");
            html.append("        <h1>📊 Test Execution Report</h1>\n");
            html.append("    </div>\n");
            
            html.append("    <div class='container'>\n");
            
            // Environment section
            html.append("        <div class='environment-panel'>\n");
            html.append("            <h2>Environment Information</h2>\n");
            html.append("            <div class='env-grid'>\n");
            
            // Try to read environment.properties
            Path envFile = Paths.get(resultsPath, "environment.properties");
            if (Files.exists(envFile)) {
                Files.lines(envFile).forEach(line -> {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        html.append("                <div class='env-item'>\n");
                        html.append("                    <div class='env-key'>").append(parts[0]).append("</div>\n");
                        html.append("                    <div class='env-value'>").append(parts[1]).append("</div>\n");
                        html.append("                </div>\n");
                    }
                });
            }
            
            html.append("            </div>\n");
            html.append("        </div>\n");
            
            // Statistics
            long total = testResults.size();
            long passed = testResults.stream().filter(t -> "passed".equals(t.status)).count();
            long failed = testResults.stream().filter(t -> "failed".equals(t.status)).count();
            long broken = testResults.stream().filter(t -> "broken".equals(t.status)).count();
            
            html.append("        <div class='stats-grid'>\n");
            html.append("            <div class='stat-card'>\n");
            html.append("                <h3>Total Tests</h3>\n");
            html.append("                <div class='value'>").append(total).append("</div>\n");
            html.append("            </div>\n");
            html.append("            <div class='stat-card'>\n");
            html.append("                <h3>Passed</h3>\n");
            html.append("                <div class='value' style='color: #137333;'>").append(passed).append("</div>\n");
            html.append("                <div class='label'>").append(total > 0 ? Math.round((passed * 100.0) / total) : 0).append("%</div>\n");
            html.append("            </div>\n");
            html.append("            <div class='stat-card'>\n");
            html.append("                <h3>Failed</h3>\n");
            html.append("                <div class='value' style='color: #c5221f;'>").append(failed).append("</div>\n");
            html.append("                <div class='label'>").append(total > 0 ? Math.round((failed * 100.0) / total) : 0).append("%</div>\n");
            html.append("            </div>\n");
            html.append("            <div class='stat-card'>\n");
            html.append("                <h3>Broken</h3>\n");
            html.append("                <div class='value' style='color: #b45f06;'>").append(broken).append("</div>\n");
            html.append("            </div>\n");
            html.append("        </div>\n");
            
            // Test list header
            html.append("        <div class='test-list'>\n");
            html.append("            <div class='test-header'>\n");
            html.append("                <div>Status</div>\n");
            html.append("                <div>Test Name</div>\n");
            html.append("                <div>Duration</div>\n");
            html.append("                <div>Details</div>\n");
            html.append("            </div>\n");
            
            // Test rows
            for (int i = 0; i < testResults.size(); i++) {
                TestResult result = testResults.get(i);
                String statusClass = "status-" + (result.status != null ? result.status : "unknown");
                String duration = formatDuration(result.getDuration());
                
                html.append("            <div class='test-row' onclick='toggleDetails(").append(i).append(")'>\n");
                html.append("                <div><span class='status-badge ").append(statusClass).append("'>")
                    .append(result.status != null ? result.status.toUpperCase() : "UNKNOWN").append("</span></div>\n");
                html.append("                <div>").append(result.name != null ? result.name : "Unknown").append("</div>\n");
                html.append("                <div>").append(duration).append("</div>\n");
                html.append("                <div>🔽 Click for details</div>\n");
                html.append("            </div>\n");
                
                // Details panel
                html.append("            <div id='details-").append(i).append("' class='test-details'>\n");
                html.append("                <div class='details-grid'>\n");
                html.append("                    <div class='detail-item'>\n");
                html.append("                        <div class='detail-label'>Full Name</div>\n");
                html.append("                        <div class='detail-value'>").append(result.fullName != null ? result.fullName : "N/A").append("</div>\n");
                html.append("                    </div>\n");
                html.append("                    <div class='detail-item'>\n");
                html.append("                        <div class='detail-label'>Test ID</div>\n");
                html.append("                        <div class='detail-value'>").append(result.id).append("</div>\n");
                html.append("                    </div>\n");
                html.append("                </div>\n");
                
                // Attachments
                if (!result.attachments.isEmpty()) {
                    html.append("                <h3>Attachments</h3>\n");
                    html.append("                <ul class='attachment-list'>\n");
                    for (String attachment : result.attachments) {
                        html.append("                    <li class='attachment-item'>\n");
                        html.append("                        <span class='attachment-icon'>📎</span>\n");
                        html.append("                        <a class='attachment-link' href='#' onclick='viewAttachment(\"")
                            .append(resultsPath).append("/").append(attachment).append("\")'>")
                            .append(attachment).append("</a>\n");
                        html.append("                    </li>\n");
                    }
                    html.append("                </ul>\n");
                }
                
                html.append("            </div>\n");
            }
            
            html.append("        </div>\n");
            
            // Timestamp
            html.append("        <div class='timestamp'>\n");
            html.append("            Report generated: ").append(new Date()).append("\n");
            html.append("        </div>\n");
            
            html.append("    </div>\n");
            
            // JavaScript
            html.append("    <script>\n");
            html.append("        function toggleDetails(index) {\n");
            html.append("            var details = document.getElementById('details-' + index);\n");
            html.append("            details.classList.toggle('active');\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        function viewAttachment(path) {\n");
            html.append("            // In a real environment, you might want to open the file\n");
            html.append("            alert('Attachment: ' + path + '\\n\\nIn a real environment, this would open the file.');\n");
            html.append("        }\n");
            html.append("    </script>\n");
            
            html.append("</body>\n");
            html.append("</html>\n");
            
            Files.writeString(reportDir.resolve("index.html"), html.toString());
            
        } catch (Exception e) {
            System.out.println("❌ Error generating HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String formatDuration(long ms) {
        if (ms < 1000) return ms + " ms";
        if (ms < 60000) return String.format("%.2f s", ms / 1000.0);
        long minutes = ms / 60000;
        long seconds = (ms % 60000) / 1000;
        return minutes + " min " + seconds + " s";
    }
}