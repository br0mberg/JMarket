package ru.brombin.JMarket.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name="Item")
public class Item {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    @NotBlank(message="Name should not be empty")
    @Size(min=2, max=50, message="Name should be between 2 and 50 characters")
    private String name;

    @Column(name="description")
    @NotBlank(message="Description should not be empty")
    @Size(max=255, message="Description should be lower than 255")
    private String description;

    @Column(name="category")
    @NotBlank(message="Category should not be empty")
    @Size(min=2, max=20, message="Category should be between 2 and 20 characters")
    private String category;

    @Column(name="price")
    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Price should be greater than 0")
    private int price;

    @Column(name="quantity")
    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Quantity should be greater than 0")
    private int quantity;

    @Column(name="article_number", unique = true)
    @NotBlank(message="Article Number can't be null")
    @Size(min=6, max=12, message="Article Number should be between 6 and 12 characters")
    @Pattern(regexp="[A-Za-z0-9]+")
    private String articleNumber;

    @Column(name="created_date")
    @NotBlank(message="Created date can't be empty")
    @PastOrPresent
    private Date createdDate;

    @Column(name="quantity_change_date")
    @NotBlank(message="Quantity change date can't be empty")
    @PastOrPresent
    private Date quantityChangeDate;

    @ManyToOne
    @JoinColumn(name="person_id", referencedColumnName = "id")
    private Person owner;

    public Item() {

    }

    public Item(String name, String description, String category,
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
        this.setQuantityChangeDate(Date.valueOf(LocalDate.now()));
    }
}

