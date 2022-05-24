package com.todo.app.unittest;

import com.todo.app.domain.Todo;
import com.todo.app.repository.TodoRepository;
import com.todo.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
  @Mock
  private TodoRepository todoRepository;
  @InjectMocks
  private TodoService todoService;


  @Test
  public void test_todoCreate_withMessage_shouldBeValid() throws Exception {
    Todo todo = new Todo();
    todo.setMessage("test message");
    todo.setCreatedAt(Timestamp.from(Instant.now()));

    when(todoRepository.save(any(Todo.class))).then(returnsFirstArg());
    Todo newTodo = todoService.createTodo(todo);
    assertThat(newTodo.getCreatedAt()).isNotNull();
  }

  @Test
  public void test_todoCreate_withInvalidArgs_shouldThrowException() {
    Exception exception = assertThrows(Exception.class, () -> todoService.createTodo(new Todo()));

    String expected_message = "Creation error";
    String actual_message = exception.getMessage();
    assertTrue(expected_message.contains(actual_message));
  }
}
