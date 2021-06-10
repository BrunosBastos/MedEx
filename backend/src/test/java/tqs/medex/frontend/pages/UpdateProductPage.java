package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpdateProductPage {
  private static final String IMAGE_URL =
      "https://lh3.googleusercontent.com/proxy/IkK4-SABrOjuqg5yXrr7oiepiQZCQZc7CDDbyLx-JgLnqLOtVhWjNx_UtiGUpUjhYb1la2YL8XPOhyDnyn_5wZjZiGx95hI1ukYVOxttsmiEYGGPPIFNpm5Br_TuIuHDRcZp801rgRSpPss7rNBq";
  private final WebDriver driver;

  public UpdateProductPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo(int prodid) {
    driver.get("http://localhost:3000/app/product/" + prodid);
  }

  public void insertInfo(int stock, double price) {
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".MuiButton-containedPrimary")));
    driver.findElement(By.cssSelector(".MuiButton-containedPrimary > .MuiButton-label")).click();
    {
      WebElement element = driver.findElement(By.name("image"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
      element.sendKeys(IMAGE_URL);
    }
    driver.findElement(By.id("prodprice")).click();
    driver.findElement(By.id("prodprice")).sendKeys(String.valueOf(price));
    driver.findElement(By.id("prodstock")).click();
    driver.findElement(By.id("prodstock")).sendKeys(String.valueOf(stock));
    confirmChanges();
  }

  public void confirmChanges() {
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
    driver
        .findElement(
            By.cssSelector(
                ".css-1nsnpoi-MuiButtonBase-root-MuiButton-root:nth-child(1) > .MuiButton-label"))
        .click();
  }
}
