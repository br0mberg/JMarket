package ru.brombin.JMarket.model;

import java.sql.Date;

public class Product {

    private int id;

    private String name;

    private String description;

    private String category;

    private int price;

    private int quantity;

    private int articleNumber;

    private Date createdDate;

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

    public int getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(int articleNumber) {
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

