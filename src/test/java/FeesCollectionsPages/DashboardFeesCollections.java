package FeesCollectionsPages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BasePage;

public class DashboardFeesCollections extends BasePage {

	@FindBy (xpath = "//input[@placeholder='Search...']")
	WebElement searchField;
	
	@FindBy(id="combo-box-demo")
	WebElement SearchDetails;
	
	
	public void Search_Input() {
		searchField.sendKeys("Fee collection");
		searchField.click();
		
		
		
	}
	
	public void Search_Student_Details() {
		SearchDetails.sendKeys("EN10332531294");
		SearchDetails.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true)";
		try {
			Thread.sleep(1000); // Wait for 1 second for scrolling to complete
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
	}
	
}
