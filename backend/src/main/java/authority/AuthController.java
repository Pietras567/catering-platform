package authority;

import dto.LoginRequest;
import dto.MessageResponse;
import dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public MessageResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.info("Login request received for user: {}", loginRequest.getUsername());
        return authService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public MessageResponse logout(HttpServletResponse response) {
        log.info("Logout request received");
        return authService.logout(response);
    }

    @PostMapping("/register")
    public MessageResponse register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        log.info("Registration request received for user: {}", registrationRequest.getUsername());
        return authService.register(registrationRequest);
    }
}