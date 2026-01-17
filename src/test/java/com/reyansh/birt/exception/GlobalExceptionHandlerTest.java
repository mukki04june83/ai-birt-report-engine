package com.reyansh.birt.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GlobalExceptionHandler
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("reportRequest", "reportName", "Report name is required");
        FieldError fieldError2 = new FieldError("reportRequest", "outputFormat", "Output format is required");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Validation failed", body.get("message"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Report name is required", errors.get("reportName"));
        assertEquals("Output format is required", errors.get("outputFormat"));
    }

    @Test
    void testHandleValidationExceptions_SingleError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("reportRequest", "reportName", "Report name is required");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals(1, errors.size());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid report format");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Invalid argument", body.get("message"));
        assertEquals("Invalid report format", body.get("error"));
    }

    @Test
    void testHandleIllegalArgumentException_NullMessage() {
        IllegalArgumentException exception = new IllegalArgumentException();

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Something went wrong");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Internal server error", body.get("message"));
        assertEquals("Something went wrong", body.get("error"));
    }

    @Test
    void testHandleGenericException_NullMessage() {
        Exception exception = new Exception();

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
    }

    @Test
    void testHandleValidationExceptions_MultipleFieldsWithSameError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("reportRequest", "field1", "Required field");
        FieldError fieldError2 = new FieldError("reportRequest", "field2", "Required field");
        FieldError fieldError3 = new FieldError("reportRequest", "field3", "Required field");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2, fieldError3));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertEquals(3, errors.size());
    }

    @Test
    void testHandleIllegalArgumentException_LongMessage() {
        String longMessage = "This is a very long error message that contains detailed information " +
                "about what went wrong during the report generation process and why the argument was invalid.";
        IllegalArgumentException exception = new IllegalArgumentException(longMessage);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertEquals(longMessage, body.get("error"));
    }

    @Test
    void testHandleGenericException_RuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime error occurred");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertEquals("Runtime error occurred", body.get("error"));
    }

    @Test
    void testHandleGenericException_IOException() {
        Exception exception = new java.io.IOException("File not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        Map<String, Object> body = response.getBody();
        assertEquals("File not found", body.get("error"));
    }

    @Test
    void testHandleValidationExceptions_EmptyErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertTrue(errors.isEmpty());
    }

    @Test
    void testResponseStructure_ValidationException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("test", "field", "error");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);

        Map<String, Object> body = response.getBody();
        assertTrue(body.containsKey("success"));
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("errors"));
        assertEquals(3, body.size());
    }

    @Test
    void testResponseStructure_IllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("test");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        Map<String, Object> body = response.getBody();
        assertTrue(body.containsKey("success"));
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("error"));
        assertEquals(3, body.size());
    }

    @Test
    void testResponseStructure_GenericException() {
        Exception exception = new Exception("test");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        Map<String, Object> body = response.getBody();
        assertTrue(body.containsKey("success"));
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("error"));
        assertEquals(3, body.size());
    }
}
