package tqs.medex.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tqs.medex.entity.CustomUserDetails;
import tqs.medex.entity.User;
import tqs.medex.exception.UserNotFoundException;
import tqs.medex.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String email) {

    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    return new CustomUserDetails(user);
  }

  @SneakyThrows
  public UserDetails loadUserById(long id) {

    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    return new CustomUserDetails(user);
  }
}
