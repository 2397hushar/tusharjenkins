package mobile.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import mobile.android.pages.AndroidLoginPage;
import mobile.android.pages.AndroidBasePage;
import mobile.android.pages.AndroidMarksheetPage;
import mobile.android.pages.AndroidReportCardPage;
import utilities.ConfigReader;

public class AndroidReportCardSteps {
    
    private AndroidLoginPage loginPage;
    private AndroidBasePage basePage;
    private AndroidMarksheetPage marksheetPage;
    private AndroidReportCardPage reportCardPage;
    private static String lastUser = "";
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    private void waitForElement(int seconds) {
        if (basePage == null) {
            basePage = new AndroidBasePage() {};
        }
        basePage.explicitWait(seconds);
    }
    
    private void takeScreenshot(String stepName) {
        if (loginPage != null) {
            loginPage.takeScreenshotForStep(stepName);
        } else if (marksheetPage != null) {
            marksheetPage.takeScreenshotForStep(stepName);
        } else if (reportCardPage != null) {
            reportCardPage.takeScreenshotForStep(stepName);
        } else if (basePage != null) {
            basePage.takeScreenshotForStep(stepName);
        }
    }
    
    // ============================================
    // GIVEN STEPS - ANDROID SPECIFIC
    // ============================================
    
    @Given("I am on android application login page")
    public void i_am_on_android_application_login_page() {
        System.out.println("=========================================");
        System.out.println("Starting Android Test on Pixel Device");
        System.out.println("=========================================");
        
       
        
        loginPage = new AndroidLoginPage();
        loginPage.navigateToLoginPage();
        loginPage.takeScreenshotForStep("1_Login_Page");
        waitForElement(1);

    }
    
    // ============================================
    // WHEN STEPS - LOGIN (ANDROID SPECIFIC)
    // ============================================
    
    @When("I login with valid android admin credentials for {string}")
    public void i_login_with_valid_android_admin_credentials_for_user(String userType) {

        System.out.println("🔐 Logging in as: " + userType);

        try {

            int userIndex = extractUserIndex(userType);

            String username = ConfigReader.getUserUsername(userIndex);
            String password = ConfigReader.getUserPassword(userIndex);

            System.out.println("👤 Username : " + username);

            loginPage.login(username, password);

            waitForElement(1);

            loginPage.takeScreenshotForStep(
                    "2_After_Login_" + userType);

            lastUser = userType;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to login for user : " + userType,
                    e);
        }
    }
    
    private int extractUserIndex(String userType) {

        try {

            return Integer.parseInt(
                    userType.replace("User", "")
                            .trim()
            ) - 1;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid User Format : " + userType);
        }
    }

    private void logoutAndPrepareForNextUser() {

        try {

            if (reportCardPage == null) {
                reportCardPage =
                        new AndroidReportCardPage();
            }

            if (loginPage == null) {
                loginPage =
                        new AndroidLoginPage();
            }

            reportCardPage.clickOnLogoutButton();

            waitForElement(2);

            reportCardPage.clickCloseButton();

            waitForElement(2);

            loginPage.clickLetsConnectButtonForRelogin();

            waitForElement(1);

            System.out.println(
                    "✅ Ready for next user");

        } catch (Exception e) {

            System.out.println(
                    "❌ Logout flow failed : "
                            + e.getMessage());
        }
    }

	@When("I login with valid android admin credentials")
    public void i_login_with_valid_android_admin_credentials() {
        String username = ConfigReader.getUsername();
        String password = ConfigReader.getPassword();
        
        loginPage.login(username, password);
        waitForElement(1);

        loginPage.takeScreenshotForStep("2_After_Login");
    }
    
    // ============================================
    // AND STEPS - NAVIGATION (ANDROID SPECIFIC)
    // ============================================
    
    @And("I click on android Marksheet option")
    public void i_click_on_android_marksheet_option() {
        System.out.println("🖱️ Clicking on Marksheet option");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }

        marksheetPage.clickStudentMarksheet();
        waitForElement(1);

        marksheetPage.takeScreenshotForStep("3_Marksheet_");
    }
    
    @And("I select current android academic year from dropdown")
    public void i_select_current_android_academic_year_from_dropdown() {
        System.out.println("📅 Selecting current academic year");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
        
        marksheetPage.clickAcademicYearDropdown();
        waitForElement(2);
        marksheetPage.takeScreenshotForStep("4_Academic_Year_Selected");
    }
    
    @And("I select android student from student dropdown")
    public void i_select_android_student_from_student_dropdown() {
        System.out.println("👨‍🎓 Selecting student from dropdown");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
      
        marksheetPage.clickStudentDropdown();
        waitForElement(2);
        marksheetPage.takeScreenshotForStep("5_Student_Selected");
    }
    
    @When("I click on android View button for report card")
    public void i_click_on_android_view_button_for_report_card() {

        System.out.println("👁️ Clicking View button for report card");

        marksheetPage = new AndroidMarksheetPage();

        marksheetPage.clickViewButton();

        waitForElement(1);

        reportCardPage = new AndroidReportCardPage();

        reportCardPage.takeScreenshotForStep("6_Report_Card_Opened");

        marksheetPage.clickBackArrow();
    }
    
    // ============================================
    // THEN STEPS - VERIFICATION (ANDROID SPECIFIC)
    // ============================================
    
    @Then("I should see android report card details page")
    public void i_should_see_android_report_card_details_page() {
        System.out.println("📄 Verifying report card details page");
        
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        reportCardPage.isReportCardPageDisplayed();
        waitForElement(1);

    }
    
    @When("I click on android Download button")
    public void i_click_on_android_download_button() {
        System.out.println("⬇️ Clicking Download button");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        if (loginPage == null) {
            loginPage = new AndroidLoginPage();
        }
        
        // Click download buttons
        marksheetPage.clickDownloadButtons();
        waitForElement(3);
        marksheetPage.takeScreenshot("7_Download_Clicked");
        logoutAndPrepareForNextUser();
       
    }
    
    @Then("android report card file should download successfully")
    public void android_report_card_file_should_download_successfully() {
        System.out.println("✅ Verifying download...");
        
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        reportCardPage.isDownloadSuccessful();
        waitForElement(1);

        System.out.println("✅ Report card downloaded successfully!");
        
        reportCardPage.takeScreenshotForStep("8_Download_Success");
    }
    
    @Then("android report card file should download successfully for {string}")
    public void android_report_card_file_should_download_successfully_for_user(String userType) {
        System.out.println("✅ Verifying download for " + userType);
        
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        reportCardPage.isDownloadSuccessful();
        waitForElement(1);

        System.out.println("✅ Report card downloaded successfully for " + userType);
        
        reportCardPage.takeScreenshotForStep("10_Download_Success_" + userType);
    }
    
    // ============================================
    // ADDITIONAL STEPS
    // ============================================
    
    @Then("I should see android marksheet page")
    public void i_should_see_android_marksheet_page() {
        System.out.println("📊 Verifying marksheet page");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
        marksheetPage.isMarksheetPageDisplayed();
        waitForElement(1);

        marksheetPage.takeScreenshotForStep("Marksheet_Verified");
    }
    
    @And("android academic year dropdown should be visible")
    public void android_academic_year_dropdown_should_be_visible() {
        System.out.println("✅ Verifying academic year dropdown");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
        marksheetPage.isAcademicYearDropdownVisible();
        waitForElement(1);

        marksheetPage.takeScreenshotForStep("Academic_Year_Dropdown_Visible");
    }
    
    @And("android student dropdown should be visible")
    public void android_student_dropdown_should_be_visible() {
        System.out.println("✅ Verifying student dropdown");
        
        if (marksheetPage == null) {
            marksheetPage = new AndroidMarksheetPage();
        }
        
        marksheetPage.isStudentDropdownVisible();
        waitForElement(1);

        marksheetPage.takeScreenshotForStep("Student_Dropdown_Visible");
    }
    
    @When("I press android back button")
    public void i_press_android_back_button() {
        if (basePage == null) {
            basePage = new AndroidBasePage() {};
        }
        waitForElement(1);
        basePage.pressBackButton();
      
        basePage.takeScreenshotForStep("Back_Button_Pressed");
    }
    
    @Then("I should be on android login page")
    public void i_should_be_on_android_login_page() {
        if (loginPage == null) {
            loginPage = new AndroidLoginPage();
        }
        
        loginPage.navigateToLoginPage();
        waitForElement(1);
        loginPage.takeScreenshotForStep("Returned_To_Login");
    }
    
    @When("I swipe down on android report card")
    public void i_swipe_down_on_android_report_card() {
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        reportCardPage.swipeReportCard();
        waitForElement(1);
        reportCardPage.takeScreenshotForStep("Report_Card_Swiped");
    }
    
    @Then("all android grades should be visible")
    public void all_android_grades_should_be_visible() {
        System.out.println("📊 All grades are visible");
        
        if (reportCardPage == null) {
            reportCardPage = new AndroidReportCardPage();
        }
        
        reportCardPage.isPDFViewerDisplayed();
        waitForElement(1);
        reportCardPage.takeScreenshotForStep("Grades_Visible");
    }
}