package spring.boot.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring.boot.bookstore.controller.CategoryControllerTest.convertObjectToJsonString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.boot.bookstore.dto.user.UserLoginRequestDto;
import spring.boot.bookstore.dto.user.UserLoginResponseDto;
import spring.boot.bookstore.dto.user.UserRegistrationRequestDto;
import spring.boot.bookstore.dto.user.UserResponseDto;
import spring.boot.bookstore.security.AuthenticationService;
import spring.boot.bookstore.service.user.UserService;

@ContextConfiguration(classes = {AuthControllerTest.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthControllerTest.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("User registration")
    void registerUser() throws Exception {
        UserRegistrationRequestDto registrationRequest = new UserRegistrationRequestDto();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFirstName("Bob");
        registrationRequest.setLastName("Doe");
        registrationRequest.setPassword("bobDob???orAlicon#$%^&*DFGHJ$%^&*45678");
        registrationRequest.setRepeatPassword("bobDob???orAlicon#$%^&*DFGHJ$%^&*45678");
        registrationRequest.setShippingAddress("123 main street");
        when(userService.register(any(UserRegistrationRequestDto.class)))
                .thenReturn(new UserResponseDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(registrationRequest));
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User login with valid data")
    void loginUser() throws Exception {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("bobDob???orAlicon#$%^&*DFGHJ$%^&*45678");
        String token = "random_token_?";
        when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenReturn(new UserLoginResponseDto(token));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(loginRequest));
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
