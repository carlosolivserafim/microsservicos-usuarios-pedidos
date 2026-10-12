package io.github.carlosolivserafim.order.api.controller;

import io.github.carlosolivserafim.order.api.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerNotFoundException(Exception exception){
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.toString(), exception.getMessage());
        return errorDto;
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerBadRequestException(Exception exception){
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return errorDto;
    }
}