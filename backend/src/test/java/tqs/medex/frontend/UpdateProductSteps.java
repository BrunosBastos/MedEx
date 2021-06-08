package tqs.medex.frontend;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.medex.frontend.pages.ErrorMessage;
import tqs.medex.frontend.pages.LoginPage;
import tqs.medex.frontend.pages.UpdateProductPage;

public class UpdateProductSteps {

    private final UpdateProductPage updateProductPage;
    private final ErrorMessage errorMessage;
    private final LoginPage loginPage;
    private final WebDriver driver;

    public UpdateProductSteps() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        loginPage = new LoginPage(driver);
        updateProductPage = new UpdateProductPage(driver);
        errorMessage = new ErrorMessage(driver);

    }

    @Given("I am logged in as the pharmacy owner And I am on the update product {int} page")
    public void logInandSwitchPage(int prodid){
        loginPage.loggInAs("clara@gmail.com", "string");
        updateProductPage.goTo(1);
    }

    @When("I insert new information like an imageUrl, current stock of {int}, and price of {double}")
    public void insertInfo(int stock, double price) {
        updateProductPage.insertInfo(stock,price);
    }

    @Then("A {string} success message should appear")
    public void checkFeedBackSuccess(String message) {
        errorMessage.checkSuccessMessage(message);
        driver.quit();
    }
    @Then("A {string} error message should appear")
    public void checkFeedBackError(String message) {
        errorMessage.checkErrorMessage(message);
        driver.quit();
    }
}
