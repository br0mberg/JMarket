package ru.brombin.JMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.brombin.JMarket.dao.ProductDao;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductDao productDao;

    @Autowired
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("products", productDao.index());
        return "product/index";
    }

    @GetMapping("/{articleNumber}")
    public String show(@PathVariable("articleNumber") int articleNumber, Model model) {
        model.addAttribute("product", productDao.show(articleNumber));
        return "product/show";
    }
}
