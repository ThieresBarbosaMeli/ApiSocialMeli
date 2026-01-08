package com.example.apisocialmeli;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private final Map<Integer, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public User findById(int id) {
        return users.get(id);
    }

    public boolean existsById(int id) {
        return users.containsKey(id);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public void delete(int id) {
        users.remove(id);
    }
}