package com.gecm.lostfound.service;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.model.User;
import com.gecm.lostfound.repository.UserRepository;
import com.gecm.lostfound.util.AppUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserSession> login(String email, String password, boolean adminLogin) {
        if (!AppUtils.isGmailAddress(email)) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByEmail(email.trim());
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Optional.empty();
        }

        if (adminLogin && user.getRole() != com.gecm.lostfound.model.UserRole.admin) {
            return Optional.empty();
        }

        return Optional.of(toSession(user));
    }

    @Transactional
    public String register(String name, String email, String password) {
        if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            return "All fields required";
        }
        if (!AppUtils.isGmailAddress(email)) {
            return "Use a Gmail address";
        }
        if (userRepository.existsByEmail(email.trim())) {
            return "Email already registered";
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return null;
    }

    public enum LoginResult {
        SUCCESS,
        INVALID_CREDENTIALS,
        NOT_ADMIN,
        GMAIL_REQUIRED
    }

    public LoginResult authenticate(String email, String password, boolean adminLogin) {
        if (!adminLogin && !AppUtils.isGmailAddress(email)) {
            return LoginResult.GMAIL_REQUIRED;
        }

        Optional<User> userOpt = userRepository.findByEmail(email.trim());
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return LoginResult.INVALID_CREDENTIALS;
        }

        User user = userOpt.get();
        if (adminLogin && user.getRole() != com.gecm.lostfound.model.UserRole.admin) {
            return LoginResult.NOT_ADMIN;
        }

        return LoginResult.SUCCESS;
    }

    public Optional<UserSession> findSessionByEmail(String email) {
        return userRepository.findByEmail(email.trim()).map(this::toSession);
    }

    private UserSession toSession(User user) {
        return new UserSession(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
