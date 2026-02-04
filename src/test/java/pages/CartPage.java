package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class CartPage extends BasePage {
    
    @FindBy(xpath  = "//span[text()='Your Cart']")
    private WebElement cartLabel;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(xpath = "//button[contains(text(),'REMOVE')]")
    private List<WebElement> removeButtons;
    
    @FindBy(className = "btn_action")
    private WebElement checkoutButton;
    
    public void verifyCartPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(cartLabel));
        Assert.assertTrue(cartLabel.isDisplayed(), "Cart page is not displayed");
        Assert.assertTrue(cartLabel.getText().contains("Your Cart"), "Cart label text is incorrect");
    }

    
    public void removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            removeButtons.get(0).click();
        }
    }
    
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    public void proceedToCheckout() {
        checkoutButton.click();
    }
}