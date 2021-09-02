package com.getir.rig.handler;

import com.getir.rig.exception.LoginFailedException;
import com.getir.rig.exception.UserCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerExceptionHandler.class);

    @ExceptionHandler(value = {LoginFailedException.class})
    public ResponseEntity<Object> handleLoginFailedException(LoginFailedException ex){
        logger.error("Login failed : ",ex.getMessage());
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserCreationException.class})
    public ResponseEntity<Object> handleUserCreationException(UserCreationException ex){
        logger.error("User creation failed : ",ex.getMessage());
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
