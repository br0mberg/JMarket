package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.ItemDTO;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.services.ItemService;
import ru.brombin.JMarket.util.ErrorResponse;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.exceptions.NotFoundException;
import ru.brombin.JMarket.util.validators.ItemValidator;

import java.util.List;
import java.util.stream.Collectors;

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
    private final ModelMapper modelMapper;


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("items", convertToItemDTOList(itemService.findAll()));
        return "item/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Item item = itemService.findOne(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        model.addAttribute("item", convertToItemDTO(item));
        return "item/show";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("item", new ItemDTO());
        model.addAttribute("categories", Item.getItemCategories());
        return "item/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("item") @Valid ItemDTO itemDTO, BindingResult bindingResult) {
        return handleItemForm(itemDTO, bindingResult, "item/new", null);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        ItemDTO itemDTO = convertToItemDTO(itemService.findOne(id).orElseThrow(() -> new NotFoundException("Item not found with id: " + id)));
        model.addAttribute("categories", Item.getItemCategories());
        model.addAttribute("item", itemDTO);
        return "item/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("item") @Valid ItemDTO itemDTO, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        return handleItemForm(itemDTO, bindingResult, "item/edit", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        itemService.delete(id);
        return "redirect:/items";
    }

    private List<ItemDTO> convertToItemDTOList(List<Item> items) {
        return items.stream().map(this::convertToItemDTO).collect(Collectors.toList());
    }

    private ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    private Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    private String handleItemForm(ItemDTO itemDTO, BindingResult bindingResult, String errorView, Integer id) {
        itemValidator.validate(itemDTO, bindingResult);
        if (bindingResult.hasErrors()) return errorView;

        Item item = convertToItem(itemDTO);
        if (id != null) {
            itemService.update(id, item);
        } else {
            itemService.save(item);
        }

        return id == null ? "redirect:/items" : "redirect:/items/" + id;
    }
}
