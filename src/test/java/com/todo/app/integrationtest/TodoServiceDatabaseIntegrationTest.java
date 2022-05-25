package com.todo.app.integrationtest;

import com.todo.app.entity.Todo;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static com.todo.app.utils.TodoUtils.createValidTodo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TodoServiceDatabaseIntegrationTest {
  @Autowired
  private DataSource dataSource;

  @Autowired
  private TodoService todoService;


  @Test
  public void test_createTodo_getTodoCreated_deleteTodo_getExpection() throws Exception {
    var todo = new Todo();
    todo.setMessage("FirstTestMessage");

    var createTodo = todoService.create(todo);
    var getTodo = todoService.findById(createTodo.getId());

    assertEquals(createTodo.getId(), getTodo.getId());
    assertEquals(createTodo.getMessage(), getTodo.getMessage());
    assertThat(createTodo.getCreatedAt()).isNotNull();
    assertThat(createTodo.getUpdatedAt()).isNotNull();
    assert(!createTodo.isDone());

    todoService.deleteById(getTodo.getId());
    assertThrows(ResourceNotFoundException.class, () -> todoService.findById(getTodo.getId()));
  }

  @Test
  public void test_createTodos_assertAllEqualMessages() throws Exception {
    String message = "SecondTestMessage";
    for(int i = 0; i<10; ++i) {
      var todo = new Todo();
      todo.setMessage(message);
      todoService.create(todo);
    }

    var todos = todoService.findAll();
    long numberOfDifferentMessages = todos.stream().filter(x -> x.getMessage().equals(message)).count();
    assertEquals(10, numberOfDifferentMessages);
  }

  @Test
  public void test_createTodo_UpdateMessageStatus() throws Exception{
    var todo = new Todo();
    String initialMessage = "message";
    String newMessage = "this is different";
    todo.setMessage(initialMessage);

    var createdTodo = todoService.create(todo);
    var createdTime = createdTodo.getCreatedAt();
    var updateTime = createdTodo.getUpdatedAt();

    todoService.updateMessage(createdTodo.getId(), newMessage);
    todoService.updateStatus(createdTodo.getId(), true);
    var getTodo = todoService.findById(createdTodo.getId());
    assertEquals(getTodo.getMessage(), newMessage);
    assert(getTodo.isDone());
    assertThat(getTodo.getCreatedAt()).isEqualTo(createdTime);
    assertThat(getTodo.getUpdatedAt()).isNotEqualTo(updateTime);
  }
}
