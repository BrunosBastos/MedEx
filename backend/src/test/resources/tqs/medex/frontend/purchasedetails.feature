Feature: Get Purchase Details

  Background:
    Given I am a client on the purchase details page with id 1

  Scenario: Purchase Already Delivered
    Then two Products should appear
    And the final price should be of 69.70
    And a card to submit a review should appear