package ru.brombin.JMarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.exceptions.NotFoundException;
import ru.brombin.JMarket.util.validators.UserValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserService userService;
    private UserController userController;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, userValidator);
    }

    @Test
    void testIndex() {
        // Подготовка
        when(userService.getCurrentUser()).thenReturn(createCurrentUser()); // Подделываем текущего пользователя
        when(userService.findAll()).thenReturn(List.of(new User(), new User())); // Подделываем список пользователей

        // Тест
        String viewName = userController.index(model);

        // Проверка
        verify(userService).findAll(); // Проверяем, что метод findAll был вызван
        assertEquals("user/index", viewName); // Проверяем возвращаемое представление
    }

    @Test
    void testShowUser() {
        User testUser = createTestUser();

        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.of(testUser));

        String viewName = userController.show(1, model);

        verify(userService).findOne(1);
        verify(model).addAttribute("user", testUser);
        assertEquals("user/show", viewName);
    }

    @Test
    void testShowUserNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.empty()); // Подделываем, что пользователя нет

        assertThrows(NotFoundException.class, () -> userController.show(1, model));
    }

    @Test
    void testCreateValidUser() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(false); // Подделываем отсутствие ошибок валидации

        String viewName = userController.create(testUser, bindingResult);

        verify(userValidator).validate(testUser, bindingResult);
        verify(userService).save(testUser);
        assertEquals("redirect:/users", viewName);
    }

    @Test
    void testCreateInvalidUser() {
        User invalidUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(true); // Подделываем наличие ошибок валидации

        String viewName = userController.create(invalidUser, bindingResult);

        verify(userValidator).validate(invalidUser, bindingResult);
        verify(userService, never()).save(invalidUser); // Проверяем, что сохранение не вызвано
        assertEquals("user/new", viewName);
    }

    @Test
    void testEditUser() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.of(testUser));

        String viewName = userController.edit(1, model);

        verify(userService).findOne(1);
        verify(model).addAttribute("user", testUser);
        assertEquals("user/edit", viewName);
    }

    @Test
    void testEditUserNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userController.edit(1, model));
    }

    @Test
    void testUpdateUserValidUser() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = userController.update(testUser, bindingResult, testUser.getId());

        verify(userValidator).validate(testUser, bindingResult);
        verify(userService).update(testUser.getId(), testUser);
        assertEquals("redirect:/users/" + testUser.getId(), viewName);
    }

    @Test
    void testUpdateNotValidUser() {
        User invalidUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.update(invalidUser, bindingResult, invalidUser.getId());

        verify(userValidator).validate(invalidUser, bindingResult);
        verify(userService, never()).update(invalidUser.getId(), invalidUser);
        assertEquals("user/edit", viewName);
    }

    @Test
    void testDeleteUser() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());

        String viewName = userController.delete(1);

        verify(userService).delete(1);
        assertEquals("redirect:/users", viewName);
    }

    private User createTestUser() {
        User testUser = new User();
        testUser.setId(1);
        return testUser;
    }
    private User createCurrentUser() {
        User currentUser = new User();
        currentUser.setId(2);
        return currentUser;
    }
}
