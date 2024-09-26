package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.service.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;

    @GetMapping()
    public String index(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("Users list requested from User '{}'", userService.getCurrentUser().getId());

        if (page < 0 || size <= 0) {
            log.warn("Invalid page or size values: page = {}, size = {}", page, size);
            return "redirect:/users?page=0&size=10";
        }

        Page<User> userPage = userService.findAllWithPagination(page, size);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "user/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.info("User detail page requested for user '{}' from User '{}'", id, userService.getCurrentUser().getId());
        User user = userService.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        model.addAttribute("user", user);
        return "user/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        log.info("New user form requested from User '{}'", userService.getCurrentUser().getId());
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.getUserRoles());
        return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        log.info("New user creation request received from User '{}'", userService.getCurrentUser().getId());
        return handleUserForm(user, bindingResult, "user/new", null);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        log.info("User edit form requested for user '{}' from User author'{}'",
                id, userService.getCurrentUser().getId());
        model.addAttribute("user", userService.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id)));
        return "user/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        log.info("Update request for user '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        return handleUserForm(user, bindingResult, "user/edit", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        log.info("Delete request for user '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        userService.delete(id);
        return "redirect:/users";
    }

    private String handleUserForm(User user, BindingResult bindingResult, String errorView, Integer id) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("New User validation failed for author user: {}.", userService.getCurrentUser().getId());
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
