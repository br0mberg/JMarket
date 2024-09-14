package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.ErrorResponse;
import ru.brombin.JMarket.util.UserValidator;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        User user = userService.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        model.addAttribute("user", convertToUserDTO(user));
        return "user/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", User.getUserRoles());
        return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        return handleUserForm(userDTO, bindingResult, "user/new", null);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", convertToUserDTO(userService.findOne(id).orElseThrow(() -> new NotFoundException("User not found: " + id))));
        return "user/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid UserDTO userDTO, BindingResult bindingResult, @PathVariable("id") int id) {
        return handleUserForm(userDTO, bindingResult, "user/edit", id);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/users";
    }

    private String handleUserForm(UserDTO userDTO, BindingResult bindingResult, String errorView, Integer id) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) return errorView;

        User user = convertToUser(userDTO);
        if (id != null) {
            userService.update(id, user);
        } else {
            userService.save(user);
        }

        return id == null ? "redirect:/users" : "redirect:/users/" + id;
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
