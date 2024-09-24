package ru.brombin.JMarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByName(String  itemName);
    List<Item> findByOwner(User owner);
    Item findByArticleNumber(String articleNumber);
    Page<Item> findByOwner(User owner, Pageable pageable);
    Page<Item> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Item i SET i.name = :name, " +
            "i.description = :description, " +
            "i.category = :category, " +
            "i.price = :price, " +
            "i.quantity = :quantity, " +
            "i.articleNumber = :articleNumber " +
            "WHERE i.id = :id")
    void updateFields(@Param("id") int id,
                      @Param("name") String name,
                      @Param("description") String description,
                      @Param("category") Item.ItemCategory category,
                      @Param("price") int price,
                      @Param("quantity") int quantity,
                      @Param("articleNumber") String articleNumber);
}
