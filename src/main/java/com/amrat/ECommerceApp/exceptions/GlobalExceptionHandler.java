package com.amrat.ECommerceApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.amrat.HospitalManagementApp.exceptions.ExceptionMessage> handleException(Exception exception){
        com.amrat.HospitalManagementApp.exceptions.ExceptionMessage errorMessage = new com.amrat.HospitalManagementApp.exceptions.ExceptionMessage(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.ok(errorMessage);
    }

}
