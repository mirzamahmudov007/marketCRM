// File: src/main/java/uz/gayratjon/minimarketcrm/controller/AuthController.java
package uz.gayratjon.minimarketcrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.gayratjon.minimarketcrm.conroller.req.JwtAuthenticationResponse;
import uz.gayratjon.minimarketcrm.conroller.req.LoginRequest;
import uz.gayratjon.minimarketcrm.conroller.req.SignUpRequest;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.model.UserRole;
import uz.gayratjon.minimarketcrm.security.JwtTokenProvider;
import uz.gayratjon.minimarketcrm.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            if (userService.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body("Username is already taken!");
            }

            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setPassword(signUpRequest.getPassword());
            user.setRole(UserRole.ROLE_CASHIER);
            user.setActive(true);
            // Set required fields
            user.setEmail(signUpRequest.getUsername() + "@example.com"); // temporary email
            user.setFullName(signUpRequest.getUsername()); // using username as fullname temporarily

            userService.createUser(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.badRequest().body("Error occurred during registration: " + e.getMessage());
        }
    }
}