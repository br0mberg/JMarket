package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.services.ItemService;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.exceptions.NotFoundException;
import ru.brombin.JMarket.util.validators.ItemValidator;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final ItemValidator itemValidator;
    @Autowired
    private final UserService userService;


    @GetMapping()
    public String index(Model model) {
        logger.info("Items list requested from User '{}'", userService.getCurrentUser().getId());
        model.addAttribute("items", itemService.findAll());
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        logger.info("Item detail page requested for item '{}' from User '{}'", id, userService.getCurrentUser().getId());

        Item item = itemService.findOne(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));

        model.addAttribute("item", item);
        return "item/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        logger.info("New item form requested from User '{}'", userService.getCurrentUser().getId());
        model.addAttribute("item", new Item());
        model.addAttribute("categories", Item.getItemCategories());
        return "item/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult) {
        logger.info("New item creation request received from User '{}'", userService.getCurrentUser().getId());
        return handleItemForm(item, bindingResult, "item/new", null);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        logger.info("Item edit form requested for item '{}' from User '{}'",
                id, userService.getCurrentUser().getId());
        Item item = itemService.findOne(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        model.addAttribute("categories", Item.getItemCategories());
        model.addAttribute("item", item);
        return "item/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("item") @Valid Item item,
                         BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        logger.info("Update request for item '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        return handleItemForm(item, bindingResult, "item/edit", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        logger.info("Delete request for item '{}' received from User '{}'",
                id, userService.getCurrentUser().getId());
        itemService.delete(id);
        return "redirect:/items";
    }

    private String handleItemForm(Item item, BindingResult bindingResult, String errorView, Integer id) {
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Data validation failed for author user: {}.", userService.getCurrentUser().getId());
            return errorView;
        }

        if (id != null) {
            itemService.update(id, item);
            return "redirect:/items/" + id;
        } else {
            itemService.save(item);
            return "redirect:/items";
        }
    }
}
