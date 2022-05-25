package com.todo.app.controller;

import com.todo.app.entity.User;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.service.UserService;
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

@RestController
@RequestMapping("/user")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private UserService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<User> findById(@PathVariable long userId) {
    try {
      return ResponseEntity.ok(userService.findById(userId));
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{userId}/todos")
  public ResponseEntity<User> findTodosById(@PathVariable long userId) {
    try {
      return ResponseEntity.ok(userService.findById(userId));
    } catch (ResourceNotFoundException ex) {
      logger.error(ex.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

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
