package runners;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DebugAllureResults {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("DEBUGGING ALLURE RESULTS");
        System.out.println("=========================================");
        
        String resultsPath = "target/allure-results";
        File resultsDir = new File(resultsPath);
        
        if (!resultsDir.exists()) {
            System.out.println("❌ Allure results directory not found!");
            return;
        }
        
        try (Stream<Path> paths = Files.walk(Paths.get(resultsPath))) {
            paths.filter(p -> p.toString().endsWith("-result.json"))
                 .forEach(path -> {
                     try {
                         System.out.println("\n📄 File: " + path.getFileName());
                         String content = Files.readString(path);
                         
                         // Check if file is empty
                         if (content.trim().isEmpty()) {
                             System.out.println("   ❌ File is EMPTY!");
                             return;
                         }
                         
                         System.out.println("   File size: " + content.length() + " bytes");
                         System.out.println("   First 200 characters:");
                         System.out.println("   " + content.substring(0, Math.min(200, content.length())));
                         
                         // Check for key fields
                         System.out.println("\n   Key fields found:");
                         if (content.contains("\"name\"")) {
                             String name = extractValue(content, "name");
                             System.out.println("   - name: " + name);
                         }
                         if (content.contains("\"status\"")) {
                             String status = extractValue(content, "status");
                             System.out.println("   - status: " + status);
                         }
                         if (content.contains("\"steps\"")) {
                             System.out.println("   - steps: present");
                         }
                         
                     } catch (Exception e) {
                         System.out.println("   ❌ Error reading file: " + e.getMessage());
                     }
                 });
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=========================================");
    }
    
    private static String extractValue(String content, String key) {
        try {
            String pattern = "\"" + key + "\":\"";
            int start = content.indexOf(pattern);
            if (start > 0) {
                start += pattern.length();
                int end = content.indexOf("\"", start);
                if (end > start) {
                    return content.substring(start, end);
                }
            }
            
            // Try without quotes for non-string values
            pattern = "\"" + key + "\":";
            start = content.indexOf(pattern);
            if (start > 0) {
                start += pattern.length();
                int end = content.indexOf(",", start);
                if (end == -1) end = content.indexOf("}", start);
                if (end > start) {
                    return content.substring(start, end).trim();
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return "Not found";
    }
}