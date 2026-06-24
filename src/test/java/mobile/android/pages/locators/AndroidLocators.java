package mobile.android.pages.locators;

public class AndroidLocators {
    
    // ============================================
    // LOGIN PAGE - ANDROID (UPDATED for HubbleHox)
    // ============================================
    public static class Login {
        
        // ===== IDs =====
        public static final String USERNAME_ID = "email"; 
        public static final String PASSWORD_ID = "password"; 
        public static final String LOGIN_BUTTON_ID = "kc-login"; 
        
        // ===== XPaths =====
        public static final String LETTSCONNECTBUTTON = "//android.widget.Button[@content-desc='Lets Get Started' or @text='Lets Get Started' or contains(@text, 'Let')]";
        
        public static final String USERNAME_XPATH = "//android.widget.EditText[@resource-id='email']";
        public static final String PASSWORD_XPATH = "//android.widget.EditText[@resource-id='password']";
        
        // ===== ADD THESE - MORE ROBUST LOCATORS =====
        
        // Login button - Multiple variations (CRITICAL FIX)
        public static final String LOGIN_BUTTON_XPATH = "//android.widget.Button[@text='Login']";
        public static final String LOGIN_BUTTON_ALT_XPATH = "//android.widget.Button[contains(@text, 'Sign In')]";
        public static final String LOGIN_BUTTON_ALT2_XPATH = "//android.widget.Button[contains(@text, 'Sign')]";
        public static final String LOGIN_BUTTON_ALT3_XPATH = "//android.widget.Button[@resource-id='kc-login']";
        public static final String LOGIN_BUTTON_BY_CLASS = "android.widget.Button";
        
        // Generic button finder (last resort)
        public static final String ANY_BUTTON_XPATH = "//android.widget.Button";
        
        // Username field - Alternative locators
        public static final String USERNAME_ALT_XPATH = "//android.widget.EditText[contains(@hint, 'Email') or contains(@text, 'Email')]";
        public static final String USERNAME_BY_CLASS = "android.widget.EditText";
        
        // Password field - Alternative locators
        public static final String PASSWORD_ALT_XPATH = "//android.widget.EditText[contains(@hint, 'Password') or contains(@text, 'Password')]";
        public static final String PASSWORD_BY_CLASS = "android.widget.EditText";
        
        // ===== Accessibility IDs =====
        public static final String USERNAME_ACCESSIBILITY = "Email Address";
        public static final String PASSWORD_ACCESSIBILITY = "Password";
        public static final String LOGIN_BUTTON_ACCESSIBILITY = "Login";
    }
    
    // ============================================
    // DASHBOARD PAGE - ANDROID
    // ============================================
 // Add this to AndroidLocators.java
    public static class WelcomeScreen {
        public static final String LETS_GET_STARTED_BUTTON_XPATH = "//android.widget.Button[@content-desc='Lets Get Started' or @text='Lets Get Started']";
        public static final String HUBBLEHOX_TITLE_XPATH = "//android.widget.TextView[contains(@text, 'HUBBLEHOX')]";
        public static final String APP_VERSION_XPATH = "//android.widget.TextView[contains(@text, 'v1.')]";
    }
    public static class Dashboard {
        // IDs
        public static final String DASHBOARD_TITLE_ID = "com.hubblehox.hubbleorion:id/dashboardTitle";
        public static final String USER_AVATAR_ID = "com.hubblehox.hubbleorion:id/userAvatar";
        
        // XPaths
        public static final String DASHBOARD_TITLE_XPATH = "//android.widget.TextView[@text='Dashboard']";
        public static final String ACADEMIC_PROGRESS_XPATH = "//android.widget.TextView[@text='Academic Progress']";
        public static final String MARK_SHEET_OPTION_XPATH = "//android.widget.ImageView[@content-desc='Student Marksheet']";
        
        // Alternative locators
        public static final String ACADEMIC_PROGRESS_CONTAINS_XPATH = "//android.widget.TextView[contains(@text, 'Academic')]";
        public static final String MARK_SHEET_CONTAINS_XPATH = "//android.widget.TextView[contains(@text, 'Mark')]";
        
        // Resource IDs
        public static final String ACADEMIC_PROGRESS_ID = "com.hubblehox.hubbleorion:id/academicProgressSection";
    }
    
    // ============================================
    // MARKSHEET PAGE - ANDROID
    // ============================================
    public static class Marksheet {
        // IDs - Updated with correct package name
        public static final String MARKSHEET_TITLE_ID = "com.hubblehox.hubbleorion:id/marksheetTitle";
        public static final String ACADEMIC_YEAR_SPINNER_ID = "com.hubblehox.hubbleorion:id/academicYearSpinner";
        public static final String STUDENT_SPINNER_ID = "com.hubblehox.hubbleorion:id/studentSpinner";
        public static final String VIEW_BUTTON_ID = "com.hubblehox.hubbleorion:id/viewButton";
        
        // XPaths
        public static final String MARKSHEET_TITLE_XPATH = "//android.widget.TextView[@text='Marksheet']";
        public static final String ACADEMIC_YEAR_DROPDOWN_XPATH = "//android.widget.Button[@content-desc='2026 - 27']";
        public static final String ACADEMIC_YEAR_DROPDOWN_XPATH1 = "//android.widget.Button[@content-desc='2025 - 26']";
        public static final String  academicYearOptions="//android.widget.Button";
        
        public static final String STUDENT_DROPDOWN_XPATH = "//android.widget.Button[@content-desc='Aarvika Sharma - EN10122551291']";
        
        public static final String VIEW_BUTTON_XPATH = "//android.view.View[@content-desc='CIE Grade 1 to 10 Annual Performance Portrait']/android.widget.ImageView[2]";
        public static final String DOWNLOAD_BUTTON_XPATH = "//android.view.View[@content-desc='CIE Grade 1 to 10 Annual Performance Portrait']/android.widget.ImageView[1]";
        public static final String Navigate_back_button="Navigate up";
        public static final String Arrow_Back="Navigate up";
        
    }
    // ============================================
    // REPORT CARD PAGE - ANDROID
    // ============================================
    public static class ReportCard {
        // IDs - Updated with correct package name
        public static final String REPORT_CARD_TITLE_ID = "com.hubblehox.hubbleorion:id/reportCardTitle";
        public static final String SHARE_BUTTON_ID = "com.hubblehox.hubbleorion:id/shareButton";
        
       
        public static final String REPORT_CARD_TITLE_XPATH = "//android.widget.TextView[@text='Report Card']";
        public static final String PDF_VIEWER_XPATH = "//android.webkit.WebView";
        public static final String CLOSE_BUTTON_XPATH = "//android.widget.Button[@content-desc='Ok']";
        public static final String VIEW_BUTTONS_XPATH = "//android.view.View[@content-desc='PS']";
        public static final String CLICK_ON_LOGOUT_BUTTON="PS";

        public static final String NAVIAGTE_BACK_BUTTON="Navigate up";
        public static final String SAVE_BUTTON="android:id/button1";
        
        
        public static final String STUDENT_NAME_ID = "com.hubblehox.hubbleorion:id/studentName";
        public static final String STUDENT_CLASS_ID = "com.hubblehox.hubbleorion:id/studentClass";
        public static final String STUDENT_ROLL_NUMBER_ID = "com.hubblehox.hubbleorion:id/rollNumber";
        
        // Grades table
        public static final String GRADES_TABLE_ID = "com.hubblehox.hubbleorion:id/gradesTable";
        public static final String SUBJECT_ROW_XPATH = "//android.widget.TableRow";
        public static final String SUBJECT_NAME_XPATH = "//android.widget.TextView[@resource-id='subjectName']";
        public static final String MARKS_OBTAINED_XPATH = "//android.widget.TextView[@resource-id='marksObtained']";
        
        // Download status
        public static final String DOWNLOAD_PROGRESS_ID = "com.hubblehox.hubbleorion:id/downloadProgress";
        public static final String DOWNLOAD_SUCCESS_TOAST = "//android.widget.Toast[contains(@text, 'Downloaded') or contains(@text, 'Success')]";
    }
    
    // ============================================
    // MENU & NAVIGATION - ANDROID
    // ============================================
    public static class Navigation {
        // Toolbar/Menu
        public static final String MENU_BUTTON_ID = "com.hubblehox.hubbleorion:id/menuButton";
        public static final String BACK_BUTTON_XPATH = "//android.widget.ImageButton[@content-desc='Navigate up' or @content-desc='Back']";
        public static final String LOGOUT_OPTION_XPATH = "//android.widget.TextView[@text='Logout' or @text='Sign Out']";
        
        // Confirmation dialogs
        public static final String CONFIRM_LOGOUT_BUTTON_ID = "android:id/button1";
        public static final String CANCEL_LOGOUT_BUTTON_ID = "android:id/button2";
        
        // Alternative logout locators
        public static final String LOGOUT_OPTION_ALT_XPATH = "//android.widget.TextView[contains(@text, 'Log') or contains(@text, 'Sign')]";
        
        // Drawer menu items
        public static final String HOME_MENU_XPATH = "//android.widget.TextView[@text='Home']";
        public static final String PROFILE_MENU_XPATH = "//android.widget.TextView[@text='Profile']";
        public static final String SETTINGS_MENU_XPATH = "//android.widget.TextView[@text='Settings']";
    }
    
    // ============================================
    // LOADING & PROGRESS - ANDROID
    // ============================================
    public static class Progress {
        public static final String PROGRESS_BAR_ID = "com.hubblehox.hubbleorion:id/progressBar";
        public static final String LOADING_TEXT_XPATH = "//android.widget.TextView[@text='Loading...' or contains(@text, 'Loading')]";
        public static final String SHIMMER_LOADER_ID = "com.hubblehox.hubbleorion:id/shimmerLoader";
    }
    
    // ============================================
    // ERROR MESSAGES - ANDROID
    // ============================================
    public static class ErrorMessages {
        public static final String ERROR_TOAST_XPATH = "//android.widget.Toast";
        public static final String NETWORK_ERROR_ID = "com.hubblehox.hubbleorion:id/networkError";
        public static final String NO_DATA_TEXT_XPATH = "//android.widget.TextView[@text='No data available']";
        public static final String LOGIN_ERROR_XPATH = "//android.widget.TextView[contains(@text, 'Invalid') or contains(@text, 'Error')]";
    }
}