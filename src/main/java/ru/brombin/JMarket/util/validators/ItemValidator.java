package ru.brombin.JMarket.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brombin.JMarket.dto.ItemDTO;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.repositories.ItemRepository;

@Component
public class ItemValidator implements Validator {

    private final ItemRepository itemRepository;
    @Autowired
    public ItemValidator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemDTO product = (ItemDTO) target;

        if (itemRepository.findByArticleNumber(product.getArticleNumber()) != null) {
            errors.rejectValue("articleNumber", "", "Article number is already exist");
        }
    }
}
