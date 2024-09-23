package ru.brombin.JMarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repository.ItemRepository;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    private User testUser;
    private Item testItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");

        testItem = new Item();
        testItem.setId(1);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setCategory(Item.ItemCategory.ZooItems);
        testItem.setPrice(100);
        testItem.setQuantity(10);
        testItem.setArticleNumber("ART123");
        testItem.setOwner(testUser); // Устанавливаем владельца

        when(userService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void testFindAll() {
        when(itemRepository.findAll()).thenReturn(List.of(testItem));

        List<Item> items = itemService.findAll();

        assertEquals(1, items.size());
        verify(itemRepository).findAll();
        verify(userService).getCurrentUser();
    }

    @Test
    void testFindOne_Success() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));

        Optional<Item> item = itemService.findOne(1);

        assertTrue(item.isPresent());
        assertEquals(testItem.getId(), item.get().getId());
        verify(itemRepository).findById(1);
    }

    @Test
    void testFindOne_NotFound() {
        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Item> item = itemService.findOne(1);

        assertTrue(item.isEmpty());
        verify(itemRepository).findById(1);
    }

    @Test
    void testSave() {
        itemService.save(testItem);

        assertEquals(testUser, testItem.getOwner());
        verify(itemRepository).save(testItem);
        verify(userService).getCurrentUser();
    }

    @Test
    void testUpdate_Success() {
        // Подготавливаем данные для обновления
        testItem.setName("Updated Item");

        // Настраиваем поведение моков
        doNothing().when(itemRepository).updateFields(
                eq(1), anyString(), anyString(), eq(Item.ItemCategory.ZooItems), anyInt(), anyInt(), anyString()
        );

        itemService.update(1, testItem);

        // Проверки
        verify(itemRepository).updateFields(
                eq(1),
                eq("Updated Item"),
                eq("Test Description"),
                eq(Item.ItemCategory.ZooItems),
                eq(100),
                eq(10),
                eq("ART123")
        );
    }

    @Test
    void testUpdate_ItemNotFound() {
        doThrow(new NotFoundException("Item not found")).when(itemRepository).updateFields(
                eq(1), anyString(), anyString(), eq(Item.ItemCategory.ZooItems), anyInt(), anyInt(), anyString()
        );

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.update(1, testItem);
        });

        assertEquals("Item with id 1 not found", thrown.getMessage());
    }

    @Test
    void testDelete_Success() {
        doNothing().when(itemRepository).deleteById(1);

        itemService.delete(1);

        verify(itemRepository).deleteById(1);
    }

    @Test
    void testDelete_ItemNotFound() {
        doThrow(new NotFoundException("Item not found")).when(itemRepository).deleteById(1);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.delete(1);
        });

        assertEquals("Item with id 1 not found", thrown.getMessage());
    }
}