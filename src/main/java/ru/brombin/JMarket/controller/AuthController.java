package ru.brombin.JMarket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.RegistrationService;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private  final UserValidator userValidator;
    @Autowired
    private final RegistrationService registrationService;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        logger.info("Login page requested from IP '{}'", userService.getClientIp(request));
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDTO userDTO, HttpServletRequest request) {
        userDTO.setRole(User.UserRole.ROLE_USER);
        logger.info("Registration page requested from IP '{}'", userService.getClientIp(request));
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, HttpServletRequest request) {
        userValidator.validate(userDTO, bindingResult);
        String userIp = userService.getClientIp(request);

        if (bindingResult.hasErrors()) {
            logger.warn("Registration failed due to validation errors for user: {}", userIp);
            return "auth/registration";
        }

        int userId = registrationService.register(convertToUser(userDTO));
        logger.info("User '{}' with ip '{}' successfully registered.", userId, userIp);
        return "redirect:/auth/login";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
