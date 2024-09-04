package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    List<ru.brombin.JMarket.entity.User> findByUsernameOrderByAge(String username);

    User findByEmail(String email);

    List<User> findByEmailStartingWith(String startingWith);

}
