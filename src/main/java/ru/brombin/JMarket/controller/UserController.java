package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.UserValidator;

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
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "user/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.getUserRoles());
        return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) return "user/new";

        userService.save(convertToUser(userDTO));
        return "redirect:/users";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        UserDTO userDTO = convertToUserDTO(userService.findOne(id));
        model.addAttribute("user", userDTO);
        return "user/edit";
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid UserDTO userDTO, BindingResult bindingResult, @PathVariable("id") int id) {
        userValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) return "user/edit";

        userService.update(id, convertToUser(userDTO));
        return "redirect:/users/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
