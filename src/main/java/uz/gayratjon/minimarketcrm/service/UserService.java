// File: src/main/java/uz/gayratjon/minimarketcrm/service/UserService.java
package uz.gayratjon.minimarketcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.reposiroty.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default values if they're null
        if (user.getEmail() == null) {
            user.setEmail(user.getUsername() + "@example.com"); // temporary email
        }
        if (user.getFullName() == null) {
            user.setFullName(user.getUsername());
        }

        return userRepository.save(user);
    }
}