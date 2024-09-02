package ru.brombin.JMarket.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Person")
@Data
public class Person {

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
    private PersonRole role;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Item> items;

    @Column(name="date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDateTime dateOfBirth;

    @Column(name="registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;

    public void addItem(Item item) {
        if (this.getItems() == null)
            this.setItems(new ArrayList<>());

        this.getItems().add(item);
        item.setOwner(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && age == person.age && username.equals(person.username) && email.equals(person.email) && role == person.role && dateOfBirth.equals(person.dateOfBirth) && registrationDate.equals(person.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, age, email, role, dateOfBirth, registrationDate);
    }
}
