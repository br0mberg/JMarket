package ru.brombin.JMarket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.PersonRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PersonValidator implements Validator {

    private final PersonRepository personRepository;
    @Autowired
    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // Проверка уникальности имени пользователя
        if (personRepository.findByUsername(person.getUsername()).isPresent())
            errors.rejectValue("username", "", "This username is already taken");

        // Проверка уникальности email
        if (personRepository.findByEmail(person.getEmail()) != null)
            errors.rejectValue("email", "", "This email is already taken");

        // Проверка формата даты рождения
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(dateFormat.format(person.getDateOfBirth()));

            // Проверка на будущее время
            if (parsedDate.after(new Date())) {
                errors.rejectValue("dateOfBirth", "", "Date of birth cannot be in the future");
            }

        } catch (ParseException e) {
            errors.rejectValue("dateOfBirth", "", "The entered date of birth does not match the format: d/M/yyyy");
        }
    }

}
