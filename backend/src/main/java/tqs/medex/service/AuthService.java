package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Client;
import tqs.medex.entity.CustomUserDetails;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.JwtAuthenticationResponse;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.repository.ClientRepository;
import tqs.medex.repository.UserRepository;
import tqs.medex.security.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserRepository userRepository;

  @Autowired private ClientRepository clientRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JwtTokenProvider tokenProvider;

  public JwtAuthenticationResponse authenticateUser(LoginRequest request) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    System.out.println(user);
    boolean isSuperUser = user.getUser().isSuperUser();

    return new JwtAuthenticationResponse(jwt, isSuperUser);
  }

  public User registerUser(RegisterRequest request) throws EmailAlreadyInUseException {

    Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
    if (dbUser.isPresent()) {
      throw new EmailAlreadyInUseException();
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Client client = new Client();
    client.setName(request.getName());
    user = userRepository.save(user);

    client.setUser(user);
    clientRepository.save(client);

    return user;
  }
}
