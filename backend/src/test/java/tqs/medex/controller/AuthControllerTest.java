package tqs.medex.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Client;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.JwtAuthenticationResponse;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.service.AuthService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AuthControllerTest {
  RegisterRequest registerRequest;
  LoginRequest loginRequest;
  @Autowired private MockMvc mvc;
  @MockBean private AuthService authService;

  @BeforeEach
  void setUp() {
    registerRequest = new RegisterRequest();
    registerRequest.setPassword("password");
    registerRequest.setEmail("test@email.com");
    registerRequest.setName("Test");

    loginRequest = new LoginRequest();
    loginRequest.setEmail("test@email.com");
    loginRequest.setPassword("password");

    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  void whenRegisterWithValidData_thenReturnData() throws EmailAlreadyInUseException {

    User user = setUpUserRegister();

    when(authService.registerUser(any(RegisterRequest.class))).thenReturn(user);

    RestAssured.defaultParser = Parser.JSON;
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(registerRequest)
        .post("api/v1/register")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("email", is(user.getEmail()))
        .and()
        .body("client.name", is(user.getClient().getName()));

    verify(authService, times(1)).registerUser(any());
  }

  @Test
  void whenRegisterWithInValidData_thenReturnEmailAlreadyInUseException()
      throws EmailAlreadyInUseException {

    when(authService.registerUser(any(RegisterRequest.class)))
        .thenThrow(new EmailAlreadyInUseException());

    RestAssured.defaultParser = Parser.JSON;
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(registerRequest)
        .post("api/v1/register")
        .then()
        .assertThat()
        .statusCode(400);

    verify(authService, times(1)).registerUser(any());
  }

  @Test
  void whenLoginWithValidCredentials_thenReturnToken() {

    JwtAuthenticationResponse jwt = new JwtAuthenticationResponse("valid token");

    when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwt);
    RestAssured.defaultParser = Parser.JSON;
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(loginRequest)
        .post("api/v1/login")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("accessToken", is(jwt.getAccessToken()))
        .and()
        .body("tokenType", is(jwt.getTokenType()));
    verify(authService, times(1)).authenticateUser(any());
  }

  User setUpUserRegister() {
    User user = new User();
    user.setEmail("test@email.com");
    user.setPassword("password");
    user.setUserId(1L);

    Client client = new Client();
    client.setName("Test");

    user.setClient(client);
    return user;
  }
}
