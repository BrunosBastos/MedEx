Feature: Add a new Product to an Existent Pharmacy

  Scenario: Introduce Valid Information to create a Product
    When I navigate to 'http://localhost:3000/app/addProduct'
    And I insert information like the name 'UmProduto', the price 3.56, and stock 3
    Then a success message should appear like 'Success creating new Product!'

  Scenario: Introduce Invalid Information to create a Product
    When I navigate to 'http://localhost:3000/app/addProduct'
    And I insert information like the name 'UmProduto', the price -9.56, and stock 2
    Then an error message should appear like 'Error Creating Product'