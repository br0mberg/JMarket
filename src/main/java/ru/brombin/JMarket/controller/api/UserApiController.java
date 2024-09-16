package ru.brombin.JMarket.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.UserDTO;
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
    @Autowired
    private final ModelMapper modelMapper;
    
    @GetMapping()
    public ResponseEntity<List<User>> getAllPeople() {
        logger.info("User '{}' is fetching all users", getCurrentUserName());
        List<User> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        logger.info("User '{}' is fetching user with ID: {}", getCurrentUserName(), id);
        return userService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        logger.info("User '{}' is creating a new user: {}", getCurrentUserName(), userDTO.getUsername());
        validateUser(userDTO, bindingResult);
        User user = convertToUser(userDTO);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> createPeople(@RequestBody List<UserDTO> usersDTO) {
        logger.info("User '{}' is creating a batch of users", getCurrentUserName());
        List<User> users = usersDTO.stream()
                .map(userDTO -> {
                    validateUser(userDTO, new BeanPropertyBindingResult(userDTO, "UserDTO"));
                    return convertToUser(userDTO);
                })
                .toList();
        userService.saveAll(users);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        logger.info("User '{}' is updating user with ID: {}", getCurrentUserName(), id);
        validateUser(userDTO, bindingResult);
        User user = convertToUser(userDTO);
        String currentUserName = getCurrentUserName();
        return userService.findOne(id)
                .map(existingUser -> {
                    userService.update(id, user);
                    logger.info("User '{}' successfully updated user with ID: {}", currentUserName, id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("User '{}' attempted to update non-existing user with ID: {}", currentUserName, id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") int id) {
        String currentUserName = getCurrentUserName();
        logger.info("User '{}' is deleting user with ID: {}", currentUserName, id);
        return userService.findOne(id)
                .map(user -> {
                    userService.delete(id);
                    logger.info("User '{}' successfully deleted user with ID: {}", currentUserName, id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> {
                    logger.warn("User '{}' attempted to delete non-existing user with ID: {}", currentUserName, id);
                    return ResponseEntity.notFound().build();
                });
    }

    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "unknown";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private void validateUser(UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.error("Data validation failed for author user: {}.", getCurrentUserName());
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
