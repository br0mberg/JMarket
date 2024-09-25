package ru.brombin.JMarket.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repository.ItemRepository;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserService userService;

    public Page<Item> findAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public List<Item> findAll() {
        logger.info("Fetching all items from the repository by User {}", userService.getCurrentUser().getId());
        return itemRepository.findAll();
    }

    public Optional<Item> findOne(int id) {
        logger.info("Searching for item with id {} by user {}", id, userService.getCurrentUser().getId());
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            logger.warn("Item with id {} not found", id);
        }
        return item;
    }

    public List<Item> findByName(String name) {
        logger.info("Searching for items with name {} by user {}", name, userService.getCurrentUser().getId());
        return itemRepository.findByName(name);
    }

    public List<Item> findByOwner(User owner) {
        logger.info("Searching for items owned by {} by user {}", owner.getUsername(), userService.getCurrentUser().getId());
        return itemRepository.findByOwner(owner);
    }

    public Item findByArticleNumber(String articleNumber) {
        logger.info("Searching for item with article number {} by user {}", articleNumber, userService.getCurrentUser().getId());
        return itemRepository.findByArticleNumber(articleNumber);
    }

    @Transactional
    public void save(Item item) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            logger.info("Setting current user {} as owner for the item", currentUser.getId());
            item.setOwner(currentUser);
        } else {
            logger.warn("No current user found to set as owner for the item");
        }

        item.setCreatedDate(LocalDateTime.now());
        item.setQuantityChangeDate(LocalDateTime.now());
        itemRepository.save(item);
        logger.info("Item '{}' saved successfully by user ID {}", item.getId(), currentUser != null ? currentUser.getId() : "unknown");
    }

    @Transactional
    public void update(int id, Item item) {
        User currentUser = userService.getCurrentUser();
        logger.info("Updating item with id {} by user ID {}", id, currentUser != null ? currentUser.getId() : "unknown");
        try {
            itemRepository.updateFields(id,
                    item.getName(),
                    item.getDescription(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getArticleNumber());
            logger.info("Item with id {} updated successfully by user ID {}", id, currentUser != null ? currentUser.getId() : "unknown");
        } catch (Exception e) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
    }

    @Transactional
    public void delete(int id) {
        User currentUser = userService.getCurrentUser();
        logger.info("Deleting item with id {} by user ID {}", id, currentUser != null ? currentUser.getId() : "unknown");
        try {
            itemRepository.deleteById(id);
            logger.info("Item with id {} deleted successfully by user ID {}", id, currentUser != null ? currentUser.getId() : "unknown");
        } catch (Exception e) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
    }

    @Transactional
    public List<Item> findItemsWithLowQuantity(int threshold) {
        return itemRepository.findItemsWithLowQuantity(threshold);
    }
}
