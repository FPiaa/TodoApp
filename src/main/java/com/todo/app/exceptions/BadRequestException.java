package com.todo.app.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class BadRequestException extends Exception{
  private List<String> exceptionMessages = new ArrayList<>();

  public BadRequestException() {}

  public BadRequestException(String message){
    super(message);
  }

  public void addException(String message) {
    exceptionMessages.add(message);
  }
}
