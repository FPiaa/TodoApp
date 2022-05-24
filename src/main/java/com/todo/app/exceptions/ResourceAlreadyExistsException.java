package com.todo.app.exceptions;

public class ResourceAlreadyExistsException extends Exception {
  public ResourceAlreadyExistsException() {}

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
