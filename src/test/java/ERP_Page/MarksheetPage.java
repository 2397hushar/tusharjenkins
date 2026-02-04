//package ERP_Page;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//
//import pages.BasePage;
//
//import java.time.Duration;
//import java.util.List;
//
//public class MarksheetPage extends BasePage {
//
//	@FindBy(xpath = "//span[contains(text(),'Marksheet')]")
//	private static WebElement marksheetLabel;
//
//	@FindBy(xpath = "//button[contains(@class,'MuiButtonBase-root') and @aria-label='Open']")
//	private WebElement selectAYdropdown;
//
//	@FindBy(xpath = "//input[@value='AY 2025 - 26']")
//	private WebElement academicYearDropdown;
//
//	@FindBy(id = "studentDropdown")
//	private WebElement studentDropdown;
//
//	@FindBy(className = "report-card-item")
//	private List<WebElement> reportCards;
//
//	@FindBy(xpath = "(//*[local-name()='path' and starts-with(@d, 'M12 4.5C7')])[1]")
//	private static WebElement viewButton;
//
//	// New locators for dropdown selections
//	@FindBy(xpath = "//ul[@role='listbox']//li") // Common locator for dropdown options
//	private List<WebElement> dropdownOptions;
//
//	@FindBy(xpath = "//div[contains(@class,'MuiSelect-select')]") // For student dropdown
//	private WebElement studentDropdownButton;
//
//	// Method to select the current academic year
//	public void selectCurrentAcademicYear() {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//			// Click on AY dropdown
//			wait.until(ExpectedConditions.elementToBeClickable(selectAYdropdown));
//			selectAYdropdown.click();
//			System.out.println("Clicked on Academic Year dropdown");
//
//			// Wait for dropdown options to appear
//			Thread.sleep(2000);
//
//			// Select AY 2025-26 from dropdown options
//			boolean yearSelected = selectDropdownOption("AY 2025 - 26");
//
//			if (yearSelected) {
//				System.out.println("Successfully selected AY 2025-26");
//			} else {
//				// Alternative approach: try clicking directly on the option
//				selectAcademicYearAlternative();
//			}
//
//		} catch (Exception e) {
//			System.out.println("Error in selectCurrentAcademicYear: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// Alternative method to select academic year
//	private void selectAcademicYearAlternative() {
//		try {
//			// Try different locators for AY 2025-26
//			String[] possibleSelectors = { "//li[contains(text(),'AY 2025 - 26')]",
//					"//div[contains(text(),'AY 2025 - 26')]", "//*[contains(text(),'AY 2025 - 26')]",
//					"//input[@value='AY 2025 - 26']/following-sibling::div" };
//
//			for (String selector : possibleSelectors) {
//				try {
//					WebElement ayOption = driver.findElement(By.xpath(selector));
//					if (ayOption.isDisplayed()) {
//						ayOption.click();
//						System.out.println("Selected AY 2025-26 using alternative selector: " + selector);
//						return;
//					}
//				} catch (Exception e) {
//					// Continue to next selector
//				}
//			}
//
//			System.out.println("Could not select AY 2025-26 using any selector");
//
//		} catch (Exception e) {
//			System.out.println("Error in selectAcademicYearAlternative: " + e.getMessage());
//		}
//	}
//
//	// Method to select student from dropdown
//	public void selectStudentFromDropdown() {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//			// Wait for student dropdown to be clickable
//			wait.until(ExpectedConditions.elementToBeClickable(studentDropdownButton));
//			studentDropdownButton.click();
//			System.out.println("Clicked on Student dropdown");
//
//			// Wait for dropdown options to appear
//			Thread.sleep(2000);
//
//			// Select any available student (skip the first option if it's "Select Student")
//			boolean studentSelected = selectStudentOption();
//
//			if (studentSelected) {
//				System.out.println("Successfully selected a student");
//			} else {
//				selectStudentAlternative();
//			}
//
//			// Wait for report cards to load after student selection
//			Thread.sleep(3000);
//
//		} catch (Exception e) {
//			System.out.println("Error in selectStudentFromDropdown: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// Method to select student option from dropdown
//	private boolean selectStudentOption() {
//		try {
//			// Get all dropdown options
//			List<WebElement> options = dropdownOptions;
//
//			if (options.size() > 1) {
//				// Skip the first option if it's "Select Student" or placeholder
//				for (int i = 1; i < options.size(); i++) {
//					WebElement option = options.get(i);
//					if (option.isDisplayed() && option.isEnabled()) {
//						String optionText = option.getText().trim();
//						if (!optionText.isEmpty() && !optionText.equalsIgnoreCase("Select Student")
//								&& !optionText.equalsIgnoreCase("Select")) {
//
//							option.click();
//							System.out.println("Selected student: " + optionText);
//							return true;
//						}
//					}
//				}
//			}
//
//			return false;
//
//		} catch (Exception e) {
//			System.out.println("Error in selectStudentOption: " + e.getMessage());
//			return false;
//		}
//	}
//
//	// Alternative method to select student
//	private void selectStudentAlternative() {
//		try {
//			// Try different approaches to select student
//			String[] studentSelectors = { "//li[contains(@role,'option')][2]", // Second option
//					"//div[contains(@class,'MuiMenuItem-root')][2]", "//ul[@role='listbox']/li[1]" };
//
//			for (String selector : studentSelectors) {
//				try {
//					WebElement studentOption = driver.findElement(By.xpath(selector));
//					if (studentOption.isDisplayed()) {
//						String studentName = studentOption.getText();
//						studentOption.click();
//						System.out.println("Selected student using alternative: " + studentName);
//						return;
//					}
//				} catch (Exception e) {
//					// Continue to next selector
//				}
//			}
//
//			System.out.println("Could not select any student");
//
//		} catch (Exception e) {
//			System.out.println("Error in selectStudentAlternative: " + e.getMessage());
//		}
//	}
//
//	// Generic method to select dropdown option by text
//	private boolean selectDropdownOption(String optionText) {
//		try {
//			for (WebElement option : dropdownOptions) {
//				if (option.getText().trim().equals(optionText)) {
//					option.click();
//					return true;
//				}
//			}
//			return false;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	// Method to verify if the report cards are displayed
//	public boolean areReportCardsDisplayed() {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			wait.until(ExpectedConditions.visibilityOfAllElements(reportCards));
//			return !reportCards.isEmpty() && reportCards.get(0).isDisplayed();
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	public static void clickViewReportCard() {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement viewBtn = wait.until(ExpectedConditions.elementToBeClickable(viewButton));
//			viewBtn.click();
//			System.out.println("Clicked on View Report Card button");
//		} catch (Exception e) {
//			System.out.println("Error clicking view button: " + e.getMessage());
//			throw e;
//		}
//	}
//
//	// Method to verify marksheet page is loaded
//	public void verifyMarksheetPage() {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			wait.until(ExpectedConditions.visibilityOf(marksheetLabel));
//			Assert.assertTrue(marksheetLabel.isDisplayed(), "Marksheet page is not displayed");
//			System.out.println("Marksheet page verified successfully");
//		} catch (Exception e) {
//			System.out.println("Error verifying marksheet page: " + e.getMessage());
//			throw e;
//		}
//	}
//}
//************************************************* updated belwo

package ERP_Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pages.BasePage;

import java.time.Duration;
import java.util.List;

public class MarksheetPage extends BasePage {

	@FindBy(xpath = "//span[contains(text(),'Marksheet')]")
	private static WebElement marksheetLabel;

	@FindBy(xpath = "//button[contains(@class,'MuiButtonBase-root') and @aria-label='Open']")
	private WebElement selectAYdropdown;

	@FindBy(xpath = "//input[@value='AY 2025 - 26']")
	private WebElement academicYearDropdown;

	@FindBy(id = "studentDropdown")
	private WebElement studentDropdown;

	@FindBy(className = "report-card-item")
	private List<WebElement> reportCards;

	@FindBy(xpath = "(//*[local-name()='path' and starts-with(@d, 'M12 4.5C7')])[1]")
	private static WebElement viewButton;

	// New locators for dropdown selections
	@FindBy(xpath = "//ul[@role='listbox']//li") // Common locator for dropdown options
	private List<WebElement> dropdownOptions;

	@FindBy(xpath = "//div[contains(@class,'MuiSelect-select')]") // For student dropdown
	private WebElement studentDropdownButton;

	// Method to select the current academic year
	public void selectCurrentAcademicYear() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Click on AY dropdown
			wait.until(ExpectedConditions.elementToBeClickable(selectAYdropdown));
			selectAYdropdown.click();
			System.out.println("Clicked on Academic Year dropdown");

			// Wait for dropdown options to appear
			Thread.sleep(2000);

			// Select AY 2025-26 from dropdown options
			boolean yearSelected = selectDropdownOption("AY 2025 - 26");

			if (yearSelected) {
				System.out.println("Successfully selected AY 2025-26");
			} else {
				// Alternative approach: try clicking directly on the option
				selectAcademicYearAlternative();
			}

		} catch (Exception e) {
			System.out.println("Error in selectCurrentAcademicYear: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Alternative method to select academic year
	private void selectAcademicYearAlternative() {
		try {
			// Try different locators for AY 2025-26
			String[] possibleSelectors = { "//li[contains(text(),'AY 2025 - 26')]",
					"//div[contains(text(),'AY 2025 - 26')]", "//*[contains(text(),'AY 2025 - 26')]",
					"//input[@value='AY 2025 - 26']/following-sibling::div" };

			for (String selector : possibleSelectors) {
				try {
					WebElement ayOption = driver.findElement(By.xpath(selector));
					if (ayOption.isDisplayed()) {
						ayOption.click();
						System.out.println("Selected AY 2025-26 using alternative selector: " + selector);
						return;
					}
				} catch (Exception e) {
					// Continue to next selector
				}
			}

			System.out.println("Could not select AY 2025-26 using any selector");

		} catch (Exception e) {
			System.out.println("Error in selectAcademicYearAlternative: " + e.getMessage());
		}
	}

	// Method to select student from dropdown
	public void selectStudentFromDropdown() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for student dropdown to be clickable
			wait.until(ExpectedConditions.elementToBeClickable(studentDropdownButton));
			studentDropdownButton.click();
			System.out.println("Clicked on Student dropdown");

			// Wait for dropdown options to appear
			Thread.sleep(2000);

			// Select the first available student
			boolean studentSelected = selectFirstStudent();

			if (studentSelected) {
				System.out.println("Successfully selected the first student");
			} else {
				selectStudentAlternative();
			}

			// Wait for report cards to load after student selection
			Thread.sleep(3000);

		} catch (Exception e) {
			System.out.println("Error in selectStudentFromDropdown: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Method to select the first student option from dropdown
	private boolean selectFirstStudent() {
		try {
			// Get all dropdown options
			List<WebElement> options = dropdownOptions;

			if (!options.isEmpty()) {
				// Find the first non-placeholder student option
				for (WebElement option : options) {
					if (option.isDisplayed() && option.isEnabled()) {
						String optionText = option.getText().trim();
						
						// Skip placeholder options
						if (!optionText.isEmpty() && 
							!optionText.equalsIgnoreCase("Select Student") &&
							!optionText.equalsIgnoreCase("Select") &&
							!optionText.equalsIgnoreCase("Choose Student") &&
							!optionText.equalsIgnoreCase("-- Select --")) {
							
							option.click();
							System.out.println("Selected first student: " + optionText);
							return true;
						}
					}
				}
				
				// If no valid student found in first pass, try selecting any non-empty option
				for (WebElement option : options) {
					if (option.isDisplayed() && option.isEnabled()) {
						String optionText = option.getText().trim();
						if (!optionText.isEmpty()) {
							option.click();
							System.out.println("Selected available student: " + optionText);
							return true;
						}
					}
				}
			}

			System.out.println("No valid student options found in dropdown");
			return false;

		} catch (Exception e) {
			System.out.println("Error in selectFirstStudent: " + e.getMessage());
			return false;
		}
	}

	// Alternative method to select student
	private void selectStudentAlternative() {
		try {
			System.out.println("Trying alternative student selection methods...");
			
			// Try different approaches to select student
			String[] studentSelectors = { 
				"//li[contains(@role,'option')][1]", // First option
				"//div[contains(@class,'MuiMenuItem-root')][1]", // First menu item
				"//ul[@role='listbox']/li[1]", // First list item
				"//*[@role='option'][1]", // First option with role
				"//*[contains(@class,'MuiMenuItem-root') and not(contains(text(),'Select'))][1]" // First non-select menu item
			};

			for (String selector : studentSelectors) {
				try {
					WebElement studentOption = driver.findElement(By.xpath(selector));
					if (studentOption.isDisplayed() && studentOption.isEnabled()) {
						String studentName = studentOption.getText().trim();
						if (!studentName.isEmpty() && 
							!studentName.equalsIgnoreCase("Select Student") &&
							!studentName.equalsIgnoreCase("Select")) {
							
							studentOption.click();
							System.out.println("Selected student using alternative selector '" + selector + "': " + studentName);
							return;
						}
					}
				} catch (Exception e) {
					// Continue to next selector
				}
			}

			// Last resort: try to click any visible student option
			try {
				List<WebElement> allOptions = driver.findElements(By.xpath("//*[@role='option'] | //li | //div[contains(@class,'menu-item')]"));
				for (WebElement option : allOptions) {
					if (option.isDisplayed() && option.isEnabled()) {
						String optionText = option.getText().trim();
						if (!optionText.isEmpty() && 
							!optionText.equalsIgnoreCase("Select Student") &&
							!optionText.equalsIgnoreCase("Select")) {
							
							option.click();
							System.out.println("Selected student from all options: " + optionText);
							return;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Could not select any student using alternative methods");
			}

		} catch (Exception e) {
			System.out.println("Error in selectStudentAlternative: " + e.getMessage());
		}
	}

	// Enhanced method to select specific student by index
	public void selectStudentByIndex(int index) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Wait for student dropdown to be clickable
			wait.until(ExpectedConditions.elementToBeClickable(studentDropdownButton));
			studentDropdownButton.click();
			System.out.println("Clicked on Student dropdown");

			// Wait for dropdown options to appear
			Thread.sleep(2000);

			// Select student by specific index
			boolean studentSelected = selectStudentAtIndex(index);

			if (studentSelected) {
				System.out.println("Successfully selected student at index: " + index);
			} else {
				System.out.println("Could not select student at index: " + index + ", falling back to first student");
				selectFirstStudent();
			}

			// Wait for report cards to load after student selection
			Thread.sleep(3000);

		} catch (Exception e) {
			System.out.println("Error in selectStudentByIndex: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Method to select student at specific index
	private boolean selectStudentAtIndex(int index) {
		try {
			// Get all dropdown options
			List<WebElement> options = dropdownOptions;

			if (options.size() > index) {
				WebElement option = options.get(index);
				if (option.isDisplayed() && option.isEnabled()) {
					String optionText = option.getText().trim();
					option.click();
					System.out.println("Selected student at index " + index + ": " + optionText);
					return true;
				}
			}

			System.out.println("No student found at index: " + index);
			return false;

		} catch (Exception e) {
			System.out.println("Error in selectStudentAtIndex: " + e.getMessage());
			return false;
		}
	}

	// Method to get available student count
	public int getAvailableStudentCount() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			
			// Click dropdown to see options
			wait.until(ExpectedConditions.elementToBeClickable(studentDropdownButton));
			studentDropdownButton.click();
			Thread.sleep(1000);
			
			// Count non-placeholder options
			List<WebElement> options = dropdownOptions;
			int validStudentCount = 0;
			
			for (WebElement option : options) {
				String optionText = option.getText().trim();
				if (!optionText.isEmpty() && 
					!optionText.equalsIgnoreCase("Select Student") &&
					!optionText.equalsIgnoreCase("Select")) {
					validStudentCount++;
				}
			}
			
			// Close dropdown
			studentDropdownButton.click();
			
			System.out.println("Available students count: " + validStudentCount);
			return validStudentCount;
			
		} catch (Exception e) {
			System.out.println("Error getting student count: " + e.getMessage());
			return 0;
		}
	}

	// Generic method to select dropdown option by text
	private boolean selectDropdownOption(String optionText) {
		try {
			for (WebElement option : dropdownOptions) {
				if (option.getText().trim().equals(optionText)) {
					option.click();
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	// Method to verify if the report cards are displayed
	public boolean areReportCardsDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOfAllElements(reportCards));
			return !reportCards.isEmpty() && reportCards.get(0).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public static void clickViewReportCard() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement viewBtn = wait.until(ExpectedConditions.elementToBeClickable(viewButton));
			viewBtn.click();
			System.out.println("Clicked on View Report Card button");
		} catch (Exception e) {
			System.out.println("Error clicking view button: " + e.getMessage());
			throw e;
		}
	}

	// Method to verify marksheet page is loaded
	public void verifyMarksheetPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(marksheetLabel));
			Assert.assertTrue(marksheetLabel.isDisplayed(), "Marksheet page is not displayed");
			System.out.println("Marksheet page verified successfully");
		} catch (Exception e) {
			System.out.println("Error verifying marksheet page: " + e.getMessage());
			throw e;
		}
	}
}







