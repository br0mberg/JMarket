package ru.brombin.JMarket.controller.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.services.PersonService;
import ru.brombin.JMarket.util.ErrorResponse;
import ru.brombin.JMarket.util.PersonValidator;
import ru.brombin.JMarket.util.exceptions.PersonNotCreatedException;
import ru.brombin.JMarket.util.exceptions.PersonNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PeopleApiController {

    private final PersonService personService;

    private final PersonValidator personValidator;

    @Autowired
    public PeopleApiController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> people = personService.findAll();
        return ResponseEntity.ok(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Person person = personService.findOne(id);
        return ResponseEntity.ok(person);
    }

    // TODO: вынести логику в контроллер
    @PostMapping()
    public ResponseEntity<Person> createPerson(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PersonNotCreatedException(buildErrorMessage(bindingResult));
        }
        personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
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

    // TODO: вынести логику в контроллер
    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> createPeople(@RequestBody List<Person> people) {
        StringBuilder errorMessage = new StringBuilder();

        for (Person person : people) {
            BindingResult bindingResult = new BeanPropertyBindingResult(person, "person");
            personValidator.validate(person, bindingResult);

            if (bindingResult.hasErrors()) {
                errorMessage.append("Error of validation on Person: " + person.getId() + " ");
                errorMessage.append(buildErrorMessage(bindingResult));
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new PersonNotCreatedException(errorMessage.toString());
        }

        personService.saveAll(people);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") int id, @RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (personService.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        personService.update(id, person);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) {
        if (personService.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        }
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
