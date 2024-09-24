package ru.brombin.JMarket.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional(readOnly = true)
public class PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Set<User> findAllPeopleWithAllItems() {
        return new HashSet<User>(entityManager.createQuery("select u from User u LEFT JOIN FETCH u.items").getResultList());
    }

    public List<User> findAllPeopleWithAllItems(int page, int size) {
        return entityManager.createQuery("select u from User u LEFT JOIN FETCH u.items", User.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}