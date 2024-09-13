package ru.brombin.JMarket.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.ErrorResponse;
import ru.brombin.JMarket.util.UserValidator;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserApiController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;
    @Autowired
    private final ModelMapper modelMapper;
    
    @GetMapping()
    public ResponseEntity<List<User>> getAllPeople() {
        List<User> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        return userService.findOne(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        validateUser(userDTO, bindingResult);
        User user = convertToUser(userDTO);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> createPeople(@RequestBody List<UserDTO> usersDTO) {
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
        validateUser(userDTO, bindingResult);
        User user = convertToUser(userDTO);
        return userService.findOne(id)
                .map(existingUser -> {
                    userService.update(id, user);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") int id) {
        return userService.findOne(id)
                .map(user -> {
                    userService.delete(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private void validateUser(UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
        return buildErrorResponse("User with this id wasn't found!", HttpStatus.NOT_FOUND);
    }
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotCreatedOrUpdatedException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
