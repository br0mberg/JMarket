package ru.brombin.JMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dao.ProductDao;
import ru.brombin.JMarket.model.Product;

import javax.validation.Valid;
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
        if (productOptional.isEmpty()) return "redirect:/products";
        model.addAttribute("product", productOptional.get());
        return "product/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("product", new Product());
        return "product/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "product/new";
        productDao.addNewProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Product> productOptional = productDao.show(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "product/edit";
        } else {
            // Логика обработки случая, когда продукт не найден
            return "redirect:/products";
        }

    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "product/edit";
        productDao.update(id, product);
        return "redirect:/products/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        productDao.delete(id);
        return "redirect:/products";
    }
}
