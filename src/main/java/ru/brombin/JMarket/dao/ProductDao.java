package ru.brombin.JMarket.dao;

import org.springframework.stereotype.Component;
import ru.brombin.JMarket.model.Product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    private List<Product> productList;

    {
        productList = new ArrayList<>();
        productList.add(new Product("ak47", "description", "gun", 1500, 12, 1, new Date(12), new Date(1)));
        productList.add(new Product("pencil", "description2", "office attribute", 15, 120, 2, new Date(200), new Date(2)));
    }

    public List<Product> index() {
        return productList;
    }

    public void addNewProduct(Product product) {
        productList.add(product);
    }

    public Optional<Product> show(int articleNumber) {
        return productList
                .stream()
                .filter(p -> p.getArticleNumber() == articleNumber)
                .findAny();
    }

}
