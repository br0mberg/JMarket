package ru.brombin.JMarket.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.brombin.JMarket.model.Product;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> index() {
        return jdbcTemplate.query("SELECT * FROM Product", new BeanPropertyRowMapper<>(Product.class));
    }

    public int update(int id, Product updatedProduct) {
        updatedProduct.setQuantityChangeDate(Date.valueOf(LocalDate.now()));
        return jdbcTemplate.update("UPDATE Product SET " +
                "name=?, description=?, category=?," +
                "price=?, quantity=?, article_number=?, quantity_change_date=? WHERE id=?",
                updatedProduct.getName(), updatedProduct.getDescription(), updatedProduct.getCategory(),
                updatedProduct.getPrice(), updatedProduct.getQuantity(), updatedProduct.getArticleNumber(),
                updatedProduct.getQuantityChangeDate(), id);
    }

    public Optional<Product> showByArticleNumber(int articleNumber) {
        return jdbcTemplate.query("SELECT * FROM Product WHERE article_number=?",
                new Object[]{articleNumber}, new BeanPropertyRowMapper<>(Product.class)).stream().findAny();
    }

    public Optional<Product> show(int id) {
        return jdbcTemplate.query("SELECT * FROM Product WHERE id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Product.class)).stream().findAny();
    }

    public void addNewProduct(Product product) {
        product.setCreatedDate(Date.valueOf(LocalDate.now()));
        product.setQuantityChangeDate(Date.valueOf(LocalDate.now()));

        jdbcTemplate.update("INSERT INTO Product (name, description, category, price, quantity, article_number, created_date, quantity_change_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                product.getName(), product.getDescription(), product.getCategory(),
                product.getPrice(), product.getQuantity(), product.getArticleNumber(),
                product.getCreatedDate(), product.getQuantityChangeDate());
    }

    public void delete(int id) {
        String sql = "DELETE FROM Product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
