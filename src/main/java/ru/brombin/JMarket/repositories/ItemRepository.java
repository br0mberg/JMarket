package ru.brombin.JMarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.Person;
import ru.brombin.JMarket.entity.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByName(String  itemName);

    List<Item> findByOwner(User owner);

    Item findByArticleNumber(String articleNumber);

}
