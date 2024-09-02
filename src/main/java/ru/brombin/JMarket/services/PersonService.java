package ru.brombin.JMarket.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.PersonRepository;
import ru.brombin.JMarket.security.PersonDetails;
import ru.brombin.JMarket.util.exceptions.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }

    public List<Person> findByName(String name) {
        return personRepository.findByUsernameOrderByAge(name);
    }

    public Person findByEmail(String email){
        return personRepository.findByEmail(email);
    }

    public Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = ((PersonDetails) authentication.getPrincipal()).getUsername();
        return personRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setRegistrationDate(LocalDateTime.now());
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Optional<Person> existingPersonOpt = personRepository.findById(id);

        if (existingPersonOpt.isEmpty()) {
            throw new EntityNotFoundException("Person with id " + id + " not found");
        }

        Person existingPerson = existingPersonOpt.get();

        updatedPerson.setId(existingPerson.getId());
        updatedPerson.setRegistrationDate(existingPerson.getRegistrationDate());

        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(List<Person> people) {
        for (Person p : people) {
            p.setRegistrationDate(LocalDateTime.now());
        }
        personRepository.saveAll(people);
    }
}
