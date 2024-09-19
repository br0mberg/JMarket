package ru.brombin.JMarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="Item")
@Data
public class Item {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    @NotNull(message="Name should not be empty")
    @Size(min=2, max=50, message="Name should be between 2 and 50 characters")
    private String name;

    @Column(name="description")
    @NotNull(message="Description should not be empty")
    @Size(max=255, message="Description should be lower than 255")
    private String description;

    @Column(name="category")
    @Enumerated(EnumType.STRING)
    @NotNull(message="Category should not be empty")
    private ItemCategory category;

    @Column(name="price")
    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Price should be greater than 0")
    private int price;

    @Column(name="quantity")
    @NotNull(message="Quantity can't be null")
    @PositiveOrZero(message="Quantity should be greater than 0")
    private int quantity;

    @Column(name="article_number", nullable = false, unique = true)
    @NotNull(message="Article Number can't be empty")
    @Size(min=6, max=12, message="Article Number should be between 6 and 12 characters")
    @Pattern(regexp="[A-Za-z0-9]+")
    private String articleNumber;

    @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @Column(name="quantity_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime quantityChangeDate;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = true)
    private User owner;

    public enum ItemCategory {
        Books, ZooItems, RepairAndConstruction, Electronics,
        AutomotiveItems, Furniture, Sport
    }

    public static ItemCategory[] getItemCategories() {
        return ItemCategory.values();
    }
}

