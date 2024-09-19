package ru.brombin.JMarket.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;

    
    @GetMapping()
    public ResponseEntity<List<User>> getAllPeople() {
        logger.info("User '{}' is fetching all users", userService.getCurrentUser().getId());
        List<User> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        logger.info("User '{}' is fetching user with ID: {}", userService.getCurrentUser().getId(), id);
        return userService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user, BindingResult bindingResult) {
        logger.info("User '{}' is creating a new user: {}", userService.getCurrentUser().getId(), user.getUsername());
        validateUser(user, bindingResult);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> createPeople(@RequestBody List<User> users) {
        logger.info("User '{}' is creating a batch of users", userService.getCurrentUser().getId());
        users.stream().map(user -> {
                    validateUser(user, new BeanPropertyBindingResult(user, "UserDTO"));
                    return user;
                })
                .toList();
        userService.saveAll(users);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody @Valid User user, BindingResult bindingResult) {
        logger.info("User '{}' is updating user with ID: {}", userService.getCurrentUser().getId(), id);
        validateUser(user, bindingResult);
        int currentUserId = userService.getCurrentUser().getId();
        return userService.findOne(id)
                .map(existingUser -> {
                    userService.update(id, user);
                    logger.info("User '{}' successfully updated user with ID: {}", currentUserId, id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("User '{}' attempted to update non-existing user with ID: {}", currentUserId, id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") int id) {
        logger.info("User '{}' is deleting user with ID: {}", userService.getCurrentUser().getId(), id);
        return userService.findOne(id)
                .map(user -> {
                    userService.delete(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }


    private void validateUser(User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Data validation failed for author user: {}.", userService.getCurrentUser().getId());
            throw new NotCreatedOrUpdatedException(buildErrorMessage(bindingResult));
        }
    }

    private String buildErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError f : errors) {
            errorMessage.append(f.getField())
                    .append(" - ").append(f.getDefaultMessage())
                    .append(";");
        }
        return errorMessage.toString();
    }
}
