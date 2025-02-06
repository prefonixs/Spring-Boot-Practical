package com.sid.validation.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BindException.class)
    public ResponseEntity<List<String>> handleValidationException(BindException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleDuplicateEntryException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
