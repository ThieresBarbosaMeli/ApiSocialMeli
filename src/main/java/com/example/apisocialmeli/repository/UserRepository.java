package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.User;

import java.util.Collection;

public interface UserRepository {

    void save(User user);

    User findById(int id);

    boolean existsById(int id);

    Collection<User> findAll();

    void delete(int id);
}