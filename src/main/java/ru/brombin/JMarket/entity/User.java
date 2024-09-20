package ru.brombin.JMarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="\"user\"")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username", nullable = false, unique = true)
    @NotEmpty(message="Name should not be empty")
    @Size(min=2, max=30, message="Name should be between 2 and 30 characters")
    private String username;

    @Column(name="password", nullable = false)
    @NotEmpty(message="Password can't be empty")
    private String password;

    @Column(name="age", nullable = false)
    @Min(value=0, message="Age should be greater than 0")
    private int age;

    @Column(name="email", nullable = false, unique = true)
    @NotEmpty(message="Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message="Role should be set")
    private UserRole role = UserRole.ROLE_USER;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Item> items;

    @Column(name="date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Date of birth is required")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate dateOfBirth;

    @Column(name="registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;

    public enum UserRole {
        ROLE_ADMIN, ROLE_USER, ROLE_SELLER
    }

    public static UserRole[] getUserRoles() {
        return UserRole.values();
    }

    public void addItem(Item item) {
        if (this.getItems() == null)
            this.setItems(new ArrayList<>());

        this.getItems().add(item);
        item.setOwner(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User person = (User) o;
        return id == person.id && age == person.age && username.equals(person.username) && email.equals(person.email) && role == person.role && dateOfBirth.equals(person.dateOfBirth) && registrationDate.equals(person.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, age, email, role, dateOfBirth, registrationDate);
    }
}
