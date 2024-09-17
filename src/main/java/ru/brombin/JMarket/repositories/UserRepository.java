package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameOrderByAge(String username);

    User findByEmail(String email);

    List<User> findByEmailStartingWith(String startingWith);

    @Modifying
    @Query("UPDATE User u SET u.username = :username, " +
            "u.password = :password, " +
            "u.age = :age, " +
            "u.email = :email, " +
            "u.role = :role, " +
            "u.dateOfBirth = :dateOfBirth " +
            "WHERE u.id = :id")
    void updateUserFields(@Param("id") int id,
                          @Param("username") String username,
                          @Param("password") String password,
                          @Param("age") int age,
                          @Param("email") String email,
                          @Param("role") User.UserRole role,
                          @Param("dateOfBirth") LocalDateTime dateOfBirth);
}
