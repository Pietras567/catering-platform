package authority;

import dto.LoginRequest;
import dto.MessageResponse;
import dto.RegistrationRequest;
import exceptions.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        MessageResponse messageResponse = new MessageResponse("Login successful!");
        Mockito.when(authService.login(eq(loginRequest), any(HttpServletResponse.class))).thenReturn(messageResponse);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "username": "testUser",
                    "password": "testPassword"
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Login successful!"));

        Mockito.verify(authService, Mockito.times(1)).login(eq(loginRequest), any(HttpServletResponse.class));
    }

    @Test
    public void testLoginFailureInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invalidUser", "invalidPassword");
        MessageResponse messageResponse = new MessageResponse("Invalid username or password.");
        Mockito.when(authService.login(eq(loginRequest), any(HttpServletResponse.class))).thenReturn(messageResponse);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "username": "invalidUser",
                    "password": "invalidPassword"
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Invalid username or password."));

        Mockito.verify(authService, Mockito.times(1)).login(eq(loginRequest), any(HttpServletResponse.class));
    }

    @Test
    public void testLoginFailureMissingUsername() throws Exception {
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "password": "testPassword"
                }
                """)).andExpect(status().is4xxClientError());

        Mockito.verify(authService, Mockito.never()).login(any(), any());
    }

    @Test
    public void testLoginFailureMissingPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "username": "testUser"
                }
                """)).andExpect(status().is4xxClientError());

        Mockito.verify(authService, Mockito.never()).login(any(), any());
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        MessageResponse messageResponse = new MessageResponse("Logout successful!");
        Mockito.when(authService.logout(any(HttpServletResponse.class))).thenReturn(messageResponse);

        mockMvc.perform(post("/api/auth/logout").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Logout successful!"));

        Mockito.verify(authService, Mockito.times(1)).logout(any(HttpServletResponse.class));
    }

    @Test
    public void testLogoutSuccess_EvenWithNullResponse() throws Exception {
        Mockito.when(authService.logout(any(HttpServletResponse.class))).thenReturn(null);

        mockMvc.perform(post("/api/auth/logout").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Mockito.verify(authService, Mockito.times(1)).logout(any(HttpServletResponse.class));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("testUser", "testPassword", "test@example.com", "John", "Doe", "1234567890");
        MessageResponse messageResponse = new MessageResponse("Registration successful!");
        Mockito.when(authService.register(eq(registrationRequest))).thenReturn(messageResponse);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "username": "testUser",
                    "password": "testPassword",
                    "email": "test@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "phone": "1234567890"
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Registration successful!"));

        Mockito.verify(authService, Mockito.times(1)).register(eq(registrationRequest));
    }

    @Test
    public void testRegisterFailureInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "username": "testUser",
                    "password": "testPassword",
                    "email": "invalid-email",
                    "firstName": "John",
                    "lastName": "Doe",
                    "phone": "1234567890"
                }
                """)).andExpect(status().is4xxClientError());

    }

    @Test
    public void testRegisterFailureMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "password": "testPassword",
                    "email": "test@example.com"
                }
                """)).andExpect(status().is4xxClientError());

    }
}