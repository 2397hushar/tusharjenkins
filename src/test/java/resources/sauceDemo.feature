Feature: SauceDemo E-commerce Website Automation

  
  
  Scenario: Complete purchase flow on SauceDemo website
    Given I am on the SauceDemo login page
    When I login with valid credentials
    Then I should be redirected to products page
    When I select first product and add to cart
    And I apply filter by "Price (low to high)"
    And I select another product and add to cart
    And I click on side menu and go to About page
    And I go back to products page
    And I click on cart button
    Then I should see cart page with added items
    When I remove one product from cart
    And I click on checkout button
    Then I should see checkout information page
    When I verify validation messages
    And I enter required information and continue
    Then I should see overview page with products
    When I click on finish button
    Then I should see thank you message