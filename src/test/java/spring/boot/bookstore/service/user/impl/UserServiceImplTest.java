package spring.boot.bookstore.service.user.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import spring.boot.bookstore.dto.user.UserRegistrationRequestDto;
import spring.boot.bookstore.exception.RegistrationException;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    private UserRegistrationRequestDto registrationRequest = new UserRegistrationRequestDto();

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequestDto();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Alison");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password%^&*(");
        registrationRequest.setRepeatPassword("password%^&*(");
        registrationRequest.setShippingAddress("123 Main St");
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@example.com", "password");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Successful Registration")
    void successfulRegistration() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationException.class, () -> {
            userService.register(registrationRequest);
        });
    }

    @Test
    @DisplayName("Registration With Existing Email")
    void registrationWithExistingEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationException.class, () -> userService.register(registrationRequest));
    }

    @Test
    @DisplayName("Get Authenticated User")
    void getAuthenticatedUser() {
        User authenticatedUser = new User();
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(authenticatedUser));
        User retrievedUser = userService.getAuthenticated();
        assertThat(retrievedUser).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("Registration With Existing Email Throws Exception")
    void registrationWithExistingEmail_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationException.class, () -> userService.register(registrationRequest));
    }

    @Test
    @DisplayName("Registration With Non-Unique Email")
    void testRegistrationWithNonUniqueEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationException.class, () -> userService.register(registrationRequest));
    }
}
