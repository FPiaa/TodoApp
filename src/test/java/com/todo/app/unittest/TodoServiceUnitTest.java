package com.todo.app.unittest;

import com.todo.app.domain.Todo;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.repository.TodoRepository;
import com.todo.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.todo.app.utils.TodoUtils.createValidTodo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
  public void test_todoCreate_withValidMessage_shouldReturnTodo() throws Exception {
    when(todoRepository.save(any(Todo.class))).then(returnsFirstArg());

    Todo todo = createValidTodo(1L);
    Todo newTodo = todoService.create(todo);

    assertThat(newTodo.getCreatedAt()).isNotNull();
  }

  @Test
  public void test_create_withInvalidMessage_shouldThrowBadRequestException() {
    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.create(new Todo()));

    String expectedMessage = "TODO message cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_create_insertingExistingId_shouldThrowResourceAlreadyExistsException() {
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);

    ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
            () -> todoService.create(createValidTodo(1L)));

    String expectedMessage = "TODO with ID: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findById_withValidId_ShouldReturnTodo() throws Exception {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidTodo(1L, "test")));

    var todo = todoService.findById(1L);

    assertEquals(1L, todo.getId());
    assertEquals("test", todo.getMessage());
  }

  @Test
  public void test_findById_withInvalidID_ShouldThrowResourceNotFoundException() {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> todoService.findById(1L)
    );

    String expectedMessage = "Cannot find TODO with ID: ";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findAll_ShouldReturnTodosList() {
    when(todoRepository.findAll())
        .thenReturn(
            List.of(new Todo[]{createValidTodo(1L), createValidTodo(2L), createValidTodo(3L)}
            )
        );
    var todos = todoService.findAll();
    assertThat(todos.size()).isEqualTo(3);
  }

  @Test
  public void test_updateTodo_validTodo_ShouldBeValid() throws Exception {
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);

    var todo = createValidTodo(1L);
    todoService.updateTodo(todo);
  }

  @Test
  public void test_updateTodo_emptyMessage_ShouldThrowBadRequestException() {
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);

    var todo = createValidTodo(1L, "");
    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.updateTodo(todo)
    );

    String expectedMesssage = "TODO message cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMesssage));
  }

  @Test
  public void test_updateTodo_TodoDoesNotExist_ShouldThrowResourceNotFoundException() {
    when(todoRepository.existsById(any(Long.class))).thenReturn(false);

    var todo = createValidTodo(1L);
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> todoService.updateTodo(todo));

    String expectedMessage = "Cannot find TODO";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_updateMessage_ValidMessage_ShouldBeValid() throws Exception {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidTodo(1L)));

    todoService.updateMessage(1L, "updateMessage");
  }

  @Test
  public void test_updateMessage_EmptyMessage_ShouldThrowBadRequestException() {
    when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidTodo(1L)));

    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.updateMessage(1L, ""));

    String expectedMessage = "TODO message cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_deleteById_ValidId_ShouldBeValid() throws Exception{
    when(todoRepository.existsById(any(Long.class))).thenReturn(true);
    todoService.deleteById(1L);
  }

  @Test
  public void test_deleteById_IdDoesNotExist_ShouldThrowResourceNotFoundException() {
    when(todoRepository.existsById(any(Long.class))).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> todoService.deleteById(1L));

    String expectedMessage = "Cannot find TODO with ID: ";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
