package com.amrat.ECommerceApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionMessage> handleAccessDeniedException(AccessDeniedException exception){
        ExceptionMessage errorMessage = new ExceptionMessage(exception.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(403).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleException(Exception exception){
        com.amrat.ECommerceApp.exceptions.ExceptionMessage errorMessage = new com.amrat.ECommerceApp.exceptions.ExceptionMessage(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.ok(errorMessage);
    }

}
