package ru.brombin.JMarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="User")
@Data
public class User implements UserDetails {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="age", nullable = false)
    private int age;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Item> items;

    @Column(name="date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDateTime dateOfBirth;

    @Column(name="registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;

    public static enum UserRole {
        ROLE_ADMIN, ROLE_USER, ROLE_SELLER
    }

    public static UserRole[] getPersonRoles() {
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
        return this.getPassword();
    }

    @Override
    public String getUsername() {
        return this.getUsername();
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
