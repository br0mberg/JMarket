package ru.brombin.JMarket.model;

import org.hibernate.validator.constraints.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.sql.Date;

public class Product {
    private int id;
    @NotBlank(message="Name should not be empty")
    @Size(min=2, max=50, message="Name should be between 2 and 50 characters")
    private String name;
    @NotBlank(message="Description should not be empty")
    @Size(max=255, message="Description should be lower than 255")
    private String description;
    @NotBlank(message="Category should not be empty")
    @Size(min=2, max=20, message="Category should be between 2 and 20 characters")
    private String category;
    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Price should be greater than 0")
    private int price;
    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Quantity should be greater than 0")
    private int quantity;
    @NotBlank(message="Article Number can't be null")
    @Size(min=6, max=12, message="Article Number should be between 6 and 12 characters")
    @Pattern(regexp="[A-Za-z0-9]+")
    private String articleNumber;
    @PastOrPresent
    private Date createdDate;
    @PastOrPresent
    private Date quantityChangeDate;

    public Product() {

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
}

