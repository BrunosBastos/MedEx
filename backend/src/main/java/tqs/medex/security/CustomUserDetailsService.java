package tqs.medex.security;

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

  @Autowired
  private UserRepository userRepository;

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
