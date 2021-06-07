Feature: Add a new Product to an Existent Pharmacy

  Background:
    Given Logged in with email 'clara@gmail.com' and password 'string'
    When I navigate to 'http://localhost:3000/app/addProduct'

  Scenario: Introduce Valid Information to create a Product
    When I insert information like the name 'UmProduto', the price 3.56, and stock 3
    Then a success message should appear like 'Success Adding new Product!'

  Scenario: Introduce Invalid Information to create a Product
    When I insert information like the name 'UmProduto', the price -9.56, and stock 2
    Then an error message should appear like 'Error Creating Product'