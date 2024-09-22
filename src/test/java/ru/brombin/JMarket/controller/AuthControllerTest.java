package ru.brombin.JMarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.RegistrationService;
import ru.brombin.JMarket.services.UserService;
import ru.brombin.JMarket.util.validators.UserValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserValidator userValidator;
    @Mock
    private RegistrationService registrationService;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userValidator, registrationService, userService);
    }

    @Test
    void testLoginPage() {
        String viewName = authController.loginPage(request);

        verify(userService).getClientIp(request);
        assertEquals("auth/login", viewName);
    }

    @Test
    void testRegistrationPage() {
        User user = new User();
        user.setRole(User.UserRole.ROLE_USER);
        when(userService.getClientIp(request)).thenReturn("127.0.0.1");

        String viewName = authController.registrationPage(user, request);

        verify(userService).getClientIp(request);
        assertEquals("auth/registration", viewName);
        assertEquals(User.UserRole.ROLE_USER, user.getRole());
    }

    @Test
    void testPerformRegistrationValid() {
        User user = new User();
        when(userService.getClientIp(request)).thenReturn("127.0.0.1");
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.performRegistration(user, bindingResult, request);

        verify(userValidator).validate(user, bindingResult);
        verify(registrationService).register(user);
        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    void testPerformRegistrationInvalid() {
        User user = new User();
        when(userService.getClientIp(request)).thenReturn("127.0.0.1");
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.performRegistration(user, bindingResult, request);

        verify(userValidator).validate(user, bindingResult);
        verify(registrationService, never()).register(user);
        assertEquals("auth/registration", viewName);
    }
}