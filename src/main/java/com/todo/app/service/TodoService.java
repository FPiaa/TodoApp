package com.todo.app.service;

import com.todo.app.domain.Todo;
import com.todo.app.repository.TodoRepository;
import com.todo.app.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  public Todo createTodo(Todo todo) throws Exception{
    if(!StringUtils.isEmpty(todo.getMessage())) {
      return todoRepository.save(todo);
    } else {
      throw new Exception("Creation error");
    }
  }
}
