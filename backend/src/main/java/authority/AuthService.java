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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.AccountRepository;
import repositories.UserRepository;
import utils.CookieUtil;
import utils.JwtUtil;

@Service
@Slf4j
public class AuthService {

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

    public MessageResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            log.warn("Authentication failed for user: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = accountDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        cookieUtil.addJwtCookie(response, jwt);
        log.info("User {} logged in successfully", loginRequest.getUsername());

        return new MessageResponse("Logged in successfully!");
    }

    public MessageResponse logout(HttpServletResponse response) {
        log.info("User logging out");
        cookieUtil.clearJwtCookie(response);
        return new MessageResponse("Logged out successfully!");
    }

    @Transactional
    public MessageResponse register(RegistrationRequest registrationRequest) {
        log.info("Attempting to register new user: {}", registrationRequest.getUsername());

        validateRegistrationRequest(registrationRequest);
        checkIfUserExists(registrationRequest);

        Account savedAccount = createAccount(registrationRequest);
        createUser(registrationRequest, savedAccount);

        log.info("User {} registered successfully", registrationRequest.getUsername());
        return new MessageResponse("The user was registered successfully!");
    }

    private void validateRegistrationRequest(RegistrationRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required!");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException("A password is required!");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required!");
        }

        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required!");
        }

        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required!");
        }
    }

    private void checkIfUserExists(RegistrationRequest request) {
        if (accountRepository.existsByLogin(request.getUsername())) {
            throw new UserAlreadyExistsException("The username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }
    }

    private Account createAccount(RegistrationRequest request) {
        Account account = new Account();
        account.setLogin(request.getUsername().trim());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEnabled(true);
        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setCredentialsNonExpired(true);

        return accountRepository.save(account);
    }

    private void createUser(RegistrationRequest request, Account account) {
        Client user = new Client();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : "");
        user.setAccount(account);

        userRepository.save(user);
    }
}