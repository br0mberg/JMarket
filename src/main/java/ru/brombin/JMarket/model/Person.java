package ru.brombin.JMarket.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="Person")
public class Person {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable = false)
    @NotBlank(message="Name should not be empty")
    @Size(min=2, max=30, message="Name should be between 2 and 30 characters")
    private String name;

    @Column(name="age", nullable = false)
    @Min(value=0, message="Age should be greater than 0")
    private int age;

    @Column(name="email", nullable = false, unique = true)
    @NotEmpty(message="Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @OneToMany(mappedBy = "owner")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Item> items;

    public Person() {
    }

    public void addItem(Item item) {
        if (this.getItems() == null)
            this.setItems(new ArrayList<>());

        this.getItems().add(item);
        item.setOwner(this);
    }
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
