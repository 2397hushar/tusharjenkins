package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CheckoutPage;

public class CheckoutSteps {
    
    private CheckoutPage checkoutPage = new CheckoutPage();
    
    @Then("I should see checkout information page")
    public void i_should_see_checkout_information_page() {
        checkoutPage.verifyCheckoutPage();
    }
    
    @When("I verify validation messages")
    public void i_verify_validation_messages() {
        checkoutPage.verifyValidation();
    }
    
    @When("I enter required information and continue")
    public void i_enter_required_information_and_continue() {
        checkoutPage.enterCheckoutInformation("John", "Doe", "12345");
    }
    
    @Then("I should see overview page with products")
    public void i_should_see_overview_page_with_products() {
        checkoutPage.verifyOverviewPage();
    }
    
    @When("I click on finish button")
    public void i_click_on_finish_button() {
        checkoutPage.completePurchase();
    }
    
    @Then("I should see thank you message")
    public void i_should_see_thank_you_message() {
        checkoutPage.verifyThankYouMessage();
    }
}