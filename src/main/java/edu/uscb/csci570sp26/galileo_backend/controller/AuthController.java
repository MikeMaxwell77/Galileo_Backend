package edu.uscb.csci570sp26.galileo_backend.controller;

import edu.uscb.csci570sp26.galileo_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        return authService.registerUser(authRequest.getEmail(), authRequest.getPassword());
    }

    // ✅ Login user
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());
    }
}