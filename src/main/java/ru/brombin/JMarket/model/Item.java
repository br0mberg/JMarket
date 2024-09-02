package ru.brombin.JMarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

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
    @JoinColumn(name="person_id", referencedColumnName = "id", nullable = true)
    private Person owner;
}

