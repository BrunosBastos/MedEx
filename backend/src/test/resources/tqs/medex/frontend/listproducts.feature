Feature: Browse all available Products

  Background:
    Given I am logged in as the pharmacy owner And I am on the list products page

  Scenario: Check a list with the two products
    When there are 2 products
    Then some info about them should appear like, for example, the name 'ProductTest' and the price 4.99