package web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.BasePage;

import java.time.Duration;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//div[contains(text(),'Dashboard')]")
    private WebElement dashboardLabel;

    @FindBy(xpath = "//li[@aria-label='Academic Progress']")
    private WebElement academicProgressSection;

    @FindBy(xpath = "//p[contains(text(),'Marksheet')]")
    private WebElement marksheetOption;

    public void scrollToAcademicProgress() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until element is visible
        WebElement element = wait.until(
                ExpectedConditions.visibilityOf(academicProgressSection));

        // Scroll to element
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);

        // Wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(element));

        // Double click action
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }

    public void clickMarksheetOption() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement marksheet = wait.until(
                ExpectedConditions.elementToBeClickable(marksheetOption));

        marksheet.click();
    }
}