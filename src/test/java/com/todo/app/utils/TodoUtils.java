package com.todo.app.utils;

import com.todo.app.domain.Todo;

import java.sql.Timestamp;
import java.time.Instant;

public class TodoUtils {
  public static Todo createValidTodo(Long id) {
      var todo = new Todo();
      todo.setId(id);
      todo.setMessage("Todo message");
      return todo;
  }

  public static Todo createValidTodo(Long id, String message) {
    var todo = new Todo();
    todo.setId(id);
    todo.setMessage(message);
    return todo;
  }

  public static Todo createValidTodo(String message) {
    var todo = new Todo();
    todo.setMessage(message);
    return todo;
  }
}
