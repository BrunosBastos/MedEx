package tqs.medex.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.medex.frontend.pages.LoginPage;
import tqs.medex.frontend.pages.PurchaseDetailsPage;

public class PurchaseDetailsSteps {
  private final PurchaseDetailsPage purchaseDetailsPage;
  private final WebDriver driver;
  private final LoginPage loginPage;

  public PurchaseDetailsSteps() {
    WebDriverManager.firefoxdriver().setup();
    driver = new FirefoxDriver();
    loginPage = new LoginPage(driver);
    purchaseDetailsPage = new PurchaseDetailsPage(driver);
  }

  @Given("I am a client on the purchase details page with id {int}")
  public void iAmAClientOnThePurchaseDetailsPageWithId(int id) {
    loginPage.loggInAs("henrique@gmail.com", "string");
    purchaseDetailsPage.goTo(id);
  }

  @Then("two Products should appear")
  public void productsShouldAppear() {
    purchaseDetailsPage.checkProducts();
  }

  @And("the final price should be of {double}")
  public void theFinalPriceShouldBeOf(double price) {
    purchaseDetailsPage.checkFinalPrice(price);
  }

  @And("a card to submit a review should appear")
  public void aCardToSubmitAReviewShouldAppear() {
    purchaseDetailsPage.checkReviewForm();
  }
}
