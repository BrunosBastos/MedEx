package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
  private final WebDriver driver;

  public LoginPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get("http://localhost:3000/login");
  }

  public void loggInAs(String email, String password) {
    driver.get("http://localhost:3000/login");
    driver.findElement(By.name("email")).sendKeys(email);
    driver.findElement(By.name("password")).sendKeys(password);
    driver.findElement(By.cssSelector(".MuiButton-label")).click();
  }

  public void insertEmail(String email) {
    driver.findElement(By.name("email")).sendKeys(email);
  }

  public void insertPassword(String password) {
    driver.findElement(By.name("password")).sendKeys(password);
  }

  public void pressLogin() {
    driver.findElement(By.cssSelector(".MuiButton-label")).click();
  }
}
