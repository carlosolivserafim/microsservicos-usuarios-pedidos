package io.github.carlosolivserafim.user.api.controller;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){

        super(message);
    }
}