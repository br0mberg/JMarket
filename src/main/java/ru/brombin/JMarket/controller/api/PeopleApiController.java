package ru.brombin.JMarket.controller.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.services.PersonService;
import ru.brombin.JMarket.util.PersonValidator;

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
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(person);
    }

    @PostMapping()
    public ResponseEntity<Person> createPerson(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Person>> createPeople(@RequestBody List<Person> people) {
        personService.saveAll(people);
        return ResponseEntity.status(HttpStatus.CREATED).body(people);
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
}
