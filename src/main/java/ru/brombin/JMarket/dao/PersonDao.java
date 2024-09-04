package ru.brombin.JMarket.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.Person;
import ru.brombin.JMarket.entity.User;

import java.util.HashSet;
import java.util.Set;

@Component
@Transactional(readOnly = true)
public class PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Set<Person> findAllPeopleWithAllItems() {
        Set<Person> people = new HashSet<User>(entityManager.createQuery("select p from Person p LEFT JOIN FETCH p.items").getResultList());
        return people;
    }
}