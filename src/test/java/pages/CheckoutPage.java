package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class CheckoutPage extends BasePage {
    
    @FindBy( xpath = "//span[contains(text(),'Checkout: Your Information')]")
    private WebElement checkoutLabel;
    
    @FindBy( xpath = "//span[starts-with(text(),'Checkout: Overview')]")
    private WebElement CheckoutOverview;

    
    @FindBy(id = "first-name")
    private WebElement firstNameField;
    
    @FindBy(id = "last-name")
    private WebElement lastNameField;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeField;
    
    @FindBy(className = "btn_primary")
    private WebElement continueButton;
    
    @FindBy(className = "error-button")
    private WebElement errorButton;
    
    @FindBy(className = "cart_button")
    private WebElement finishButton;
    
    @FindBy(className = "complete-header")
    private WebElement thankYouMessage;
    
    public void verifyCheckoutPage() {
        Assert.assertTrue(checkoutLabel.isDisplayed(), "Checkout page is not displayed");
        Assert.assertTrue(checkoutLabel.getText().contains("Checkout"), 
                         "Checkout label text is incorrect");
    }
    
    public void verifyValidation() {
        continueButton.click();
        Assert.assertTrue(errorButton.isDisplayed(), "Validation error not displayed");
    }
    
    public void enterCheckoutInformation(String firstName, String lastName, String postalCode) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        postalCodeField.sendKeys(postalCode);
        continueButton.click();
    }
    
    public void verifyOverviewPage() {
        Assert.assertTrue(CheckoutOverview.getText().contains("Overview"), 
                         "Overview page not displayed");
    }
    
    public void completePurchase() {
        finishButton.click();
    }
    
    public void verifyThankYouMessage() {
        Assert.assertTrue(thankYouMessage.isDisplayed(), "Thank you message not displayed");
        Assert.assertEquals(thankYouMessage.getText(), "Thank you for your order!");
    }
}