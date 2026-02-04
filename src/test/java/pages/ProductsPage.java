package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class ProductsPage extends BasePage {
    
    @FindBy(className = "product_label")
    private WebElement productsLabel;
    
    @FindBy(xpath = "//div[@class='inventory_item']")
    private List<WebElement> productItems;
    
    @FindBy(xpath = "//button[@id='add-to-cart-sauce-labs-backpack']")
    private List<WebElement> addToCartButtons;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;
    
    public void verifyProductsPage() {
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement productsLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Products')]")));

        
        Assert.assertTrue(productsLabel.isDisplayed(), "Products page is not displayed");

      
        Assert.assertTrue(productsLabel.getText().contains("Products"), "Products label text is incorrect");
    }

    
    public void addFirstProductToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));  

        
        wait.until(ExpectedConditions.visibilityOfAllElements(productItems));
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));

        if (!productItems.isEmpty() && !addToCartButtons.isEmpty()) {
            addToCartButtons.get(0).click();
        } else {
            System.out.println("No products or Add to Cart buttons available to add to the cart.");
        }
    }

    
    public void applyFilter(String filterType) {
        Select sortSelect = new Select(sortDropdown);
        switch (filterType) {
            case "Price (low to high)":
                sortSelect.selectByValue("lohi");
                break;
            case "Price (high to low)":
                sortSelect.selectByValue("hilo");
                break;
            case "Name (A to Z)":
                sortSelect.selectByValue("az");
                break;
            case "Name (Z to A)":
                sortSelect.selectByValue("za");
                break;
        }
    }
    
    public void addSecondProductToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[contains(@class, 'inventory_item')]"), 1));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//button[contains(@id, 'add-to-cart')]"), 1));

        
        List<WebElement> products = driver.findElements(By.xpath("//div[contains(@class, 'inventory_item')]"));
        List<WebElement> buttons = driver.findElements(By.xpath("//button[contains(@id, 'add-to-cart')]"));

        System.out.println("Products found: " + products.size());
        System.out.println("Buttons found: " + buttons.size());

        if (products.size() > 1 && buttons.size() > 1) {
            buttons.get(1).click();
        } else {
            System.out.println("Not enough products/buttons.");
        }
    }

    
    public void openMenu() {
        menuButton.click();
    }
    
    public void goToAboutPage() {
        openMenu();
        aboutLink.click();
    }
    
    public void goBackToProducts() {
        driver.navigate().back();
    }
    
    public void goToCart() {
        cartIcon.click();
    }
}