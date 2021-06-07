Feature: Register as a Client

  Background: I navigate to 'http://localhost:3000/register'

  Scenario: Introduce Invalid Information to Register
    And I insert an invalid email like 'teste@'
    Then an error message appears saying 'Invalid Email'