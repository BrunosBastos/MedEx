package tqs.medex.controller;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.MedExApplication;
import tqs.medex.entity.Client;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.security.JwtTokenProvider;
import tqs.medex.service.AuthService;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    void whenRegisterWithValidData_thenReturnData() throws EmailAlreadyInUseException {

        RegisterRequest request = new RegisterRequest();
        request.setPassword("password");
        request.setEmail("test@email.com");
        request.setName("Test");

        User user = setUpUserRegister();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(user);

        RestAssured.defaultParser = Parser.JSON;
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(request)
                .post("api/v1/register")
                .then()
                .assertThat()
                .statusCode(200).and()
                .body("email", is(user.getEmail()))
                .and()
                .body("client.name", is(user.getClient().getName()))
                ;

        verify(authService, times(1)).registerUser(any());
    }

    User setUpUserRegister(){
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