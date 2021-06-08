package tqs.medex.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.medex.frontend.pages.AddProductPage;
import tqs.medex.frontend.pages.ErrorMessage;
import tqs.medex.frontend.pages.LoginPage;

public class AddProductSteps {
  private final WebDriver driver;
  private final LoginPage loginPage;
  private final AddProductPage addProductPage;
  private final ErrorMessage errorMessage;

  public AddProductSteps() {
    WebDriverManager.firefoxdriver().setup();
    driver = new FirefoxDriver();
    loginPage = new LoginPage(driver);
    addProductPage = new AddProductPage(driver);
    errorMessage = new ErrorMessage(driver);
  }

  @Given("I am logged in as the pharmacy owner")
  public void loggInAsOwner() {
    loginPage.loggInAs("clara@gmail.com", "string");
  }

  @And("I am on the add product page")
  public void goToProductPage() {
    addProductPage.goTo();
  }

  @And("I insert information like the name {string}, the price {double}, and stock {int}")
  public void insertProductInfo(String name, double price, int stock) {
    addProductPage.insertProductInfo(name, price, stock);
  }

  @Then("A successfully adding a new product message should appear")
  public void successResponse() {
    errorMessage.checkSuccessMessage("Success Adding new Product!");
    driver.quit();
  }

  @Then("A failed creating product message should appear")
  public void anErrorMessageShouldAppearLikeErrorCreatingProduct() {
    errorMessage.checkErrorMessage("Error Creating Product");
    driver.quit();
  }
}
