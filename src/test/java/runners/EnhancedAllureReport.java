package runners;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EnhancedAllureReport {
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("ENHANCED ALLURE REPORT GENERATOR");
        System.out.println("=========================================");
        
        String resultsPath = "target/allure-results";
        String reportPath = "target/allure-report-enhanced";
        
        try {
            // Create report directory
            new File(reportPath).mkdirs();
            
            // Parse results and generate enhanced HTML
            generateEnhancedReport(resultsPath, reportPath);
            
            System.out.println("\n✅ Enhanced report generated: " + reportPath + "/index.html");
            
            // Open in browser
            try {
                Runtime.getRuntime().exec("cmd /c start " + reportPath + "/index.html");
            } catch (Exception e) {
                System.out.println("Please open manually: " + reportPath + "/index.html");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void generateEnhancedReport(String resultsPath, String reportPath) throws Exception {
        List<TestResult> results = parseResults(resultsPath);
        
        String html = generateHtml(results, resultsPath);
        Files.write(Paths.get(reportPath, "index.html"), html.getBytes());
        
        // Copy any screenshot attachments
        copyAttachments(resultsPath, reportPath);
    }
    
    private static List<TestResult> parseResults(String resultsPath) {
        List<TestResult> results = new ArrayList<>();
        File dir = new File(resultsPath);
        
        if (!dir.exists()) return results;
        
        File[] jsonFiles = dir.listFiles((d, name) -> name.endsWith("-result.json"));
        if (jsonFiles == null) return results;
        
        for (File file : jsonFiles) {
            try {
                String content = readFile(file);
                TestResult result = new TestResult();
                result.id = file.getName();
                result.name = extractValue(content, "name");
                result.status = extractValue(content, "status");
                result.fullName = extractValue(content, "fullName");
                
                // Skip TestNG setup methods
                if (result.name.contains("setUpClass") || result.name.contains("tearDownClass")) {
                    continue;
                }
                
                // Look for attachments
                String baseId = result.id.replace("-result.json", "");
                File[] attachments = dir.listFiles((d, name) -> 
                    name.contains(baseId) && (name.endsWith(".png") || name.endsWith(".txt"))
                );
                
                if (attachments != null) {
                    for (File att : attachments) {
                        result.attachments.add(att.getName());
                    }
                }
                
                results.add(result);
                
            } catch (Exception e) {
                System.out.println("Error parsing " + file.getName() + ": " + e.getMessage());
            }
        }
        
        return results;
    }
    
    private static String generateHtml(List<TestResult> results, String resultsPath) {
        StringBuilder html = new StringBuilder();
        
        // Calculate stats
        int total = results.size();
        int passed = (int) results.stream().filter(r -> "passed".equals(r.status)).count();
        int failed = (int) results.stream().filter(r -> "failed".equals(r.status)).count();
        int broken = (int) results.stream().filter(r -> "broken".equals(r.status)).count();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>Enhanced Allure Report</title>\n");
        html.append("    <style>\n");
        html.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
        html.append("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f7fa; }\n");
        html.append("        .navbar { background: #1a237e; color: white; padding: 1rem 2rem; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
        html.append("        .navbar h1 { font-size: 1.5rem; display: flex; align-items: center; gap: 10px; }\n");
        html.append("        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }\n");
        
        // Stats cards
        html.append("        .stats-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 1.5rem; margin-bottom: 2rem; }\n");
        html.append("        .stat-card { background: white; padding: 1.5rem; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }\n");
        html.append("        .stat-card h3 { color: #5f6368; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.5rem; }\n");
        html.append("        .stat-card .value { font-size: 2rem; font-weight: 600; }\n");
        html.append("        .stat-card .trend { font-size: 0.85rem; color: #5f6368; margin-top: 0.5rem; }\n");
        
        // Environment panel
        html.append("        .environment-panel { background: white; border-radius: 10px; padding: 1.5rem; margin-bottom: 2rem; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }\n");
        html.append("        .env-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 1rem; margin-top: 1rem; }\n");
        html.append("        .env-item { padding: 0.75rem; background: #f8f9fa; border-radius: 6px; }\n");
        html.append("        .env-label { color: #5f6368; font-size: 0.8rem; }\n");
        html.append("        .env-value { font-weight: 500; margin-top: 0.25rem; }\n");
        
        // Test list
        html.append("        .test-list { background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }\n");
        html.append("        .test-header { display: grid; grid-template-columns: 100px 1fr 120px 100px; padding: 1rem; background: #f8f9fa; border-bottom: 2px solid #e0e0e0; font-weight: 600; color: #5f6368; }\n");
        html.append("        .test-row { display: grid; grid-template-columns: 100px 1fr 120px 100px; padding: 1rem; border-bottom: 1px solid #e0e0e0; cursor: pointer; transition: background 0.2s; }\n");
        html.append("        .test-row:hover { background: #f5f5f5; }\n");
        html.append("        .status-badge { padding: 0.25rem 0.75rem; border-radius: 20px; font-size: 0.85rem; font-weight: 500; display: inline-block; text-align: center; }\n");
        html.append("        .status-passed { background: #e6f4ea; color: #137333; }\n");
        html.append("        .status-failed { background: #fce8e6; color: #c5221f; }\n");
        html.append("        .status-broken { background: #fef7e0; color: #b45f06; }\n");
        html.append("        .test-details { display: none; padding: 2rem; background: #f8f9fa; border-top: 1px solid #e0e0e0; }\n");
        html.append("        .test-details.active { display: block; }\n");
        html.append("        .attachment-list { list-style: none; display: flex; gap: 10px; flex-wrap: wrap; }\n");
        html.append("        .attachment-item { padding: 0.5rem 1rem; background: white; border-radius: 20px; border: 1px solid #e0e0e0; font-size: 0.85rem; }\n");
        html.append("        .attachment-item img { max-width: 200px; margin-top: 10px; }\n");
        html.append("        .footer { text-align: right; margin-top: 2rem; color: #5f6368; font-size: 0.85rem; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // Navbar
        html.append("    <div class='navbar'>\n");
        html.append("        <h1>📊 Enhanced Allure Test Report</h1>\n");
        html.append("    </div>\n");
        
        html.append("    <div class='container'>\n");
        
        // Environment section
        html.append("        <div class='environment-panel'>\n");
        html.append("            <h2 style='margin-bottom: 1rem;'>Environment</h2>\n");
        html.append("            <div class='env-grid'>\n");
        
        // Read environment.properties
        try {
            Path envFile = Paths.get(resultsPath, "environment.properties");
            if (Files.exists(envFile)) {
                List<String> lines = Files.readAllLines(envFile);
                for (String line : lines) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        html.append("                <div class='env-item'>\n");
                        html.append("                    <div class='env-label'>").append(parts[0]).append("</div>\n");
                        html.append("                    <div class='env-value'>").append(parts[1]).append("</div>\n");
                        html.append("                </div>\n");
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        // Stats
        html.append("        <div class='stats-grid'>\n");
        html.append("            <div class='stat-card'>\n");
        html.append("                <h3>Total Tests</h3>\n");
        html.append("                <div class='value'>").append(total).append("</div>\n");
        html.append("                <div class='trend'>All scenarios</div>\n");
        html.append("            </div>\n");
        html.append("            <div class='stat-card'>\n");
        html.append("                <h3>Passed</h3>\n");
        html.append("                <div class='value' style='color: #137333;'>").append(passed).append("</div>\n");
        html.append("                <div class='trend'>").append(total > 0 ? Math.round((passed * 100.0) / total) : 0).append("% success</div>\n");
        html.append("            </div>\n");
        html.append("            <div class='stat-card'>\n");
        html.append("                <h3>Failed</h3>\n");
        html.append("                <div class='value' style='color: #c5221f;'>").append(failed).append("</div>\n");
        html.append("                <div class='trend'>Need attention</div>\n");
        html.append("            </div>\n");
        html.append("            <div class='stat-card'>\n");
        html.append("                <h3>Broken</h3>\n");
        html.append("                <div class='value' style='color: #b45f06;'>").append(broken).append("</div>\n");
        html.append("                <div class='trend'>Setup issues</div>\n");
        html.append("            </div>\n");
        html.append("            <div class='stat-card'>\n");
        html.append("                <h3>Duration</h3>\n");
        html.append("                <div class='value'>").append(results.size()).append("s</div>\n");
        html.append("                <div class='trend'>Total time</div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        
        // Test list header
        html.append("        <div class='test-list'>\n");
        html.append("            <div class='test-header'>\n");
        html.append("                <div>Status</div>\n");
        html.append("                <div>Test Name</div>\n");
        html.append("                <div>Duration</div>\n");
        html.append("                <div>Actions</div>\n");
        html.append("            </div>\n");
        
        // Test rows
        for (int i = 0; i < results.size(); i++) {
            TestResult r = results.get(i);
            String statusClass = "status-" + (r.status != null ? r.status : "unknown");
            
            html.append("            <div class='test-row' onclick='toggleDetails(").append(i).append(")'>\n");
            html.append("                <div><span class='status-badge ").append(statusClass).append("'>")
                .append(r.status != null ? r.status.toUpperCase() : "UNKNOWN").append("</span></div>\n");
            html.append("                <div>").append(escapeHtml(r.name)).append("</div>\n");
            html.append("                <div>-</div>\n");
            html.append("                <div>🔽 Details</div>\n");
            html.append("            </div>\n");
            
            // Details panel
            html.append("            <div id='details-").append(i).append("' class='test-details'>\n");
            html.append("                <h3 style='margin-bottom: 1rem;'>Test Details</h3>\n");
            html.append("                <p><strong>Full Name:</strong> ").append(escapeHtml(r.fullName)).append("</p>\n");
            html.append("                <p><strong>Test ID:</strong> ").append(r.id).append("</p>\n");
            
            // Attachments
            if (!r.attachments.isEmpty()) {
                html.append("                <h4 style='margin: 1rem 0 0.5rem;'>Attachments</h4>\n");
                html.append("                <ul class='attachment-list'>\n");
                for (String attachment : r.attachments) {
                    html.append("                    <li class='attachment-item'>📎 ").append(attachment).append("</li>\n");
                }
                html.append("                </ul>\n");
            }
            
            html.append("            </div>\n");
        }
        
        html.append("        </div>\n");
        
        // Footer
        html.append("        <div class='footer'>\n");
        html.append("            Generated: ").append(new Date()).append("\n");
        html.append("        </div>\n");
        
        html.append("    </div>\n");
        
        // JavaScript
        html.append("    <script>\n");
        html.append("        function toggleDetails(index) {\n");
        html.append("            var details = document.getElementById('details-' + index);\n");
        html.append("            details.classList.toggle('active');\n");
        html.append("        }\n");
        html.append("    </script>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private static void copyAttachments(String resultsPath, String reportPath) throws IOException {
        File resultsDir = new File(resultsPath);
        File reportDir = new File(reportPath);
        
        if (resultsDir.exists()) {
            File[] attachments = resultsDir.listFiles((d, name) -> 
                name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")
            );
            
            if (attachments != null) {
                for (File attachment : attachments) {
                    Files.copy(attachment.toPath(), 
                              new File(reportDir, attachment.getName()).toPath(),
                              StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    
    private static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
    
    private static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start > 0) {
            start += pattern.length();
            int end = json.indexOf("\"", start);
            if (end > start) {
                return json.substring(start, end);
            }
        }
        return "N/A";
    }
    
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    static class TestResult {
        String id;
        String name;
        String status;
        String fullName;
        List<String> attachments = new ArrayList<>();
    }
}