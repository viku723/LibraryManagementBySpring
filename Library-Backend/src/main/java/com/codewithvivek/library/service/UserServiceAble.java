package com.codewithvivek.library.service;

import com.codewithvivek.library.model.User;

import java.util.List;

public interface UserServiceAble {
    List<User> getAllUsers() throws Exception;
    Boolean isAdmin(int id);
    void saveUser(User user) throws Exception;
    User findByEmail(String username);
}
