//package stepDefinitions;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import pages.LoginPage;
//import pages.ProductsPage;
//import utilities.ConfigReader;
//
//public class LoginSteps {
//    
//    private LoginPage loginPage;
//    private ProductsPage productsPage;
//    
//    
//    @Given("I am on the SauceDemo login page")
//    public void i_am_on_the_sauce_demo_login_page() {
//     
//        loginPage = new LoginPage();
//        loginPage.navigateToLoginPage();
//      // loginPage.verifyLoginPage();
//    }
//    
//    @When("I login with valid credentials")
//    public void i_login_with_valid_credentials() {
//        loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
//    }
//    
//    @Then("I should be redirected to products page")
//    public void i_should_be_redirected_to_products_page() {
//        productsPage = new ProductsPage();
//        productsPage.verifyProductsPage();
//    }
//}