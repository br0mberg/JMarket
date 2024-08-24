package ru.brombin.JMarket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.PersonRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

        if (personRepository.findByUsername(person.getUsername()) != null)
            errors.rejectValue("username", "", "This username is already taken");

        if (personRepository.findByEmail(person.getEmail()) != null)
            errors.rejectValue("email", "", "This email is already taken");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(person.getDateOfBirth().toString());
        } catch (ParseException e) {
            errors.rejectValue("dateOfBirth", "", "The entered date of birth does not match the format: dd/MM/yyyy");
        }
    }
}
