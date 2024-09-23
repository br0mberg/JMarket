package ru.brombin.JMarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositorie.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {
    @InjectMocks
    private RegistrationService registrationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("plainPassword");
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");

        registrationService.register(testUser);

        assertEquals("hashedPassword", testUser.getPassword());
        assertEquals(User.UserRole.ROLE_USER, testUser.getRole());
        assertNotNull(testUser.getRegistrationDate());
        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("plainPassword");
    }
}
