package com.todo.app.integrationtest;

import com.todo.app.domain.Todo;
import com.todo.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static com.todo.app.utils.TodoUtils.createValidTodo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TodoServiceDatabaseIntegrationTest {
  @Autowired
  private DataSource dataSource;

  @Autowired
  private TodoService todoService;


  @Test
  public void test_createTodo_getTodoCreated() throws Exception {
    var todo = new Todo();
    todo.setMessage("message");

    var createTodo = todoService.create(todo);
    var getTodo = todoService.findById(createTodo.getId());
  }
}
