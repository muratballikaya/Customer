package com.getir.rig.exception;

public class LoginFailedException extends RuntimeException{

    public LoginFailedException(String message){
        super(message);
    }
    public  LoginFailedException(String message, Exception e){
        super("Login failed...",e);
    }
}
