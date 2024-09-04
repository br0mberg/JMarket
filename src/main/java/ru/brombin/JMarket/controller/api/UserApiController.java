package ru.brombin.JMarket.controller.api;

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
import ru.brombin.JMarket.util.ErrorResponse;
import ru.brombin.JMarket.util.UserValidator;
import ru.brombin.JMarket.util.exceptions.UserNotCreatedException;
import ru.brombin.JMarket.util.exceptions.UserNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserApiController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;
    
    @GetMapping()
    public ResponseEntity<List<User>> getAllPeople() {
        List<User> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.findOne(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Valid User User, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(buildErrorMessage(bindingResult));
        }
        userService.save(User);
        return ResponseEntity.status(HttpStatus.CREATED).body(User);
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

    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> createPeople(@RequestBody List<User> people) {
        StringBuilder errorMessage = new StringBuilder();

        for (User User : people) {
            BindingResult bindingResult = new BeanPropertyBindingResult(User, "User");
            userValidator.validate(User, bindingResult);

            if (bindingResult.hasErrors()) {
                errorMessage.append("Error of validation on User: " + User.getId() + " ");
                errorMessage.append(buildErrorMessage(bindingResult));
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new UserNotCreatedException(errorMessage.toString());
        }

        userService.saveAll(people);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody @Valid User User, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (userService.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        userService.update(id, User);
        return ResponseEntity.ok(User);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        if (userService.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "User with this id wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
