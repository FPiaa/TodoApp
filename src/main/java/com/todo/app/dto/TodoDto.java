package com.todo.app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TodoDto implements Serializable {
  private final Long id;
  private final String message;
  private final boolean done;
  private final UserDto owner;
}
