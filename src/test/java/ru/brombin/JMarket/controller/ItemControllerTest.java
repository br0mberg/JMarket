package ru.brombin.JMarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.service.ItemService;
import ru.brombin.JMarket.service.UserService;
import ru.brombin.JMarket.util.exceptions.NotFoundException;
import ru.brombin.JMarket.util.validators.ItemValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @Mock
    private ItemValidator itemValidator;
    @Mock
    private UserService userService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;

    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemController = new ItemController(itemService, itemValidator, userService);
    }

    @Test
    void testIndex() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(itemService.findAll()).thenReturn(List.of(new Item(), new Item()));

        String viewName = itemController.index(model);

        verify(itemService).findAll();
        assertEquals("item/index", viewName);
    }

    @Test
    void testShowItem() {
        Item testItem = createTestItem();

        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(itemService.findOne(1)).thenReturn(Optional.of(testItem));

        String viewName = itemController.show(testItem.getId(), model);

        verify(itemService).findOne(testItem.getId());
        verify(model).addAttribute("item", testItem);
        assertEquals("item/show", viewName);
    }

    @Test
    void testShowItemNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(itemService.findOne(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemController.show(1, model));
    }

    @Test
    void testCreateValidItem() {
        Item testItem = createTestItem();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = itemController.create(testItem, bindingResult);

        verify(itemValidator).validate(testItem, bindingResult);
        verify(itemService).save(testItem);
        assertEquals("redirect:/items", viewName);
    }

    @Test
    void testCreateInvalidItem() {
        Item invalidItem = createTestItem();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = itemController.create(invalidItem, bindingResult);

        verify(itemValidator).validate(invalidItem, bindingResult);
        verify(itemService, never()).save(invalidItem);
        assertEquals("item/new", viewName);
    }

    @Test
    void testEditItem() {
        Item testItem = createTestItem();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(itemService.findOne(testItem.getId())).thenReturn(Optional.of(testItem));

        String viewName = itemController.edit(testItem.getId(), model);

        verify(itemService).findOne(testItem.getId());
        verify(model).addAttribute("item", testItem);
        assertEquals("item/edit", viewName);
    }

    @Test
    void testEditItemNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(itemService.findOne(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemController.edit(1, model));
    }

    @Test
    void testUpdateItemValid() {
        Item testItem = createTestItem();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = itemController.update(testItem, bindingResult, testItem.getId(), model);

        verify(itemValidator).validate(testItem, bindingResult);
        verify(itemService).update(testItem.getId(), testItem);
        assertEquals("redirect:/items/" + testItem.getId(), viewName);
    }

    @Test
    void testUpdateItemNotValid() {
        Item invalidItem = createTestItem();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = itemController.update(invalidItem, bindingResult, invalidItem.getId(), model);

        verify(itemValidator).validate(invalidItem, bindingResult);
        verify(itemService, never()).update(invalidItem.getId(), invalidItem);
        assertEquals("item/edit", viewName);
    }

    @Test
    void testDeleteItem() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());

        String viewName = itemController.delete(1);

        verify(itemService).delete(1);
        assertEquals("redirect:/items", viewName);
    }

    private Item createTestItem() {
        Item testItem = new Item();
        testItem.setId(1);
        return testItem;
    }

    private User createCurrentUser() {
        User currentUser = new User();
        currentUser.setId(2);
        return currentUser;
    }
}