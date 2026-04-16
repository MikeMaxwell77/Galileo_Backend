package edu.uscb.csci570sp26.galileo_backend.service;

import edu.uscb.csci570sp26.galileo_backend.model.Accounts;
import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;
import edu.uscb.csci570sp26.galileo_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AccountsRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user by encoding their password and saving them to the database.
     */
    public String registerUser(String email, String password) {
        // ✅ Check if user already exists
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists.");
        }

        // ✅ Encode password before saving user
        String hashedPassword = passwordEncoder.encode(password);
        Accounts newUser = new Accounts(email, hashedPassword);
        appUserRepository.save(newUser);

        return "User registered successfully";
    }

    /**
     * Authenticates a user by verifying their email and password, then generating a JWT token.
     */
    public String authenticateUser(String email, String password) {
        // ✅ Find user by email
        Optional<Accounts> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        Accounts appUser = appUserOptional.get();

        // ✅ Verify password
        if (!passwordEncoder.matches(password, appUser.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // ✅ Generate JWT token using JwtUtil
        return jwtUtil.generateToken(appUser.getEmail(),"USER", appUser.getId());
    }
    
    
}
