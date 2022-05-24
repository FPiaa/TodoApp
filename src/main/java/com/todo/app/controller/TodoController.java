package com.todo.app.controller;

import com.todo.app.domain.Todo;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class TodoController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TodoService todoService;

  @GetMapping("/todos")
  public ResponseEntity<List<Todo>> findAll() {
    return ResponseEntity.ok(todoService.findAll());
  }

  @PostMapping("/todo")
  public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) throws URISyntaxException {
    try {
      var createdTodo = todoService.create(todo);
      return ResponseEntity
          .created(new URI("/api/todo/" + createdTodo.getId()))
          .body(createdTodo);

    } catch (BadRequestException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (ResourceAlreadyExistsException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
