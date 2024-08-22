package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsername(String username);

    List<Person> findByUsernameOrderByAge(String username);

    Person findByEmail(String email);

    List<Person> findByEmailStartingWith(String startingWith);

}
