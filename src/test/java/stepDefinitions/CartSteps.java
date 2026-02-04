package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.CartPage;
import pages.ProductsPage;

public class CartSteps {
    
    private CartPage cartPage = new CartPage();
    private ProductsPage productsPage = new ProductsPage();
    
    @Then("I should see cart page with added items")
    public void i_should_see_cart_page_with_added_items() {
        cartPage.verifyCartPage();
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "No items in cart");
    }
    
    @When("I remove one product from cart")
    public void i_remove_one_product_from_cart() {
        int initialCount = cartPage.getCartItemCount();
        cartPage.removeFirstItem();
    }
    
    @When("I click on checkout button")
    public void i_click_on_checkout_button() {
        cartPage.proceedToCheckout();
    }
}