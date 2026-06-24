package mobile.android.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import com.google.common.collect.ImmutableMap;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import mobile.android.pages.locators.AndroidLocators;
import mobile.android.pages.locators.AndroidLocators.Login;
import mobile.android.pages.locators.AndroidLocators.Marksheet;
import mobile.android.pages.locators.AndroidLocators.ReportCard;

public class AndroidMarksheetPage extends AndroidBasePage {
    
    @AndroidFindBy(xpath = AndroidLocators.Dashboard.MARK_SHEET_OPTION_XPATH)
    private WebElement studentMarksheet;
    
    @AndroidFindBy(xpath = AndroidLocators.Marksheet.STUDENT_DROPDOWN_XPATH)
    private WebElement studentdropdown;
    
    @AndroidFindBy(xpath = AndroidLocators.Marksheet.ACADEMIC_YEAR_DROPDOWN_XPATH)
    private WebElement yeardropdown;
    
    @AndroidFindBy(xpath = AndroidLocators.Marksheet.ACADEMIC_YEAR_DROPDOWN_XPATH)
    private WebElement firstoption; 
    
    @AndroidFindBy(xpath=AndroidLocators.Marksheet.academicYearOptions)
    private List<WebElement> academicYearOptions;
    
    @AndroidFindBy(xpath = AndroidLocators.Marksheet.VIEW_BUTTON_XPATH)
    private WebElement viewbutton;
    
    @AndroidFindBy(xpath = AndroidLocators.Marksheet.DOWNLOAD_BUTTON_XPATH)
    private WebElement downloadButton;
    
    @AndroidFindBy(accessibility =Marksheet.Navigate_back_button)
    private WebElement navigateUpButton;
    
    @AndroidFindBy(id =ReportCard.SAVE_BUTTON)
    private WebElement savebuttonpdf;
    
    @AndroidFindBy(accessibility =Marksheet.Arrow_Back)
    private WebElement backarrowclick;
    
    public AndroidMarksheetPage() {
        super();
        System.out.println("📱 AndroidMarksheetPage initialized");
    }
    
    // ============================================
    // CLICK METHODS
    // ============================================
    
    public void clickStudentMarksheet() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(studentMarksheet));
            studentMarksheet.click();
            System.out.println("✅ Clicked on Student Marksheet option");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Student Marksheet: " + e.getMessage());
            throw new RuntimeException("Student Marksheet click failed", e);
        }
    }
    
    public void clickStudentDropdown() {
        try {

            // Open dropdown
            wait.until(ExpectedConditions.elementToBeClickable(studentdropdown));
            studentdropdown.click();

            System.out.println("✅ Student dropdown opened");

            // Wait for option
            wait.until(ExpectedConditions.visibilityOf(studentdropdown));
            wait.until(ExpectedConditions.elementToBeClickable(studentdropdown));

            // Select first option
            studentdropdown.click();

            System.out.println("✅ Student selected successfully");


        } catch (Exception e) {
            System.out.println("❌ Failed to select student: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Student selection failed", e);
        }
    }
    
    public void clickAcademicYearDropdown() {
        try {

            // Click Academic Year dropdown
            wait.until(ExpectedConditions.elementToBeClickable(yeardropdown));
            yeardropdown.click();

            System.out.println("✅ Academic Year dropdown opened");

            // Wait until year options appear
            wait.until(driver -> academicYearOptions.size() > 0);

            System.out.println("📅 Total Academic Years Found: " + academicYearOptions.size());

            // Select first option
            WebElement firstYear = academicYearOptions.get(0);

            wait.until(ExpectedConditions.visibilityOf(firstYear));
            wait.until(ExpectedConditions.elementToBeClickable(firstYear));

            System.out.println("📅 Selecting Year: " + firstYear.getAttribute("content-desc"));

            firstYear.click();

            System.out.println("✅ Academic Year selected successfully");


        } catch (Exception e) {
            System.out.println("❌ Failed to select Academic Year: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Academic Year selection failed", e);
        }
    }
    public void clickViewButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(viewbutton));
            viewbutton.click();
            System.out.println("✅ Clicked View button");
        } catch (Exception e) {
            System.out.println("❌ Failed to click View button: " + e.getMessage());
            throw new RuntimeException("View button click failed", e);
        }
    }
    
    public void clickDownloadButtons() {
        try {


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            // Click Download Button
            WebElement downloadElement = wait.until(
                    ExpectedConditions.elementToBeClickable(downloadButton));

            downloadElement.click();

            System.out.println("✅ Clicked Download button on Marksheet page");

            explicitWait(1);

            // Click Save Button
            WebElement saveElement = wait.until(
                    ExpectedConditions.elementToBeClickable(savebuttonpdf));

            saveElement.click();

            System.out.println("✅ Clicked Save PDF button");

        } catch (Exception e) {
            System.out.println("❌ Failed to click Download/Save button: " + e.getMessage());
            throw new RuntimeException("Download or Save button click failed", e);
        }
    }
    
   
   
    
    
   
    
    
    
    
    // ============================================
    // VERIFICATION METHODS
    // ============================================
    public void clickNavigateUp() {
        try {
            wait.until(ExpectedConditions.visibilityOf(navigateUpButton));
            wait.until(ExpectedConditions.elementToBeClickable(navigateUpButton));
            navigateUpButton.click();
            System.out.println("✅ Clicked Navigate up");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Navigate up: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public void clickBackArrow() {

        try {

            ((AndroidDriver) driver)
                    .pressKey(new KeyEvent(AndroidKey.BACK));

            System.out.println("Clicked Android Back");

        } catch (Exception e) {

            System.out.println("Failed to click back: " + e.getMessage());
        }
    }
    
    
    public boolean isMarksheetPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(studentMarksheet));
            System.out.println("✅ Marksheet page is displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Marksheet page not displayed: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isStudentDropdownVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(studentdropdown));
            System.out.println("✅ Student dropdown is visible");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Student dropdown not visible");
            return false;
        }
    }
    
    public boolean isAcademicYearDropdownVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(yeardropdown));
            System.out.println("✅ Academic year dropdown is visible");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Academic year dropdown not visible");
            return false;
        }
    }
    
    public boolean isViewButtonVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(viewbutton));
            System.out.println("✅ View button is visible");
            return true;
        } catch (Exception e) {
            System.out.println("❌ View button not visible");
            return false;
        }
    }
    
    public boolean isDownloadButtonVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(downloadButton));
            System.out.println("✅ Download button is visible");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Download button not visible");
            return false;
        }
    }
    
    // ============================================
    // ACTION METHODS
    // ============================================
    
    public void viewReportCard() {
        clickViewButton();
        System.out.println("✅ View report card clicked");
    }
    
    public void downloadReportCard() {
        clickDownloadButtons();
        System.out.println("✅ Download report card clicked");
    }
}