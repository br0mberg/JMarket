package ru.brombin.JMarket.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.dto.ItemDTO;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositories.ItemRepository;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserService userService;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findOne(int id) {
        Optional<Item> item = itemRepository.findById(id);
        return item;
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findByOwner(ru.brombin.JMarket.entity.User owner) {
        return itemRepository.findByOwner(owner);
    }

    public Item findByArticleNumber(String articleNumber) {
        return itemRepository.findByArticleNumber(articleNumber);
    }

    @Transactional
    public void save(Item item) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            item.setOwner(currentUser);
        }

        item.setCreatedDate(LocalDateTime.now());
        item.setQuantityChangeDate(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Transactional
    public void update(int id, Item item) {
        try {
            itemRepository.updateFields(id,
                    item.getName(),
                    item.getDescription(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getArticleNumber());
        } catch (Exception e) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
    }

    @Transactional
    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}
