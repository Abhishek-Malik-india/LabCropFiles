Feature: LabCorp Career Test

  @LabCrop
  Scenario: Navigate to a job listing on LabCorp's website
    Given I open the LabCorp homepage
    When I click on the Careers link
    And I search for a job position "QA Test Automation Developer"
    And I select a job from the search results
    Then I verify the job details
    And I click Apply Now and verify the application page details
    And I return to the Job Search page