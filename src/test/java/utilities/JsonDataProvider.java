//package utilities;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.HashMap;
//import java.util.Map;
//
//public class JsonDataProvider {
//    
//    private static JSONArray usersArray;
//    private static boolean isInitialized = false;
//    
//    static {
//        initialize();
//    }
//    
//    private static void initialize() {
//        if (!isInitialized) {
//            loadUsersFromJson();
//            isInitialized = true;
//        }
//    }
//    
//    private static void loadUsersFromJson() {
//        try {
//            String jsonContent = null;
//            
//            // Try multiple possible paths for the JSON file
//            String[] possiblePaths = {
//                "src/test/resources/users.json",
//                "target/test-classes/users.json",
//                "./users.json"
//            };
//            
//            for (String path : possiblePaths) {
//                try {
//                    jsonContent = new String(Files.readAllBytes(Paths.get(path)));
//                    System.out.println("✅ JsonDataProvider: Found users.json at: " + path);
//                    break;
//                } catch (Exception e) {
//                    // Continue to next path
//                }
//            }
//            
//            if (jsonContent == null) {
//                // Try loading from classpath as last resort
//                try {
//                    InputStream is = JsonDataProvider.class.getClassLoader().getResourceAsStream("users.json");
//                    if (is != null) {
//                        jsonContent = new String(is.readAllBytes());
//                        System.out.println("✅ JsonDataProvider: Found users.json in classpath");
//                    }
//                } catch (Exception e) {
//                    System.out.println("❌ JsonDataProvider: Could not find users.json in any location");
//                }
//            }
//            
//            if (jsonContent != null) {
//                JSONObject jsonObject = new JSONObject(jsonContent);
//                usersArray = jsonObject.getJSONArray("users");
//                System.out.println("✅ JsonDataProvider: Loaded " + usersArray.length() + " users from JSON");
//            } else {
//                System.out.println("⚠️ JsonDataProvider: No users.json found, will use empty data set");
//                usersArray = new JSONArray();
//            }
//            
//        } catch (Exception e) {
//            System.out.println("❌ JsonDataProvider: Error loading users from JSON: " + e.getMessage());
//            usersArray = new JSONArray();
//        }
//    }
//    
//    // User Management Methods
//    public static JSONArray getUsers() {
//        return usersArray;
//    }
//    
//    public static int getUserCount() {
//        return usersArray != null ? usersArray.length() : 0;
//    }
//    
//    public static boolean hasUsers() {
//        return usersArray != null && usersArray.length() > 0;
//    }
//    
//    public static String getUsername(int index) {
//        if (isValidIndex(index)) {
//            return usersArray.getJSONObject(index).getString("username");
//        }
//        return null;
//    }
//    
//    public static String getPassword(int index) {
//        if (isValidIndex(index)) {
//            return usersArray.getJSONObject(index).getString("password");
//        }
//        return null;
//    }
//    
//    public static String getDescription(int index) {
//        if (isValidIndex(index)) {
//            return usersArray.getJSONObject(index).getString("description");
//        }
//        return "User " + (index + 1);
//    }
//    
//    public static Map<String, String> getUserData(int index) {
//        Map<String, String> userData = new HashMap<>();
//        if (isValidIndex(index)) {
//            JSONObject user = usersArray.getJSONObject(index);
//            userData.put("username", user.getString("username"));
//            userData.put("password", user.getString("password"));
//            userData.put("description", user.getString("description"));
//            userData.put("index", String.valueOf(index));
//        }
//        return userData;
//    }
//    
//    public static List<Map<String, String>> getAllUsersData() {
//        List<Map<String, String>> allUsers = new ArrayList<>();
//        if (usersArray != null) {
//            for (int i = 0; i < usersArray.length(); i++) {
//                allUsers.add(getUserData(i));
//            }
//        }
//        return allUsers;
//    }
//    
//    // Iterator Methods for Sequential Access
//    private static int currentIndex = 0;
//    
//    public static Map<String, String> getNextUser() {
//        if (!hasUsers()) {
//            return null;
//        }
//        
//        Map<String, String> user = getUserData(currentIndex);
//        currentIndex = (currentIndex + 1) % usersArray.length(); // Cycle through users
//        return user;
//    }
//    
//    public static Map<String, String> getCurrentUser() {
//        if (!hasUsers()) {
//            return null;
//        }
//        return getUserData(currentIndex);
//    }
//    
//    public static void resetIterator() {
//        currentIndex = 0;
//        System.out.println("🔄 JsonDataProvider: User iterator reset to index 0");
//    }
//    
//    public static int getCurrentIndex() {
//        return currentIndex;
//    }
//    
//    // Validation Methods
//    private static boolean isValidIndex(int index) {
//        return usersArray != null && index >= 0 && index < usersArray.length();
//    }
//    
//    // Bulk Operations
//    public static List<String> getAllUsernames() {
//        List<String> usernames = new ArrayList<>();
//        if (usersArray != null) {
//            for (int i = 0; i < usersArray.length(); i++) {
//                usernames.add(getUsername(i));
//            }
//        }
//        return usernames;
//    }
//    
//    public static List<String> getAllDescriptions() {
//        List<String> descriptions = new ArrayList<>();
//        if (usersArray != null) {
//            for (int i = 0; i < usersArray.length(); i++) {
//                descriptions.add(getDescription(i));
//            }
//        }
//        return descriptions;
//    }
//    
//    // Filter Methods
//    public static Map<String, String> findUserByUsername(String username) {
//        if (usersArray != null) {
//            for (int i = 0; i < usersArray.length(); i++) {
//                JSONObject user = usersArray.getJSONObject(i);
//                if (user.getString("username").equalsIgnoreCase(username)) {
//                    return getUserData(i);
//                }
//            }
//        }
//        return null;
//    }
//    
//    public static Map<String, String> findUserByDescription(String description) {
//        if (usersArray != null) {
//            for (int i = 0; i < usersArray.length(); i++) {
//                JSONObject user = usersArray.getJSONObject(i);
//                if (user.getString("description").equalsIgnoreCase(description)) {
//                    return getUserData(i);
//                }
//            }
//        }
//        return null;
//    }
//    
//    // Status Methods
//    public static void printUserSummary() {
//        if (!hasUsers()) {
//            System.out.println("ℹ️ JsonDataProvider: No users available");
//            return;
//        }
//        
//        System.out.println("📊 JsonDataProvider: User Summary (" + usersArray.length() + " users)");
//        for (int i = 0; i < usersArray.length(); i++) {
//            JSONObject user = usersArray.getJSONObject(i);
//            System.out.println("   👤 [" + i + "] " + user.getString("description") + 
//                             " - " + user.getString("username"));
//        }
//        System.out.println("   🎯 Current iterator position: " + currentIndex);
//    }
//    
//    // Data Validation
//    public static boolean validateUserData() {
//        if (!hasUsers()) {
//            System.out.println("⚠️ JsonDataProvider: No users to validate");
//            return false;
//        }
//        
//        boolean isValid = true;
//        for (int i = 0; i < usersArray.length(); i++) {
//            JSONObject user = usersArray.getJSONObject(i);
//            if (!user.has("username") || !user.has("password") || !user.has("description")) {
//                System.out.println("❌ JsonDataProvider: User at index " + i + " is missing required fields");
//                isValid = false;
//            } else if (user.getString("username").trim().isEmpty() || 
//                       user.getString("password").trim().isEmpty()) {
//                System.out.println("❌ JsonDataProvider: User at index " + i + " has empty username/password");
//                isValid = false;
//            }
//        }
//        
//        if (isValid) {
//            System.out.println("✅ JsonDataProvider: All user data is valid");
//        }
//        
//        return isValid;
//    }
//}


package utilities;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonDataProvider {
    
    private static List<Map<String, String>> usersList;
    private static boolean isInitialized = false;
    private static int currentIndex = 0;
    
    static {
        initialize();
    }
    
    private static void initialize() {
        if (!isInitialized) {
            loadUsersFromJson();
            isInitialized = true;
        }
    }
    
    private static void loadUsersFromJson() {
        try {
            String jsonContent = null;
            
            // Try multiple possible paths for the JSON file
            String[] possiblePaths = {
                "src/test/resources/users.json",
                "target/test-classes/users.json",
                "./users.json"
            };
            
            for (String path : possiblePaths) {
                try {
                    jsonContent = new String(Files.readAllBytes(Paths.get(path)));
                    System.out.println("✅ JsonDataProvider: Found users.json at: " + path);
                    break;
                } catch (Exception e) {
                    // Continue to next path
                }
            }
            
            if (jsonContent == null) {
                // Try loading from classpath as last resort
                try {
                    InputStream is = JsonDataProvider.class.getClassLoader().getResourceAsStream("users.json");
                    if (is != null) {
                        jsonContent = new String(is.readAllBytes());
                        System.out.println("✅ JsonDataProvider: Found users.json in classpath");
                    }
                } catch (Exception e) {
                    System.out.println("❌ JsonDataProvider: Could not find users.json in any location");
                }
            }
            
            if (jsonContent != null) {
                parseJsonContent(jsonContent);
            } else {
                System.out.println("⚠️ JsonDataProvider: No users.json found, will use empty data set");
                usersList = new ArrayList<>();
                // Set manual users as fallback
                setUsersManually();
            }
            
        } catch (Exception e) {
            System.out.println("❌ JsonDataProvider: Error loading users from JSON: " + e.getMessage());
            usersList = new ArrayList<>();
            // Set manual users as fallback
            setUsersManually();
        }
    }
    
    // ADD THIS MISSING METHOD
    public static void setUsersManually() {
        usersList = new ArrayList<>();
        
        // Add users manually as fallback - using your actual user data
        usersList.add(createUser("swatipatil22@yahoo.com", "Swat@123", "User 1"));
        usersList.add(createUser("pandey.soni1988@gmail.com", "Pand@123", "User 2"));
        usersList.add(createUser("ms1@vgos.org", "L8XcljPmjmGea322", "User 3"));
        usersList.add(createUser("ps1@vgos.org", "L8XcljPmjmGea322", "User 4"));
        
        System.out.println("✅ JsonDataProvider: Loaded " + usersList.size() + " manual users as fallback");
    }
    
    private static Map<String, String> createUser(String username, String password, String description) {
        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        user.put("description", description);
        return user;
    }
    
    private static void parseJsonContent(String jsonContent) {
        try {
            usersList = new ArrayList<>();
            jsonContent = jsonContent.trim();
            
            // Simple JSON parsing for the specific structure
            if (jsonContent.contains("\"users\"")) {
                String usersSection = jsonContent.substring(jsonContent.indexOf('[') + 1, jsonContent.lastIndexOf(']'));
                String[] userEntries = usersSection.split("\\},\\s*\\{");
                
                for (int i = 0; i < userEntries.length; i++) {
                    String userEntry = userEntries[i];
                    // Clean up the entry
                    if (i == 0) userEntry = userEntry + "}";
                    else if (i == userEntries.length - 1) userEntry = "{" + userEntry;
                    else userEntry = "{" + userEntry + "}";
                    
                    userEntry = userEntry.trim();
                    if (userEntry.startsWith("{")) userEntry = userEntry.substring(1);
                    if (userEntry.endsWith("}")) userEntry = userEntry.substring(0, userEntry.length() - 1);
                    
                    Map<String, String> userData = parseUserEntry(userEntry);
                    if (userData != null) {
                        usersList.add(userData);
                    }
                }
            }
            
            System.out.println("✅ JsonDataProvider: Parsed " + usersList.size() + " users from JSON");
            
        } catch (Exception e) {
            System.out.println("❌ JsonDataProvider: Error parsing JSON content: " + e.getMessage());
            usersList = new ArrayList<>();
            // Set manual users as fallback if parsing fails
            setUsersManually();
        }
    }
    
    private static Map<String, String> parseUserEntry(String userEntry) {
        try {
            Map<String, String> userData = new HashMap<>();
            String[] keyValuePairs = userEntry.split(",");
            
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");
                    
                    if (key.equals("username") || key.equals("password") || key.equals("description")) {
                        userData.put(key, value);
                    }
                }
            }
            
            // Validate that we have all required fields
            if (userData.containsKey("username") && userData.containsKey("password") && userData.containsKey("description")) {
                return userData;
            }
            
        } catch (Exception e) {
            System.out.println("❌ JsonDataProvider: Error parsing user entry: " + e.getMessage());
        }
        
        return null;
    }
    
    // User Management Methods
    public static List<Map<String, String>> getUsers() {
        return new ArrayList<>(usersList);
    }
    
    public static int getUserCount() {
        return usersList != null ? usersList.size() : 0;
    }
    
    public static boolean hasUsers() {
        return usersList != null && !usersList.isEmpty();
    }
    
    public static String getUsername(int index) {
        if (isValidIndex(index)) {
            return usersList.get(index).get("username");
        }
        return null;
    }
    
    public static String getPassword(int index) {
        if (isValidIndex(index)) {
            return usersList.get(index).get("password");
        }
        return null;
    }
    
    public static String getDescription(int index) {
        if (isValidIndex(index)) {
            return usersList.get(index).get("description");
        }
        return "User " + (index + 1);
    }
    
    public static Map<String, String> getUserData(int index) {
        if (isValidIndex(index)) {
            Map<String, String> userData = new HashMap<>(usersList.get(index));
            userData.put("index", String.valueOf(index));
            return userData;
        }
        return new HashMap<>();
    }
    
    public static List<Map<String, String>> getAllUsersData() {
        List<Map<String, String>> allUsers = new ArrayList<>();
        if (usersList != null) {
            for (int i = 0; i < usersList.size(); i++) {
                allUsers.add(getUserData(i));
            }
        }
        return allUsers;
    }
    
    // Iterator Methods for Sequential Access
    public static Map<String, String> getNextUser() {
        if (!hasUsers()) {
            return null;
        }
        
        Map<String, String> user = getUserData(currentIndex);
        currentIndex = (currentIndex + 1) % usersList.size(); // Cycle through users
        return user;
    }
    
    public static Map<String, String> getCurrentUser() {
        if (!hasUsers()) {
            return null;
        }
        return getUserData(currentIndex);
    }
    
    public static void resetIterator() {
        currentIndex = 0;
        System.out.println("🔄 JsonDataProvider: User iterator reset to index 0");
    }
    
    public static int getCurrentIndex() {
        return currentIndex;
    }
    
    // Validation Methods
    private static boolean isValidIndex(int index) {
        return usersList != null && index >= 0 && index < usersList.size();
    }
    
    // Bulk Operations
    public static List<String> getAllUsernames() {
        if (usersList == null) return new ArrayList<>();
        return usersList.stream()
                .map(user -> user.get("username"))
                .collect(Collectors.toList());
    }
    
    public static List<String> getAllDescriptions() {
        if (usersList == null) return new ArrayList<>();
        return usersList.stream()
                .map(user -> user.get("description"))
                .collect(Collectors.toList());
    }
    
    // Filter Methods
    public static Map<String, String> findUserByUsername(String username) {
        if (usersList != null) {
            for (int i = 0; i < usersList.size(); i++) {
                Map<String, String> user = usersList.get(i);
                if (user.get("username").equalsIgnoreCase(username)) {
                    return getUserData(i);
                }
            }
        }
        return null;
    }
    
    public static Map<String, String> findUserByDescription(String description) {
        if (usersList != null) {
            for (int i = 0; i < usersList.size(); i++) {
                Map<String, String> user = usersList.get(i);
                if (user.get("description").equalsIgnoreCase(description)) {
                    return getUserData(i);
                }
            }
        }
        return null;
    }
    
    // Status Methods
    public static void printUserSummary() {
        if (!hasUsers()) {
            System.out.println("ℹ️ JsonDataProvider: No users available");
            return;
        }
        
        System.out.println("📊 JsonDataProvider: User Summary (" + usersList.size() + " users)");
        for (int i = 0; i < usersList.size(); i++) {
            Map<String, String> user = usersList.get(i);
            System.out.println("   👤 [" + i + "] " + user.get("description") + 
                             " - " + user.get("username"));
        }
        System.out.println("   🎯 Current iterator position: " + currentIndex);
    }
    
    // Data Validation
    public static boolean validateUserData() {
        if (!hasUsers()) {
            System.out.println("⚠️ JsonDataProvider: No users to validate");
            return false;
        }
        
        boolean isValid = true;
        for (int i = 0; i < usersList.size(); i++) {
            Map<String, String> user = usersList.get(i);
            if (!user.containsKey("username") || !user.containsKey("password") || !user.containsKey("description")) {
                System.out.println("❌ JsonDataProvider: User at index " + i + " is missing required fields");
                isValid = false;
            } else if (user.get("username").trim().isEmpty() || user.get("password").trim().isEmpty()) {
                System.out.println("❌ JsonDataProvider: User at index " + i + " has empty username/password");
                isValid = false;
            }
        }
        
        if (isValid) {
            System.out.println("✅ JsonDataProvider: All user data is valid");
        } else {
            System.out.println("⚠️ JsonDataProvider: Some user data is invalid, but will continue with available data");
        }
        
        return isValid;
    }
}