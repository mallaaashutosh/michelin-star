package com.micheline-star.dao;

import com.micheline-star.entity.User;

public interface UserDao {

    boolean insertUser(User user);
    User findByUsername(String name);
    User findByEmail(String email);
}
