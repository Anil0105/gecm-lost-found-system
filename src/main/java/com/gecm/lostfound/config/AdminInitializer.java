package com.gecm.lostfound.config;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.model.User;
import com.gecm.lostfound.model.UserRole;
import com.gecm.lostfound.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@gecm.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@gecm.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            admin.setRole(UserRole.admin);
            userRepository.save(admin);
        }
    }
}
