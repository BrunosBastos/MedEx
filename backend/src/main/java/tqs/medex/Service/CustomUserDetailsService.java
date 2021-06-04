package tqs.medex.Service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tqs.medex.Entity.CustomUserDetails;
import tqs.medex.Entity.User;
import tqs.medex.Repository.UserRepository;

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

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new Exception("User not found [email: " + email + "]")
        );

        CustomUserDetails ud = null;
        if (user != null){
            ud = new CustomUserDetails(user);
        }else{
            throw new UsernameNotFoundException("Email Not Found: "+ email);
        }
        return ud;
    }

    @SneakyThrows
    public UserDetails loadUserById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new Exception("User not found [id: " + id + "]")
                );

        CustomUserDetails ud = null;
        if (user != null){
            ud = new CustomUserDetails(user);
        }else{
            throw new UsernameNotFoundException("Id Not Found: "+ id);
        }
        return ud;
    }



}
