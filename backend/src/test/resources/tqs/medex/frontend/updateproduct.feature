Feature: Update a specific Product

  Background:
    Given I am logged in as the pharmacy owner And I am on the update product 1 page

  Scenario: Update Sucessfully the Product Details
    When I insert new information like an imageUrl, current stock of 5, and price of 6.99
    Then A 'Succesfully updated product' success message should appear

  Scenario: Error Updating Product Details
    When I insert new information like an imageUrl, current stock of -5, and price of -2.0
    Then A 'Error updating product' error message should appear
