package com.todo.app.exceptions;

public class ResourceNotFoundException extends Exception {
  public ResourceNotFoundException(){

  }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
