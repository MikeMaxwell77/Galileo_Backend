package edu.uscb.csci570sp26.galileo_backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testGenerateAndExtractToken() {

        String email = "test@test.com";
        String role = "USER";
        long id = 42L;

        String token = jwtUtil.generateToken(email, role, id);

        assertNotNull(token);

        Long extractedId = jwtUtil.extractUserId(token);
        String extractedEmail = jwtUtil.extractEmail(token);

        assertEquals(id, extractedId);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testInvalidTokenThrows() {

        String badToken = "invalid.token.value";

        assertThrows(Exception.class, () -> {
            jwtUtil.extractUserId(badToken);
        });
    }
}

