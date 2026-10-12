package io.github.carlosolivserafim.order.api.controller;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){

        super(message);
    }
}