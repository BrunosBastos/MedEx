package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AuthControllerTest {
  RegisterRequest registerRequest;
  LoginRequest loginRequest;
  @Autowired private MockMvc mvc;
  @MockBean private AuthService authService;
}
