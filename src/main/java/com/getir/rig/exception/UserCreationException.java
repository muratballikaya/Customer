package com.getir.rig.exception;

public class UserCreationException extends RuntimeException{
    public UserCreationException(String message,Exception ex){
        super(message,ex);
    }
}
