package ru.brombin.JMarket.util.validators;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {
    @Autowired
    private final UserRepository userRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        // Проверка уникальности имени пользователя
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            errors.rejectValue("username", "", "This username is already taken");

        // Проверка уникальности email
        if (userRepository.findByEmail(userDTO.getEmail()) != null)
            errors.rejectValue("email", "", "This email is already taken");

        // Проверка формата даты рождения
        try {
            LocalDate dateOfBirth = LocalDate.parse(userDTO.getDateOfBirth(), DATE_FORMATTER);

            // Проверка на будущее время
            if (dateOfBirth.isAfter(LocalDate.now())) {
                errors.rejectValue("dateOfBirth", "", "Date of birth cannot be in the future");
            }

        } catch (DateTimeParseException e) {
            errors.rejectValue("dateOfBirth", "", "The entered date of birth does not match the format: yyyy/MM/dd");
        }
    }

}
