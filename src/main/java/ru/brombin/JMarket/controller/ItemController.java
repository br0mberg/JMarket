package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.services.ItemService;
import ru.brombin.JMarket.util.ItemValidator;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private final ItemValidator itemValidator;

    @Autowired
    public ItemController(ItemService itemService, ItemValidator itemValidator) {
        this.itemService = itemService;
        this.itemValidator = itemValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Item item = itemService.findOne(id);
        model.addAttribute("item", item);
        return "item/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("item", new Item());
        return "item/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult) {
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) return "item/new";

        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Item item = itemService.findOne(id);
        model.addAttribute("item", item);
        return "item/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult, @PathVariable("id") int id) {
        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) return "item/edit";

        itemService.update(id, item);
        return "redirect:/items/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        itemService.delete(id);
        return "redirect:/items";
    }
}
