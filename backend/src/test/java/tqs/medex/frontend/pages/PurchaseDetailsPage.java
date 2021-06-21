package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PurchaseDetailsPage {

  private final WebDriver driver;

  public PurchaseDetailsPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo(int id) {
    driver.get("http://localhost:3000/app/order/" + id);
  }

  public void checkProducts() {
    {
      List<WebElement> elements =
          driver.findElements(By.cssSelector("form .MuiCardHeader-root .MuiTypography-root"));
      assert (elements.size() > 0);
    }
    assertThat(
        driver
            .findElement(By.cssSelector(".MuiTableRow-root:nth-child(1) .MuiTypography-root"))
            .getText(),
        is("ProductTest"));

    assertThat(
        driver
            .findElement(By.cssSelector(".MuiTableRow-root:nth-child(2) .MuiTypography-root"))
            .getText(),
        is("ProductTest2"));
  }

  public void checkFinalPrice(double price) {
    assertThat(
        driver
            .findElement(By.cssSelector(".MuiTableRow-root:nth-child(1) .MuiTypography-root"))
            .getText(),
        is("ProductTest"));
    assertThat(
        driver
            .findElement(
                By.cssSelector(".MuiTableRow-hover:nth-child(1) > .MuiTableCell-root:nth-child(6)"))
            .getText(),
        is("49.90"));
    assertThat(
        driver
            .findElement(By.cssSelector(".MuiTableRow-root:nth-child(2) .MuiTypography-root"))
            .getText(),
        is("ProductTest2"));
    assertThat(
        driver
            .findElement(
                By.cssSelector(".MuiTableRow-root:nth-child(2) > .MuiTableCell-root:nth-child(6)"))
            .getText(),
        is("19.80"));
    assertThat(
        driver.findElement(By.cssSelector(".css-blg4f7-MuiTypography-root")).getText(),
        is(price + "0€"));
  }

  public void checkReviewForm() {
    driver.findElement(By.id("standard-multiline-static")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("standard-multiline-static"));
      assert (elements.size() > 0);
    }
  }
}
