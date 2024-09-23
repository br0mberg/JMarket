package ru.brombin.JMarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositorie.UserRepository;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Spy
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User currentUser;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.currentUser = new User();
        this.currentUser.setId(2);
    }

    @Test
    void testFindOne_UserExists() {
        User user = new User();
        user.setId(1);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findOne(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindOne_UserNotFound() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<User> result = userService.findOne(1);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setPassword("password");
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");

        userService.save(user);

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testUpdateUser_UserNotFound() {
        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        User currentUser = new User();
        currentUser.setId(1);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        doThrow(new NotFoundException("User not found"))
                .when(userRepository)
                .updateUserFields(anyInt(), anyString(), anyString(), anyInt(), anyString(), any(), any());

        assertThrows(NotFoundException.class, () -> userService.update(1, updatedUser));
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = (User) userService.loadUserByUsername("username");

        assertEquals("username", result.getUsername());
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("username"));
    }
}