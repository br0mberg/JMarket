package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.model.Item;
import ru.brombin.JMarket.model.Person;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByName(String  itemName);

    List<Item> findByOwner(Person owner);

    Item findByArticleNumber(String articleNumber);

}
