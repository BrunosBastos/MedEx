Feature: Register as a Client

  Background:
    Given I am a Client trying to register

  Scenario: Introduce An Email Already in Use to Register
    When I insert an email in use like 'henrique@gmail.com'
    And I insert a name like 'Iglesias'
    And I insert a password like 'string'
    And I press the register button
    Then A failed registered message should appear
