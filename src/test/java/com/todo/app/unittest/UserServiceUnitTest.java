package com.todo.app.unittest;

import com.todo.app.entity.User;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.repository.UserRepository;
import com.todo.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.todo.app.utils.UserUtils.createValidUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserService todoService;



  @Test
  public void test_todoCreate_withValidMessage_shouldReturnUser() throws Exception {
    when(userRepository.save(any(User.class))).then(returnsFirstArg());

    User todo = createValidUser(1L, "test");
    User newUser = todoService.create(todo);

    assertThat(newUser.getName()).isNotNull();
  }

  @Test
  public void test_create_withInvalidMessage_shouldThrowBadRequestException() {
    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.create(new User()));

    String expectedMessage = "User name cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_create_insertingExistingId_shouldThrowResourceAlreadyExistsException() {
    when(userRepository.existsById(any(Long.class))).thenReturn(true);

    ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
        () -> todoService.create(createValidUser(1L, "test")));

    String expectedMessage = "USER with ID:";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findById_withValidId_ShouldReturnUser() throws Exception {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidUser(1L, "test")));

    var todo = todoService.findById(1L);

    assertEquals(1L, todo.getId());
    assertEquals("test", todo.getName());
  }

  @Test
  public void test_findById_withInvalidID_ShouldThrowResourceNotFoundException() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> todoService.findById(1L)
    );

    String expectedMessage = "Cannot find USER with ID:";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_findAll_ShouldReturnUsersList() {
    when(userRepository.findAll())
        .thenReturn(
            List.of(new User[]{createValidUser(1L, "test"),
                createValidUser(2L, "test"),
                createValidUser(3L, "test")}
            )
        );
    var todos = todoService.findAll();
    assertThat(todos.size()).isEqualTo(3);
  }

  @Test
  public void test_updateUser_validUser_ShouldBeValid() throws Exception {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidUser(1L, "test")));

    var todo = createValidUser(1L, "test");
    todoService.updateUser(todo);
  }

  @Test
  public void test_updateUser_emptyMessage_ShouldThrowBadRequestException() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidUser(1L, "test")));

    var todo = createValidUser(1L, "");
    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.updateUser(todo)
    );

    String expectedMesssage = "User name cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMesssage));
  }

  @Test
  public void test_updateUser_UserDoesNotExist_ShouldThrowResourceNotFoundException() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    var todo = createValidUser(1L, "test");
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> todoService.updateUser(todo));

    String expectedMessage = "Cannot find USER";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_updateName_ValidName_ShouldBeValid() throws Exception {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidUser(1L, "test")));

    todoService.updateName(1L, "new name");
  }

  @Test
  public void test_updateName_EmptyName_ShouldThrowBadRequestException() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createValidUser(1L, "test")));

    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> todoService.updateName(1L, ""));

    String expectedMessage = "User name cannot be empty";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void test_deleteById_ValidId_ShouldBeValid() throws Exception{
    when(userRepository.existsById(any(Long.class))).thenReturn(true);
    todoService.deleteById(1L);
  }

  @Test
  public void test_deleteById_IdDoesNotExist_ShouldThrowResourceNotFoundException() {
    when(userRepository.existsById(any(Long.class))).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> todoService.deleteById(1L));

    String expectedMessage = "Cannot find USER with ID:";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
