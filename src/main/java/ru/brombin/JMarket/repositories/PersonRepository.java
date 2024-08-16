package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
