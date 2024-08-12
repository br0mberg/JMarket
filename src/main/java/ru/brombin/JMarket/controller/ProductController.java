package ru.brombin.JMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dao.ProductDao;
import ru.brombin.JMarket.model.Product;

import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductDao productDao;

    @Autowired
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("products", productDao.index());
        return "product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Product> productOptional = productDao.show(id);
        model.addAttribute("product", productOptional.get());
        return "product/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("product", new Product());
        return "product/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") Product product) {
        productDao.addNewProduct(product);
        return "product/index";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Product> productOptional = productDao.show(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "product/edit";
        } else {
            // Логика обработки случая, когда продукт не найден
            return "product/index";
        }

    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") Product product, @PathVariable("id") int id) {
        productDao.update(id, product);
        return "product/show";
    }
}
