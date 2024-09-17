package ru.brombin.JMarket.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
import ru.brombin.JMarket.util.validators.UserValidator;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private  final UserValidator userValidator;
    @Autowired
    private final RegistrationService registrationService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDTO userDTO) {
            userDTO.setRole(User.UserRole.ROLE_USER);
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        System.out.println(userDTO.getDateOfBirth());

        registrationService.register(convertToUser(userDTO));
        return "redirect:/auth/login";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
