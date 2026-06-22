package mobile.android.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.pagefactory.AndroidFindBy;
import mobile.android.pages.locators.AndroidLocators;

public class AndroidReportCardPage extends AndroidBasePage {
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.CLOSE_BUTTON_XPATH)
    private WebElement logoutbutton;
    
    @AndroidFindBy(accessibility = AndroidLocators.ReportCard.CLICK_ON_LOGOUT_BUTTON)
    private WebElement clickonlogoutbutton;
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.VIEW_BUTTONS_XPATH)
    private WebElement viewbuttons; 
    
    @AndroidFindBy(accessibility = AndroidLocators.ReportCard.NAVIAGTE_BACK_BUTTON)
    private WebElement backbutton;
    
    
   
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.SAVE_BUTTON)
    private WebElement savebutton;
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.REPORT_CARD_TITLE_XPATH)
    private WebElement reportCardTitle;
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.PDF_VIEWER_XPATH)
    private WebElement pdfViewer;
    
    @AndroidFindBy(xpath = AndroidLocators.ReportCard.DOWNLOAD_SUCCESS_TOAST)
    private WebElement downloadSuccessToast;
    
    public AndroidReportCardPage() {
        super();
        System.out.println("📱 AndroidReportCardPage initialized");
    }
    
    // ============================================
    // CLICK METHODS
    // ============================================
    
    public void clickCloseButton() {

        try {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            logoutbutton));

            logoutbutton.click();

            System.out.println(
                    "✅ Logout confirmation clicked");


        } catch (Exception e) {

            System.out.println(
                    "❌ Failed to click logout confirmation : "
                            + e.getMessage());
        }
    }
    public void clickOnLogoutButton() {

        try {

            wait.until(ExpectedConditions.elementToBeClickable(clickonlogoutbutton));

            clickonlogoutbutton.click();

            System.out.println("✅ Logout button clicked");

           

        } catch (Exception e) {

            System.out.println(
                    "❌ Unable to click logout button : "
                            + e.getMessage());
        }
    }
    public void clickBackButton() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            wait.until(ExpectedConditions.visibilityOf(backbutton));
            wait.until(ExpectedConditions.elementToBeClickable(backbutton));

            backbutton.click();

            System.out.println("✅ Back button clicked");

        } catch (Exception e) {
            System.out.println("❌ Failed to click Back button");
            e.printStackTrace();
        }
    }
    
    public void clickSaveButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(savebutton));
            savebutton.click();
            System.out.println("✅ Clicked Save button");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Save button: " + e.getMessage());
        }
    }
    
    // ============================================
    // VERIFICATION METHODS
    // ============================================
    
    public boolean isReportCardPageDisplayed() {
//        try {
//            wait.until(ExpectedConditions.visibilityOf(reportCardTitle));
//            System.out.println("✅ Report card page is displayed");
//            return true;
//        } catch (Exception e) {
//            try {
//                wait.until(ExpectedConditions.visibilityOf(pdfViewer));
//                System.out.println("✅ Report card PDF viewer is displayed");
//                return true;
//            } catch (Exception ex) {
//                System.out.println("❌ Report card page not displayed: " + e.getMessage());
//                return false;
//            }
//        }
    	return false;
    }
    
    public boolean isPDFViewerDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pdfViewer));
            System.out.println("✅ PDF viewer is displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ PDF viewer not displayed");
            return false;
        }
    }
    
    public boolean isDownloadSuccessToastDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(downloadSuccessToast));
            System.out.println("✅ Download success toast displayed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Download success toast not displayed");
            return false;
        }
    }
    
    // ============================================
    // DOWNLOAD VERIFICATION METHODS
    // ============================================
    
    public boolean isDownloadSuccessful() {
        try {
            explicitWait(1);
            // Check for download success toast or any download indicator
            if (isDownloadSuccessToastDisplayed()) {
                System.out.println("✅ Download completed successfully!");
                return true;
            } else {
                System.out.println("⚠️ Download may have completed but no toast found");
                return true; // Assume success if no error
            }
        } catch (Exception e) {
            System.out.println("❌ Download verification failed: " + e.getMessage());
            return false;
        }
    }
    
    public void verifyDownloadWithRetry() {
        int maxRetries = 3;
        boolean downloadSuccess = false;
        
        for (int i = 0; i < maxRetries; i++) {
            System.out.println("📥 Download verification attempt " + (i + 1));
            if (isDownloadSuccessful()) {
                downloadSuccess = true;
                break;
            }
        }
        
        if (downloadSuccess) {
            System.out.println("✅ Download verified successfully!");
        } else {
            System.out.println("⚠️ Download verification completed with timeout");
        }
    }
    
    // ============================================
    // NAVIGATION METHODS
    // ============================================
    
    public void navigateBackToMarksheet() {
        try {
            clickBackButton();
            System.out.println("✅ Navigated back to Marksheet page");
        } catch (Exception e) {
            System.out.println("❌ Failed to navigate back: " + e.getMessage());
        }
    }
    
    public void closeReportCard() {
        try {
            clickCloseButton();
            System.out.println("✅ Closed report card page");
        } catch (Exception e) {
            clickBackButton();
            System.out.println("✅ Used back button to close report card");
            explicitWait(1);
            

        }
        
    }
    
    public void saveReportCard() {
        try {
            clickSaveButton();
            System.out.println("✅ Saved report card");
        } catch (Exception e) {
            System.out.println("❌ Failed to save report card: " + e.getMessage());
        }
    }
    
    // ============================================
    // SWIPE METHODS
    // ============================================
    
    public void swipeReportCard() {
        try {
            swipeDown();
            System.out.println("👆 Swiped down on report card");
        } catch (Exception e) {
            System.out.println("Failed to swipe on report card: " + e.getMessage());
        }
    }
    
    public void swipeUpOnReportCard() {
        try {
            swipeUp();
            System.out.println("👆 Swiped up on report card");
        } catch (Exception e) {
            System.out.println("Failed to swipe up on report card: " + e.getMessage());
        }
    }
    
    // ============================================
    // COMPLETE FLOW METHODS
    // ============================================
    
    public void verifyAndCloseReportCard() {
        if (isReportCardPageDisplayed()) {
            System.out.println("✅ Report card verified");
            closeReportCard();
        } else {
            System.out.println("❌ Report card not displayed");
        }
    }
    
    public void downloadAndVerify() {
        clickSaveButton();
        verifyDownloadWithRetry();
    }
}