Feature: Add a Supplier

  Background:
    Given I am the pharmacy owner on the add supplier page

  Scenario: Introduce Valid Information to create a Supplier
    When I insert information like the name 'New Pharmacy'
    And I insert the latitude 43.5652
    And I insert the longitude -55.42231
    And I press the add a new supplier button
    Then A successfully added a new supplier message should appear

  Scenario: Introduce an Already Existing Supplier
    When I insert an already existing supplier name like 'Pharmacy'
    And I insert the latitude 43.5652
    And I insert the longitude -55.42231
    And I press the add a new supplier button
    Then A failed adding a new supplier message should appear
