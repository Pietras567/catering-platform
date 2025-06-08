package authority;

import dto.LoginRequest;
import dto.MessageResponse;
import dto.RegistrationRequest;
import entities.Account;
import entities.Client;
import exceptions.InvalidCredentialsException;
import exceptions.UserAlreadyExistsException;
import exceptions.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.AccountRepository;
import repositories.UserRepository;
import utils.CookieUtil;
import utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public MessageResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = accountDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        cookieUtil.addJwtCookie(response, jwt);

        return new MessageResponse("Logged in successfully!");
    }

    @PostMapping("/logout")
    public MessageResponse logout(HttpServletResponse response) {
        cookieUtil.clearJwtCookie(response);

        return new MessageResponse("Logged out successfully!");
    }

    @PostMapping("/register")
    public MessageResponse register(@RequestBody RegistrationRequest registrationRequest) {
        if (registrationRequest.getUsername() == null || registrationRequest.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required!");
        }

        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().trim().isEmpty()) {
            throw new ValidationException("A password is required!");
        }

        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required!");
        }

        if (registrationRequest.getFirstName() == null || registrationRequest.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required!");
        }

        if (registrationRequest.getLastName() == null || registrationRequest.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required!");
        }

        if (accountRepository.existsByLogin(registrationRequest.getUsername())) {
            throw new UserAlreadyExistsException("The username is already taken!");
        }

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        Account account = new Account();
        account.setLogin(registrationRequest.getUsername().trim());
        account.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        account.setEnabled(true);
        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setCredentialsNonExpired(true);
        Account savedAccount = accountRepository.save(account);

        Client user = new Client();
        user.setUsername(registrationRequest.getUsername().trim());
        user.setEmail(registrationRequest.getEmail().trim());
        user.setFirstName(registrationRequest.getFirstName().trim());
        user.setLastName(registrationRequest.getLastName().trim());
        user.setPhone(registrationRequest.getPhone() != null ? registrationRequest.getPhone().trim() : "");
        user.setAccount(savedAccount);
        userRepository.save(user);

        return new MessageResponse("The user was registered successfully!");
    }
}
