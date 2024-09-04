package ru.brombin.JMarket.entity;

import jakarta.persistence.*;
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
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(name="price")
    private int price;

    @Column(name="quantity")
    private int quantity;

    @Column(name="article_number", nullable = false, unique = true)
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

