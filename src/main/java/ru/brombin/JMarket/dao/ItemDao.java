package ru.brombin.JMarket.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.model.Item;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class ItemDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public ItemDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Item> index() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Item p", Item.class).getResultList();
    }

    @Transactional
    public void update(int id, Item updatedItem) {
        Session session = sessionFactory.getCurrentSession();
        Item item = session.get(Item.class, id);
        item.setUpdatedItem(updatedItem);
    }

    @Transactional
    public Item showByArticleNumber(String articleNumber) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Item.class, articleNumber);
    }

    @Transactional(readOnly = true)
    public Item show(int id) {
        return sessionFactory.getCurrentSession().get(Item.class, id);
    }

    @Transactional
    public void addNewItem(Item item) {
        item.setCreatedDate(Date.valueOf(LocalDate.now()));
        item.setQuantityChangeDate(Date.valueOf(LocalDate.now()));

        Session session = sessionFactory.getCurrentSession();
        session.persist(item);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Item item = session.get(Item.class, id);
        session.remove(item);
    }
}
