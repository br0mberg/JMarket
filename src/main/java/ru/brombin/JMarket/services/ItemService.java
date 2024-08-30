package ru.brombin.JMarket.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.model.Person;
import ru.brombin.JMarket.repositories.ItemRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    private final PersonService personService;

    public ItemService(ItemRepository itemRepository, PersonService personService) {
        this.itemRepository = itemRepository;
        this.personService = personService;
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
        Person currentPerson = personService.getCurrentPerson();
        if (currentPerson != null) {
            item.setOwner(currentPerson);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);
        item.setCreatedDate(timestamp);
        item.setQuantityChangeDate(timestamp);
        item.getOwner().addItem(item);
        itemRepository.save(item);
    }

    @Transactional
    public void update(int id, Item updatedItem) {
        updatedItem.setId(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);
        updatedItem.setQuantityChangeDate(timestamp);
        itemRepository.save(updatedItem);
    }

    @Transactional
    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}
