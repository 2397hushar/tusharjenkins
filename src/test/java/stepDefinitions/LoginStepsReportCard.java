//package stepDefinitions;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.cucumber.java.en.And;
//import utilities.ConfigReader;
//import utilities.BrowserUtils;
//import org.testng.Assert;
//
//import ERP_Page.DashboardPage;
//import ERP_Page.MarksheetPage;
//import ERP_Page.ReportCardPage;
//import ERP_Page.SimonsLoginPage;
//
//public class LoginStepsReportCard {
//
//    private SimonsLoginPage simonsLoginPage;
//    private DashboardPage dashboardPage;
//    private MarksheetPage marksheetPage;
//    private ReportCardPage reportCardsPage;
//
//    @Given("I am on the application login page")
//    public void i_am_on_the_application_login_page() {
//        simonsLoginPage = new SimonsLoginPage();
//        simonsLoginPage.navigateToSimonsLoginPage();
//    }
//
//    @When("I login with valid admin credentials")
//    public void i_login_with_valid_admin_credentials() throws InterruptedException {
//        String username = ConfigReader.getUsername();
//        String password = ConfigReader.getPassword();
//        System.out.println("Logging in with: " + username);
//        simonsLoginPage.loginToSimons(username, password);
//    }
//
//    @When("I scroll down to Academic Progress section")
//    public void i_scroll_down_to_academic_progress_section() {
//        dashboardPage = new DashboardPage();
//        dashboardPage.scrollToAcademicProgress();
//    }
//
//    @When("I click on Marksheet option")
//    public void i_click_on_marksheet_option() {
//        dashboardPage.clickMarksheetOption();
//        marksheetPage = new MarksheetPage();
//    }
//
//    @When("I select current academic year from dropdown")
//    public void i_select_current_academic_year_from_dropdown() {
//        if (marksheetPage == null) {
//            marksheetPage = new MarksheetPage();
//        }
//        marksheetPage.selectCurrentAcademicYear();
//    }
//
//    @When("I select a student from student dropdown")
//    public void i_select_a_student_from_student_dropdown() {
//        marksheetPage.selectStudentFromDropdown();
//    }
//
//    @When("I click on View button for report card")
//    public void i_click_on_view_button_for_report_card() throws InterruptedException {
//        MarksheetPage.clickViewReportCard();
//        Thread.sleep(3000); // Wait for new tab to open
//        
//        // Initialize report card page
//        reportCardsPage = new ReportCardPage();
//        
//        // Verify the report card opened in new tab
//        reportCardsPage.verifyReportCardPage();
//        
//        // Switch back to marksheet tab to click download button
//        reportCardsPage.switchBackToMarksheetTab();
//    }
//
//    @Then("I should see report card details page")
//    public void i_should_see_report_card_details_page() {
//        // This is already handled in the "I click on View button for report card" step
//        System.out.println("Report card page verified and switched back to marksheet page");
//    }
//
//    @When("I click on Download button")
//    public void i_click_on_download_button() throws InterruptedException {
//        // Now we're on the marksheet page, click download button
//        ReportCardPage.clickDownloadButton();
//        Thread.sleep(8000); // Reduced from 9000 to 8000
//        
//        // VERIFY DOWNLOAD FIRST before logout
//        System.out.println("Starting download verification...");
//        boolean isDownloaded = reportCardsPage.verifyFileDownloadWithRetry();
//        System.out.println("Download verification result: " + (isDownloaded ? "SUCCESS" : "NOT VERIFIED"));
//        
//        // Only after download verification, proceed to logout
//        System.out.println("Proceeding to logout after download verification...");
//        ReportCardPage.logout_page();
//    }
//
//    @Then("report card file should download successfully")
//    public void report_card_file_should_download_successfully() throws InterruptedException {
//        try {
//            // Download verification is now completed in the previous step
//            System.out.println("Download process completed successfully");
//            
//            // Close the report card tab if it's still open
//            reportCardsPage.closeReportCardTab();
//            
//            System.out.println("All operations completed successfully!");
//            
//        } finally {
//            // Always close the browser in this step
//            closeBrowser();
//        }
//    }
//    
//    @And("close the browser")
//    public void close_the_browser() {
//        closeBrowser();
//    }
//    
//    private void closeBrowser() {
//        try {
//            System.out.println("Closing browser...");
//            
//            // Wait a moment before closing to ensure all processes are complete
//            Thread.sleep(2000);
//            
//            // Close browser using BrowserUtils
//            BrowserUtils.quitDriver();
//            
//            System.out.println("Browser closed successfully!");
//            
//        } catch (Exception e) {
//            System.out.println("Error closing browser: " + e.getMessage());
//            e.printStackTrace();
//            
//            // Force quit if normal close fails
//            try {
//                BrowserUtils.quitDriver();
//            } catch (Exception ex) {
//                System.out.println("Force quit also failed: " + ex.getMessage());
//            }
//        }
//    }
//} 


//************************** updated

package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import utilities.ConfigReader;
import utilities.JsonDataProvider;
import utilities.BrowserUtils;
import utilities.ExtentReportManager;
import java.util.Map;

import ERP_Page.DashboardPage;
import ERP_Page.MarksheetPage;
import ERP_Page.ReportCardPage;
import ERP_Page.SimonsLoginPage;

public class LoginStepsReportCard {

    private SimonsLoginPage simonsLoginPage;
    private DashboardPage dashboardPage;
    private MarksheetPage marksheetPage;
    private ReportCardPage reportCardsPage;
    private String currentUsername;
    private String currentPassword;
    private String currentUserDescription;
    private String currentScenarioName;

    // Hook to set up test in Extent Report (you'll need to call this from your test runner)
    public void setupTest(String scenarioName) {
        currentScenarioName = scenarioName;
        ExtentReportManager.createTest(scenarioName);
    }

    // For Scenario Outline with userType
    @Given("I am on the application login page for user {string}")
    public void i_am_on_the_application_login_page_for_user(String userType) {
        ExtentReportManager.infoStep("Navigating to application login page for user: " + userType);
        
        simonsLoginPage = new SimonsLoginPage();
        simonsLoginPage.navigateToSimonsLoginPage();
        
        // Find user by description
        Map<String, String> userData = JsonDataProvider.findUserByDescription(userType);
        
        if (userData != null) {
            currentUsername = userData.get("username");
            currentPassword = userData.get("password");
            currentUserDescription = userData.get("description");
            System.out.println("🚀 Testing with: " + currentUserDescription + " [Type: " + userType + "]");
            ExtentReportManager.infoStep("Using user: " + currentUserDescription + " [Type: " + userType + "]");
        } else {
            // Fallback to sequential user
            userData = JsonDataProvider.getNextUser();
            if (userData != null) {
                currentUsername = userData.get("username");
                currentPassword = userData.get("password");
                currentUserDescription = userData.get("description");
                System.out.println("🔄 Using sequential user: " + currentUserDescription);
                ExtentReportManager.infoStep("Using sequential user: " + currentUserDescription);
            } else {
                // Final fallback to config
                currentUsername = ConfigReader.getUsername();
                currentPassword = ConfigReader.getPassword();
                currentUserDescription = "Default User";
                System.out.println("🔧 Using default user from config");
                ExtentReportManager.infoStep("Using default user from config");
            }
        }
        
        System.out.println("📧 Username: " + currentUsername);
        ExtentReportManager.passStep("Successfully navigated to login page and loaded user credentials");
    }

    @When("I login with valid admin credentials for {string}")
    public void i_login_with_valid_admin_credentials_for_user(String userType) throws InterruptedException {
        ExtentReportManager.infoStep("Attempting login for user: " + currentUserDescription);
        System.out.println("🔐 Attempting login for: " + currentUserDescription);
        
        try {
            simonsLoginPage.loginToSimons(currentUsername, currentPassword);
            System.out.println("✅ Login completed for: " + currentUserDescription);
            ExtentReportManager.passStep("Login successful for: " + currentUserDescription);
            
            // Take screenshot after successful login
            String screenshot = BrowserUtils.takeScreenshotAsBase64("Login_Success");
            if (screenshot != null) {
                ExtentReportManager.addScreenshotBase64("After Login", screenshot);
            }
            
        } catch (Exception e) {
            ExtentReportManager.failStep("Login failed for: " + currentUserDescription + " - " + e.getMessage());
            throw e;
        }
    }

    // For Scenario Outline with index
    @Given("I am on the application login page for user index {int}")
    public void i_am_on_the_application_login_page_for_user_index(int userIndex) {
        ExtentReportManager.infoStep("Navigating to application login page for user index: " + userIndex);
        
        simonsLoginPage = new SimonsLoginPage();
        simonsLoginPage.navigateToSimonsLoginPage();
        
        Map<String, String> userData = JsonDataProvider.getUserData(userIndex);
        
        if (userData != null && !userData.isEmpty()) {
            currentUsername = userData.get("username");
            currentPassword = userData.get("password");
            currentUserDescription = userData.get("description");
            System.out.println("🚀 Testing with user index [" + userIndex + "]: " + currentUserDescription);
            ExtentReportManager.infoStep("Using user index [" + userIndex + "]: " + currentUserDescription);
        } else {
            currentUsername = ConfigReader.getUsername();
            currentPassword = ConfigReader.getPassword();
            currentUserDescription = "Default User";
            System.out.println("⚠️ User index " + userIndex + " not found, using default");
            ExtentReportManager.infoStep("User index " + userIndex + " not found, using default");
        }
        
        System.out.println("📧 Username: " + currentUsername);
        ExtentReportManager.passStep("Successfully navigated to login page");
    }

    @When("I login with user index {int} credentials")
    public void i_login_with_user_index_credentials(int userIndex) throws InterruptedException {
        ExtentReportManager.infoStep("Attempting login for user index: " + userIndex);
        System.out.println("🔐 Attempting login for user index [" + userIndex + "]: " + currentUserDescription);
        
        try {
            simonsLoginPage.loginToSimons(currentUsername, currentPassword);
            System.out.println("✅ Login completed for user index [" + userIndex + "]");
            ExtentReportManager.passStep("Login successful for user index: " + userIndex);
        } catch (Exception e) {
            ExtentReportManager.failStep("Login failed for user index: " + userIndex + " - " + e.getMessage());
            throw e;
        }
    }

    @When("I scroll down to Academic Progress section")
    public void i_scroll_down_to_academic_progress_section() {
        ExtentReportManager.infoStep("Scrolling to Academic Progress section");
        
        try {
            dashboardPage = new DashboardPage();
            dashboardPage.scrollToAcademicProgress();
            System.out.println("📊 Scrolled to Academic Progress section for: " + currentUserDescription);
            ExtentReportManager.passStep("Successfully scrolled to Academic Progress section");
        } catch (Exception e) {
            ExtentReportManager.failStep("Failed to scroll to Academic Progress section: " + e.getMessage());
            throw e;
        }
    }

    @When("I click on Marksheet option")
    public void i_click_on_marksheet_option() {
        ExtentReportManager.infoStep("Clicking on Marksheet option");
        
        try {
            dashboardPage.clickMarksheetOption();
            marksheetPage = new MarksheetPage();
            System.out.println("📝 Clicked Marksheet option for: " + currentUserDescription);
            ExtentReportManager.passStep("Successfully clicked Marksheet option");
        } catch (Exception e) {
            ExtentReportManager.failStep("Failed to click Marksheet option: " + e.getMessage());
            throw e;
        }
    }

    @When("I select current academic year from dropdown")
    public void i_select_current_academic_year_from_dropdown() {
        ExtentReportManager.infoStep("Selecting current academic year");
        
        try {
            if (marksheetPage == null) {
                marksheetPage = new MarksheetPage();
            }
            marksheetPage.selectCurrentAcademicYear();
            System.out.println("📅 Selected academic year for: " + currentUserDescription);
            ExtentReportManager.passStep("Successfully selected academic year");
        } catch (Exception e) {
            ExtentReportManager.failStep("Failed to select academic year: " + e.getMessage());
            throw e;
        }
    }

    @When("I select a student from student dropdown")
    public void i_select_a_student_from_student_dropdown() {
        ExtentReportManager.infoStep("Selecting student from dropdown");
        
        try {
            marksheetPage.selectStudentFromDropdown();
            System.out.println("👨‍🎓 Selected student from dropdown for: " + currentUserDescription);
            ExtentReportManager.passStep("Successfully selected student from dropdown");
        } catch (Exception e) {
            ExtentReportManager.failStep("Failed to select student from dropdown: " + e.getMessage());
            throw e;
        }
    }

    @When("I click on View button for report card")
    public void i_click_on_view_button_for_report_card() throws InterruptedException {
        ExtentReportManager.infoStep("Clicking View button for report card");
        
        try {
            MarksheetPage.clickViewReportCard();
            Thread.sleep(3000);
            
            reportCardsPage = new ReportCardPage();
            reportCardsPage.verifyReportCardPage();
            reportCardsPage.switchBackToMarksheetTab();
            System.out.println("👁️ Viewed report card for: " + currentUserDescription);
            ExtentReportManager.passStep("Successfully viewed report card");
            
            // Take screenshot after viewing report card
            String screenshot = BrowserUtils.takeScreenshotAsBase64("Report_Card_View");
            if (screenshot != null) {
                ExtentReportManager.addScreenshotBase64("Report Card View", screenshot);
            }
            
        } catch (Exception e) {
            ExtentReportManager.failStep("Failed to view report card: " + e.getMessage());
            throw e;
        }
    }

    @Then("I should see report card details page")
    public void i_should_see_report_card_details_page() {
        ExtentReportManager.infoStep("Verifying report card details page");
        System.out.println("✅ Report card page verified successfully for: " + currentUserDescription);
        ExtentReportManager.passStep("Report card details page verified successfully");
    }

    @When("I click on Download button")
    public void i_click_on_download_button() throws InterruptedException {
        ExtentReportManager.infoStep("Clicking Download button");
        
        try {
            ReportCardPage.clickDownloadButton();
            Thread.sleep(8000);
            
            System.out.println("📥 Starting download verification for: " + currentUserDescription);
            ExtentReportManager.infoStep("Starting download verification");
            
            boolean isDownloaded = reportCardsPage.verifyFileDownloadWithRetry();
            
            if (isDownloaded) {
                System.out.println("✅ Download SUCCESSFUL for: " + currentUserDescription);
                ExtentReportManager.passStep("File downloaded successfully");
            } else {
                System.out.println("❌ Download NOT VERIFIED for: " + currentUserDescription);
                ExtentReportManager.failStep("File download not verified");
            }
            
            System.out.println("🚪 Proceeding to logout for: " + currentUserDescription);
            ExtentReportManager.infoStep("Proceeding to logout");
            ReportCardPage.logout_page();
            ExtentReportManager.passStep("Logout successful");
            
        } catch (Exception e) {
            ExtentReportManager.failStep("Download process failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("report card file should download successfully")
    public void report_card_file_should_download_successfully() {
        ExtentReportManager.infoStep("Finalizing test execution");
        
        try {
            System.out.println("🎉 Download process completed for: " + currentUserDescription);
            reportCardsPage.closeReportCardTab();
            System.out.println("=========================================");
            System.out.println("✅ ALL OPERATIONS COMPLETED SUCCESSFULLY FOR: " + currentUserDescription);
            System.out.println("=========================================");
            
            ExtentReportManager.passStep("All operations completed successfully for: " + currentUserDescription);
            ExtentReportManager.passTest("Test Case Passed: Report card downloaded successfully for " + currentUserDescription);
            
        } catch (Exception e) {
            ExtentReportManager.failStep("Final operations failed: " + e.getMessage());
            ExtentReportManager.failTest("Test Case Failed: " + e.getMessage());
            throw e;
        } finally {
            closeBrowser();
            // End the test in Extent Report
            ExtentReportManager.endTest();
        }
    }

    @Then("report card file should download successfully for {string}")
    public void report_card_file_should_download_successfully_for_user(String userType) {
        ExtentReportManager.infoStep("Finalizing test execution for user: " + userType);
        
        try {
            System.out.println("🎉 Download process completed for: " + userType);
            reportCardsPage.closeReportCardTab();
            System.out.println("=========================================");
            System.out.println("✅ ALL OPERATIONS COMPLETED SUCCESSFULLY FOR: " + userType);
            System.out.println("=========================================");
            
            ExtentReportManager.passStep("All operations completed successfully for: " + userType);
            ExtentReportManager.passTest("Test Case Passed: Report card downloaded successfully for " + userType);
            
        } catch (Exception e) {
            ExtentReportManager.failStep("Final operations failed: " + e.getMessage());
            ExtentReportManager.failTest("Test Case Failed: " + e.getMessage());
            throw e;
        } finally {
            closeBrowser();
            // End the test in Extent Report
            ExtentReportManager.endTest();
        }
    }
    
    @And("close the browser")
    public void close_the_browser() {
        closeBrowser();
    }
    
    private void closeBrowser() {
        try {
            System.out.println("🔚 Closing browser session for: " + currentUserDescription);
            Thread.sleep(2000);
            BrowserUtils.quitDriver();
            System.out.println("✅ Browser closed successfully");
        } catch (Exception e) {
            System.out.println("❌ Error closing browser: " + e.getMessage());
            try {
                BrowserUtils.quitDriver();
            } catch (Exception ex) {
                System.out.println("❌ Force quit also failed: " + ex.getMessage());
            }
        }
    }
}







