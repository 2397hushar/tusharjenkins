package ERP_Page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pages.BasePage;

import java.time.Duration;

public class DashboardPage extends BasePage {

	@FindBy(xpath = "//div[contains(text(),'Dashboard')]")
	private static WebElement dashboardLabel;

	@FindBy(xpath = "//li[@aria-label='Academic Progress']")
	private WebElement academicProgressSection;

	@FindBy(xpath = "//p[contains(text(),'Marksheet')]")
	private WebElement marksheetOption;

	public void scrollToAcademicProgress() {
		// JavaScript scroll to academic progress section
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", academicProgressSection);
		try {
			Thread.sleep(1000); // Wait for 1 second for scrolling to complete
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		academicProgressSection.click();

	}

	public void clickMarksheetOption() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(marksheetOption));
		marksheetOption.click();
	}

}