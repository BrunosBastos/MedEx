package tqs.medex.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
        final WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".Toastify__toast-body")));
        assertThat(
                driver.findElement(By.cssSelector(".Toastify__toast-body")).getText(),
                is("Successfully logged in"));
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
