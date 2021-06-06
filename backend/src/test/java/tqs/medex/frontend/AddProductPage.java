package tqs.medex.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import tqs.medex.MedExApplication;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.service.ProductService;
import tqs.medex.service.SupplierService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@CucumberContextConfiguration
@SpringBootTest(
    classes = MedExApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"security.basic.enabled=false"})
@AutoConfigureTestDatabase
public class AddProductPage {

  @Autowired public ProductService productService;
  @Autowired public SupplierService supplierService;
  private WebDriver driver;
  @LocalServerPort private int port;

  @When("I navigate to {string}")
  public void navigateTo(String url) {
    driver = new FirefoxDriver();
    setUpSuppliers();
    driver.get(url);
    driver.manage().window().setSize(new Dimension(1489, 1026));
  }

  @And("I insert information like the name {string}, the price {double}, and stock {int}")
  public void insertInfo(String name, double price, int stock) {
    driver.findElement(By.id("prodname")).click();
    driver.findElement(By.id("prodname")).sendKeys(name);
    driver.findElement(By.id("prodaddress")).sendKeys("Aveiro,PT");
    driver.findElement(By.id("prodstock")).sendKeys(String.valueOf(price));
    driver.findElement(By.id("prodprice")).click();
    driver.findElement(By.id("prodprice")).sendKeys(String.valueOf(stock));
    driver.findElement(By.cssSelector(".MuiGrid-grid-md-4")).click();
    driver.findElement(By.id("prodphoto")).click();
    driver.findElement(By.id("prodphoto")).sendKeys("http://image.png");
    final WebDriverWait wait = new WebDriverWait(driver, 2);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("age-native-simple")));
    driver.findElement(By.id("age-native-simple")).click();
    {
      WebElement dropdown = driver.findElement(By.id("age-native-simple"));
      dropdown.findElement(By.xpath("//option[. = 'farmacia']")).click();
    }
    driver.findElement(By.cssSelector("option:nth-child(3)")).click();
    driver.findElement(By.id("proddescription")).click();
    driver.findElement(By.id("proddescription")).sendKeys("A description");
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
  }

  @Then("a success message should appear like {string}")
  public void successResponse(String message) {
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
    assertThat(driver.findElement(By.cssSelector(".Toastify__toast-body")).getText(), is(message));
  }

  @Then("an error message should appear like {string}")
  public void anErrorMessageShouldAppearLikeErrorCreatingProduct(String message) {
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
    assertThat(driver.findElement(By.cssSelector(".Toastify__toast-body")).getText(), is(message));
    driver.quit();
  }

  void setUpSuppliers() {
    supplierService.addSupplier(new SupplierPOJO("test", 50, 50));
    supplierService.addSupplier(new SupplierPOJO("test2", 60, 60));
    supplierService.addSupplier(new SupplierPOJO("farmacia", 55, 55));
  }
}
