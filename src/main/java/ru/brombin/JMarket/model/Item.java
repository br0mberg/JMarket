package ru.brombin.JMarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="Item")
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
    @NotNull(message="Category should not be empty")
    @Enumerated(EnumType.STRING)
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
    private Date createdDate;

    @Column(name="quantity_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date quantityChangeDate;

    @ManyToOne
    @JoinColumn(name="person_id", referencedColumnName = "id")
    private Person owner;

    public Item() {

    }

    public Item(String name, String description, ItemCategory category,
                int price, int quantity, String articleNumber,
                Date createdDate, Date quantityChangeDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.articleNumber = articleNumber;
        this.createdDate = createdDate;
        this.quantityChangeDate = quantityChangeDate;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getQuantityChangeDate() {
        return quantityChangeDate;
    }

    public void setQuantityChangeDate(Date quantityChangeDate) {
        this.quantityChangeDate = quantityChangeDate;
    }

    public void setUpdatedItem(Item updatedItem) {
        this.setName(updatedItem.getName());
        this.setDescription(updatedItem.getDescription());
        this.setOwner(updatedItem.getOwner());
        this.setCategory(updatedItem.getCategory());
        this.setPrice(updatedItem.getPrice());
        this.setArticleNumber(updatedItem.getArticleNumber());
        this.setQuantity(updatedItem.getQuantity());
        this.setQuantityChangeDate(new Date());
    }
}

