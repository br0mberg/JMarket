package ru.brombin.JMarket.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.items")
    Set<User> findAllWithItems();
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.items")
    Page<User> findAllWithItems(Pageable pageable);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameOrderByAge(String username);
    User findByEmail(String email);

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
