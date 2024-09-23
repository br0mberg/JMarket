package ru.brombin.JMarket.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.service.UserService;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.validators.UserValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserApiControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private BindingResult bindingResult;

    private UserApiController userApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userApiController = new UserApiController(userService, userValidator);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userService.findAll()).thenReturn(users);
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());

        ResponseEntity<List<User>> response = userApiController.getAllPeople();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    private User createCurrentUser() {
        User currentUser = new User();
        currentUser.setId(2);
        return currentUser;
    }

    @Test
    void testGetUserByIdFound() {
        User testUser = createTestUser();
        when(userService.findOne(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());

        ResponseEntity<User> response = userApiController.getUserById(testUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    private User createTestUser() {
        User testUser = new User();
        testUser.setId(1);
        return testUser;
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userApiController.getUserById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateUserValid() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<User> response = userApiController.createUser(testUser, bindingResult);

        verify(userValidator).validate(testUser, bindingResult);
        verify(userService).save(testUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testCreateUserInvalid() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("testUser", "username", "Username is required")));

        NotCreatedOrUpdatedException exception = assertThrows(NotCreatedOrUpdatedException.class, () -> {
            userApiController.createUser(testUser, bindingResult);
        });

        assertNotNull(exception);
    }

    @Test
    void testUpdateUserFound() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.of(new User()));
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userValidator).validate(testUser, bindingResult);

        ResponseEntity<User> response = userApiController.updateUser(1, testUser, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).update(1, testUser);
    }

    @Test
    void testUpdateUserNotFound() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userApiController.updateUser(1, testUser, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUserFound() {
        User testUser = createTestUser();
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<Object> response = userApiController.deleteUser(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userService.getCurrentUser()).thenReturn(createCurrentUser());
        when(userService.findOne(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userApiController.deleteUser(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
