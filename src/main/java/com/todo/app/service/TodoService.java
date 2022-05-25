package com.todo.app.service;

import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.repository.TodoRepository;
import com.todo.app.specification.TodoSpecification;
import com.todo.app.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  public Todo findById(Long id) throws ResourceNotFoundException {
    var todo = todoRepository.findById(id);

    if(todo.isEmpty()) {
      throw new ResourceNotFoundException("Cannot find TODO with ID: " + id);
    }

    return todo.get();

  }

  public List<Todo> findAll() {
    List<Todo> todo = new ArrayList<>();
    todoRepository.findAll().forEach(todo::add);
    return todo;
  }

  public List<Todo> findAllByUserId(Long userId) {
    User owner = new User();
    owner.setId(userId);

    Todo filter = new Todo();
    filter.setOwner(owner);

    Specification<Todo> spec = new TodoSpecification(filter);
    return todoRepository.findAll(spec);
  }

  public Todo create(Todo todo) throws BadRequestException, ResourceAlreadyExistsException{
    if(StringUtils.isEmpty(todo.getMessage())) {
      throw new BadRequestException("TODO message cannot be empty");
    }

    if(todo.getId() != null && todoRepository.existsById(todo.getId())) {
      throw new ResourceAlreadyExistsException("TODO with ID: " + todo.getId() + " already exists");
    }

    return todoRepository.save(todo);
  }


  public void updateTodo(Todo todo) throws ResourceNotFoundException, BadRequestException {
    var currentTodo = todoRepository.findById(todo.getId());
    if(currentTodo.isEmpty()) {
      throw new ResourceNotFoundException("Cannot find TODO with ID: " + todo.getId());
    }

    if(StringUtils.isEmpty(todo.getMessage())) {
      throw new BadRequestException("TODO message cannot be empty");
    }

    todo.setCreatedAt(currentTodo.get().getCreatedAt());
    todoRepository.save(todo);

  }
  public void updateStatus(Long id, boolean status) throws ResourceNotFoundException{
    Todo todo = findById(id);
    todo.setDone(status);
    todoRepository.save(todo);
  }

  public void updateMessage(Long id, String message) throws ResourceNotFoundException, BadRequestException {
    var todo = findById(id);
    if(StringUtils.isEmpty(message)) {
      throw new BadRequestException("TODO message cannot be empty");
    }
    todo.setMessage(message);
    todoRepository.save(todo);
  }

  public void deleteById(Long id) throws ResourceNotFoundException {
    if(!todoRepository.existsById(id)) {
      throw new ResourceNotFoundException("Cannot find TODO with ID: " + id);
    }

    todoRepository.deleteById(id);
  }

  public void delete(Todo todo) throws ResourceNotFoundException {
    deleteById(todo.getId());
  }
}
