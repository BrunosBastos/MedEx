package tqs.medex.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import tqs.medex.frontend.pages.ErrorMessage;
import tqs.medex.frontend.pages.ListProductsPage;
import tqs.medex.frontend.pages.LoginPage;
import tqs.medex.frontend.pages.ShoppingCartPage;

public class PurchaseSteps {
  private final WebDriver driver;
  private final LoginPage loginPage;
  private final ListProductsPage listProductsPage;
  private final ShoppingCartPage shoppingCartPage;
  private final ErrorMessage errorMessage;

  public PurchaseSteps() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--headless");
    driver = new FirefoxDriver(options);
    loginPage = new LoginPage(driver);
    listProductsPage = new ListProductsPage(driver);
    shoppingCartPage = new ShoppingCartPage(driver);
    errorMessage = new ErrorMessage(driver);
  }

  @Given("I am a client on the products list page")
  public void loggInAsOwner() {
    loginPage.loggInAs("henrique@gmail.com", "string");
    listProductsPage.goTo();
  }

  @When("I add a few products to my Cart")
  public void goToProductPage() {
    listProductsPage.addProducts();
  }

  @And("I go to my Shopping Cart")
  public void goToShoppingCart() {
    shoppingCartPage.goTo();
  }

  @And("I set the quantity of a product to {int}")
  public void insertProductQuantity(int quantity) {
    shoppingCartPage.changeProductQuantity(quantity);
  }

  @And("I press the purchase button")
  public void pressPurchase() {
    shoppingCartPage.pressPurchase();
  }

  @And("I insert the latitude for the delivery location as {double}")
  public void insertProductLatitude(double lat) {
    shoppingCartPage.insertProductLatitude(lat);
  }

  @And("I insert the longitude for the delivery location as {double}")
  public void insertProductLongitude(double lon) {
    shoppingCartPage.insertProductLongitude(lon);
  }

  @And("I finalize my purchase")
  public void finalizePurchase() {
    shoppingCartPage.finalizePurchase();
  }

  @Then("A successfully made a purchase message should appear")
  public void successResponse() {
    errorMessage.checkSuccessMessage("Successfully made a purchase");
    driver.quit();
  }

  @Then("A purchase failed message should appear")
  public void anErrorMessageShouldAppearLikeErrorCreatingProduct() {
    errorMessage.checkErrorMessage("Failed making a purchase");
    driver.quit();
  }
}
