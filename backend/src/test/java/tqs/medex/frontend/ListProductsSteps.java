package tqs.medex.frontend;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import tqs.medex.frontend.pages.ListProductsPage;
import tqs.medex.frontend.pages.LoginPage;

public class ListProductsSteps {
  private final ListProductsPage listProductsPage;
  private final WebDriver driver;
  private final LoginPage loginPage;

  public ListProductsSteps() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--headless");
    driver = new FirefoxDriver();
    loginPage = new LoginPage(driver);
    listProductsPage = new ListProductsPage(driver);
  }

  @Given("I am logged in as the pharmacy owner And I am on the list products page")
  public void logInAndSwitchPage() {
    loginPage.loggInAs("clara@gmail.com", "string");
    listProductsPage.goTo();
  }

  @When("there are {int} products")
  public void assertNumberofProductsPresent(int numprods) {
    listProductsPage.checkNumProducts(numprods);
  }

  @Then(
      "some info about them should appear like, for example, the name {string} and the price {double}")
  public void someInfo(String name, double price) {
    listProductsPage.checkInformationinProduct(name, price);
    driver.quit();
  }
}
