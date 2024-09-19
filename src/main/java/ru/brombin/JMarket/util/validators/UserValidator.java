package ru.brombin.JMarket.util.validators;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositories.UserRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // Проверка уникальности имени пользователя
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            errors.rejectValue("username", "", "This username is already taken");

        // Проверка уникальности email
        if (userRepository.findByEmail(user.getEmail()) != null)
            errors.rejectValue("email", "", "This email is already taken");

        try {
            LocalDate dateOfBirth = user.getDateOfBirth();

            // Проверка на будущее время
            if (dateOfBirth.isAfter(LocalDate.now())) {
                errors.rejectValue("dateOfBirth", "", "Date of birth cannot be in the future");
            }

        } catch (DateTimeParseException e) {
            errors.rejectValue("dateOfBirth", "", "The entered date of birth does not match the format: yyyy/MM/dd");
        }
    }

}
