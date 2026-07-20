package com.gecm.lostfound.controller;

import com.gecm.lostfound.service.AuthService;
import com.gecm.lostfound.util.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "admin", required = false) String admin, Model model) {
        boolean isAdmin = admin != null;
        model.addAttribute("isAdmin", isAdmin);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(value = "admin", required = false) String admin,
                        Model model) {
        boolean isAdmin = admin != null;
        model.addAttribute("isAdmin", isAdmin);

        AuthService.LoginResult result = authService.authenticate(email, password, isAdmin);
        switch (result) {
            case GMAIL_REQUIRED -> {
                model.addAttribute("error", "Only Gmail addresses are allowed to log in.");
                return "login";
            }
            case INVALID_CREDENTIALS -> {
                model.addAttribute("error", "Invalid credentials");
                return "login";
            }
            case NOT_ADMIN -> {
                model.addAttribute("error", "Not an admin account");
                return "login";
            }
            case SUCCESS -> {
                authService.findSessionByEmail(email).ifPresent(SessionUtils::setCurrentUser);
                return isAdmin ? "redirect:/admin" : "redirect:/dashboard";
            }
            default -> {
                model.addAttribute("error", "Invalid credentials");
                return "login";
            }
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        String error = authService.register(name, email, password);
        if (error != null) {
            model.addAttribute("error", error);
            return "register";
        }
        model.addAttribute("success", "Registration successful. You can now log in.");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
