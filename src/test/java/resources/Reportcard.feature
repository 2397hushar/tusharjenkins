@android
Feature: Android Report Card

  Background:
    Given I am on android application login page

  Scenario Outline: Download report card in android - <user>
    When I login with valid android admin credentials for "<user>"
    And I click on android Marksheet option
    And I select current android academic year from dropdown 
    And I select android student from student dropdown
    When I click on android View button for report card
    Then I should see android report card details page
    When I click on android Download button
    Then android report card file should download successfully for "<user>" 

    Examples:
      | user   |    
      | User 1 |
      | User 2 |
      | User 3 |
      | User 4 |
       