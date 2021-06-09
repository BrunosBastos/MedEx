package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddSupplierPage {

  private final WebDriver driver;

  public AddSupplierPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get("http://localhost:3000/app/addSupplier");
  }

  public void insertLat(Double lat) {
    driver.findElement(By.name("latitude")).sendKeys(String.valueOf(lat));
  }

  public void insertLon(Double lon) {
    driver.findElement(By.name("longitude")).sendKeys(String.valueOf(lon));
  }

  public void insertName(String name) {
    driver.findElement(By.name("name")).sendKeys(name);
  }

  public void addNewSupplier() {
    driver.findElement(By.cssSelector(".MuiButton-contained > .MuiButton-label")).click();
  }
}
