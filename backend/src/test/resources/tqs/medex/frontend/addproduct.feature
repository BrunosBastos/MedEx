Feature: Add a new Product to an Existent Pharmacy

  Background:
    Given I am logged in as the pharmacy owner
    And I am on the add product page

  Scenario: Introduce Valid Information to create a Product
    When I insert information like the name 'UmProduto', the price 3.56, and stock 3
    Then A successfully adding a new product message should appear

  Scenario: Introduce Invalid Information to create a Product
    When I insert information like the name 'UmProduto', the price -9.56, and stock 2
    Then A failed creating product message should appear