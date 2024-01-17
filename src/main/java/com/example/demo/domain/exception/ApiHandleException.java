package com.example.demo.domain.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Joel NOUMIA
 */
@ControllerAdvice
public class ApiHandleException {

    private static Logger logger = LoggerFactory.getLogger(ApiHandleException.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Vous pouvez personnaliser la logique de gestion de l'exception ici
        return new ResponseEntity<>("Une erreur s'est produite : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {

        return new ResponseEntity<>("Une erreur s'est produite : " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
