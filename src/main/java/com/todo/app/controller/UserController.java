package com.todo.app.controller;

import com.todo.app.config.SwaggerConfig;
import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = SwaggerConfig.userControllerTag)
@RestController
@RequestMapping("/user")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private UserService userService;

  @ApiOperation(value = "Query a user with ID:",
      tags = SwaggerConfig.userControllerTag,
      produces = "User")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = User.class),
      @ApiResponse(code = 404, message = "User does not exist"),
  })
  @GetMapping("/{userId}")
  public ResponseEntity<User> findById(@PathVariable long userId) {
    try {
      return ResponseEntity.ok(userService.findById(userId));
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @ApiOperation(value = "List all user todos, redirects to api/todos/id",
      tags = SwaggerConfig.userControllerTag,
      produces = "User")
  @ApiResponses({
      @ApiResponse(code = 308, message = "Redirect to query TODO"),
  })
  @GetMapping("/todos/{userId}")
  public ResponseEntity<List<User>> findAllTodos(@PathVariable long userId) throws URISyntaxException{
    return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(new URI("/api/todos/"+userId)).build();
  }

  @ApiOperation(value = "Create a new USER",
      tags = SwaggerConfig.userControllerTag,
      produces = "User")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful operation", response = Todo.class),
      @ApiResponse(code = 400, message = "User name cannot be empty"),
      @ApiResponse(code = 409, message = "User with this ID already exists")
  })
  @PostMapping("")
  public ResponseEntity<User> addUser(@Valid @RequestBody User user) throws URISyntaxException {
    try {
      var newUser = userService.create(user);
      return ResponseEntity.created(new URI("/" + newUser.getId())).body(newUser);
    } catch (BadRequestException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.badRequest().build();
    }catch (ResourceAlreadyExistsException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
