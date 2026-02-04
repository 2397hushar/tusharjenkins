#Feature: Report Cards Module Functionality

 # Scenario Outline: Complete report cards flow for student marksheet
  #  Given I am on the application login page
  #  When I login with valid admin credentials
  #  And I scroll down to Academic Progress section
  #  And I click on Marksheet option
  #  And I select current academic year from dropdown
  #  And I select a student from student dropdown
  #  When I click on View button for report card
  #  Then I should see report card details page
  #  When I click on Download button
  #  Then report card file should download successfully
  
  Feature: Report Cards Module Functionality with Multiple Users

  Scenario Outline: Complete report cards flow for student marksheet with different users
    Given I am on the application login page for user "<userType>"
    When I login with valid admin credentials for "<userType>"
    And I scroll down to Academic Progress section
    And I click on Marksheet option
    And I select current academic year from dropdown
    And I select a student from student dropdown
    When I click on View button for report card
    Then I should see report card details page
    When I click on Download button
    Then report card file should download successfully for "<userType>"

    Examples:
      | userType |
      | User 1   |
      | User 2   |
      | User 3   |
      | User 4   |
