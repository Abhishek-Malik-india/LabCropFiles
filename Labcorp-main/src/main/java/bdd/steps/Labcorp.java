package bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class Labcorp {
    WebDriver driver;
    WebDriverWait wait;

    @Given("I open the LabCorp homepage")
    public void i_open_the_labcorp_homepage() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//        WebDriverManager
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.labcorp.com");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @When("I click on the Careers link")
    public void i_click_on_the_careers_link() {
        WebElement careersLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Careers")));
        careersLink.click();
    }

    @When("I search for a job position {string}")
    public void i_search_for_a_job_position(String jobTitle) {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-keyword")));
        searchBox.sendKeys(jobTitle);
        WebElement searchButton = driver.findElement(By.id("search-button"));
        searchButton.click();
    }

    @When("I select a job from the search results")
    public void i_select_a_job_from_the_search_results() {
        WebElement firstJob = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'job-title-link')]")));
        firstJob.click();
    }

    @Then("I verify the job details")
    public void i_verify_the_job_details() {
        // Assertions
        WebElement jobTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='job-title']")));
        assertEquals("QA Test Automation Developer", jobTitle.getText());

        WebElement jobLocation = driver.findElement(By.xpath("//span[@class='job-location']"));
        assertEquals("Durham, NC", jobLocation.getText());

        WebElement jobId = driver.findElement(By.xpath("//span[@class='job-id']"));
        assertTrue(jobId.getText().contains("Job ID: 12345"));

        // Additional Assertions
        WebElement description = driver.findElement(By.xpath("(//div[@class='job-description']//p)[3]"));
        assertEquals("The right candidate for this role will participate in the test automation technology development and best practice models.", description.getText());

        WebElement bulletPoint = driver.findElement(By.xpath("(//ul[@class='job-requirements']//li)[2]"));
        assertEquals("Prepare test plans, budgets, and schedules.", bulletPoint.getText());

        WebElement experienceRequirement = driver.findElement(By.xpath("(//ul[@class='job-requirements']//li)[3]"));
        assertEquals("5+ years of experience in QA automation development and scripting.", experienceRequirement.getText());
    }

    @Then("I click Apply Now and verify the application page details")
    public void i_click_apply_now_and_verify_the_application_page_details() {
        WebElement applyNowButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Apply Now')]")));
        applyNowButton.click();

        WebElement jobTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='job-title']")));
        assertEquals("QA Test Automation Developer", jobTitle.getText());

        WebElement jobLocation = driver.findElement(By.xpath("//span[@class='job-location']"));
        assertEquals("Durham, NC", jobLocation.getText());

        WebElement jobId = driver.findElement(By.xpath("//span[@class='job-id']"));
        assertTrue(jobId.getText().contains("Job ID: 12345"));

        WebElement additionalText = driver.findElement(By.xpath("//div[@class='application-info']//p"));
        assertEquals("Ensure to complete your profile before submitting your application.", additionalText.getText());
    }

    @Then("I return to the Job Search page")
    public void i_return_to_the_job_search_page() {
        WebElement returnToSearchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Return to Job Search')]")));
        returnToSearchButton.click();
        assertTrue(driver.getCurrentUrl().contains("search-results"));
    }
}