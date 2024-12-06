Feature: API Automation with REST Assured

  Scenario: Validate the GET request response
    Given I send a GET request to "https://echo.free.beeceptor.com/sample-request?author=beeceptor"
    Then I validate the response contains "path", "ip", and all headers

  Scenario: Validate the POST request response
    Given I send a POST request to "http://echo.free.beeceptor.com/sample-request?author=beeceptor" with the payload
    Then I validate the POST response for customer, payment, and items