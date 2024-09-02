package ru.brombin.JMarket.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.model.PersonRole;
import ru.brombin.JMarket.repositories.PersonRepository;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RegistrationService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);
        person.setRegistrationDate(timestamp.toLocalDateTime());
        person.setRole(PersonRole.ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }
}
