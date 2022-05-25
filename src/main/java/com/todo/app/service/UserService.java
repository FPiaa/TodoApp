package com.todo.app.service;

import com.todo.app.entity.User;
import com.todo.app.exceptions.BadRequestException;
import com.todo.app.exceptions.ResourceAlreadyExistsException;
import com.todo.app.exceptions.ResourceNotFoundException;
import com.todo.app.repository.UserRepository;
import com.todo.app.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User findById(Long id) throws ResourceNotFoundException {
    var user = userRepository.findById(id);

    if(user.isEmpty()) {
      throw new ResourceNotFoundException("Cannot find USER with ID:" + id);
    }

    return user.get();

  }

  public List<User> findAll() {
    List<User> user = new ArrayList<>();
    userRepository.findAll().forEach(user::add);
    return user;
  }

  public User create(User user) throws BadRequestException, ResourceAlreadyExistsException{
    if(StringUtils.isEmpty(user.getName())) {
      throw new BadRequestException("User name cannot be empty");
    }

    if(user.getId() != null && userRepository.existsById(user.getId())) {
      throw new ResourceAlreadyExistsException("USER with ID:" + user.getId() + " already exists");
    }

    return userRepository.save(user);
  }


  public void updateUser(User user) throws ResourceNotFoundException, BadRequestException {
    var currentUser = userRepository.findById(user.getId());
    if(currentUser.isEmpty()) {
      throw new ResourceNotFoundException("Cannot find USER with ID:" + user.getId());
    }

    if(StringUtils.isEmpty(user.getName())) {
      throw new BadRequestException("User name cannot be empty");
    }

    userRepository.save(user);

  }

  public void updateName(Long id, String name) throws ResourceNotFoundException, BadRequestException {
    var user = findById(id);
    if(StringUtils.isEmpty(name)) {
      throw new BadRequestException("User name cannot be empty");
    }
    user.setName(name);
    userRepository.save(user);
  }

  public void deleteById(Long id) throws ResourceNotFoundException {
    if(!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Cannot find USER with ID:" + id);
    }

    userRepository.deleteById(id);
  }

  public void delete(User user) throws ResourceNotFoundException {
    deleteById(user.getId());
  }
}
