package ru.brombin.JMarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.service.RegistrationService;
import ru.brombin.JMarket.service.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    @Autowired
    private  final UserValidator userValidator;
    @Autowired
    private final RegistrationService registrationService;
    @Autowired
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        log.info("Login page requested from IP '{}'", userService.getClientIp(request));
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, HttpServletRequest request) {
        user.setRole(User.UserRole.ROLE_USER);
        log.info("Registration page requested from IP '{}'", userService.getClientIp(request));
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        userValidator.validate(user, bindingResult);
        String userIp = userService.getClientIp(request);

        if (bindingResult.hasErrors()) {
            log.warn("Registration failed due to validation errors for user: {}", userIp);
            return "auth/registration";
        }

        registrationService.register(user);
        return "redirect:/auth/login";
    }
}
