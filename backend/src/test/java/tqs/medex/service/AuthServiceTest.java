package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import tqs.medex.entity.CustomUserDetails;
import tqs.medex.entity.User;
import tqs.medex.exception.EmailAlreadyInUseException;
import tqs.medex.pojo.JwtAuthenticationResponse;
import tqs.medex.pojo.LoginRequest;
import tqs.medex.pojo.RegisterRequest;
import tqs.medex.repository.UserRepository;
import tqs.medex.security.JwtTokenProvider;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    LoginRequest valid;
    RegisterRequest validRegister;
    RegisterRequest invalidRegister;
    User registeredUser;

    @Mock(lenient = true)
    private AuthenticationManager authenticationManager;

    @Mock(lenient = true)
    private PasswordEncoder passwordEncoder;

    @Mock(lenient = true)
    private JwtTokenProvider tokenProvider;

    @Mock(lenient = true)
    private UserRepository repository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {

        valid = new LoginRequest();
        valid.setEmail("valid@test.com");
        valid.setPassword("test");

        validRegister = new RegisterRequest();
        validRegister.setName("Test");
        validRegister.setEmail("valid@test.com");
        validRegister.setPassword("test");

        invalidRegister = new RegisterRequest();
        invalidRegister.setName("Test");
        invalidRegister.setEmail("usedemail@test.com");
        invalidRegister.setPassword("test");

        registeredUser = new User();
        registeredUser.setEmail("valid@test.com");
        registeredUser.setPassword("test");

        Authentication auth =
                new Authentication() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public boolean implies(Subject subject) {
                        return false;
                    }

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public Object getCredentials() {
                        return null;
                    }

                    @Override
                    public Object getDetails() {
                        return null;
                    }

                    @Override
                    public Object getPrincipal() {
                        return new CustomUserDetails(registeredUser);
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return false;
                    }

                    @Override
                    public void setAuthenticated(boolean b) throws IllegalArgumentException {
                    }
                };

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(valid.getEmail(), valid.getPassword())))
                .thenReturn(auth);

        when(tokenProvider.generateToken(auth)).thenReturn("valid token");

        when(repository.findByEmail(validRegister.getEmail())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(registeredUser);

        when(repository.findByEmail("usedemail@test.com")).thenReturn(Optional.of(registeredUser));
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnValidToken() {

        JwtAuthenticationResponse response = authService.authenticateUser(valid);

        assertThat(response.getUser().isSuperUser()).isFalse();
        assertThat(response.getAccessToken()).contains("valid token");
        assertThat(response.getTokenType()).contains("Bearer");

        verify(authenticationManager, VerificationModeFactory.times(1)).authenticate(any());
        verify(tokenProvider, VerificationModeFactory.times(1)).generateToken(any());
    }

    @Test
    void whenRegisterWithValidData_thenReturnSuccess() throws EmailAlreadyInUseException {

        assertThat(authService.registerUser(validRegister).getUser()).isEqualTo(registeredUser);

        verify(repository, VerificationModeFactory.times(1)).findByEmail(any());
        verify(repository, VerificationModeFactory.times(1)).save(any());
    }

    @Test
    void whenRegisterWithInvalidData_thenThrowEmailAlreadyInUse() {

        assertThrows(
                EmailAlreadyInUseException.class,
                () -> authService.registerUser(invalidRegister),
                "Expected registerUser to throw, but it didn't");

        verify(repository, VerificationModeFactory.times(1)).findByEmail(any());
    }
}
