Feature: Purchase medicine

  Background:
    Given I am a client on the products list page
    When I add a few products to my Cart
    And I go to my Shopping Cart

  Scenario: Make a valid purchase
    And I set the quantity of a product to 2
    And I press the purchase button
    And I insert the latitude for the delivery location as 40.34
    And I insert the longitude for the delivery location as 55.12
    And I finalize my purchase
    Then A successfully made a purchase message should appear

  Scenario: Make a purchase requesting amounts that exceed stock
    And I set the quantity of a product to 99999
    And I press the purchase button
    And I insert the latitude for the delivery location as 40.34
    And I insert the longitude for the delivery location as 55.12
    And I finalize my purchase
    Then A purchase failed message should appear

  Scenario: Make a purchase with an invalid delivery location
    And I press the purchase button
    And I insert the latitude for the delivery location as 250.01
    And I insert the longitude for the delivery location as 45.123
    And I finalize my purchase
    Then A purchase failed message should appear