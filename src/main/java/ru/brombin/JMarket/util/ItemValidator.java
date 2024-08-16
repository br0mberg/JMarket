package ru.brombin.JMarket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.dao.ItemDao;
import ru.brombin.JMarket.model.Item;

@Component
public class ItemValidator implements Validator {

    private final ItemDao itemDao;
    @Autowired
    public ItemValidator(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item product = (Item) target;

        if (itemDao.showByArticleNumber(product.getArticleNumber()).isPresent()) {
            errors.rejectValue("articleNumber", "", "Article number is already exist");
        }
    }
}
