package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ShoppingCartPage {
  private final WebDriver driver;

  public ShoppingCartPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get("http://localhost:3000/app/shoppingCart");
  }

  public void finalizePurchase() {
    driver.findElement(By.cssSelector(".MuiButton-root:nth-child(2) > .MuiButton-label")).click();
  }

  public void insertProductLongitude(double lon) {
    driver.findElement(By.id("longitude")).sendKeys(Double.toString(lon));
  }

  public void insertProductLatitude(double lat) {
    driver.findElement(By.id("latitude")).sendKeys(Double.toString(lat));
  }

  public void pressPurchase() {
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
  }

  public void changeProductQuantity(int quantity) {
    {
      WebElement element = driver.findElement(By.id("1quant"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.id("1quant")).sendKeys(Integer.toString(quantity));
  }
}
