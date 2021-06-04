package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.JwtAuthenticationResponse;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.repository.UserRepository;
import tqs.medex.security.JwtTokenProvider;
import tqs.medex.service.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @Autowired private AuthService service;

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest request) {

    try{
      User user = service.registerUser(request);
      return ResponseEntity.status(HttpStatus.OK).body(user);
    }catch (EmailAlreadyInUseException e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest request) {
      JwtAuthenticationResponse jwt = service.authenticateUser(request);
      return ResponseEntity.status(HttpStatus.OK).body(jwt);
  }
}
