package tqs.medex.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIT {

  private static RegisterRequest registerRequest;
  private static LoginRequest validLoginRequest;
  private static LoginRequest invalidLoginRequest;
  private static String baseUrl;
  @LocalServerPort private int port;

  @BeforeAll
  static void init() {

    baseUrl = "http://127.0.0.1:";

    registerRequest = new RegisterRequest();
    registerRequest.setEmail("test@example.com");
    registerRequest.setPassword("password");
    registerRequest.setName("Test");

    validLoginRequest = new LoginRequest();
    validLoginRequest.setEmail("test@example.com");
    validLoginRequest.setPassword("password");

    invalidLoginRequest = new LoginRequest();
    invalidLoginRequest.setEmail(registerRequest.getEmail());
    invalidLoginRequest.setPassword("invalid password");
  }

  @Test
  @Order(1)
  void whenRegisterWithValidCredentials_thenReturnData() {
    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(registerRequest)
        .when()
        .post(baseUrl + port + "/api/v1/register")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("user.email", is(registerRequest.getEmail()))
        .and()
        .body("user.name", is(registerRequest.getName()))
        .and()
        .body("$", not(hasKey("user.password")))
        .and()
        .body("$", hasKey("accessToken"))
        .and()
        .body("tokenType", is("Bearer"));
  }

  @Test
  @Order(2)
  void whenRegisterWithSameEmail_thenReturnBadRequest() {
    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(registerRequest)
        .when()
        .post(baseUrl + port + "/api/v1/register")
        .then()
        .statusCode(400)
        .contentType(emptyString());
  }

  @Test
  @Order(3)
  void whenLoginWithValidCredentials_thenReturnData() {

    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(validLoginRequest)
        .when()
        .post(baseUrl + port + "/api/v1/login")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("user.email", is(validLoginRequest.getEmail()))
        .and()
        .body("user.name", is("Test"))
        .and()
        .body("$", not(hasKey("user.password")))
        .and()
        .body("$", hasKey("accessToken"))
        .and()
        .body("tokenType", is("Bearer"));
  }

  @Test
  @Order(4)
  void whenLoginWithInvalidCredentials_thenReturnUnauthorized() {

    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(invalidLoginRequest)
        .when()
        .post(baseUrl + port + "/api/v1/login")
        .then()
        .statusCode(401)
        .contentType(emptyString());
  }
}
