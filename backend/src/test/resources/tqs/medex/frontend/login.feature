Feature: Login as a Client

  Background: I navigate to 'http://localhost:3000/login'

  Scenario: Introduce Invalid Information to Register
    When I insert my email 'henrique@gmail.com'
    And I insert my password 'wrong'
    Then I see a message an error appear

  Scenario: Introduce Valid Information to Login
    When I insert my email 'henrique@gmail.com'
    And I insert my password 'string'
    Then I see a message appear logged in and I am redirected to another page
