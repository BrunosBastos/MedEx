package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddProductPage {

  private final WebDriver driver;

  public AddProductPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get("http://localhost:3000/app/addProduct");
  }

  public void insertProductInfo(String name, double price, int stock) {
    driver.findElement(By.id("prodname")).click();
    driver.findElement(By.id("prodname")).sendKeys(name);
    driver.findElement(By.id("prodaddress")).sendKeys("Aveiro,PT");
    driver.findElement(By.id("prodstock")).sendKeys(String.valueOf(stock));
    driver.findElement(By.id("prodprice")).click();
    driver.findElement(By.id("prodprice")).sendKeys(String.valueOf(price));
    driver.findElement(By.cssSelector(".MuiGrid-grid-md-4")).click();
    driver.findElement(By.id("prodphoto")).click();
    driver.findElement(By.id("prodphoto")).sendKeys("http://image.png");
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("age-native-simple")));
    driver.findElement(By.id("age-native-simple")).click();
    {
      WebElement dropdown = driver.findElement(By.id("age-native-simple"));
      dropdown.findElement(By.xpath("//option[. = 'Pharmacy2']")).click();
    }
    driver.findElement(By.cssSelector("option:nth-child(2)")).click();
    driver.findElement(By.id("proddescription")).click();
    driver.findElement(By.id("proddescription")).sendKeys("A description");
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
  }
}
