package stepDefinitions;

import io.cucumber.java.en.When;
import pages.ProductsPage;

public class ProductSteps {
    
    private ProductsPage productsPage = new ProductsPage();
    
    @When("I select first product and add to cart")
    public void i_select_first_product_and_add_to_cart() {
        productsPage.addFirstProductToCart();
    }
    
    @When("I apply filter by {string}")
    public void i_apply_filter_by(String filterType) {
        productsPage.applyFilter(filterType);
    }
    
    @When("I select another product and add to cart")
    public void i_select_another_product_and_add_to_cart() {
        productsPage.addSecondProductToCart();
    }
    
    @When("I click on side menu and go to About page")
    public void i_click_on_side_menu_and_go_to_about_page() {
        productsPage.goToAboutPage();
    }
    
    @When("I go back to products page")
    public void i_go_back_to_products_page() {
        productsPage.goBackToProducts();
    }
    
    @When("I click on cart button")
    public void i_click_on_cart_button() {
        productsPage.goToCart();
    }
}