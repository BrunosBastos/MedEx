package tqs.medex.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.medex.frontend.pages.AddSupplierPage;
import tqs.medex.frontend.pages.ErrorMessage;
import tqs.medex.frontend.pages.LoginPage;

public class AddSupplierSteps {
  private final WebDriver driver;
  private final LoginPage loginPage;
  private final AddSupplierPage addSupplierPage;
  private final ErrorMessage errorMessage;

  public AddSupplierSteps() {
    WebDriverManager.firefoxdriver().setup();
    driver = new FirefoxDriver();
    loginPage = new LoginPage(driver);
    addSupplierPage = new AddSupplierPage(driver);
    errorMessage = new ErrorMessage(driver);
  }

  @Given("I am the pharmacy owner on the add supplier page")
  public void loggInAsOwner() {
    loginPage.loggInAs("clara@gmail.com", "string");
    addSupplierPage.goTo();
  }

  @When("I insert information like the name {string}")
  public void insertSupplierName(String name) {
    addSupplierPage.insertName(name);
  }

  @And("I insert the latitude {double}")
  public void insertSupplierLat(Double lat) {
    addSupplierPage.insertLat(lat);
  }

  @And("I insert the longitude {double}")
  public void insertSupplierLon(Double lon) {
    addSupplierPage.insertLon(lon);
  }

  @When("I insert an already existing supplier name like {string}")
  public void insertExistingSupplierName(String name) {
    addSupplierPage.insertName(name);
  }

  @And("I press the add a new supplier button")
  public void iPressTheAddANewSupplierButton() {
    addSupplierPage.addNewSupplier();
  }

  @Then("A successfully added a new supplier message should appear")
  public void successResponse() {
    errorMessage.checkSuccessMessage("Successfully added a new supplier");
    driver.quit();
  }

  @Then("A failed adding a new supplier message should appear")
  public void anErrorMessageShouldAppearLikeErrorCreatingSupplier() {
    errorMessage.checkErrorMessage("Failed adding a new supplier");
    driver.quit();
  }
}
