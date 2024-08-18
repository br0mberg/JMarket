package ru.brombin.JMarket.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.ItemRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findOne(int id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElse(null);
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findByOwner(Person owner) {
        return itemRepository.findByOwner(owner);
    }

    public Item findByArticleNumber(String articleNumber) {
        return itemRepository.findByArticleNumber(articleNumber);
    }

    @Transactional
    public void save(Item item) {
        item.setCreatedDate(new Date());
        item.setQuantityChangeDate(new Date());
        item.getOwner().addItem(item);
        itemRepository.save(item);
    }

    @Transactional
    public void update(int id, Item updatedItem) {
        updatedItem.setId(id);
        updatedItem.setQuantityChangeDate(new Date());
        itemRepository.save(updatedItem);
    }

    @Transactional
    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}
