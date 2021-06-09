package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.medex.entity.CustomUserDetails;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.JwtAuthenticationResponse;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.repository.UserRepository;
import tqs.medex.security.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JwtTokenProvider tokenProvider;

  public JwtAuthenticationResponse authenticateUser(LoginRequest request)
      throws AuthenticationException {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

    return new JwtAuthenticationResponse(jwt, user);
  }

  public JwtAuthenticationResponse registerUser(RegisterRequest request)
      throws EmailAlreadyInUseException {

    Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
    if (dbUser.isPresent()) {
      throw new EmailAlreadyInUseException();
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    user = userRepository.save(user);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);

    return new JwtAuthenticationResponse(jwt, user);
  }
}
