package edu.uscb.csci570sp26.galileo_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.uscb.csci570sp26.galileo_backend.model.*;

import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {


    @Autowired
    private AuthService authService;

    @Autowired
    private AccountsRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testAuthenticateUserSuccess() {
        Accounts user = new Accounts();
        user.setEmail("test26@test.com");
        user.setPassword(passwordEncoder.encode("password"));

        repo.save(user);

        String token = authService.authenticateUser("test26@test.com", "password");

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT format
    }

    @Test
    void testAuthenticateUserWrongPassword() {
        Accounts user = new Accounts();
        user.setEmail("test37@test.com");
        user.setPassword(passwordEncoder.encode("password"));

        repo.save(user);

        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser("test37@test.com", "wrong");
        });
    }
}

