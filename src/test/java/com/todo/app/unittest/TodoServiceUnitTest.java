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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.todo.app.utils.TodoUtils.createValidTodo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceUnitTest {
  @Mock
  private TodoRepository todoRepository;
  @InjectMocks
  private TodoService todoService;



  @Test
  public void test_todoCreate_withMessage_shouldReturnTodo() throws Exception {
    Todo todo = new Todo();
    todo.setMessage("test message");
    todo.setCreatedAt(Timestamp.from(Instant.now()));

    when(todoRepository.save(any(Todo.class))).then(returnsFirstArg());
    Todo newTodo = todoService.create(todo);
    assertThat(newTodo.getCreatedAt()).isNotNull();
  }

  @Test
  public void test_todoCreate_withInvalidArgs_shouldThrowException() {
    Exception exception = assertThrows(Exception.class, () -> todoService.create(new Todo()));

    String expectedMessage = "Creation error";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findById_withValidId_ShouldReturnTodo() throws Exception {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidTodo(1L)));
    var todo = todoService.findById(1L);
    assertThat(todo.getId()).isEqualTo(1L);
  }

  @Test
  public void test_findById_withInvalid_ShouldThrowException() {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    Exception exception = assertThrows(Exception.class, () -> todoService.findById(1L));

    String expectedMessage = "Cannot find TODO with ID: ";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findAll_ShouldReturnTodosIterable() {
    when(todoRepository.findAll())
        .thenReturn(
            List.of(new Todo[]{createValidTodo(1L), createValidTodo(2L), createValidTodo(3L)}
            )
        );
    var todos = todoService.findAll();
    List<Todo> todoList = new ArrayList<>();
    for(Todo todo : todos) {
      todoList.add(todo);
    }
    assertThat(todoList.size()).isEqualTo(3);
  }

  @Test
  public void test_updateTodo_validTodo_ShouldBeValid() throws Exception {
    var todo = createValidTodo(1L);
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);
    todoService.updateTodo(todo);
  }

  @Test
  public void test_updateTodo_emptyMessage_ShouldThrowException() {
    var todo = createValidTodo(1L, "");
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);
    Exception exception = assertThrows(Exception.class, () -> todoService.updateTodo(todo));

    String expectedMesssage = "Invalid TODO";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMesssage));
  }

  @Test
  public void test_updateTodo_DoesNotExist_ShouldThrowException() {
    var todo = createValidTodo(1L);
    when(todoRepository.existsById(any(Long.class))).thenReturn(false);
    Exception exception = assertThrows(Exception.class, () -> todoService.updateTodo(todo));

    String expectedMessage = "Cannot find TODO";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_updateMessage_EmptyMessage_ShouldThrowException() {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidTodo(1L)));
    Exception exception = assertThrows(Exception.class, () -> todoService.updateMessage(1L, ""));

    String expectedMessage = "Message cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_deleteById_ValidId_ShouldBeValid() throws Exception{
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);
    todoService.deleteById(1L);
  }

  @Test
  public void test_deleteById_IdDoesNotExist_ShouldThrowException() {
    when(todoRepository.existsById(any(Long.class))).thenReturn(false);
    Exception exception = assertThrows(Exception.class, () -> todoService.deleteById(1L));
    String expectedMessage = "Cannot find TODO with ID: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
