package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryApplicationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleResourceException(Exception ex){
        return new ResponseEntity("Error in processing the request with message: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
