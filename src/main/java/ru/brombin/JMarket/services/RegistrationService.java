package ru.brombin.JMarket.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.PersonRepository;

import java.util.Date;

@Service
public class RegistrationService {

    private final PersonRepository personRepository;

    @Autowired
    public RegistrationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void register(Person person) {
        person.setRegistrationDate(new Date());
        personRepository.save(person);
    }
}
