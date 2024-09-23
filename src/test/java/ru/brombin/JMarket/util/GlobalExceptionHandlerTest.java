package ru.brombin.JMarket.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import ru.brombin.JMarket.util.exceptions.NotCreatedOrUpdatedException;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFoundStatus() {
        NotFoundException notFoundException = new NotFoundException("User not found");
        when(webRequest.getDescription(false)).thenReturn("uri=/users/1");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleNotFoundException(notFoundException, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody().getMessage());
        verify(webRequest, times(1)).getDescription(false);
    }

    @Test
    void handleNotCreatedOrUpdatedException_ShouldReturnBadRequestStatus() {
        NotCreatedOrUpdatedException notCreatedOrUpdatedException = new NotCreatedOrUpdatedException("User not created");
        when(webRequest.getDescription(false)).thenReturn("uri=/users");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleNotCreatedOrUpdatedException(notCreatedOrUpdatedException, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User not created", responseEntity.getBody().getMessage());
        verify(webRequest, times(1)).getDescription(false);
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorStatus() {
        Exception exception = new Exception("Unexpected error");
        when(webRequest.getDescription(false)).thenReturn("uri=/error");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGlobalException(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred", responseEntity.getBody().getMessage());
        verify(webRequest, times(1)).getDescription(false);
    }
}