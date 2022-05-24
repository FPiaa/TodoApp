package com.todo.app.service;

import com.sun.xml.bind.v2.TODO;
import com.todo.app.domain.Todo;
import com.todo.app.repository.TodoRepository;
import com.todo.app.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  public Todo findById(Long id) throws Exception {
    var todo = todoRepository.findById(id);

    if(todo.isEmpty()) {
      throw new Exception("Cannot find TODO with ID: " + id);
    }

    return todo.get();

  }

  public Iterable<Todo> findAll() {
    return todoRepository.findAll();
  }

  public Todo create(Todo todo) throws Exception {
    if(!StringUtils.isEmpty(todo.getMessage())) {
      return todoRepository.save(todo);
    } else {
      throw new Exception("Creation error");
    }
  }


  public void updateTodo(Todo todo) throws Exception {
    if(!todoRepository.existsById(todo.getId())) {
      throw new Exception("Cannot find TODO with ID: " +todo.getId());
    }

    if(StringUtils.isEmpty(todo.getMessage())) {
      throw new Exception("Invalid TODO");
    }

    todoRepository.save(todo);

  }
  public void updateStatus(Long id, boolean status) throws Exception{
    Todo todo = findById(id);
    todo.setDone(status);
    todoRepository.save(todo);
  }

  public void updateMessage(Long id, String message) throws Exception {
    var todo = findById(id);
    if(StringUtils.isEmpty(message)) {
      throw new Exception("Message cannot be empty");
    }
    todo.setMessage(message);
    todoRepository.save(todo);
  }

  public void deleteById(Long id) throws Exception {
    if(!todoRepository.existsById(id)) {
      throw new Exception("Cannot find TODO with ID: " + id);
    }

    todoRepository.deleteById(id);
  }

  public void delete(Todo todo) throws Exception {
    deleteById(todo.getId());
  }
}
