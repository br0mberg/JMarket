package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;

    @GetMapping()
    public String index(Model model) {
        logger.info("Users list requested from User '{}'", userService.getCurrentUser().getId());
        model.addAttribute("users", userService.findAll());
        return "user/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        logger.info("User detail page requested for user '{}' from User '{}'", id, userService.getCurrentUser().getId());
        User user = userService.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        model.addAttribute("user", user);
        return "user/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        logger.info("New user form requested from User '{}'", userService.getCurrentUser().getId());
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.getUserRoles());
        return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        logger.info("New user creation request received from User '{}'", userService.getCurrentUser().getId());
        return handleUserForm(user, bindingResult, "user/new", null);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        logger.info("User edit form requested for user '{}' from User author'{}'",
                id, userService.getCurrentUser().getId());
        model.addAttribute("user", userService.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id)));
        return "user/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        logger.info("Update request for user '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        return handleUserForm(user, bindingResult, "user/edit", id);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        logger.info("Delete request for user '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        userService.delete(id);
        return "redirect:/users";
    }

    private String handleUserForm(User user, BindingResult bindingResult, String errorView, Integer id) {
        userValidator.validate(user, bindingResult);
        System.out.println(bindingResult.getFieldErrors());
        if (bindingResult.hasErrors()) {
            logger.info("New User validation failed for author user: {}.", userService.getCurrentUser().getId());
            return errorView;
        }

        if (id != null) {
            userService.update(id, user);
        } else {
            userService.save(user);
        }

        return id == null ? "redirect:/users" : "redirect:/users/" + id;
    }
}
