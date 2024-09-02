package ru.brombin.JMarket.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brombin.JMarket.dto.ItemDTO;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.model.ItemCategory;
import ru.brombin.JMarket.services.ItemService;
import ru.brombin.JMarket.util.ItemValidator;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemValidator itemValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemValidator itemValidator, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.itemValidator = itemValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("items", convertToItemDTO(itemService.findAll()));
        return "item/index";
    }

    private List<ItemDTO> convertToItemDTO(List<Item> items) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (Item item : items) {
            itemDTOList.add(modelMapper.map(item, ItemDTO.class));
        }

        return itemDTOList;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Item item = itemService.findOne(id);
        model.addAttribute("item", convertToItemDTO(item));
        return "item/show";
    }

    private ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("item", new ItemDTO());
        model.addAttribute("categories", ItemCategory.values());
        return "item/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("item") @Valid ItemDTO itemDTO, BindingResult bindingResult) {
        itemValidator.validate(itemDTO, bindingResult);
        if (bindingResult.hasErrors())
            return "item/new";

        itemService.save(convertToItem(itemDTO));
        return "redirect:/items";
    }

    private Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        ItemDTO itemDTO = convertToItemDTO(itemService.findOne(id));
        model.addAttribute("categories", ItemCategory.values());
        model.addAttribute("item", itemDTO);
        return "item/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("item") @Valid ItemDTO itemDTO, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        itemValidator.validate(itemDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", ItemCategory.values());
            return "item/edit";
        }

        itemService.update(id, convertToItem(itemDTO));
        return "redirect:/items/" + id;
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        itemService.delete(id);
        return "redirect:/items";
    }
}
