package com.todo.app.controller;

import com.todo.app.config.SwaggerConfig;
import com.todo.app.entity.Message;
import com.todo.app.entity.Todo;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Api(tags = SwaggerConfig.todoControllerTag)
@RestController
@RequestMapping("/api")
public class TodoController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TodoService todoService;

  @ApiOperation(value = "List all TODOs",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = List.class),
  })
  @GetMapping(value = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Todo>> findAll() {
    return ResponseEntity.ok(todoService.findAll());
  }

  @ApiOperation(value = "List all TODOs from user with userID",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = List.class),
  })
  @GetMapping(value = "/todos/{userId}")
  public ResponseEntity<List<Todo>> findAllByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(todoService.findAllByUserId(userId));
  }

  @ApiOperation(value = "List a specific TODO with and ID",
      tags = SwaggerConfig.todoControllerTag,
  produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = Todo.class),
      @ApiResponse(code = 404, message = "TODO not found")
  })
  @GetMapping(value = "/todo/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Todo> findTodoById(@PathVariable long todoId) {
    try {
      return ResponseEntity.ok(todoService.findById(todoId));
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @ApiOperation(value = "Create a new TODO",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = Todo.class),
      @ApiResponse(code = 400, message = "TODO message cant be empty"),
      @ApiResponse(code = 409, message = "TODO with this ID already exists")
  })
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

  @ApiOperation(value = "Update an existing TODO",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation"),
      @ApiResponse(code = 400, message = "TODO message cant be empty"),
      @ApiResponse(code = 404, message = "TODO not found")
  })
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

  @ApiOperation(value = "Update an existing TODO message",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation"),
      @ApiResponse(code = 400, message = "TODO message cant be empty"),
      @ApiResponse(code = 404, message = "TODO not found")
  })
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

  @ApiOperation(value = "Delete an existing TODO with ID",
      tags = SwaggerConfig.todoControllerTag,
      produces = "Todo")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation"),
      @ApiResponse(code = 404, message = "TODO not found")
  })
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
