package ERP_Page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.BasePage;
import utilities.ConfigReader;

import java.time.Duration;

public class SimonsLoginPage extends BasePage {

	@FindBy(id = "email")
	private WebElement usernameField;

	@FindBy(id = "password")
	private WebElement passwordField;

	@FindBy(id = "kc-login")
	private WebElement loginButton;

	public void navigateToSimonsLoginPage() {
		String url = ConfigReader.getUrl();
		driver.get(url);
	}

	public void loginToSimons(String username, String password) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		// Enter username
		wait.until(ExpectedConditions.visibilityOf(usernameField));
		// usernameField.clear();
		usernameField.sendKeys(username);

		// Enter password
		wait.until(ExpectedConditions.visibilityOf(passwordField));
		// passwordField.clear();
		passwordField.sendKeys(password);

		// Click login
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();

		// Wait for navigation
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
	}

	public void clickForgotPassword() {
		// TODO: Implement when needed
	}
}