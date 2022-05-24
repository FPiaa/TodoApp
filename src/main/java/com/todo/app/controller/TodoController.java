package com.todo.app.controller;

import com.todo.app.domain.Message;
import com.todo.app.domain.Todo;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.service.TodoService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class TodoController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TodoService todoService;

  @GetMapping(value = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Todo>> findAll() {
    return ResponseEntity.ok(todoService.findAll());
  }


  @GetMapping(value = "/todo/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Todo> findTodoById(@PathVariable long todoId) {
    try {
      return ResponseEntity.ok(todoService.findById(todoId));
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/todo")
  public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo todo) throws URISyntaxException {
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

  @PutMapping("/todo/{todoId}")
  public ResponseEntity<Void> updateTodo(@Valid @RequestBody Todo todo, @PathVariable long todoId) {
    try {
      todo.setId(todoId);
      todoService.updateTodo(todo);
      return ResponseEntity.ok().build();

    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    } catch (BadRequestException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/todo/{todoId}")
  public ResponseEntity<Void> updateTodoMessage(@PathVariable long todoId, @RequestBody Message message) {
    try {
      todoService.updateMessage(todoId, message.getMessage());
      return ResponseEntity.ok().build();
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    } catch (BadRequestException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/todo/{todoId}")
  public ResponseEntity<Void> deleteTodo(@PathVariable long todoId) {
    try{
      todoService.deleteById(todoId);
      return ResponseEntity.ok().build();
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }
}
