package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListProductsPage {

  private final WebDriver driver;

  public ListProductsPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get("http://localhost:3000/app/products");
  }

  public void checkNumProducts(int num) {
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiGrid-root")));
    int counter = 0;
    {
      List<WebElement> elements =
          driver.findElements(
              By.cssSelector(
                  ".MuiGrid-root:nth-child(1) > .MuiPaper-root > .MuiCardContent-root > .MuiBox-root"));
      assertThat(elements.isEmpty(), is(false));
      counter++;
    }
    {
      List<WebElement> elements =
          driver.findElements(
              By.cssSelector(".MuiGrid-root:nth-child(2) .MuiCardContent-root > .MuiBox-root"));
      assertThat(elements.isEmpty(), is(false));
      counter++;
    }
    assertThat(counter, is(num));
  }

  public void checkInformationinProduct(String productname, double price) {
    final WebDriverWait wait = new WebDriverWait(driver, 2);
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".MuiGrid-root:nth-child(1)")));
    assertThat(
        driver
            .findElement(
                By.cssSelector(
                    ".MuiGrid-root:nth-child(1) > .MuiPaper-root > .MuiCardContent-root > .MuiTypography-root:nth-child(2)"))
            .getText(),
        is(productname));
    assertThat(
        driver
            .findElement(
                By.cssSelector(
                    ".MuiGrid-root:nth-child(1) > .MuiPaper-root .MuiGrid-root > .MuiTypography-root"))
            .getText(),
        is(price + " €"));
  }

  public void addProducts() {
    driver
        .findElement(
            By.cssSelector(
                ".MuiGrid-root:nth-child(1) > .MuiPaper-root .MuiButton-root .MuiTypography-root"))
        .click();
    driver
        .findElement(
            By.cssSelector(".MuiGrid-root:nth-child(2) > .MuiPaper-root .MuiButton-root path"))
        .click();
  }
}
