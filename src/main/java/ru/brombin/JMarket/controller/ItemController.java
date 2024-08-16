package ru.brombin.JMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dao.ItemDao;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.util.ItemValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemDao itemDao;

    private final ItemValidator itemValidator;

    @Autowired
    public ItemController(ItemDao itemDao, ItemValidator itemValidator) {
        this.itemDao = itemDao;
        this.itemValidator = itemValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("items", itemDao.index());
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Item item = itemDao.show(id);
        model.addAttribute("item", item);
        return "item/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("item", new Item());
        return "item/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Valid Item item, BindingResult bindingResult) {
        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) return "item/new";

        itemDao.addNewItem(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Item item = itemDao.show(id);
        model.addAttribute("item", item);
        return "item/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") @Valid Item item, BindingResult bindingResult, @PathVariable("id") int id) {
        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) return "item/edit";

        itemDao.update(id, item);
        return "redirect:/items/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        itemDao.delete(id);
        return "redirect:/items";
    }
}
