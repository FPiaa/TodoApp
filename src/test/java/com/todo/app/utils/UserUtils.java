package com.todo.app.utils;

import com.todo.app.entity.User;

public class UserUtils {
  public static User createValidUser(long id, String name) {
    var user = new User();
    user.setName(name);
    user.setId(id);
    return user;
  }

  public static User createValidUser(String name) {
    var user = new User();
    user.setName(name);
    return user;
  }
}
